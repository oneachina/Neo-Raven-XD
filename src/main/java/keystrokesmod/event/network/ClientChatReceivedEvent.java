package keystrokesmod.event.network;

import keystrokesmod.eventbus.CancellableEvent;
import keystrokesmod.minecraft.chat.IChatComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ClientChatReceivedEvent extends CancellableEvent {
    private IChatComponent message;
    private byte type;
}
