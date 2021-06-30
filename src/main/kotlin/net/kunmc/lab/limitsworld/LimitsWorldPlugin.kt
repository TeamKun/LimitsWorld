package net.kunmc.lab.limitsworld

import net.kunmc.lab.limitsworld.commands.*
import net.kunmc.lab.limitsworld.events.EventListener

import org.bukkit.plugin.java.JavaPlugin

class LimitsWorldPlugin: JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onEnable() {
        plugin = this
        // config.ymlがない場合は生成する
        saveDefaultConfig()

        getCommand("limits")?.setExecutor(CommandListener())
        getCommand("limits")?.tabCompleter = TabCompleter()
        server.pluginManager.registerEvents(EventListener(), this)
    }
}