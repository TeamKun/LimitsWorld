package net.kunmc.lab.limitsworld.commands

import net.kunmc.lab.limitsworld.LimitsWorldPlugin
import net.kunmc.lab.limitsworld.Manager

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.WorldBorder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class CommandListener: CommandExecutor {
    // config.ymlを読み込む
    private val config = LimitsWorldPlugin.plugin.config

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(args.isEmpty()) {
            sender.sendMessage("" + ChatColor.RED + "error: 引数の数が不正です")
            return true
        }

        when(args[0]) {
            "set" -> {
                set(sender, args)
            }
            "clear" -> {
                clear(sender)
            }
            "size" -> {
                size(sender, args)
            }
            "showConfig" -> {
                showConfig(sender)
            }
            "reloadConfig" -> {
                reloadConfig(sender)
            }
            else -> {
                sender.sendMessage("" + ChatColor.RED + "error: 不正な引数が入力されました")
            }
        }
        return true
    }

    // ワールドボーダーの初期設定
    private fun set(sender: CommandSender, args: Array<out String>) {
        if(args.size != 2) {
            sender.sendMessage("" + ChatColor.RED + "error: 引数の数が不正です")
            return
        }

        // すでにワールドボーダーが設定されていたら
        if(Manager.isEnable) {
            sender.sendMessage(
                "" + ChatColor.RED + "error: すでにワールドボーダーが設定されています\n"+
                "" + ChatColor.RED + "/limits clear でワールドボーダーを一度解除してください"
            )
            return
        }

        // 入力されたプレイヤーが存在しているかの確認
        if(!checkPlayer(sender, args[1])) {
            return
        }

        // 実績クリア＆インベントリクリア
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, "clear @a")
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, "advancement revoke @a everything")
        Manager.isEnable = true
        // ワールドデータの存在確認
        if(Manager.world == null) {
            Manager.world = (sender as Player).world
        }

        val p: Player = Bukkit.getPlayer(args[1])!!
        val wb: WorldBorder = Manager.world!!.worldBorder
        val l: Location = p.location
        // ワールドボーダーの設定
        wb.setCenter(l.blockX.toDouble() + 0.5, l.blockZ.toDouble() + 0.5)
        wb.size = 1.0
        wb.damageAmount = 100.0
        wb.damageBuffer = 0.0
        wb.warningDistance = 0

        Manager.world!!.spawnLocation = p.location

        sender.sendMessage("" + ChatColor.GREEN + "ワールドボーダーの設定が完了しました")
    }

    // ワールドボーダーの解除
    private fun clear(sender: CommandSender) {
        // ワールドボーダーが設定されていない
        if(!Manager.isEnable) {
            sender.sendMessage("" + ChatColor.RED + "error: ワールドボーダーが設定されていません")
            return
        }

        val wb: WorldBorder = Manager.world!!.worldBorder
        wb.reset()
        Manager.isEnable = false
        sender.sendMessage("" + ChatColor.GREEN + "ワールドボーダーが解除されました")
    }

    // ワールドボーダーの拡張サイズの設定
    private fun size(sender: CommandSender, args: Array<out String>) {
        if(args.size != 2) {
            sender.sendMessage("" + ChatColor.RED + "error: 引数の数が不正です")
            return
        }

        try {
            val num: Int = Integer.parseInt(args[1])

            config.set("size", num)
            LimitsWorldPlugin.plugin.saveConfig()
            sender.sendMessage("" + ChatColor.GREEN + "拡張サイズが${config.get("size")}ブロックに変更されました")
        }
        catch(e: NumberFormatException) {
            sender.sendMessage("" + ChatColor.RED + "error: 第２引数には整数値を入力してください")
            return
        }
    }

    // 設定値の確認
    private fun showConfig(sender: CommandSender) {
        sender.sendMessage("ワールドボーダーの拡張サイズ: ${config.get("size")}")
    }

    // コンフィグの再読み込み
    private fun reloadConfig(sender: CommandSender) {
        LimitsWorldPlugin.plugin.reloadConfig()
        sender.sendMessage("" + ChatColor.GREEN + "config.ymlが再読み込みされました")
    }

    // 入力されたプレイヤーの存在チェック
    private fun checkPlayer(sender: CommandSender, name: String): Boolean {
        val entities: MutableList<Entity>
        try {
            entities = Bukkit.selectEntities(sender, name)
        }
        // 存在しないプレイヤー名が入力されたら
        catch(e: Exception) {
            sender.sendMessage("" + ChatColor.RED + "error: 存在しないプレイヤー名が入力されました")
            return false
        }
        // サーバに接続していないプレイヤー名が入力されたら
        if(entities.isEmpty()) {
            sender.sendMessage("" + ChatColor.RED + "error: サーバに接続していないプレイヤー名が入力されました")
            return false
        }
        return true
    }
}