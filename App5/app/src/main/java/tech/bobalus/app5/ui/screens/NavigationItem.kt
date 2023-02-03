package tech.bobalus.app5.ui.screens

import tech.bobalus.app5.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Smart : NavigationItem("smart", R.drawable.ic_smart_green, "Smart")
    object Home : NavigationItem("home", R.drawable.ic_home_green, "Home")
    object Devices : NavigationItem("devices", R.drawable.ic_chip_green, "Devices")
}

