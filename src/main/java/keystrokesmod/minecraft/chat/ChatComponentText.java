package keystrokesmod.minecraft.chat;

import java.util.Iterator;

public class ChatComponentText extends ChatComponentStyle {
    private final String text;

    public ChatComponentText(String p_i45159_1_) {
        this.text = p_i45159_1_;
    }

    public String getChatComponentText_TextValue() {
        return this.text;
    }

    public String getUnformattedTextForChat() {
        return this.text;
    }

    public ChatComponentText createCopy() {
        ChatComponentText lvt_1_1_ = new ChatComponentText(this.text);
        lvt_1_1_.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator lvt_2_1_ = this.getSiblings().iterator();

        while(lvt_2_1_.hasNext()) {
            IChatComponent lvt_3_1_ = (IChatComponent)lvt_2_1_.next();
            lvt_1_1_.appendSibling(lvt_3_1_.createCopy());
        }

        return lvt_1_1_;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof ChatComponentText)) {
            return false;
        } else {
            ChatComponentText lvt_2_1_ = (ChatComponentText)p_equals_1_;
            return this.text.equals(lvt_2_1_.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
        }
    }

    public String toString() {
        return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}

