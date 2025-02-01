package org.browsit.mavenquests;

import me.pikamug.quests.module.BukkitCustomRequirement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class MavenRequirement extends BukkitCustomRequirement {

    public MavenRequirement() {
        setName("Maven Requirement");
        setAuthor("Browsit");
        setItem("STONE", (short)0);
        addStringPrompt("Maven Hunger Level", "Minimum level of player hunger", 5);
        setDisplay("Player is too hungry. Grab a bite then try again!");
    }

    @Override
    public String getModuleName() {
        return MavenModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return MavenModule.getModuleItem();
    }

    @Override
    public boolean testRequirement(UUID uuid, Map<String, Object> data) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            Bukkit.getLogger().severe("[" + getModuleName() + "] Player was null for UUID " + uuid);
            return false;
        }
        if (data != null && data.containsKey("Maven Hunger Level")) {
            final int level = Integer.parseInt((String) data.get("Maven Hunger Level"));
            return player.getFoodLevel() > level;
        }
        return false;
    }
}
