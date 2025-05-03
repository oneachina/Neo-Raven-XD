package keystrokesmod.minecraft.chat;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n; // 新增导入 I18n 类
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatComponentTranslation extends ChatComponentStyle {
    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock = new Object();
    // 移除 lastTranslationUpdateTimeInMilliseconds 相关逻辑
    // private long lastTranslationUpdateTimeInMilliseconds = -1L;
    List<IChatComponent> children = Lists.newArrayList();
    public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public ChatComponentTranslation(String p_i45160_1_, Object... p_i45160_2_) {
        this.key = p_i45160_1_;
        this.formatArgs = p_i45160_2_;
        // 移除冗余局部变量
        for (Object lvt_6_1_ : p_i45160_2_) {
            if (lvt_6_1_ instanceof IChatComponent) {
                ((IChatComponent)lvt_6_1_).getChatStyle().setParentStyle(this.getChatStyle());
            }
        }
    }

    synchronized void ensureInitialized() {
        synchronized(this.syncLock) {
            // 移除 StatCollector.getLastTranslationUpdateTimeInMilliseconds 相关逻辑
            // long lvt_2_1_ = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
            // if (lvt_2_1_ == this.lastTranslationUpdateTimeInMilliseconds) {
            //     return;
            // }
            // this.lastTranslationUpdateTimeInMilliseconds = lvt_2_1_;
            this.children.clear();
        }

        try {
            this.initializeFromFormat(I18n.format(this.key));
        } catch (ChatComponentTranslationFormatException var6) {
            ChatComponentTranslationFormatException lvt_1_1_ = var6;
            this.children.clear();

            try {
                // 替换为 I18n.format
                this.initializeFromFormat(I18n.format(this.key));
            } catch (ChatComponentTranslationFormatException var5) {
                throw lvt_1_1_;
            }
        }
    }

    protected void initializeFromFormat(String p_initializeFromFormat_1_) {
        boolean lvt_2_1_ = false;
        Matcher lvt_3_1_ = stringVariablePattern.matcher(p_initializeFromFormat_1_);
        int lvt_4_1_ = 0;
        int lvt_5_1_ = 0;

        try {
            int lvt_7_1_;
            for(; lvt_3_1_.find(lvt_5_1_); lvt_5_1_ = lvt_7_1_) {
                int lvt_6_1_ = lvt_3_1_.start();
                lvt_7_1_ = lvt_3_1_.end();
                if (lvt_6_1_ > lvt_5_1_) {
                    ChatComponentText lvt_8_1_ = new ChatComponentText(String.format(p_initializeFromFormat_1_.substring(lvt_5_1_, lvt_6_1_)));
                    lvt_8_1_.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(lvt_8_1_);
                }

                String lvt_8_2_ = lvt_3_1_.group(2);
                String lvt_9_1_ = p_initializeFromFormat_1_.substring(lvt_6_1_, lvt_7_1_);
                if ("%".equals(lvt_8_2_) && "%%".equals(lvt_9_1_)) {
                    ChatComponentText lvt_10_1_ = new ChatComponentText("%");
                    lvt_10_1_.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(lvt_10_1_);
                } else {
                    if (!"s".equals(lvt_8_2_)) {
                        throw new ChatComponentTranslationFormatException(this, "Unsupported format: '" + lvt_9_1_ + "'");
                    }

                    String lvt_10_2_ = lvt_3_1_.group(1);
                    int lvt_11_1_ = lvt_10_2_ != null ? Integer.parseInt(lvt_10_2_) - 1 : lvt_4_1_++;
                    if (lvt_11_1_ < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(lvt_11_1_));
                    }
                }
            }

            if (lvt_5_1_ < p_initializeFromFormat_1_.length()) {
                ChatComponentText lvt_6_2_ = new ChatComponentText(String.format(p_initializeFromFormat_1_.substring(lvt_5_1_)));
                lvt_6_2_.getChatStyle().setParentStyle(this.getChatStyle());
                this.children.add(lvt_6_2_);
            }

        } catch (IllegalFormatException var12) {
            IllegalFormatException lvt_6_3_ = var12;
            throw new ChatComponentTranslationFormatException(this, lvt_6_3_);
        }
    }

    private IChatComponent getFormatArgumentAsComponent(int p_getFormatArgumentAsComponent_1_) {
        if (p_getFormatArgumentAsComponent_1_ >= this.formatArgs.length) {
            throw new ChatComponentTranslationFormatException(this, p_getFormatArgumentAsComponent_1_);
        } else {
            Object lvt_2_1_ = this.formatArgs[p_getFormatArgumentAsComponent_1_];
            Object lvt_3_2_;
            if (lvt_2_1_ instanceof IChatComponent) {
                lvt_3_2_ = (IChatComponent)lvt_2_1_;
            } else {
                lvt_3_2_ = new ChatComponentText(lvt_2_1_ == null ? "null" : lvt_2_1_.toString());
                ((IChatComponent)lvt_3_2_).getChatStyle().setParentStyle(this.getChatStyle());
            }

            return (IChatComponent)lvt_3_2_;
        }
    }

    public void setChatStyle(ChatStyle p_setChatStyle_1_) {
        super.setChatStyle(p_setChatStyle_1_);
        Object[] lvt_2_1_ = this.formatArgs;
        int lvt_3_1_ = lvt_2_1_.length;

        for(int lvt_4_1_ = 0; lvt_4_1_ < lvt_3_1_; ++lvt_4_1_) {
            Object lvt_5_1_ = lvt_2_1_[lvt_4_1_];
            if (lvt_5_1_ instanceof IChatComponent) {
                ((IChatComponent)lvt_5_1_).getChatStyle().setParentStyle(this.getChatStyle());
            }
        }

        long lastTranslationUpdateTimeInMilliseconds = 0;
        if (lastTranslationUpdateTimeInMilliseconds > -1L) {
            Iterator lvt_2_2_ = this.children.iterator();

            while(lvt_2_2_.hasNext()) {
                IChatComponent lvt_3_2_ = (IChatComponent)lvt_2_2_.next();
                lvt_3_2_.getChatStyle().setParentStyle(p_setChatStyle_1_);
            }
        }

    }

    public @NotNull Iterator<IChatComponent> iterator() {
        this.ensureInitialized();
        return Iterators.concat(createDeepCopyIterator(this.children), createDeepCopyIterator(this.siblings));
    }

    public String getUnformattedTextForChat() {
        this.ensureInitialized();
        StringBuilder lvt_1_1_ = new StringBuilder();
        Iterator lvt_2_1_ = this.children.iterator();

        while(lvt_2_1_.hasNext()) {
            IChatComponent lvt_3_1_ = (IChatComponent)lvt_2_1_.next();
            lvt_1_1_.append(lvt_3_1_.getUnformattedTextForChat());
        }

        return lvt_1_1_.toString();
    }

    public ChatComponentTranslation createCopy() {
        Object[] lvt_1_1_ = new Object[this.formatArgs.length];

        for(int lvt_2_1_ = 0; lvt_2_1_ < this.formatArgs.length; ++lvt_2_1_) {
            if (this.formatArgs[lvt_2_1_] instanceof IChatComponent) {
                lvt_1_1_[lvt_2_1_] = ((IChatComponent)this.formatArgs[lvt_2_1_]).createCopy();
            } else {
                lvt_1_1_[lvt_2_1_] = this.formatArgs[lvt_2_1_];
            }
        }

        ChatComponentTranslation lvt_2_2_ = new ChatComponentTranslation(this.key);
        lvt_2_2_.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator lvt_3_1_ = this.getSiblings().iterator();

        while(lvt_3_1_.hasNext()) {
            IChatComponent lvt_4_1_ = (IChatComponent)lvt_3_1_.next();
            lvt_2_2_.appendSibling(lvt_4_1_.createCopy());
        }

        return lvt_2_2_;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof ChatComponentTranslation)) {
            return false;
        } else {
            ChatComponentTranslation lvt_2_1_ = (ChatComponentTranslation)p_equals_1_;
            return Arrays.equals(this.formatArgs, lvt_2_1_.formatArgs) && this.key.equals(lvt_2_1_.key) && super.equals(p_equals_1_);
        }
    }

    public int hashCode() {
        int lvt_1_1_ = super.hashCode();
        lvt_1_1_ = 31 * lvt_1_1_ + this.key.hashCode();
        lvt_1_1_ = 31 * lvt_1_1_ + Arrays.hashCode(this.formatArgs);
        return lvt_1_1_;
    }

    public String toString() {
        return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    public String getKey() {
        return this.key;
    }

    public Object[] getFormatArgs() {
        return this.formatArgs;
    }
}

