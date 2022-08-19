package net.threetag.palladium.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.threetag.palladium.addonpack.log.AddonPackLogEntry;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class AddonPackLogEntryScreen extends Screen {

    public final Screen parent;
    private final AddonPackLogEntry entry;
    private Panel panel;

    protected AddonPackLogEntryScreen(Screen parent, AddonPackLogEntry entry) {
        super(new TranslatableComponent("gui.palladium.addon_pack_log_entry"));
        this.parent = parent;
        this.entry = entry;
    }

    @Override
    protected void init() {
        super.init();

        this.addWidget(this.panel = new Panel(this.minecraft, this.width, this.height - 64 - 48, 48, 0));

        this.addRenderableWidget(new Button(this.width / 2 - 310, this.height - 64 + 32 - 10, 250, 20, new TranslatableComponent("gui.palladium.addon_pack_log_entry.copy_to_clipboard"), (button) -> {
            Objects.requireNonNull(this.minecraft).keyboardHandler.setClipboard(this.entry.getText() + "\n" + this.entry.getStacktrace());
        }));

        this.addRenderableWidget(new Button(this.width / 2 - 50, this.height - 64 + 32 - 10, 250, 20, new TranslatableComponent("gui.palladium.addon_pack_log_entry.upload_to_pastebin"), (button) -> {
            try {
                String url = this.uploadPastebin();
                Objects.requireNonNull(this.minecraft).setScreen(new ConfirmLinkScreen((b) -> {
                    if (b) {
                        Util.getPlatform().openUri(url);
                    }

                    this.minecraft.setScreen(this);
                }, url, true));
            } catch (Exception ignored) {

            }
        }));

        this.addRenderableWidget(new Button(this.width / 2 + 310 - 70, this.height - 64 + 32 - 10, 75, 20, CommonComponents.GUI_BACK, (button) -> {
            Objects.requireNonNull(this.minecraft).setScreen(this.parent);
        }));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);

        if (this.panel != null)
            this.panel.render(poseStack, mouseX, mouseY, partialTicks);

        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 16777215);

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    public String uploadPastebin() {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL("https://pastebin.com/api/api_post.php");
            String key = "r-BiiqpblHeaAi2CZ8cxu7xG9OzRE2yL";
            String urlParameters = "api_option=paste&api_paste_private=1&api_paste_name=" + "Palladium Addon Pack Log" + "&api_dev_key=" + key + "&api_paste_code=" + this.entry.getText() + "\n" + this.entry.getStacktrace();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    class Panel extends ScrollPanel {

        private final List<FormattedCharSequence> lines1;
        private final List<FormattedCharSequence> lines2;

        public Panel(Minecraft client, int width, int height, int top, int left) {
            super(client, width, height, top, left);
            this.lines1 = font.split(new TextComponent(AddonPackLogEntryScreen.this.entry.getText()), this.width - 25);
            this.lines2 = font.split(new TextComponent(AddonPackLogEntryScreen.this.entry.getStacktrace()), this.width - 25);
        }

        @Override
        protected int getContentHeight() {
            return this.lines1.size() * 15 + 15 + this.lines2.size() * 15;
        }

        @Override
        protected void drawPanel(PoseStack mStack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
            RenderSystem.enableBlend();
            relativeY += 15;

            for (FormattedCharSequence processor : this.lines1) {
                font.draw(mStack, processor, left + 10, relativeY, 0xfefefe);
                relativeY += 15;
            }

            Gui.fill(mStack, left + 10, relativeY, left + width - 10, ++relativeY, 0xfffefefe);
            relativeY += 15;

            for (FormattedCharSequence processor : this.lines2) {
                font.draw(mStack, processor, left + 10, relativeY, 0xfefefe);
                relativeY += 15;
            }

            RenderSystem.disableBlend();
        }

        @Override
        public NarrationPriority narrationPriority() {
            return NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {

        }
    }
}
