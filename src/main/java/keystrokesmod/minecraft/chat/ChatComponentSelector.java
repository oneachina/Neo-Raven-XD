package keystrokesmod.minecraft.chat;

import java.util.Iterator;

public class ChatComponentSelector extends ChatComponentStyle {
    private final String selector;

    public ChatComponentSelector(String p_i45996_1_) {
        this.selector = p_i45996_1_;
    }

    public String getSelector() {
        return this.selector;
    }

    public String getUnformattedTextForChat() {
        return this.selector;
    }

    public ChatComponentSelector createCopy() {
        ChatComponentSelector lvt_1_1_ = new ChatComponentSelector(this.selector);
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
        } else if (!(p_equals_1_ instanceof ChatComponentSelector)) {
            return false;
        } else {
            ChatComponentSelector lvt_2_1_ = (ChatComponentSelector)p_equals_1_;
            return this.selector.equals(lvt_2_1_.selector) && super.equals(p_equals_1_);
        }
    }

    public String toString() {
        return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}

