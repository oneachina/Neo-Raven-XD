package keystrokesmod.event.network;

import keystrokesmod.eventbus.CancellableEvent;
import keystrokesmod.minecraft.chat.IChatComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ClientChatReceivedEvent extends CancellableEvent {
    private IChatComponent message;
    private byte type;

    public ClientChatReceivedEvent(ITextComponent chatComponent, ChatType type) {
        super();
    }
}
