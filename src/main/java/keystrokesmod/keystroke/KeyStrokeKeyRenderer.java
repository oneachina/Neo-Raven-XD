package keystrokesmod.keystroke;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyStrokeKeyRenderer {
    private final Minecraft a = Minecraft.getMinecraft();
    private final KeyBinding keyBinding;
    private final int c;
    private final int d;
    private boolean e = true;
    private long f = 0L;

    public KeyStrokeKeyRenderer(KeyBinding i, int j, int k) {
        this.keyBinding = i;
        this.c = j;
        this.d = k;
    }

    public void renderKey(int l, int m, int color) {
        boolean o = Keyboard.isKeyDown(this.keyBinding.getKeyCode()) || this.keyBinding.isKeyDown();
        String p = Keyboard.getKeyName(this.keyBinding.getKeyCode());
        if (o != this.e) {
            this.e = o;
            this.f = System.currentTimeMillis();
        }

        int g;
        double h;
        if (o) {
            g = Math.min(255, (int) (2L * (System.currentTimeMillis() - this.f)));
            h = Math.max(0.0D, 1.0D - (double) (System.currentTimeMillis() - this.f) / 20.0D);
        } else {
            int b = 255 - (int) (2L * (System.currentTimeMillis() - this.f));
            g = Math.max(0, b);
            h = Math.min(1.0D, (double) (System.currentTimeMillis() - this.f) / 20.0D);
        }

        int q = color >> 16 & 255;
        int r = color >> 8 & 255;
        int s = color & 255;
        int c = (new Color(q, r, s)).getRGB();
        Gui.drawRect(l + this.c, m + this.d, l + this.c + 22, m + this.d + 22, 2013265920 + (g << 16) + (g << 8) + g);
        if (KeyStroke.f) {
            Gui.drawRect(l + this.c, m + this.d, l + this.c + 22, m + this.d + 1, c);
            Gui.drawRect(l + this.c, m + this.d + 21, l + this.c + 22, m + this.d + 22, c);
            Gui.drawRect(l + this.c, m + this.d, l + this.c + 1, m + this.d + 22, c);
            Gui.drawRect(l + this.c + 21, m + this.d, l + this.c + 22, m + this.d + 22, c);
        }

        this.a.fontRenderer.drawString(p, l + this.c + 8, m + this.d + 8, -16777216 + ((int) ((double) q * h) << 16) + ((int) ((double) r * h) << 8) + (int) ((double) s * h));
    }
}
