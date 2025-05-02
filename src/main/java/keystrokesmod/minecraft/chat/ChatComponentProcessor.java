package keystrokesmod.minecraft.chat;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer; // 新增导入

public class ChatComponentProcessor {
    public static IChatComponent processComponent(ICommandSender p_processComponent_0_, IChatComponent p_processComponent_1_, Entity p_processComponent_2_) throws CommandException {
        IChatComponent lvt_3_1_ = null;
        if (p_processComponent_1_ instanceof ChatComponentScore) {
            ChatComponentScore lvt_4_1_ = (ChatComponentScore)p_processComponent_1_;
            String lvt_5_1_ = lvt_4_1_.getName();
            if (PlayerSelector.hasArguments(lvt_5_1_)) {
                try {
                    // 获取 MinecraftServer 实例
                    MinecraftServer server = p_processComponent_0_.getServer();
                    List<Entity> lvt_6_1_ = CommandBase.getEntityList(server, p_processComponent_0_, lvt_5_1_);
//                    if (lvt_6_1_.size() != 1) {
//                        throw new EntityNotFoundException(CommandException.getMessage());
//                    }

                    lvt_5_1_ = lvt_6_1_.get(0).getName();
                } catch (CommandException e) {
                    throw new EntityNotFoundException(e.getMessage());
                }
            }

            lvt_3_1_ = p_processComponent_2_ != null && lvt_5_1_.equals("*") ? new ChatComponentScore(p_processComponent_2_.getName(), lvt_4_1_.getObjective()) : new ChatComponentScore(lvt_5_1_, lvt_4_1_.getObjective());
            ((ChatComponentScore)lvt_3_1_).setValue(lvt_4_1_.getUnformattedTextForChat());
        } else if (p_processComponent_1_ instanceof ChatComponentSelector) {
            String lvt_4_2_ = ((ChatComponentSelector)p_processComponent_1_).getSelector();
            try {
                MinecraftServer server = p_processComponent_0_.getServer();
                List<Entity> entities = null;
                if (server != null) {
                    entities = CommandBase.getEntityList(server, p_processComponent_0_, lvt_4_2_);
                }
                if (entities != null && !entities.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (Entity entity : entities) {
                        sb.append(entity.getName()).append(" ");
                    }
                    lvt_3_1_ = new ChatComponentText(sb.toString().trim());
                }
            } catch (CommandException e) {
                lvt_3_1_ = new ChatComponentText("");
            }
        } else if (p_processComponent_1_ instanceof ChatComponentText) {
            lvt_3_1_ = new ChatComponentText(((ChatComponentText)p_processComponent_1_).getChatComponentText_TextValue());
        } else {
            if (!(p_processComponent_1_ instanceof ChatComponentTranslation)) {
                return p_processComponent_1_;
            }

            Object[] lvt_4_3_ = ((ChatComponentTranslation)p_processComponent_1_).getFormatArgs();

            for(int lvt_5_2_ = 0; lvt_5_2_ < lvt_4_3_.length; ++lvt_5_2_) {
                Object lvt_6_2_ = lvt_4_3_[lvt_5_2_];
                if (lvt_6_2_ instanceof IChatComponent) {
                    lvt_4_3_[lvt_5_2_] = processComponent(p_processComponent_0_, (IChatComponent)lvt_6_2_, p_processComponent_2_);
                }
            }

            lvt_3_1_ = new ChatComponentTranslation(((ChatComponentTranslation)p_processComponent_1_).getKey(), lvt_4_3_);
        }

        ChatStyle lvt_4_4_ = p_processComponent_1_.getChatStyle();
        if (lvt_4_4_ != null) {
            ((IChatComponent)lvt_3_1_).setChatStyle(lvt_4_4_.createShallowCopy());
        }

        Iterator lvt_5_3_ = p_processComponent_1_.getSiblings().iterator();

        while(lvt_5_3_.hasNext()) {
            IChatComponent lvt_6_3_ = (IChatComponent)lvt_5_3_.next();
            ((IChatComponent)lvt_3_1_).appendSibling(processComponent(p_processComponent_0_, lvt_6_3_, p_processComponent_2_));
        }

        return (IChatComponent)lvt_3_1_;
    }
}

