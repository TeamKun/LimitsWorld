package net.kunmc.lab.limitsworld

import org.bukkit.Location
import org.bukkit.World

class Manager {
    companion object {
        var world: World? = null
        var centerLocation: Location? = null
        var isEnable: Boolean = false
        var tagMode: Boolean = true
    }
}