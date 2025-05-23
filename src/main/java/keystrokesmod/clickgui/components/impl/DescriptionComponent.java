package keystrokesmod.clickgui.components.impl;

import keystrokesmod.clickgui.components.Component;
import keystrokesmod.module.impl.client.Gui;
import keystrokesmod.module.setting.Setting;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.utility.Theme;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class DescriptionComponent extends Component {
    private final DescriptionSetting desc;

    @Nullable
    @Override
    public Setting getSetting() {
        return desc;
    }

    public DescriptionComponent(DescriptionSetting desc, ModuleComponent b, int o) {
        super(b);
        this.desc = desc;
        this.x = b.categoryComponent.getX() + b.categoryComponent.gw();
        this.y = b.categoryComponent.getY() + b.offset;
        this.o = o;
    }

    public void render() {
        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        getFont().drawString(this.desc.getPrettyDesc(), (float) ((this.parent.categoryComponent.getX() + 4) * 2), (float) ((this.parent.categoryComponent.getY() + this.o + 4) * 2), Gui.getColor(), true);
        GL11.glPopMatrix();
    }

    public void so(int n) {
        this.o = n;
    }
}
