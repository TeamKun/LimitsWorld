package net.kunmc.lab.limitsworld.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabCompleter: TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
        var result: MutableList<String> = mutableListOf()
        if(args.size == 1) {
            result.addAll(listOf("set", "clear", "size", "showConfig", "reloadConfig"))
            result = result.filter {
                it.startsWith(args[0])
            }.toMutableList()
        }
        else if(args.size == 2 && args[0] == "set") {
            result.clear()
            return null
        }
        else if(args.size == 2 && args[0] == "size") {
            result.clear()
            result.add("<拡張サイズ(整数)>")
        }
        else {
            result.clear()
        }
        return result
    }
}