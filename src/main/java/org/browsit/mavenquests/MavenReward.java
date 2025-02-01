package org.browsit.mavenquests;

import me.pikamug.quests.module.BukkitCustomReward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class MavenReward extends BukkitCustomReward {

    public MavenReward() {
        setName("Maven Reward");
        setAuthor("Browsit");
        setItem("CHEST", (short)0);
        addStringPrompt("Maven Diamond Amount", "Enter the amount of Diamond to give", 1);
        setDisplay("You got %Maven Diamond Amount% Diamond(s)!");
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
    public void giveReward(UUID uuid, Map<String, Object> data) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            Bukkit.getLogger().severe("[" + getModuleName() + "] Player was null for UUID " + uuid);
            return;
        }
        if (data != null && data.containsKey("Maven Diamond Amount")) {
            final int amount = Integer.parseInt((String) data.get("Maven Diamond Amount"));
            player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.DIAMOND, amount));
        }
    }
}
