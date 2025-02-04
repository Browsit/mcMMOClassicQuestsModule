package org.browsit.mcmmoclassicquests;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.util.player.UserManager;
import me.pikamug.quests.module.BukkitCustomRequirement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class mcMMOClassicSkillRequirement extends BukkitCustomRequirement {

    public mcMMOClassicSkillRequirement() {
        setName("mcMMO Classic Skill Requirement");
        setAuthor("Browsit, LLC");
        setItem("DIAMOND_SWORD", (short)0);
        addStringPrompt("MC Skill Type", "- Available Skill Types -"
                + mcMMOClassicModule.getSuggestions(), "ANY");
        addStringPrompt("MC Skill Amount", "Enter the quantity of skill levels to need", "1");
        setDisplay("%MC Skill Amount% %MC Skill Type% Skill Level(s)");
    }

    @Override
    public String getModuleName() {
        return mcMMOClassicModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return mcMMOClassicModule.getModuleItem();
    }

    @Override
    public boolean testRequirement(final UUID uuid, final Map<String, Object> data) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            Bukkit.getLogger().severe("[mcMMO Classic Quests Module] Player was null for UUID " + uuid);
            return false;
        }
        if (data != null) {
            if (!UserManager.hasPlayerDataKey(player)) {
                UserManager.track(new McMMOPlayer(player, new PlayerProfile(player.getName(), player.getUniqueId(),
                        false)));
            }
            final String skillType = (String)data.getOrDefault("MC Skill Type", "ANY");
            int skillLevel = 1;
            try {
                skillLevel = Integer.parseInt((String)data.getOrDefault("MC Skill Amount", "1"));
            } catch (final NumberFormatException e) {
                // Default to 1
            }
            final McMMOPlayer p = UserManager.getPlayer(player);
            if (p == null) {
                return false;
            }
            if (skillType.equalsIgnoreCase("ANY")) {
                for (final SkillType sk : SkillType.values()) {
                    if (p.getProfile().getSkillLevel(sk) >= skillLevel) {
                        return true;
                    }
                }
            } else {
                if (SkillType.getSkill(skillType) == null) {
                    Bukkit.getLogger().severe("[mcMMO Classic Quests Module] Invalid skill type " + skillType);
                    return false;
                }
                return UserManager.getOfflinePlayer(player).getProfile()
                        .getSkillLevel(SkillType.getSkill(skillType)) >= skillLevel;
            }
        }
        return false;
    }
}
