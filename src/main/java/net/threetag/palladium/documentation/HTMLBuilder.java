package net.threetag.palladium.documentation;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.threetag.palladium.Palladium;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HTMLBuilder {

    public final Identifier id;
    public final String title;
    private HTMLObject html;
    private HTMLObject head;
    private HTMLObject body;
    private boolean hasJson = false;

    public HTMLBuilder(Identifier id, String title) {
        this(id, title, "https://i.imgur.com/3sx4xJf.png");
    }

    public HTMLBuilder(Identifier id, String title, String favicon) {
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
                        "th,td.true,td.false,td.other{text-align:center;}" +
                        "pre{outline:1px solid #ccc;padding:5px;margin:5px;} .string{color:green;} .number{color:cornflowerblue;} .boolean{color:darkorange;} .null{color:orangered;} .key{color:purple;}" +
                        "hr { height: 5px; background-color: black }"))
                .add(new HTMLObject("link").addAttribute("rel", "shortcut icon").addAttribute("type", "image/x-icon").addAttribute("href", favicon));
        this.html = new HTMLObject("html").add(this.head).add(this.body = new HTMLObject("body"));
    }

    public HTMLBuilder addStyle(String style) {
        this.head.add(new HTMLObject("style", style));
        return this;
    }

    public static <T extends Documented<?, ?>> HTMLBuilder documentedPage(Identifier location, Map<Identifier, T> values, String title, HolderLookup.Provider provider) {
        HTMLBuilder builder = new HTMLBuilder(location, title);
        builder.add(HTMLBuilder.heading(title))
                .addDocumentationSettings(values.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                entry -> {
                                    var doc = entry.getValue().getDocumentation(provider);
                                    CodecDocumentationBuilder.addToDocs(location, doc.build(location, entry.getKey()));
                                    return doc;
                                })));
        return builder;
    }

    public static <T extends Documented<?, ?>> HTMLBuilder documentedPage(ResourceKey<? extends Registry<T>> registryKey, Registry<T> registry, String title, HolderLookup.Provider provider) {
        HTMLBuilder builder = new HTMLBuilder(registryKey.identifier(), title);
        builder.add(HTMLBuilder.heading(title))
                .addDocumentationSettings(registry.entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().identifier(),
                                entry -> {
                                    var doc = entry.getValue().getDocumentation(provider);
                                    CodecDocumentationBuilder.addToDocs(registryKey.identifier(), doc.build(entry.getKey()));
                                    return doc;
                                })));
        return builder;
    }

    public static HTMLObject documented(CodecDocumentationBuilder<?, ?> builder, @Nullable Identifier id) {
        HTMLObject div = div();
        builder.addToHtml(div, id);
        return div;
    }

    @SuppressWarnings("UnusedReturnValue")
    public HTMLBuilder addDocumentationSettings(Map<Identifier, CodecDocumentationBuilder<?, ?>> settings) {
        Map<String, List<Pair<Identifier, CodecDocumentationBuilder<?, ?>>>> sorted = new HashMap<>();

        // Sort abilities by mods
        for (Map.Entry<Identifier, CodecDocumentationBuilder<?, ?>> e : settings.entrySet()) {
            var mod = ModList.get().getModContainerById(e.getKey().getNamespace()).orElse(null);
            String modName = mod != null ? mod.getModInfo().getDisplayName() : e.getKey().getNamespace();
            var list = sorted.computeIfAbsent(modName, x -> new ArrayList<>());
            list.add(Pair.of(e.getKey(), e.getValue()));
            list.sort(Comparator.comparing(o -> o.getFirst().toString()));
        }

        HTMLObject overview;
        this.addStyle(".json-block { background-color: lightgray; display: inline-block; border: 5px solid darkgray; padding: 10px }")
                .add(hr())
                .add(overview = paragraph(subHeading("Overview")));

        sorted.forEach((mod, settingsList) -> {
            overview.add(subSubHeading(mod));
            overview.add(list(settingsList.stream().map(setting -> link(setting.getSecond().getName(), "#" + setting.getFirst().toString())).collect(Collectors.toList())));
        });

        sorted.values().forEach(modSettings -> {
            modSettings.forEach(setting -> {
                this.add(hr()).add(documented(setting.getSecond(), setting.getFirst()));
                this.hasJson = true;
            });
        });

        return this;
    }

    public HTMLBuilder add(HTMLObject html) {
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
                if (obj instanceof Boolean b) {
                    row.add(new HTMLObject("td", obj).addAttribute("style", "background-color: " + (b ? "lightgreen" : "lightcoral")));
                } else {
                    row.add(new HTMLObject("td", obj));
                }
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
            if (this.hasJson) {
                this.add(js("function syntaxHighlight(json) {\n" +
                        "        json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');\n" +
                        "        return json.replace(/(\"(\\\\u[a-zA-Z0-9]{4}|\\\\[^u]|[^\\\\\"])*\"(\\s*:)?|\\b(true|false|null)\\b|-?\\d+(?:\\.\\d*)?(?:[eE][+\\-]?\\d+)?)/g, function (match) {\n" +
                        "            var cls = 'number';\n" +
                        "            if (/^\"/.test(match)) {\n" +
                        "                if (/:$/.test(match)) {\n" +
                        "                    cls = 'key';\n" +
                        "                } else {\n" +
                        "                    cls = 'string';\n" +
                        "                }\n" +
                        "            } else if (/true|false/.test(match)) {\n" +
                        "                cls = 'boolean';\n" +
                        "            } else if (/null/.test(match)) {\n" +
                        "                cls = 'null';\n" +
                        "            }\n" +
                        "            return '<span class=\"' + cls + '\">' + match + '</span>';\n" +
                        "        });\n" +
                        "    }\n" +
                        "\n" +
                        "    const elements = document.getElementsByClassName(\"json-snippet\");\n" +
                        "    const amount = elements.length;\n" +
                        "    for (let i = 0; i < amount; i++) {\n" +
                        "        const element = elements[0];\n" +
                        "        const div = document.createElement(\"pre\");\n" +
                        "        div.innerHTML = syntaxHighlight(JSON.stringify(JSON.parse(element.innerText), undefined, 4));\n" +
                        "        element.parentNode.replaceChild(div, element);\n" +
                        "    }"));
            }

            var file = getFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(this.html.toString());
            bw.close();

            Palladium.LOGGER.info("Successfully generated documentation file: {}", file.getName());
        } catch (IOException e) {
            Palladium.LOGGER.warn("Error while build documentation: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private @NotNull File getFile() {
        Path folder = FMLPaths.GAMEDIR.get().resolve(CodecDocumentationBuilder.DOCUMENTATION_FOLDER)
                .resolve(this.id.getNamespace());

        var file = folder.toFile();
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException("Could not create palladium documentation directory! Please create the directory yourself, or make sure the name is not taken by a file and you have permission to create directories.");

        file = folder.resolve(this.id.getPath() + ".html").toFile();
        return file;
    }

    public static class HTMLObject {

        private final String tag;
        private final List<Object> content = new LinkedList<>();
        private final List<Pair<String, String>> attributes = new ArrayList<>();

        public HTMLObject(String tag) {
            this(tag, new Object[0]);
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
            this.attributes.forEach(pair -> result.append(" ").append(pair.getFirst()).append("=\"").append(pair.getSecond()).append("\""));
            result.append(">").append("\n");
            this.content.forEach(content -> {
                if (content instanceof Supplier<?>) {
                    result.append(((Supplier<?>) content).get().toString());
                } else if (content != null) {
                    if (content instanceof String[] strings) {
                        result.append(Arrays.toString(strings));
                    } else {
                        result.append(content);
                    }
                }
            });
            if (!this.tag.equals("br") && !this.tag.equals("hr") && !this.tag.equals("link")) {
                result.append("</").append(this.tag).append(">");
            }
            return result.append("\n").toString();
        }
    }
}
