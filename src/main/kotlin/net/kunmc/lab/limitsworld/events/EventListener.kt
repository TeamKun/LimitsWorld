package net.kunmc.lab.limitsworld.events

import net.kunmc.lab.limitsworld.LimitsWorldPlugin
import net.kunmc.lab.limitsworld.Manager

import org.bukkit.WorldBorder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent

class EventListener: Listener {
    // config.ymlを読み込む
    private val config = LimitsWorldPlugin.plugin.config

    @EventHandler
    fun onAdvancement(e: PlayerAdvancementDoneEvent) {
        if(Manager.isEnable) {
            val wb: WorldBorder = Manager.world!!.worldBorder
            wb.setSize(wb.size + (config.getInt("size") * 2), 1)
        }
    }
}