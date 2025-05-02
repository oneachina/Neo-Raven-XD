package keystrokesmod.utility;

import keystrokesmod.event.player.PostUpdateEvent;
import keystrokesmod.event.network.ReceivePacketEvent;
import keystrokesmod.event.network.SendPacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import keystrokesmod.eventbus.annotations.EventListener;
import org.jetbrains.annotations.NotNull;

public class BadPacketsHandler { // ensures you don't get banned
    public boolean C08;
    public boolean C07;
    public boolean C09;
    public boolean delayAttack;
    public boolean delay;
    public int playerSlot = -1;
    public int serverSlot = -1;

    @EventListener(priority = 2)
    public void onSendPacket(@NotNull SendPacketEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPacket() instanceof CPacketUseEntity) { // sending a C07 on the same tick as C02 can ban, this usually happens when you unblock and attack on the same tick
            if (C07) {
                event.cancel();
            }
        }
        else if (event.getPacket() != null) {
            C08 = true;
        }
    }

    @EventListener
    public void onReceivePacket(ReceivePacketEvent e) {
        if (e.getPacket() instanceof SPacketHeldItemChange) {
            SPacketHeldItemChange packet = (SPacketHeldItemChange) e.getPacket();
            if (packet.getHeldItemHotbarIndex() >= 0 && packet.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize()) {
                serverSlot = packet.getHeldItemHotbarIndex();
            }
        }
        else if (e.getPacket() instanceof SPacketSpawnPlayer && Minecraft.getMinecraft().player != null) {
            if (((SPacketSpawnPlayer) e.getPacket()).getEntityID() != Minecraft.getMinecraft().player.getEntityId()) {
                return;
            }
            this.playerSlot = -1;
        }
    }

    @EventListener(priority = 2)
    public void onPostUpdate(PostUpdateEvent e) {
        if (delay) {
            delayAttack = false;
            delay = false;
        }
        if (C08 || C09) {
            delay = true;
            delayAttack = true;
        }
        C08 = C07 = C09 = false;
    }
}
