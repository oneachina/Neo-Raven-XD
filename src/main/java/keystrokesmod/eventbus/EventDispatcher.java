package keystrokesmod.eventbus;

import keystrokesmod.Client;
import keystrokesmod.event.client.PreTickEvent;
import keystrokesmod.event.network.ClientChatReceivedEvent;
import keystrokesmod.event.network.ReceivePacketEvent;
import keystrokesmod.event.render.*;
import keystrokesmod.event.world.BlockPlaceEvent;
import keystrokesmod.event.world.EntityJoinWorldEvent;
import keystrokesmod.event.world.WorldChangeEvent;
import keystrokesmod.eventbus.annotations.EventListener;
import keystrokesmod.utility.Utils;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import static keystrokesmod.Client.mc;

public final class EventDispatcher {
    private static final EventDispatcher INSTANCE = new EventDispatcher();

    private static WorldClient lastWorld = null;

    public static void init() {
        Client.EVENT_BUS.register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @EventListener
    public void onReceivePacket(@NotNull ReceivePacketEvent event) {
        if (event.getPacket() instanceof SPacketChat) {
            SPacketChat packet = (SPacketChat) event.getPacket();
            Client.EVENT_BUS.post(new ClientChatReceivedEvent(packet.getChatComponent(), packet.getType()));
        }
    }

    @EventListener
    public void onPreTick(PreTickEvent event) {
        if (!Utils.nullCheck()) return;

        if (mc.world != lastWorld) {
            lastWorld = mc.world;
            Client.EVENT_BUS.post(new WorldChangeEvent());
        }
    }

    /**
     * TODO Recode this with mixin for better performance and obf compatibility.
     */
    @SubscribeEvent
    public void onPreRenderNameTag(RenderLivingEvent.Specials.@NotNull Pre<EntityLivingBase> baseEvent) {
        PreRenderNameTag event = new PreRenderNameTag(baseEvent.getEntity(), baseEvent.getX(), baseEvent.getY(), baseEvent.getZ());
        Client.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            baseEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(net.minecraftforge.event.entity.@NotNull EntityJoinWorldEvent event) {
        Client.EVENT_BUS.post(new EntityJoinWorldEvent(event.getEntity()));
    }

    @SubscribeEvent
    public void onBlockPlaceEvent(BlockEvent.@NotNull PlaceEvent event) {
        Client.EVENT_BUS.post(new BlockPlaceEvent(event.getPlayer(), event.getPos(), event.getState()));
    }

    @SubscribeEvent
    public void onDrawBlockHighlightEvent(net.minecraftforge.client.event.@NotNull DrawBlockHighlightEvent event) {
        Client.EVENT_BUS.post(new DrawBlockHighlightEvent(
                event.getContext(), event.getPlayer(), event.getTarget(), event.getSubID(), event.getPlayer().getHeldItem(EnumHand.MAIN_HAND), event.getPartialTicks()
        ));
    }

    @SubscribeEvent
    public void onFOVUpdate(net.minecraftforge.client.event.@NotNull FOVUpdateEvent baseEvent) {
        if (baseEvent.getEntity() != mc.player) return;
        FOVUpdateEvent event = new FOVUpdateEvent(baseEvent.getFov(), baseEvent.getNewfov());
        Client.EVENT_BUS.post(event);
        baseEvent.setNewfov(event.getNewFov());
    }

    @SubscribeEvent
    public void onPreRenderPlayer(RenderPlayerEvent.@NotNull Pre baseEvent) {
        if (!(baseEvent.getEntity() instanceof EntityPlayer)) return;
        PreRenderPlayerEvent event = new PreRenderPlayerEvent(
                (EntityPlayer) baseEvent.getEntity(), baseEvent.getRenderer(), baseEvent.getPartialRenderTick(),
                baseEvent.getX(), baseEvent.getY(), baseEvent.getZ()
        );
        Client.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            baseEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPostRenderPlayer(RenderPlayerEvent.@NotNull Post baseEvent) {
        if (!(baseEvent.getEntity() instanceof EntityPlayer)) return;
        Client.EVENT_BUS.post(new PostRenderPlayerEvent(
                (EntityPlayer) baseEvent.getEntity(), baseEvent.getRenderer(), baseEvent.getPartialRenderTick(),
                baseEvent.getX(), baseEvent.getY(), baseEvent.getZ()
        ));
    }
}
