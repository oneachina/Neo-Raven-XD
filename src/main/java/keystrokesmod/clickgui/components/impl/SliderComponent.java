package keystrokesmod.clickgui.components.impl;

import keystrokesmod.Client;
import keystrokesmod.clickgui.components.Component;
import keystrokesmod.module.impl.client.Gui;
import keystrokesmod.module.setting.Setting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Utils;
import keystrokesmod.utility.profile.ProfileModule;
import keystrokesmod.utility.render.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderComponent extends Component {
    private final SliderSetting sliderSetting;
    private int o;
    private int x;
    private int y;
    private boolean d = false;
    private double w;

    public SliderComponent(SliderSetting sliderSetting, ModuleComponent moduleComponent, int o) {
        super(moduleComponent);
        this.sliderSetting = sliderSetting;
        this.x = moduleComponent.categoryComponent.getX() + moduleComponent.categoryComponent.gw();
        this.y = moduleComponent.categoryComponent.getY() + moduleComponent.offset;
        this.o = o;
    }

    @Override
    public Setting getSetting() {
        return sliderSetting;
    }

    public void render() {
        RenderUtils.drawRoundedRectangle(this.parent.categoryComponent.getX() + 4, this.parent.categoryComponent.getY() + this.o + 11, this.parent.categoryComponent.getX() + 4 + this.parent.categoryComponent.gw() - 8, this.parent.categoryComponent.getY() + this.o + 15, 3, -12302777);
        int l = this.parent.categoryComponent.getX() + 4;
        int r = this.parent.categoryComponent.getX() + 4 + (int) this.w;
        if (r - l > 84) {
            r = l + 84;
        }

        RenderUtils.drawRoundedRectangle(l, this.parent.categoryComponent.getY() + this.o + 11, r, this.parent.categoryComponent.getY() + this.o + 15, 3, Gui.getColor());
        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        String value;
        double input = this.sliderSetting.getInput();
        String info = this.sliderSetting.getPrettyInfo();
        if (this.sliderSetting.isString) {
            value = this.sliderSetting.getOptions()[(int) this.sliderSetting.getInput()];
        } else {
            value = Utils.isWholeNumber(input) ? (int) input + "" : String.valueOf(input);
        }
        getFont().drawString(
                this.sliderSetting.getName() + ": " + value + " " + info,
                (float) ((int) ((float) (this.parent.categoryComponent.getX() + 4) * 2.0F)),
                (float) ((int) ((float) (this.parent.categoryComponent.getY() + this.o + 3) * 2.0F)),
                color, true
        );
        GL11.glPopMatrix();
    }

    public void so(int n) {
        this.o = n;
    }

    public void onDrawScreen(int x, int y) {
        this.y = this.parent.categoryComponent.getY() + this.o;
        this.x = this.parent.categoryComponent.getX();
        double d = Math.min(this.parent.categoryComponent.gw() - 8, Math.max(0, x - this.x));
        this.w = (double) (this.parent.categoryComponent.gw() - 8) * (this.sliderSetting.getInput() - this.sliderSetting.getMin()) / (this.sliderSetting.getMax() - this.sliderSetting.getMin());
        if (this.d) {
            if (d == 0.0D) {
                this.sliderSetting.setValue(this.sliderSetting.getMin());
                parent.categoryComponent.render();
            } else {
                double n = roundToInterval(d / (double) (this.parent.categoryComponent.gw() - 8) * (this.sliderSetting.getMax() - this.sliderSetting.getMin()) + this.sliderSetting.getMin());
                this.sliderSetting.setValue(n);
                parent.categoryComponent.render();
            }
            if (Client.currentProfile != null) {
                ((ProfileModule) Client.currentProfile.getModule()).saved = false;
            }
        }

    }

    private static double roundToInterval(double v) {
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void onClick(int x, int y, int b) {
        if (this.getSetting() != null && !this.getSetting().isVisible()) return;

        if (this.u(x, y) && b == 0 && this.parent.open) {
            this.d = true;
        }

        if (this.i(x, y) && b == 0 && this.parent.open) {
            this.d = true;
        }

    }

    public void mouseReleased(int x, int y, int m) {
        this.d = false;
    }

    public boolean u(int x, int y) {
        return x > this.x && x < this.x + this.parent.categoryComponent.gw() / 2 + 1 && y > this.y && y < this.y + 16;
    }

    public boolean i(int x, int y) {
        return x > this.x + this.parent.categoryComponent.gw() / 2 && x < this.x + this.parent.categoryComponent.gw() && y > this.y && y < this.y + 16;
    }

    public void onGuiClosed() {
        this.d = false;
    }
}
