package org.browsit.mavenquests;

import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.quests.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

public class MavenObjective extends BukkitCustomObjective {

    public MavenObjective() {
        setName("Maven Objective");
        setAuthor("Browsit");
        setItem("GRAVEL", (short)0);
        setShowCount(true);
        addStringPrompt("Maven Objective Name", "Set a name for the objective", "Break ANY block");
        setCountPrompt("Set the amount of blocks to break");
        setDisplay("%Maven Objective Name%: %count%");
    }

    @Override
    public String getModuleName() {
        return MavenModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return MavenModule.getModuleItem();
    }

    @EventHandler(priority = EventPriority.LOW) // TODO - Always consider how priority will affect server plugins!
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        for (Quest quest : MavenModule.getQuests().getQuester(player.getUniqueId()).getCurrentQuests().keySet()) {
            incrementObjective(player.getUniqueId(), this, quest, 1);
            return;
        }
    }
}
