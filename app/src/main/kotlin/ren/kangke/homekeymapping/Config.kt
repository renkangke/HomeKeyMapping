package ren.kangke.homekeymapping

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by rarnu on 9/2/17.
 */
object Config {

    val KEY_ENABLED = "key_enabled"

    val KEY_HOME_PRESS = "key_home_press"
    val KEY_HOME_LONG_PRESS = "key_home_long_press"
    val KEY_BACK_PRESS = "key_back_press"
    val KEY_BACK_LONG_PRESS = "key_back_long_press"
    val KEY_RECENT_PRESS = "key_recent_press"
    val KEY_RECENT_LONG_PRESS = "key_recent_long_press"

    val KEY_ABOUT = "key_about"
    val KEY_OPENSOURCE = "key_opensource"

    fun getDefaultKeyCode(key: String): Int = when(key) {
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

    fun getKeyCode(ctx: Context, key: String): Int = PreferenceManager.getDefaultSharedPreferences(ctx).getInt(key, -1)

    fun setKeyCode(ctx: Context, key: String, value: Int) = PreferenceManager.getDefaultSharedPreferences(ctx).edit().putInt(key, value).apply()

}