package ren.kangke.homekeymapping

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by rarnu on 9/2/17.
 */
object Config {

    val KEY_ENABLED = "key_enabled"
    val KEY_INTERVAL = "key_interval"

    val KEY_HOME_PRESS = "key_home_press"
    val KEY_HOME_LONG_PRESS = "key_home_long_press"
    val KEY_BACK_PRESS = "key_back_press"
    val KEY_BACK_LONG_PRESS = "key_back_long_press"
    val KEY_RECENT_PRESS = "key_recent_press"
    val KEY_RECENT_LONG_PRESS = "key_recent_long_press"

    val KEY_ABOUT = "key_about"
    val KEY_OPENSOURCE = "key_opensource"

    private var needReload = true
    private var currentHomePress = 0
    private var currentHomeLongPress = 0
    private var currentBackPress = 0
    private var currentBackLongPress = 0
    private var currentRecentPress = 0
    private var currentRecentLongPress = 0
    private var currentInterval = 0

    private fun getCurrent(key: String): Int = when(key) {
        KEY_INTERVAL -> currentInterval
        KEY_HOME_PRESS -> currentHomePress
        KEY_HOME_LONG_PRESS -> currentHomeLongPress
        KEY_BACK_PRESS -> currentBackPress
        KEY_BACK_LONG_PRESS -> currentBackLongPress
        KEY_RECENT_PRESS -> currentRecentPress
        KEY_RECENT_LONG_PRESS -> currentRecentLongPress
        else -> 0
    }

    private fun reloadAll(ctx: Context) {
        val pref = PreferenceManager.getDefaultSharedPreferences(ctx)
        currentHomePress = pref.getInt(KEY_HOME_PRESS, -1)
        if (currentHomePress == -1) currentHomePress = getDefaultKeyCode(KEY_HOME_PRESS)
        currentHomeLongPress = pref.getInt(KEY_HOME_LONG_PRESS, -1)
        if (currentHomeLongPress == -1) currentHomeLongPress = getDefaultKeyCode(KEY_HOME_LONG_PRESS)
        currentBackPress = pref.getInt(KEY_BACK_PRESS, -1)
        if (currentBackPress == -1) currentBackPress = getDefaultKeyCode(KEY_BACK_PRESS)
        currentBackLongPress = pref.getInt(KEY_BACK_LONG_PRESS, -1)
        if (currentBackLongPress == -1) currentBackLongPress = getDefaultKeyCode(KEY_BACK_LONG_PRESS)

        currentRecentPress = pref.getInt(KEY_RECENT_PRESS, -1)
        if (currentRecentPress == -1) currentRecentPress = getDefaultKeyCode(KEY_RECENT_PRESS)
        currentRecentLongPress = pref.getInt(KEY_RECENT_LONG_PRESS, -1)
        if (currentRecentLongPress == -1) currentRecentLongPress = getDefaultKeyCode(KEY_RECENT_LONG_PRESS)

        currentInterval = pref.getInt(KEY_INTERVAL, 300)
    }

    fun getDefaultKeyCode(key: String): Int = when (key) {
        KEY_HOME_PRESS -> AccessibilityService.GLOBAL_ACTION_HOME
        KEY_BACK_PRESS -> AccessibilityService.GLOBAL_ACTION_BACK
        KEY_RECENT_PRESS -> AccessibilityService.GLOBAL_ACTION_RECENTS
        else -> -1
    }

    fun getKeyCodeName(keyCode: Int): String = when (keyCode) {
        AccessibilityService.GLOBAL_ACTION_HOME -> "Home"
        AccessibilityService.GLOBAL_ACTION_BACK -> "Back"
        AccessibilityService.GLOBAL_ACTION_RECENTS -> "Recent"
        AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS -> "Notification"
        AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS -> "Quick Settings"
        AccessibilityService.GLOBAL_ACTION_POWER_DIALOG -> "Power Dialog"
        AccessibilityService.GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN -> "Split Screen"
        0 -> "无"
        -1 -> "默认"
        else -> ""
    }

    fun getKeyCode(ctx: Context, key: String): Int = if (!needReload) {
        getCurrent(key)
    } else {
        reloadAll(ctx)
        needReload = false
        getCurrent(key)
    }


    fun setKeyCode(ctx: Context, key: String, value: Int) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putInt(key, value).apply()
        needReload = true
    }

}