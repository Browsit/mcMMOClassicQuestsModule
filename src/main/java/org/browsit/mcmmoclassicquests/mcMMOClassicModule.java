package org.browsit.mcmmoclassicquests;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.mcMMO;
import me.pikamug.quests.BukkitQuestsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class mcMMOClassicModule extends JavaPlugin {

    private static final BukkitQuestsPlugin quests = (BukkitQuestsPlugin) Bukkit.getServer().getPluginManager().getPlugin("Quests");
    private static final String moduleName = "mcMMO Classic Module";
    private static final Map.Entry<String, Short> moduleItem = new AbstractMap.SimpleEntry<>("DIAMOND_SWORD", (short)0);

    public static BukkitQuestsPlugin getQuests() {
        return quests;
    }

    public static String getModuleName() {
        return moduleName;
    }

    public static Map.Entry<String, Short> getModuleItem() {
        return moduleItem;
    }

    @Override
    public void onEnable() {
        getLogger().severe(ChatColor.RED + "Move this jar to your " + File.separatorChar + "Quests" + File.separatorChar
                + "modules folder!");
        getServer().getPluginManager().disablePlugin(this);
        setEnabled(false);
    }

    @Override
    public void onDisable() {
    }

    public static String getSuggestions() {
        final List<SkillType> suggestionList = Arrays.asList(SkillType.values());
        suggestionList.sort(Comparator.comparing(SkillType::name));
        final StringBuilder text = new StringBuilder("\n");
        for (int i = 0; i < suggestionList.size(); i++) {
            final String name = (suggestionList.get(i).getName());
            text.append(ChatColor.AQUA).append(name);
            if (i < (suggestionList.size() - 1)) {
                text.append(ChatColor.GRAY).append(", ");
            }
        }
        return text.toString();
    }
}
