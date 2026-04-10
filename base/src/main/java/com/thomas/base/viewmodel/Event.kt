package com.thomas.base.viewmodel

/**
 * Created by thomas on 4/11/2026.
 */

interface Event

data class NavigateEvent(
    val route: String,
    val popUpToRoute: String? = null,
    val inclusive: Boolean = false,
    val launchSingleTop: Boolean = false,
) : Event

object BackEvent : Event
data class MessageEvent(val message: String) : Event
