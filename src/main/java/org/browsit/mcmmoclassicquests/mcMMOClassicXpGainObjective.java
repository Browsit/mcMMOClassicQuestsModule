/*
 * Copyright (c) 2026 Browsit, LLC. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.browsit.mcmmoclassicquests;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import me.pikamug.quests.enums.ObjectiveType;
import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.player.BukkitQuester;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class mcMMOClassicXpGainObjective extends BukkitCustomObjective {

    public mcMMOClassicXpGainObjective() {
        setName("mcMMO Classic XP Gain Objective");
        setAuthor("Browsit, LLC");
        setItem("EXP_BOTTLE", (short)0);
        setShowCount(true);
        addStringPrompt("MC XP Obj", "Set a name for the objective", "Gain skill XP");
        addStringPrompt("MC XP Types", "- Available Skill Types -"
                + mcMMOClassicModule.getSuggestions(), "ANY");
        setCountPrompt("Set the amount of XP to gain");
        setDisplay("%MC XP Obj% in %MC XP Types%: %count%");
    }

    @Override
    public String getModuleName() {
        return mcMMOClassicModule.getModuleName();
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return mcMMOClassicModule.getModuleItem();
    }

    @EventHandler
    public void onPlayerXpGain(final McMMOPlayerXpGainEvent event) {
        if (mcMMOClassicModule.getQuests() == null) {
            return;
        }
        final BukkitQuester quester = mcMMOClassicModule.getQuests().getQuester(event.getPlayer().getUniqueId());
        if (quester == null) {
            return;
        }
        for (final Quest quest : quester.getCurrentQuests().keySet()) {
            final Player p = quester.getPlayer();
            final Map<String, Object> dataMap = getDataForPlayer(p.getUniqueId(), this, quest);
            if (dataMap != null) {
                final String skillNames = (String)dataMap.getOrDefault("MC XP Types", "ANY");
                if (skillNames == null) {
                    return;
                }
                final String[] spl = skillNames.split(",");
                for (final String str : spl) {
                    if (str.equals("ANY") || (SkillType.getSkill(str) != null
                            && SkillType.getSkill(str).equals(event.getSkill()))) {
                        incrementObjective(p.getUniqueId(), this, quest, 1);

                        quester.dispatchMultiplayerEverything(quest, ObjectiveType.CUSTOM,
                                (final Quester q, final Quest cq) -> {
                                    incrementObjective(q.getUUID(), this, quest, 1);
                                    return null;
                                });
                        break;
                    }
                }
            }
        }
    }
}
