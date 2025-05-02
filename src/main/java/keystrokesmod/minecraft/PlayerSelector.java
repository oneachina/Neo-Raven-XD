package keystrokesmod.minecraft;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import keystrokesmod.minecraft.chat.IChatComponent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class PlayerSelector {
    private static final Pattern tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w\\.=,!-]*)\\])?$");
    private static final Pattern intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
    private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w\\.-]*)(?:$|,)");
    private static final Set<String> WORLD_BINDING_ARGS = Sets.newHashSet(new String[]{"x", "y", "z", "dx", "dy", "dz", "rm", "r"});

    public PlayerSelector() {
    }

    public static EntityPlayerMP matchOnePlayer(ICommandSender p_matchOnePlayer_0_, String p_matchOnePlayer_1_) {
        return (EntityPlayerMP)matchOneEntity(p_matchOnePlayer_0_, p_matchOnePlayer_1_, EntityPlayerMP.class);
    }

    public static <T extends Entity> T matchOneEntity(ICommandSender p_matchOneEntity_0_, String p_matchOneEntity_1_, Class<? extends T> p_matchOneEntity_2_) {
        List<T> list = matchEntities(p_matchOneEntity_0_, p_matchOneEntity_1_, p_matchOneEntity_2_);
        return list.size() == 1 ? (T) list.get(0) : null;
    }

    public static IChatComponent matchEntitiesToChatComponent(ICommandSender p_matchEntitiesToChatComponent_0_, String p_matchEntitiesToChatComponent_1_) {
        List<Entity> list = matchEntities(p_matchEntitiesToChatComponent_0_, p_matchEntitiesToChatComponent_1_, Entity.class);
        if (list.isEmpty()) {
            return null;
        } else {
            List<ITextComponent> list1 = Lists.newArrayList();
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
                Entity entity = (Entity)var4.next();
                list1.add((ITextComponent) entity.getDisplayName());
            }

            return (IChatComponent) CommandBase.join(list1);
        }
    }

    public static <T extends Entity> List<T> matchEntities(ICommandSender p_matchEntities_0_, String p_matchEntities_1_, Class<? extends T> p_matchEntities_2_) {
        Matcher matcher = tokenPattern.matcher(p_matchEntities_1_);
        if (matcher.matches() && p_matchEntities_0_.canCommandSenderUseCommand(1, "@")) {
            Map<String, String> map = getArgumentMap(matcher.group(2));
            if (!isEntityTypeValid(p_matchEntities_0_, map)) {
                return Collections.emptyList();
            } else {
                String s = matcher.group(1);
                BlockPos blockpos = func_179664_b(map, p_matchEntities_0_.getPosition());
                List<World> list = getWorlds(p_matchEntities_0_, map);
                List<T> list1 = Lists.newArrayList();
                Iterator var9 = list.iterator();

                while(var9.hasNext()) {
                    World world = (World)var9.next();
                    if (world != null) {
                        List<Predicate<Entity>> list2 = Lists.newArrayList();
                        list2.addAll(func_179663_a(map, s));
                        list2.addAll(func_179648_b(map));
                        list2.addAll(func_179649_c(map));
                        list2.addAll(func_179659_d(map));
                        list2.addAll(func_179657_e(map));
                        list2.addAll(func_179647_f(map));
                        list2.addAll(func_180698_a(map, blockpos));
                        list2.addAll(func_179662_g(map));
                        list1.addAll(filterResults(map, p_matchEntities_2_, list2, s, world, blockpos));
                    }
                }

                return func_179658_a(list1, map, p_matchEntities_0_, p_matchEntities_2_, s, blockpos);
            }
        } else {
            return Collections.emptyList();
        }
    }

    private static List<World> getWorlds(ICommandSender p_getWorlds_0_, Map<String, String> p_getWorlds_1_) {
        List<World> list = Lists.newArrayList();
        if (func_179665_h(p_getWorlds_1_)) {
            list.add(p_getWorlds_0_.getEntityWorld());
        } else {
            Collections.addAll(list, MinecraftServer.getServer().worldServers);
        }

        return list;
    }

    private static <T extends Entity> boolean isEntityTypeValid(ICommandSender p_isEntityTypeValid_0_, Map<String, String> p_isEntityTypeValid_1_) {
        String s = func_179651_b(p_isEntityTypeValid_1_, "type");
        s = s != null && s.startsWith("!") ? s.substring(1) : s;
        if (s != null && !EntityList.isStringValidEntityName(s)) {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.entity.invalidType", new Object[]{s});
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            p_isEntityTypeValid_0_.addChatMessage(chatcomponenttranslation);
            return false;
        } else {
            return true;
        }
    }

    private static List<Predicate<Entity>> func_179663_a(Map<String, String> p_179663_0_, String p_179663_1_) {
        List<Predicate<Entity>> list = Lists.newArrayList();
        String s = func_179651_b(p_179663_0_, "type");
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }

        boolean flag1 = !p_179663_1_.equals("e");
        boolean flag2 = p_179663_1_.equals("r") && s != null;
        if ((s == null || !p_179663_1_.equals("e")) && !flag2) {
            if (flag1) {
                list.add(new Predicate<Entity>() {
                    public boolean apply(Entity p_apply_1_) {
                        return p_apply_1_ instanceof EntityPlayer;
                    }
                });
            }
        } else {
            final String s_f = s;
            list.add(new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    return EntityList.isStringEntityName(p_apply_1_, s_f) != flag;
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179648_b(Map<String, String> p_179648_0_) {
        List<Predicate<Entity>> list = Lists.newArrayList();
        final int i = parseIntWithDefault(p_179648_0_, "lm", -1);
        final int j = parseIntWithDefault(p_179648_0_, "l", -1);
        if (i > -1 || j > -1) {
            list.add(new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                        return false;
                    } else {
                        EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                        return (i <= -1 || entityplayermp.experienceLevel >= i) && (j <= -1 || entityplayermp.experienceLevel <= j);
                    }
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179649_c(Map<String, String> p_179649_0_) {
        List<Predicate<Entity>> list = Lists.newArrayList();
        final int i = parseIntWithDefault(p_179649_0_, "m", GameType.NOT_SET.getID());
        if (i != GameType.NOT_SET.getID()) {
            list.add(new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                        return false;
                    } else {
                        EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                        return entityplayermp.theItemInWorldManager.getGameType().getID() == i;
                    }
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179659_d(Map<String, String> p_179659_0_) {
        List<Predicate<Entity>> list = Lists.newArrayList();
        String s = func_179651_b(p_179659_0_, "team");
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }

        if (s != null) {
            final String s_f = s;
            list.add(new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityLivingBase)) {
                        return false;
                    } else {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
                        Team team = entitylivingbase.getTeam();
                        String s1 = team == null ? "" : team.getRegisteredName();
                        return s1.equals(s_f) != flag;
                    }
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179657_e(Map<String, String> p_179657_0_) {
        List<Predicate<Entity>> list = Lists.newArrayList();
        final Map<String, Integer> map = func_96560_a(p_179657_0_);
        if (map != null && map.size() > 0) {
            list.add(new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
                    Iterator var3 = map.entrySet().iterator();

                    Map.Entry entry;
                    boolean flag;
                    int i;
                    do {
                        if (!var3.hasNext()) {
                            return true;
                        }

                        entry = (Map.Entry)var3.next();
                        String s = (String)entry.getKey();
                        flag = false;
                        if (s.endsWith("_min") && s.length() > 4) {
                            flag = true;
                            s = s.substring(0, s.length() - 4);
                        }

                        ScoreObjective scoreobjective = scoreboard.getObjective(s);
                        if (scoreobjective == null) {
                            return false;
                        }

                        String s1 = p_apply_1_ instanceof EntityPlayerMP ? p_apply_1_.getName() : p_apply_1_.getUniqueID().toString();
                        if (!scoreboard.entityHasObjective(s1, scoreobjective)) {
                            return false;
                        }

                        Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
                        i = score.getScorePoints();
                        if (i < (Integer)entry.getValue() && flag) {
                            return false;
                        }
                    } while(i <= (Integer)entry.getValue() || flag);

                    return false;
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179647_f(Map<String, String> p_179647_0_) {
        List<Predicate<Entity>> list = Lists.newArrayList();
        String s = func_179651_b(p_179647_0_, "name");
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }

        if (s != null) {
            final String s_f = s;
            list.add(new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    return p_apply_1_.getName().equals(s_f) != flag;
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_180698_a(Map<String, String> p_180698_0_, final BlockPos p_180698_1_) {
        List<Predicate<Entity>> list = Lists.newArrayList();
        final int i = parseIntWithDefault(p_180698_0_, "rm", -1);
        final int j = parseIntWithDefault(p_180698_0_, "r", -1);
        if (p_180698_1_ != null && (i >= 0 || j >= 0)) {
            final int k = i * i;
            final int l = j * j;
            list.add(new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    int i1 = (int)p_apply_1_.getDistanceSqToCenter(p_180698_1_);
                    return (i < 0 || i1 >= k) && (j < 0 || i1 <= l);
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179662_g(Map<String, String> p_179662_0_) {
        List<Predicate<Entity>> list = Lists.newArrayList();
        final int k;
        final int l;
        if (p_179662_0_.containsKey("rym") || p_179662_0_.containsKey("ry")) {
            k = func_179650_a(parseIntWithDefault(p_179662_0_, "rym", 0));
            l = func_179650_a(parseIntWithDefault(p_179662_0_, "ry", 359));
            list.add(new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    int i1 = PlayerSelector.func_179650_a((int)Math.floor((double)p_apply_1_.rotationYaw));
                    return k > l ? i1 >= k || i1 <= l : i1 >= k && i1 <= l;
                }
            });
        }

        if (p_179662_0_.containsKey("rxm") || p_179662_0_.containsKey("rx")) {
            k = func_179650_a(parseIntWithDefault(p_179662_0_, "rxm", 0));
            l = func_179650_a(parseIntWithDefault(p_179662_0_, "rx", 359));
            list.add(new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    int i1 = PlayerSelector.func_179650_a((int)Math.floor((double)p_apply_1_.rotationPitch));
                    return k > l ? i1 >= k || i1 <= l : i1 >= k && i1 <= l;
                }
            });
        }

        return list;
    }

    private static <T extends Entity> List<T> filterResults(Map<String, String> p_filterResults_0_, Class<? extends T> p_filterResults_1_, List<Predicate<Entity>> p_filterResults_2_, String p_filterResults_3_, World p_filterResults_4_, BlockPos p_filterResults_5_) {
        List<T> list = Lists.newArrayList();
        String s = func_179651_b(p_filterResults_0_, "type");
        s = s != null && s.startsWith("!") ? s.substring(1) : s;
        boolean flag = !p_filterResults_3_.equals("e");
        boolean flag1 = p_filterResults_3_.equals("r") && s != null;
        int i = parseIntWithDefault(p_filterResults_0_, "dx", 0);
        int j = parseIntWithDefault(p_filterResults_0_, "dy", 0);
        int k = parseIntWithDefault(p_filterResults_0_, "dz", 0);
        int l = parseIntWithDefault(p_filterResults_0_, "r", -1);
        Predicate<Entity> predicate = Predicates.and(p_filterResults_2_);
        Predicate<Entity> predicate1 = Predicates.and(EntitySelectors.selectAnything, predicate);
        if (p_filterResults_5_ != null) {
            int i1 = p_filterResults_4_.playerEntities.size();
            int j1 = p_filterResults_4_.loadedEntityList.size();
            boolean flag2 = i1 < j1 / 16;
            final AxisAlignedBB axisalignedbb1;
            if (!p_filterResults_0_.containsKey("dx") && !p_filterResults_0_.containsKey("dy") && !p_filterResults_0_.containsKey("dz")) {
                if (l >= 0) {
                    axisalignedbb1 = new AxisAlignedBB((double)(p_filterResults_5_.getX() - l), (double)(p_filterResults_5_.getY() - l), (double)(p_filterResults_5_.getZ() - l), (double)(p_filterResults_5_.getX() + l + 1), (double)(p_filterResults_5_.getY() + l + 1), (double)(p_filterResults_5_.getZ() + l + 1));
                    if (flag && flag2 && !flag1) {
                        list.addAll(p_filterResults_4_.getPlayers(p_filterResults_1_, predicate1));
                    } else {
                        list.addAll(p_filterResults_4_.getEntitiesWithinAABB(p_filterResults_1_, axisalignedbb1, predicate1));
                    }
                } else if (p_filterResults_3_.equals("a")) {
                    list.addAll(p_filterResults_4_.getPlayers(p_filterResults_1_, predicate));
                } else if (!p_filterResults_3_.equals("p") && (!p_filterResults_3_.equals("r") || flag1)) {
                    list.addAll(p_filterResults_4_.getEntities(p_filterResults_1_, predicate1));
                } else {
                    list.addAll(p_filterResults_4_.getPlayers(p_filterResults_1_, predicate1));
                }
            } else {
                axisalignedbb1 = func_179661_a(p_filterResults_5_, i, j, k);
                if (flag && flag2 && !flag1) {
                    Predicate<Entity> predicate2 = new Predicate<Entity>() {
                        public boolean apply(Entity p_apply_1_) {
                            return p_apply_1_.posX >= axisalignedbb1.minX && p_apply_1_.posY >= axisalignedbb1.minY && p_apply_1_.posZ >= axisalignedbb1.minZ ? p_apply_1_.posX < axisalignedbb1.maxX && p_apply_1_.posY < axisalignedbb1.maxY && p_apply_1_.posZ < axisalignedbb1.maxZ : false;
                        }
                    };
                    list.addAll(p_filterResults_4_.getPlayers(p_filterResults_1_, Predicates.and(predicate1, predicate2)));
                } else {
                    list.addAll(p_filterResults_4_.getEntitiesWithinAABB(p_filterResults_1_, axisalignedbb1, predicate1));
                }
            }
        } else if (p_filterResults_3_.equals("a")) {
            list.addAll(p_filterResults_4_.getPlayers(p_filterResults_1_, predicate));
        } else if (p_filterResults_3_.equals("p") || p_filterResults_3_.equals("r") && !flag1) {
            list.addAll(p_filterResults_4_.getPlayers(p_filterResults_1_, predicate1));
        } else {
            list.addAll(p_filterResults_4_.getEntities(p_filterResults_1_, predicate1));
        }

        return list;
    }

    private static <T extends Entity> List<T> func_179658_a(List<T> p_179658_0_, Map<String, String> p_179658_1_, ICommandSender p_179658_2_, Class<? extends T> p_179658_3_, String p_179658_4_, final BlockPos p_179658_5_) {
        int i = parseIntWithDefault(p_179658_1_, "c", !p_179658_4_.equals("a") && !p_179658_4_.equals("e") ? 1 : 0);
        if (!p_179658_4_.equals("p") && !p_179658_4_.equals("a") && !p_179658_4_.equals("e")) {
            if (p_179658_4_.equals("r")) {
                Collections.shuffle((List)p_179658_0_);
            }
        } else if (p_179658_5_ != null) {
            Collections.sort((List)p_179658_0_, new Comparator<Entity>() {
                public int compare(Entity p_compare_1_, Entity p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(p_179658_5_), p_compare_2_.getDistanceSq(p_179658_5_)).result();
                }
            });
        }

        Entity entity = p_179658_2_.getCommandSenderEntity();
        if (entity != null && p_179658_3_.isAssignableFrom(entity.getClass()) && i == 1 && ((List)p_179658_0_).contains(entity) && !"r".equals(p_179658_4_)) {
            p_179658_0_ = Lists.newArrayList(new Entity[]{entity});
        }

        if (i != 0) {
            if (i < 0) {
                Collections.reverse((List)p_179658_0_);
            }

            p_179658_0_ = ((List)p_179658_0_).subList(0, Math.min(Math.abs(i), ((List)p_179658_0_).size()));
        }

        return (List)p_179658_0_;
    }

    private static AxisAlignedBB func_179661_a(BlockPos p_179661_0_, int p_179661_1_, int p_179661_2_, int p_179661_3_) {
        boolean flag = p_179661_1_ < 0;
        boolean flag1 = p_179661_2_ < 0;
        boolean flag2 = p_179661_3_ < 0;
        int i = p_179661_0_.getX() + (flag ? p_179661_1_ : 0);
        int j = p_179661_0_.getY() + (flag1 ? p_179661_2_ : 0);
        int k = p_179661_0_.getZ() + (flag2 ? p_179661_3_ : 0);
        int l = p_179661_0_.getX() + (flag ? 0 : p_179661_1_) + 1;
        int i1 = p_179661_0_.getY() + (flag1 ? 0 : p_179661_2_) + 1;
        int j1 = p_179661_0_.getZ() + (flag2 ? 0 : p_179661_3_) + 1;
        return new AxisAlignedBB((double)i, (double)j, (double)k, (double)l, (double)i1, (double)j1);
    }

    public static int func_179650_a(int p_179650_0_) {
        p_179650_0_ %= 360;
        if (p_179650_0_ >= 160) {
            p_179650_0_ -= 360;
        }

        if (p_179650_0_ < 0) {
            p_179650_0_ += 360;
        }

        return p_179650_0_;
    }

    private static BlockPos func_179664_b(Map<String, String> p_179664_0_, BlockPos p_179664_1_) {
        return new BlockPos(parseIntWithDefault(p_179664_0_, "x", p_179664_1_.getX()), parseIntWithDefault(p_179664_0_, "y", p_179664_1_.getY()), parseIntWithDefault(p_179664_0_, "z", p_179664_1_.getZ()));
    }

    private static boolean func_179665_h(Map<String, String> p_179665_0_) {
        Iterator var1 = WORLD_BINDING_ARGS.iterator();

        String s;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            s = (String)var1.next();
        } while(!p_179665_0_.containsKey(s));

        return true;
    }

    private static int parseIntWithDefault(Map<String, String> p_parseIntWithDefault_0_, String p_parseIntWithDefault_1_, int p_parseIntWithDefault_2_) {
        return p_parseIntWithDefault_0_.containsKey(p_parseIntWithDefault_1_) ? MathHelper.parseIntWithDefault((String)p_parseIntWithDefault_0_.get(p_parseIntWithDefault_1_), p_parseIntWithDefault_2_) : p_parseIntWithDefault_2_;
    }

    private static String func_179651_b(Map<String, String> p_179651_0_, String p_179651_1_) {
        return (String)p_179651_0_.get(p_179651_1_);
    }

    public static Map<String, Integer> func_96560_a(Map<String, String> p_96560_0_) {
        Map<String, Integer> map = Maps.newHashMap();
        Iterator var2 = p_96560_0_.keySet().iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            if (s.startsWith("score_") && s.length() > "score_".length()) {
                map.put(s.substring("score_".length()), MathHelper.parseIntWithDefault((String)p_96560_0_.get(s), 1));
            }
        }

        return map;
    }

    public static boolean matchesMultiplePlayers(String p_matchesMultiplePlayers_0_) {
        Matcher matcher = tokenPattern.matcher(p_matchesMultiplePlayers_0_);
        if (!matcher.matches()) {
            return false;
        } else {
            Map<String, String> map = getArgumentMap(matcher.group(2));
            String s = matcher.group(1);
            int i = !"a".equals(s) && !"e".equals(s) ? 1 : 0;
            return parseIntWithDefault(map, "c", i) != 1;
        }
    }

    public static boolean hasArguments(String p_hasArguments_0_) {
        return tokenPattern.matcher(p_hasArguments_0_).matches();
    }

    private static Map<String, String> getArgumentMap(String p_getArgumentMap_0_) {
        Map<String, String> map = Maps.newHashMap();
        if (p_getArgumentMap_0_ == null) {
            return map;
        } else {
            int i = 0;
            int j = -1;

            Matcher matcher;
            for(matcher = intListPattern.matcher(p_getArgumentMap_0_); matcher.find(); j = matcher.end()) {
                String s = null;
                switch (i++) {
                    case 0:
                        s = "x";
                        break;
                    case 1:
                        s = "y";
                        break;
                    case 2:
                        s = "z";
                        break;
                    case 3:
                        s = "r";
                }

                if (s != null && matcher.group(1).length() > 0) {
                    map.put(s, matcher.group(1));
                }
            }

            if (j < p_getArgumentMap_0_.length()) {
                matcher = keyValueListPattern.matcher(j == -1 ? p_getArgumentMap_0_ : p_getArgumentMap_0_.substring(j));

                while(matcher.find()) {
                    map.put(matcher.group(1), matcher.group(2));
                }
            }

            return map;
        }
    }
}

