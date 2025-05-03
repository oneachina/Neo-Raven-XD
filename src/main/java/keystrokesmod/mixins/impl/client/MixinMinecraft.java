package keystrokesmod.mixins.impl.client;

import keystrokesmod.Client;
import keystrokesmod.event.client.ClickEvent;
import keystrokesmod.event.client.PreTickEvent;
import keystrokesmod.event.client.RightClickEvent;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.combat.HitBox;
import keystrokesmod.module.impl.combat.Reach;
import keystrokesmod.module.impl.exploit.ExploitFixer;
import keystrokesmod.module.impl.render.Animations;
import keystrokesmod.module.impl.render.FreeLook;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static keystrokesmod.Client.mc;

@Mixin(value = Minecraft.class, priority = 1001)
public abstract class MixinMinecraft {

    @Inject(method = "runTick", at = @At("HEAD"))
    private void runTickPre(CallbackInfo ci) {
        Client.EVENT_BUS.post(new PreTickEvent());
    }

    @Unique
    private void onRunTick$usingWhileDigging(CallbackInfo ci) {
        if (ModuleManager.animations != null && ModuleManager.animations.isEnabled() && Animations.swingWhileDigging.isToggled()
                && mc.gameSettings.keyBindAttack.isKeyDown()) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    @Unique
    private void neo_Raven_XD_Test$beforeSwingByClick(CallbackInfo ci) {
        ClickEvent event = new ClickEvent();
        Client.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    /**
     * @author xia__mc
     * @reason to fix reach and hitBox won't work with autoClicker
     */
    @Inject(method = "clickMouse", at = @At("HEAD"))
    private void onLeftClickMouse(CallbackInfo ci) {
        FreeLook.call();
        Reach.call();
        HitBox.call();
    }

    /**
     * @author xia__mc
     * @reason to fix freelook do impossible action
     */
    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    private void onRightClickMouse(CallbackInfo ci) {
        RightClickEvent event = new RightClickEvent();
        Client.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "crashed", at = @At("HEAD"), cancellable = true)
    private void onCrashed(CrashReport crashReport, CallbackInfo ci) {
        try {
            if (ExploitFixer.onCrash(crashReport)) {
                ci.cancel();
            }
        } catch (Throwable ignored) {
        }
    }

    @Inject(method = "createDisplay", at = @At(value = "RETURN"))
    private void onSetTitle(@NotNull CallbackInfo ci) {
        Display.setTitle(String.format("%s %s", Client.NAME, Client.VERSION));
    }
}
