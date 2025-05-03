package keystrokesmod.minecraft.chat;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public class ChatComponentScore extends ChatComponentStyle {
    @Getter
    private final String name;
    @Getter
    private final String objective;
    @Setter
    private String value = "";

    public ChatComponentScore(String p_i45997_1_, String p_i45997_2_) {
        this.name = p_i45997_1_;
        this.objective = p_i45997_2_;
    }

    public String getUnformattedTextForChat() {
        Minecraft mc = Minecraft.getMinecraft();
        MinecraftServer lvt_1_1_ = mc.getIntegratedServer();
        if (lvt_1_1_ != null && lvt_1_1_.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value)) {
            Scoreboard lvt_2_1_ = lvt_1_1_.getWorld(0).getScoreboard();
            ScoreObjective lvt_3_1_ = lvt_2_1_.getObjective(this.objective);
            if (lvt_3_1_ != null && lvt_2_1_.entityHasObjective(this.name, lvt_3_1_)) {
                Score lvt_4_1_ = lvt_2_1_.getOrCreateScore(this.name, lvt_3_1_);
                this.setValue(String.format("%d", lvt_4_1_.getScorePoints()));
            }
        }

        return this.value;
    }

    public ChatComponentScore createCopy() {
        ChatComponentScore lvt_1_1_ = new ChatComponentScore(this.name, this.objective);
        lvt_1_1_.setValue(this.value);
        lvt_1_1_.setChatStyle(this.getChatStyle().createShallowCopy());

        for (IChatComponent lvt_3_1_ : this.getSiblings()) {
            lvt_1_1_.appendSibling(lvt_3_1_.createCopy());
        }

        return lvt_1_1_;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof ChatComponentScore)) {
            return false;
        } else {
            ChatComponentScore lvt_2_1_ = (ChatComponentScore)p_equals_1_;
            return this.name.equals(lvt_2_1_.name) && this.objective.equals(lvt_2_1_.objective) && super.equals(p_equals_1_);
        }
    }

    public String toString() {
        return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}

