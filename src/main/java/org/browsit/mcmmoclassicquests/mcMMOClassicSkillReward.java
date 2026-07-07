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

import com.gmail.nossr50.config.Config;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.util.player.UserManager;
import me.pikamug.quests.module.BukkitCustomReward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class mcMMOClassicSkillReward extends BukkitCustomReward {

    public mcMMOClassicSkillReward() {
        setName("mcMMO Classic Skill Reward");
        setAuthor("Browsit, LLC");
        setItem("DIAMOND_SWORD", (short)0);
        addStringPrompt("MC Skill Type", "- Available Skill Types -"
                + mcMMOClassicModule.getSuggestions(), "ANY");
        addStringPrompt("MC Skill Amount", "Enter the quantity of skill levels to give", "1");
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
    public void giveReward(UUID uuid, final Map<String, Object> data) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            Bukkit.getLogger().severe("[mcMMO Classic Quests Module] Player was null for UUID " + uuid);
            return;
        }
        if (data != null) {
            if (!UserManager.hasPlayerDataKey(player)) {
                UserManager.track(new McMMOPlayer(player, new PlayerProfile(player.getName(), player.getUniqueId(),
                        false)));
            }
            final String skillType = (String)data.getOrDefault("MC Skill Type", "ANY");
            int skillLevels = 1;
            try {
                skillLevels = Integer.parseInt((String)data.getOrDefault("MC Skill Amount", "1"));
            } catch (final NumberFormatException e) {
                // Default to 1
            }
            final McMMOPlayer p = UserManager.getPlayer(player);
            if (p == null) {
                return;
            }

            if (skillType.equalsIgnoreCase("ANY")) {
                for (final SkillType sk : SkillType.values()) {
                    final int current = UserManager.getOfflinePlayer(player).getProfile().getSkillLevel(sk);
                    final int max = Config.getInstance().getLevelCap(sk);
                    if (current + skillLevels > max) {
                        p.getProfile().modifySkill(sk, max);
                    } else {
                        p.getProfile().addLevels(sk, skillLevels);
                    }
                }
            } else {
                SkillType sk = SkillType.getSkill(skillType);
                if (sk == null) {
                    Bukkit.getLogger().severe("[mcMMO Classic Quests Module] Invalid skill type " + skillType);
                    return;
                }
                final int current = UserManager.getOfflinePlayer(player).getProfile().getSkillLevel(sk);
                final int max = Config.getInstance().getLevelCap(sk);
                if (current + skillLevels > max) {
                    p.getProfile().modifySkill(sk, max);
                } else {
                    p.getProfile().addLevels(sk, skillLevels);
                }
            }
        }
    }
}
