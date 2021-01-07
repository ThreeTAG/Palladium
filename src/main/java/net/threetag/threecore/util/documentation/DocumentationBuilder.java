package net.threetag.threecore.util.documentation;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.AbilityType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DocumentationBuilder {

    public static final File SUBFOLDER = new File("mods/documentation/");

    public final ResourceLocation id;
    public final String title;
    private HTMLObject html;
    private HTMLObject head;
    private HTMLObject body;

    public DocumentationBuilder(ResourceLocation id, String title) {
        this(id, title, "https://i.imgur.com/am80ox1.png");
    }

    public DocumentationBuilder(ResourceLocation id, String title, String favicon) {
        this.id = id;
        this.title = title;
        this.init(favicon);
    }

    private void init(String favicon) {
        this.head = new HTMLObject("head")
                .add(new HTMLObject("title", this.title))
                .add(new HTMLObject("style", "table{font-family:arial, sans-serif;border-collapse:collapse;}\n" +
                        "td,th{border:1px solid #666666;text-align:left;padding:8px;min-width:45px;}\n" +
                        "th{background-color:#CCCCCC;}\n" +
                        "p{margin:0;}\n" +
                        "tr:nth-child(even){background-color:#D8D8D8;}\n" +
                        "tr:nth-child(odd){background-color:#EEEEEE;}\n" +
                        "td.true{background-color:#72FF85AA;}\n" +
                        "td.false{background-color:#FF6666AA;}\n" +
                        "td.other{background-color:#42A3FFAA;}\n" +
                        "td.error{color:#FF0000;}\n" +
                        "th,td.true,td.false,td.other{text-align:center;}"))
                .add(new HTMLObject("link").addAttribute("rel", "shortcut icon").addAttribute("type", "image/x-icon").addAttribute("href", favicon));
        this.html = new HTMLObject("html").add(this.head).add(this.body = new HTMLObject("body"));
    }

    public DocumentationBuilder addStyle(String style) {
        this.head.add(new HTMLObject("style", style));
        return this;
    }

    public DocumentationBuilder addDocumentationSettings(List<IDocumentationSettings> settings) {
        Map<String, List<IDocumentationSettings>> sorted = new HashMap<>();
        // Sort abilities by mods
        for (IDocumentationSettings setting : settings) {
            String modName = AbilityType.getModContainerFromId(setting.getId().getNamespace()) != null ? AbilityType.getModContainerFromId(setting.getId().getNamespace()).getDisplayName() : setting.getId().getNamespace();
            List<IDocumentationSettings> modsList = sorted.containsKey(modName) ? sorted.get(modName) : new ArrayList<>();
            modsList.add(setting);
            sorted.put(modName, modsList);
        }

        HTMLObject overview;
        this.addStyle(".json-block { background-color: lightgray; display: inline-block; border: 5px solid darkgray; padding: 10px }")
                .add(hr())
                .add(overview = paragraph(subHeading("Overview")));

        sorted.forEach((mod, settingsList) -> {
            overview.add(subSubHeading(mod));
            overview.add(list(settingsList.stream().map(setting -> link(setting.getId(), "#" + setting.getId().toString())).collect(Collectors.toList())));
        });

        sorted.values().forEach(modSettings -> {
            modSettings.forEach(setting -> {
                HTMLObject div;
                this.add(hr()).add(div = div().setId(setting.getId().toString())
                        .add(subHeading(setting.getId().toString()))
                        .add(subSubHeading("Data Settings:"))
                        .add(table(setting.getColumns(), setting.getRows().stream().map(rows -> {
                            List<Object> list = new ArrayList<>();
                            for (Object object : rows) {
                                if (object == null) {
                                    list.add("/");
                                } else if (object instanceof Boolean) {
                                    list.add((Boolean) object ? "True" : "False");
                                } else if (object instanceof Class<?>) {
                                    list.add(((Class<?>) object).getSimpleName());
                                } else {
                                    list.add(object);
                                }
                            }
                            return list;
                        }).collect(Collectors.toList()))));
                if (setting.getExampleJson() != null) {
                    div.add(subSubHeading("Example:"))
                            .add(new HTMLObject("pre").addAttribute("class", "json-block").setId(setting.getId().toString() + "_example"))
                            .add(js("var json = JSON.parse('" + setting.getExampleJson().toString() + "'); document.getElementById('" + setting.getId().toString() + "_example').innerHTML = JSON.stringify(json, undefined, 2);"));
                }
            });
        });

        return this;
    }

    public DocumentationBuilder add(HTMLObject html) {
        this.body.add(html);
        return this;
    }

    public static HTMLObject heading(String heading) {
        return new HTMLObject("h1", heading);
    }

    public static HTMLObject subHeading(String heading) {
        return new HTMLObject("h2", heading);
    }

    public static HTMLObject subSubHeading(String heading) {
        return new HTMLObject("h3", heading);
    }

    public static HTMLObject paragraph(Object... objects) {
        return new HTMLObject("p", objects);
    }

    public static HTMLObject div(Object... contents) {
        return new HTMLObject("div", contents);
    }

    public static HTMLObject js(String javascript) {
        return new HTMLObject("script", javascript);
    }

    public static HTMLObject link(Object text, String href) {
        return new HTMLObject("a", text).addAttribute("href", href);
    }

    public static HTMLObject list(Iterable<Object> objects) {
        HTMLObject list = new HTMLObject("ul");
        for (Object object : objects) {
            list.add(new HTMLObject("li", object));
        }
        return list;
    }

    public static HTMLObject numberedList(Iterable<Object> objects) {
        HTMLObject list = new HTMLObject("ol");
        for (Object object : objects) {
            list.add(new HTMLObject("li", object));
        }
        return list;
    }

    public static HTMLObject table(Iterable<?> head, Iterable<Iterable<?>> rows) {
        HTMLObject list = new HTMLObject("table");

        HTMLObject headRow;
        list.add(new HTMLObject("thead").add(headRow = new HTMLObject("tr")));
        for (Object obj : head) {
            headRow.add(new HTMLObject("th", obj));
        }

        HTMLObject body;
        list.add(body = new HTMLObject("tbody"));
        for (Iterable<?> rowObjects : rows) {
            HTMLObject row = new HTMLObject("tr");
            for (Object obj : rowObjects) {
                row.add(new HTMLObject("td", obj));
            }
            body.add(row);
        }

        return list;
    }

    public static HTMLObject br() {
        return new HTMLObject("br");
    }

    public static HTMLObject hr() {
        return new HTMLObject("hr");
    }

    public void save() {
        try {
            File file = new File(SUBFOLDER, this.id.getNamespace() + "/" + this.id.getPath() + ".html");

            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(this.html.toString());
            bw.close();

            ThreeCore.LOGGER.info("Successfully generated documentation file: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class HTMLObject {

        private final String tag;
        private final List<Object> content = new LinkedList<>();
        private final List<Pair<String, String>> attributes = new ArrayList<>();

        public HTMLObject(String tag) {
            this(tag, null);
        }

        public HTMLObject(String tag, Object... content) {
            this.tag = tag;
            if (content != null) {
                this.content.addAll(Arrays.asList(content));
            }
        }

        public HTMLObject addAttribute(String key, String value) {
            this.attributes.add(Pair.of(key, value));
            return this;
        }

        public HTMLObject setId(String id) {
            this.attributes.add(Pair.of("id", id));
            return this;
        }

        public HTMLObject add(Object content) {
            this.content.add(content);
            return this;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder("<" + this.tag);
            this.attributes.forEach(pair -> {
                result.append(" ").append(pair.getFirst()).append("=\"").append(pair.getSecond()).append("\"");
            });
            result.append(">").append("\n");
            this.content.forEach(content -> {
                if (content instanceof Supplier<?>) {
                    result.append(((Supplier<?>) content).get().toString());
                } else {
                    result.append(content.toString());
                }
            });
            if (!this.tag.equals("br") && !this.tag.equals("hr") && !this.tag.equals("link")) {
                result.append("</").append(this.tag).append(">");
            }
            return result.append("\n").toString();
        }
    }
}
