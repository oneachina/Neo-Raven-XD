package keystrokesmod.minecraft.chat;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public abstract class ChatComponentStyle implements IChatComponent {
    protected List<IChatComponent> siblings = Lists.newArrayList();
    private ChatStyle style;

    public ChatComponentStyle() {
    }

    public ChatComponentStyle appendSibling(IChatComponent p_appendSibling_1_) {
        p_appendSibling_1_.getChatStyle().setParentStyle(this.getChatStyle());
        this.siblings.add(p_appendSibling_1_);
        return this;
    }

    public List<IChatComponent> getSiblings() {
        return this.siblings;
    }

    public IChatComponent appendText(String p_appendText_1_) {
        return this.appendSibling(new ChatComponentText(p_appendText_1_));
    }

    public IChatComponent setChatStyle(ChatStyle p_setChatStyle_1_) {
        this.style = p_setChatStyle_1_;
        Iterator var2 = this.siblings.iterator();

        while(var2.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent)var2.next();
            ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
        }

        return this;
    }

    public ChatStyle getChatStyle() {
        if (this.style == null) {
            this.style = new ChatStyle();
            Iterator var1 = this.siblings.iterator();

            while(var1.hasNext()) {
                IChatComponent ichatcomponent = (IChatComponent)var1.next();
                ichatcomponent.getChatStyle().setParentStyle(this.style);
            }
        }

        return this.style;
    }

    public Iterator<IChatComponent> iterator() {
        return Iterators.concat(Iterators.forArray(new ChatComponentStyle[]{this}), createDeepCopyIterator(this.siblings));
    }

    public final String getUnformattedText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator var2 = this.iterator();

        while(var2.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent)var2.next();
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
        }

        return stringbuilder.toString();
    }

    public final String getFormattedText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator var2 = this.iterator();

        while(var2.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent)var2.next();
            stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
            stringbuilder.append(EnumChatFormatting.RESET);
        }

        return stringbuilder.toString();
    }

    public static Iterator<IChatComponent> createDeepCopyIterator(Iterable<IChatComponent> p_createDeepCopyIterator_0_) {
        Iterator<IChatComponent> iterator = Iterators.concat(Iterators.transform(p_createDeepCopyIterator_0_.iterator(), p_apply_1_ -> p_apply_1_.iterator()));
        iterator = Iterators.transform(iterator, new Function<IChatComponent, IChatComponent>() {
            public IChatComponent apply(IChatComponent p_apply_1_) {
                IChatComponent ichatcomponent = p_apply_1_.createCopy();
                ichatcomponent.setChatStyle(ichatcomponent.getChatStyle().createDeepCopy());
                return ichatcomponent;
            }
        });
        return iterator;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof ChatComponentStyle)) {
            return false;
        } else {
            ChatComponentStyle chatcomponentstyle = (ChatComponentStyle)p_equals_1_;
            return this.siblings.equals(chatcomponentstyle.siblings) && this.getChatStyle().equals(chatcomponentstyle.getChatStyle());
        }
    }

    public int hashCode() {
        return 31 * this.style.hashCode() + this.siblings.hashCode();
    }

    public String toString() {
        return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
    }
}
