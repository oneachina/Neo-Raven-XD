package keystrokesmod.module.impl.other;

import keystrokesmod.event.network.ReceivePacketEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ModeSetting;
import keystrokesmod.utility.PacketUtils;
import keystrokesmod.utility.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import keystrokesmod.event.network.AttackEntityEvent;
import keystrokesmod.eventbus.annotations.EventListener;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import org.jetbrains.annotations.NotNull;

public class KillMessage extends Module {
    public static final String[] BIPAS_MESSAGE = {
            "Mit Icarus w re das nicht passiert",
            "POV: Icarus",
            "Nova is 'THE BEST CLIENT 2024', trust, no auto ban",
            "Vesper ist kein Exitscam, vertrau",
            "Spielst du Fortnite?",
            "Welcome to Meist... Meist Hacks?... Was ist den Meist Hacks?",
            "POV Icarus... Und jetzt einmal kurz... POV Augustus...",
            "Ah... Doof gelaufen f��r Augustus... Vielleicht n?chstes Mal...",
            "IQ Zellen",
            "Bro paid for a cheat to lose against me",
            "It's only cheating when you get caught!",
            "I'm on Immaculate rn, btw",
            "I'm on AstroWare rn, btw",
            "I'm on Wurst rn, btw",
            "Klientus ist keine Rat",
            "10/10 im HAZE Rating",
            "RAT im Clientlauncher / Ich wurde geRATTED!",
            "ESound Calling",
            "Adapt ist gut",
            "D��mmer als Toastbrot",
            "Jetzt erstmal 10 Minuten Rage Stream",
            "'Nius ist eine neutrale Quelle'~Verschmxtztxcole(geht so leicht in die rechte Richtung)",
            "'Alice Weidel ist nicht rechts'~Verschmxtztxcole(geht so leicht in die rechte Richtung)",
            "foiled again",
            "I love Nekomame",
            "slurp",
            "Polar is always watching",
            "e.setYaw(RotationUtils.serverYaw)",
            "Aus Protest Vernunft w?hlen ~ FDP",
            "Unser Client zuerst ~ FDP",
            "Piwo",
            "Bottom Text"
    };
    public static String killMessage = "This is a custom killMessage";

    private final ModeSetting mode;

    private EntityPlayer lastAttack = null;
    private long lastAttackTime = -1;

    public KillMessage() {
        super("KillMessage", category.other);
        this.registerSetting(mode = new ModeSetting("Mode", new String[]{"yby02", "Custom", "Heist"}, 0));
    }

    @EventListener
    public void onAttack(@NotNull AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer) {
            lastAttack = (EntityPlayer) event.getTarget();
            lastAttackTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onUpdate() {
        if (System.currentTimeMillis() - lastAttackTime > 20)
            lastAttack = null;

        if (lastAttack != null && lastAttack.isDead) {
            PacketUtils.sendPacket(new CPacketChatMessage(getKillMessage()));
            lastAttack = null;
        }
    }

    @EventListener
    public void onReceivePacket(@NotNull ReceivePacketEvent event) {
        if (lastAttack != null && event.getPacket() instanceof SPacketDestroyEntities) {
            SPacketDestroyEntities packet = (SPacketDestroyEntities) event.getPacket();
            for (int id : packet.getEntityIDs()) {
                if (id == lastAttack.getEntityId()) {
                    PacketUtils.sendPacket(new CPacketChatMessage(getKillMessage()));
                    lastAttack = null;
                    return;
                }
            }
        }
    }

    private String getKillMessage() {
        switch ((int) mode.getInput()) {
            case 0:
                return "���Ǻã��ҽ�Esound�����Ѿ���02������Neverlose��ɱ";
            case 1:
                return killMessage;
            case 2:
                return BIPAS_MESSAGE[Utils.randomizeInt(0, BIPAS_MESSAGE.length - 1)];
        }
        return "";
    }
}
