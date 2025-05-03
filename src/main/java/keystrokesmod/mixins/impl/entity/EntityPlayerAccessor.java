package keystrokesmod.mixins.impl.entity;


import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityPlayer.class)
public interface EntityPlayerAccessor {

    @Unique
    void neo_Raven_XD_Test$setItemInUseCount(int count);
}
