package keystrokesmod.module.impl.client;

import keystrokesmod.event.network.SendPacketEvent;
import keystrokesmod.eventbus.annotations.EventListener;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ModeSetting;
import keystrokesmod.utility.Commands;
import net.minecraft.network.play.client.CPacketChatMessage;
import keystrokesmod.Client;
import org.jetbrains.annotations.NotNull;

public class CommandChat extends Module {
    private static final String[] IDENTIFIERS = new String[]{".", "#", "@"};
    private static final ModeSetting identifier = new ModeSetting("Identifier", IDENTIFIERS, 0);

    public CommandChat() {
        super("Command chat", category.client);
        this.registerSetting(identifier);
        this.canBeEnabled = false;
        Client.EVENT_BUS.register(this);
    }

    public static String getIdentifier() {
        return IDENTIFIERS[(int) identifier.getInput()];
    }

    @EventListener(priority = -2)
    public void onSendPacket(@NotNull SendPacketEvent event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            final String message = ((CPacketChatMessage) event.getPacket()).getMessage();

            if (message.startsWith(getIdentifier())) {
                event.cancel();

                Commands.rCMD(message.substring(1));
            }
        }
    }
}
