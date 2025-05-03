package keystrokesmod.clickgui.components.impl;

import keystrokesmod.Client;
import keystrokesmod.clickgui.components.Component;
import keystrokesmod.module.setting.Setting;
import keystrokesmod.module.setting.impl.ModeSetting;
import lombok.Setter;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ModeComponent extends Component {
    private final ModeSetting modeSetting;  // Lowercase for instance variable
    // Renamed method for clarity
    @Setter
    private int offset;  // Renamed from 'o' to 'offset'
    public ModeComponent(ModeSetting modeSetting, ModuleComponent parentModule, int offset) {
        super(parentModule);
        this.modeSetting = modeSetting;
        this.x = parentModule.categoryComponent.getX() + parentModule.categoryComponent.gw();
        this.y = parentModule.categoryComponent.getY() + parentModule.offset;
        this.offset = offset;
    }

    @Override
    public Setting getSetting() {
        return modeSetting;
    }

    public void render() {
        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);

        // More descriptive variable name
        String currentMode = this.modeSetting.getPrettyOptions()[(int) this.modeSetting.getInput()];
        getFont().drawString(
                this.modeSetting.getPrettyName() + ": " + currentMode,
                (float) ((int) ((float) (this.parent.categoryComponent.getX() + 4) * 2.0F)),
                (float) ((int) ((float) (this.parent.categoryComponent.getY() + this.offset + 3) * 2.0F)),
                color, true
        );

        GL11.glPopMatrix();
    }

    public void updatePosition(int x, int y) {  // Renamed from onDrawScreen
        this.y = this.parent.categoryComponent.getY() + this.offset;
        this.x = this.parent.categoryComponent.getX();
    }

    // More descriptive parameter names
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        if (this.getSetting() != null && !this.getSetting().isVisible()) return;

        if (isHover(mouseX, mouseY) && this.parent.open) {  // Assuming 'po' means parent open
            changeMode(mouseButton, Keyboard.isKeyDown(Client.mc.gameSettings.keyBindSneak.getKeyCode()));
            parent.categoryComponent.render();
        }
    }

    private void changeMode(int mouseButton, boolean isShiftDown) {
        boolean directionForward;
        switch (mouseButton) {
            case 0: // Left click
                directionForward = true;
                break;
            case 1: // Right click
                directionForward = false;
                break;
            default:
                return;
        }

        if (isShiftDown) {
            directionForward = !directionForward;
        }

        if (directionForward) {
            this.modeSetting.nextValue();
        } else {
            this.modeSetting.prevValue();
        }
    }
}