package keystrokesmod.clickgui.components.impl;

import keystrokesmod.Client;
import keystrokesmod.clickgui.components.Component;
import keystrokesmod.clickgui.components.IComponent;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.client.Gui;
import keystrokesmod.module.setting.Setting;
import keystrokesmod.utility.render.RenderUtils;
import keystrokesmod.utility.profile.ProfileManagerModule;
import keystrokesmod.utility.profile.ProfileModule;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class ModuleComponent implements IComponent {
    private static final int c2 = (new Color(154, 2, 255)).getRGB();
    private static final int hoverColor = (new Color(0, 0, 0, 110)).getRGB();
    private static final int UNSAVED_COLOR = new Color(114, 188, 250).getRGB();
    private static final int INVALID_COLOR = new Color(255, 80, 80).getRGB();
    private static final int ENABLED_COLOR = new Color(24, 154, 255).getRGB();
    private static final int DISABLED_COLOR = new Color(192, 192, 192).getRGB();
    public static final int NEW_ENABLED_COLOR = new Color(255, 255, 255, 0).getRGB();
    public static final int NEW_DISABLED_COLOR = new Color(255, 255, 255).getRGB();
    public Module mod;
    public CategoryComponent categoryComponent;
    public int offset;
    public ArrayList<Component> settings;
    public boolean open;
    private boolean hovering;

    public ModuleComponent(Module mod, CategoryComponent p, int offset) {
        this.mod = mod;
        this.categoryComponent = p;
        this.offset = offset;
        this.settings = new ArrayList<>();
        this.open = false;
        updateSetting();
    }

    public void updateSetting() {
        int y = offset + 12;
        if (mod != null && !mod.getSettings().isEmpty()) {
            this.settings.clear();
            for (Setting v : mod.getSettings()) {
                this.settings.add(Component.fromSetting(v, this, y));
                y += 12;
            }
        }
        this.settings.add(new BindComponent(this, y));
    }

    public void so(int n) {
        this.offset = n;
        int y = this.offset + 16;

        for (Component co : this.settings) {
            Setting setting = co.getSetting();
            if (setting == null || setting.isVisible()) {
                co.so(y);
                if (co instanceof SliderComponent) {
                    y += 16;
                } else {
                    y += 12;
                }
            }
        }
    }

    public static void e() {
        RenderUtils.enableGL2D();
    }

    public static void f() {
        RenderUtils.disableGL2D();
        GL11.glEdgeFlag(true);
    }

    public static void g(int h) {
        float a = 0.0F;
        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;
        GL11.glColor4f(r, g, b, a);
    }

    public static void v(float x, float y, float x1, float y1, int t, int b) {
        e();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        g(t);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        g(b);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        f();
    }

    public void render() {
        if (hovering) {
            if (ModuleManager.clientTheme.test.isToggled()) {
                RenderUtils.drawRoundedRectangle(this.categoryComponent.getX(), this.categoryComponent.getY() + offset, this.categoryComponent.getX() + this.categoryComponent.gw(), this.categoryComponent.getY() + 16 + this.offset, 5, mod.isEnabled() ? Component.NEW_TOGGLE_HOVER_COLOR : Component.NEW_HOVER_COLOR);
            } else {
                RenderUtils.drawRoundedRectangle(this.categoryComponent.getX(), this.categoryComponent.getY() + offset, this.categoryComponent.getX() + this.categoryComponent.gw(), this.categoryComponent.getY() + 16 + this.offset, 8, hoverColor);
            }
        } else if (ModuleManager.clientTheme.test.isToggled() && mod.isEnabled()) {
            RenderUtils.drawRoundedRectangle(this.categoryComponent.getX(), this.categoryComponent.getY() + offset, this.categoryComponent.getX() + this.categoryComponent.gw(), this.categoryComponent.getY() + 16 + this.offset, 5, Component.NEW_TOGGLE_DEFAULT_COLOR);
        }
        v((float) this.categoryComponent.getX(), (float) (this.categoryComponent.getY() + this.offset), (float) (this.categoryComponent.getX() + this.categoryComponent.gw()), (float) (this.categoryComponent.getY() + 15 + this.offset), this.mod.isEnabled() ? c2 : -12829381, this.mod.isEnabled() ? c2 : -12302777);
        int button_rgb = getButton_rgb();
        GL11.glPushMatrix();
        if (ModuleManager.clientTheme.test.isToggled()) {
            getFont().drawString(this.mod.getPrettyName(), (float) (this.categoryComponent.getX() + (double) this.categoryComponent.gw() / 2 - getFont().width(this.mod.getPrettyName()) / 2), (float) (this.categoryComponent.getY() + this.offset + 4), button_rgb);
        } else {
            getFont().drawStringWithShadow(this.mod.getPrettyName(), (float) (this.categoryComponent.getX() + (double) this.categoryComponent.gw() / 2 - getFont().width(this.mod.getPrettyName()) / 2), (float) (this.categoryComponent.getY() + this.offset + 4), button_rgb);
        }
        GL11.glPopMatrix();
        if (this.open && !this.settings.isEmpty()) {
            for (Component c : this.settings) {
                Setting setting = c.getSetting();
                if (setting == null || setting.isVisible()) {
                    c.render();
                }
            }
        }
    }

    private int getButton_rgb() {
        int button_rgb = ModuleManager.clientTheme.test.isToggled() ? NEW_DISABLED_COLOR : DISABLED_COLOR;
        if (mod.isEnabled()) {
            button_rgb = ModuleManager.clientTheme.test.isToggled() ? NEW_ENABLED_COLOR : ENABLED_COLOR;
        }
        if (this.mod.script != null && this.mod.script.error) {
            button_rgb = INVALID_COLOR;
        }
        if (this.mod.moduleCategory() == Module.category.profiles && !(this.mod instanceof ProfileManagerModule) && !((ProfileModule) this.mod).saved && Client.currentProfile.getModule() == this.mod) {
            button_rgb = UNSAVED_COLOR;
        }
        return button_rgb;
    }

    @Override
    public @NotNull ModuleComponent getParent() {
        return this;
    }

    public int return0() {
        if (!this.open) {
            return 16;
        } else {
            int h = 16;

            for (Component c : this.settings) {
                Setting setting = c.getSetting();
                if (setting == null || setting.isVisible()) {
                    if (c instanceof SliderComponent) {
                        h += 16;
                    } else {
                        h += 12;
                    }
                }
            }

            return h;
        }
    }

    public void onDrawScreen(int x, int y) {
        if (!this.settings.isEmpty()) {
            for (Component c : this.settings) {
                c.drawScreen(x, y);
            }
        }
        hovering = isHover(x, y);

        if (hovering && categoryComponent.isCategoryOpened() && Gui.toolTip.isToggled() && mod.getPrettyToolTip() != null) {
            Client.clickGui.run(() -> RenderUtils.drawToolTip(mod.getPrettyToolTip(), x, y));
        }
    }

    public String getName() {
        return mod.getName();
    }

    public void onClick(int x, int y, int b) {
        if (this.isHover(x, y) && b == 0 && this.mod.canBeEnabled()) {
            this.mod.toggle();
            if (this.mod.moduleCategory() != Module.category.profiles) {
                if (Client.currentProfile != null) {
                    ((ProfileModule) Client.currentProfile.getModule()).saved = false;
                }
            }
        }

        if (this.isHover(x, y) && b == 1) {
            this.open = !this.open;
            this.categoryComponent.render();
        }

        for (Component c : this.settings) {
            c.onClick(x, y, b);
        }
    }

    public void mouseReleased(int x, int y, int m) {
        for (Component c : this.settings) {
            c.mouseReleased(x, y, m);
        }

    }

    public void keyTyped(char t, int k) {
        for (Component c : this.settings) {
            c.keyTyped(t, k);
        }
    }

    public void onGuiClosed() {
        for (Component c : this.settings) {
            c.onGuiClosed();
        }
    }

    public boolean isHover(int x, int y) {
        return x > this.categoryComponent.getX() && x < this.categoryComponent.getX() + this.categoryComponent.gw() && y > this.categoryComponent.getY() + this.offset && y < this.categoryComponent.getY() + 16 + this.offset;
    }
}
