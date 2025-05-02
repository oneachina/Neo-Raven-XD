package keystrokesmod.minecraft.chat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public enum EnumChatFormatting {
    BLACK("BLACK", '0', 0),
    DARK_BLUE("DARK_BLUE", '1', 1),
    DARK_GREEN("DARK_GREEN", '2', 2),
    DARK_AQUA("DARK_AQUA", '3', 3),
    DARK_RED("DARK_RED", '4', 4),
    DARK_PURPLE("DARK_PURPLE", '5', 5),
    GOLD("GOLD", '6', 6),
    GRAY("GRAY", '7', 7),
    DARK_GRAY("DARK_GRAY", '8', 8),
    BLUE("BLUE", '9', 9),
    GREEN("GREEN", 'a', 10),
    AQUA("AQUA", 'b', 11),
    RED("RED", 'c', 12),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
    YELLOW("YELLOW", 'e', 14),
    WHITE("WHITE", 'f', 15),
    OBFUSCATED("OBFUSCATED", 'k', true),
    BOLD("BOLD", 'l', true),
    STRIKETHROUGH("STRIKETHROUGH", 'm', true),
    UNDERLINE("UNDERLINE", 'n', true),
    ITALIC("ITALIC", 'o', true),
    RESET("RESET", 'r', -1);

    private static final Map<String, EnumChatFormatting> nameMapping = Maps.newHashMap();
    private static final Pattern formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
    private final String name;
    private final char formattingCode;
    private final boolean fancyStyling;
    private final String controlString;
    private final int colorIndex;

    private static String func_175745_c(String p_175745_0_) {
        return p_175745_0_.toLowerCase().replaceAll("[^a-z]", "");
    }

    private EnumChatFormatting(String p_i46291_3_, char p_i46291_4_, int p_i46291_5_) {
        this(p_i46291_3_, p_i46291_4_, false, p_i46291_5_);
    }

    private EnumChatFormatting(String p_i46292_3_, char p_i46292_4_, boolean p_i46292_5_) {
        this(p_i46292_3_, p_i46292_4_, p_i46292_5_, -1);
    }

    private EnumChatFormatting(String p_i46293_3_, char p_i46293_4_, boolean p_i46293_5_, int p_i46293_6_) {
        this.name = p_i46293_3_;
        this.formattingCode = p_i46293_4_;
        this.fancyStyling = p_i46293_5_;
        this.colorIndex = p_i46293_6_;
        this.controlString = "§" + p_i46293_4_;
    }

    public int getColorIndex() {
        return this.colorIndex;
    }

    public boolean isFancyStyling() {
        return this.fancyStyling;
    }

    public boolean isColor() {
        return !this.fancyStyling && this != RESET;
    }

    public String getFriendlyName() {
        return this.name().toLowerCase();
    }

    public String toString() {
        return this.controlString;
    }

    public static String getTextWithoutFormattingCodes(String p_getTextWithoutFormattingCodes_0_) {
        return p_getTextWithoutFormattingCodes_0_ == null ? null : formattingCodePattern.matcher(p_getTextWithoutFormattingCodes_0_).replaceAll("");
    }

    public static EnumChatFormatting getValueByName(String p_getValueByName_0_) {
        return p_getValueByName_0_ == null ? null : (EnumChatFormatting)nameMapping.get(func_175745_c(p_getValueByName_0_));
    }

    public static EnumChatFormatting func_175744_a(int p_175744_0_) {
        if (p_175744_0_ < 0) {
            return RESET;
        } else {
            EnumChatFormatting[] lvt_1_1_ = values();
            int lvt_2_1_ = lvt_1_1_.length;

            for(int lvt_3_1_ = 0; lvt_3_1_ < lvt_2_1_; ++lvt_3_1_) {
                EnumChatFormatting lvt_4_1_ = lvt_1_1_[lvt_3_1_];
                if (lvt_4_1_.getColorIndex() == p_175744_0_) {
                    return lvt_4_1_;
                }
            }

            return null;
        }
    }

    public static Collection<String> getValidValues(boolean p_getValidValues_0_, boolean p_getValidValues_1_) {
        List<String> lvt_2_1_ = Lists.newArrayList();
        EnumChatFormatting[] lvt_3_1_ = values();
        int lvt_4_1_ = lvt_3_1_.length;

        for(int lvt_5_1_ = 0; lvt_5_1_ < lvt_4_1_; ++lvt_5_1_) {
            EnumChatFormatting lvt_6_1_ = lvt_3_1_[lvt_5_1_];
            if ((!lvt_6_1_.isColor() || p_getValidValues_0_) && (!lvt_6_1_.isFancyStyling() || p_getValidValues_1_)) {
                lvt_2_1_.add(lvt_6_1_.getFriendlyName());
            }
        }

        return lvt_2_1_;
    }

    static {
        EnumChatFormatting[] lvt_0_1_ = values();
        int lvt_1_1_ = lvt_0_1_.length;

        for(int lvt_2_1_ = 0; lvt_2_1_ < lvt_1_1_; ++lvt_2_1_) {
            EnumChatFormatting lvt_3_1_ = lvt_0_1_[lvt_2_1_];
            nameMapping.put(func_175745_c(lvt_3_1_.name), lvt_3_1_);
        }

    }
}

