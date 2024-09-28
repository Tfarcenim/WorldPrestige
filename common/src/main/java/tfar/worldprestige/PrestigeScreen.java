package tfar.worldprestige;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import tfar.worldprestige.network.server.C2SSelectPrestigePowerPacket;
import tfar.worldprestige.platform.Services;

public class PrestigeScreen extends Screen {

    private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation("textures/gui/demo_background.png");


    public PrestigeScreen(Component $$0) {
        super($$0);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE,button -> {
            Services.PLATFORM.sendToServer(new C2SSelectPrestigePowerPacket(0));
        }).size(100,20).pos(width/2-50,height/2 +40).build());
    }

    @Override
    public void renderBackground(GuiGraphics p_283391_) {
        super.renderBackground(p_283391_);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        p_283391_.blit(BACKGROUND_LOCATION, i, j, 0, 0, 248, 166);
    }

    /**
     * Renders the graphical user interface (GUI) element.
     * @param pGuiGraphics the GuiGraphics object used for rendering.
     * @param pMouseX the x-coordinate of the mouse cursor.
     * @param pMouseY the y-coordinate of the mouse cursor.
     * @param pPartialTick the partial tick time.
     */
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        pGuiGraphics.drawString(this.font, this.title, i, j, 0x1f1f1f, false);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
