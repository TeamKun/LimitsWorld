package net.kunmc.lab.limitsworld.events

import net.kunmc.lab.limitsworld.LimitsWorldPlugin
import net.kunmc.lab.limitsworld.Manager

import org.bukkit.WorldBorder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerRespawnEvent

class EventListener: Listener {
    // config.ymlを読み込む
    private val config = LimitsWorldPlugin.plugin.config

    @EventHandler
    fun onAdvancement(e: PlayerAdvancementDoneEvent) {
        if(Manager.isEnable) {
            // タグモードが有効になっていて、タグがついていないプレイヤーは除外
            if(Manager.tagMode && !e.player.scoreboardTags.contains("limit")) {
                return
            }
            // レシピ開放イベントは除外
            if(e.advancement.key.key.startsWith("recipes")) {
                return
            }

            val wb: WorldBorder = Manager.world!!.worldBorder
            wb.setSize(wb.size + (config.getInt("size") * 2), 1)
        }
    }

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        if(Manager.isEnable && Manager.centerLocation != null) {
            e.respawnLocation = Manager.centerLocation!!
        }
    }
}