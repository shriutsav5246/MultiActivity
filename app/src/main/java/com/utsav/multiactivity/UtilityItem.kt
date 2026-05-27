package com.utsav.multiactivity

data class UtilityItem(
    val title: String,
    val subtitle: String,
    val color: String,
    val targetActivity: Class<*>
)