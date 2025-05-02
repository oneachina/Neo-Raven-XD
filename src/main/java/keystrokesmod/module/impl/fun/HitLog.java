package keystrokesmod.module.impl.fun;

import keystrokesmod.Client;
import keystrokesmod.event.network.ReceivePacketEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.setting.impl.ModeSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.script.classes.Vec3;
import keystrokesmod.utility.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import keystrokesmod.eventbus.annotations.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class HitLog extends Module {
    private static final ModeSetting language = new ModeSetting("Language", new String[]{"English", "Chinese"}, 0);
    private static final SliderSetting coolDown = new SliderSetting("CoolDown", 100, 0, 1000, 50);
    private static long lastS08 = 0;
    private static long lastAttack = 0;

    public HitLog() {
        super("HitLog", category.fun);
        this.registerSetting(language, coolDown);
    }

    public static void onAttack(int predTicks, EntityLivingBase target, Vec3 predHitPos, Vec3 selfPos, float yaw, float pitch) {
        if (!ModuleManager.hitLog.isEnabled()) return;
        if (target == null) return;
        if (System.currentTimeMillis() - lastAttack < coolDown.getInput()) return;
        lastAttack = System.currentTimeMillis();

        Client.getExecutor().schedule(() -> {
            HitPos hitPos = HitPos.fromY(predHitPos.y(), target);

            if (target.hurtTime == 0) {
                Reason reason;
                if (!target.getEntityBoundingBox().isVecInside(predHitPos.toVec3())) {
                    reason = Reason.PRED_FAIL;
                } else if (Math.round(System.currentTimeMillis() - lastS08) <= predTicks) {
                    reason = Reason.WATCHDOG;
                } else if (target instanceof EntityPlayer && ((EntityPlayer) target).isBlocking()) {
                    reason = Reason.BLOCK;
                } else {
                    return;
                }

                switch ((int) language.getInput()) {
                    case 0:
                        Utils.sendMessage("Miss " + target.getName() + "'s " + hitPos.getEnglish() + " Cause " + reason.getEnglish() + " | Trying to predict " + predTicks + " ticks");
                        break;
                    case 1:
                        Utils.sendMessage("���� " + target.getName() + "��" + hitPos.getChinese() + " ԭ�� " + reason.getChinese() + " | ����Ԥ�� " + predTicks + " ticks");
                        break;
                }
            } else {
                switch ((int) language.getInput()) {
                    case 0:
                        Utils.sendMessage("Hit " + target.getName() + "'s " + hitPos.getEnglish() + " | Trying to predict " + predTicks + " ticks | Health " + target.getHealth());
                        break;
                    case 1:
                        Utils.sendMessage("���� " + target.getName() + "��" + hitPos.getChinese() + " | ����Ԥ�� " + predTicks + " ticks | Ѫ�� " + target.getHealth());
                        break;
                }
            }
        }, predTicks * 50L, TimeUnit.MILLISECONDS);
    }

    @EventListener
    public void onReceivePacket(@NotNull ReceivePacketEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            lastS08 = System.currentTimeMillis();
        }
    }

    @AllArgsConstructor
    @Getter
    enum Reason {
        PRED_FAIL("Predicted failed", "Ԥ��ʧ��"),
        WATCHDOG("Watchdog", "Watchdog"),
        BLOCK("Blocking", "��");

        private final String english;
        private final String chinese;
    }

    @AllArgsConstructor
    @Getter
    public enum HitPos {
        HEAD("head", "ͷ��"),
        BODY("body", "����"),
        FOOT("foot", "��");

        private final String english;
        private final String chinese;

        public static HitPos fromY(double hitY, @NotNull EntityLivingBase entity) {
            AxisAlignedBB box = entity.getEntityBoundingBox();
            if (hitY > box.maxY - (box.maxY - Utils.getEyePos(entity).y()) * 2) {
                return HEAD;
            } else if (box.maxY - hitY <= 0.8125) {
                return FOOT;
            } else {
                return BODY;
            }
        }
    }
}
