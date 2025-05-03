package keystrokesmod.mixins.impl.network;

import net.minecraft.network.play.client.CPacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketConfirmTransaction.class)
public interface C0FPacketConfirmTransactionAccessor {

    @Accessor("uid")
    void setUid(short uid);
}
