package ren.kangke.homekeymapping

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.provider.Settings
import android.text.TextUtils
import com.rarnu.kt.android.PreferenceActivity
import com.rarnu.kt.android.resStr


/**
 * Created by renkangke .
 */

class MainActivity : PreferenceActivity(), Preference.OnPreferenceClickListener {

    private lateinit var prefEnabled: Preference
    private lateinit var prefInterval: Preference
    private lateinit var prefHomePress: Preference
    private lateinit var prefHomeLongPress: Preference
    private lateinit var prefBackPress: Preference
    private lateinit var prefBackLongPress: Preference
    private lateinit var prefRecentPress: Preference
    private lateinit var prefRecentLongPress: Preference
    private lateinit var prefAbout: Preference
    private lateinit var prefOpenSource: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.title = resStr(R.string.app_name)

        val service = Intent(this, ButtonEventService::class.java)
        startService(service)
    }

    override fun getPreferenceXml() = R.xml.key_bind_preference

    override fun onPreparedPreference() {
        prefEnabled = pref(Config.KEY_ENABLED)
        prefInterval = pref(Config.KEY_INTERVAL)
        prefHomePress = pref(Config.KEY_HOME_PRESS)
        prefHomeLongPress = pref(Config.KEY_HOME_LONG_PRESS)
        prefBackPress = pref(Config.KEY_BACK_PRESS)
        prefBackLongPress = pref(Config.KEY_BACK_LONG_PRESS)
        prefRecentPress = pref(Config.KEY_RECENT_PRESS)
        prefRecentLongPress = pref(Config.KEY_RECENT_LONG_PRESS)
        prefAbout = pref(Config.KEY_ABOUT)
        prefOpenSource = pref(Config.KEY_OPENSOURCE)

        prefEnabled.setOnPreferenceClickListener {
            goAccess()
            true
        }

        prefInterval.setOnPreferenceClickListener {
            AlertDialog.Builder(this).setItems(resources.getStringArray(R.array.interval_array)) { _, which ->
                val iv = (which + 1) * 100
                Config.setKeyCode(this, Config.KEY_INTERVAL, iv)
                prefInterval.summary = Config.getKeyCode(this, Config.KEY_INTERVAL).toString()
            }.show()
            true
        }

        prefHomePress.onPreferenceClickListener = this
        prefHomeLongPress.onPreferenceClickListener = this
        prefBackPress.onPreferenceClickListener = this
        prefBackLongPress.onPreferenceClickListener = this
        prefRecentPress.onPreferenceClickListener = this
        prefRecentLongPress.onPreferenceClickListener = this

        prefAbout.setOnPreferenceClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }
        prefOpenSource.setOnPreferenceClickListener {
            val inOpenSource = Intent(Intent.ACTION_VIEW)
            inOpenSource.data = Uri.parse("https://github.com/renkangke/HomeKeyMapping")
            startActivity(inOpenSource)
            true
        }

        updateAccessibilityServiceStatus()
        prefInterval.summary = Config.getKeyCode(this, Config.KEY_INTERVAL).toString()
        prefHomePress.summary = Config.getKeyCodeName(Config.getKeyCode(this, Config.KEY_HOME_PRESS))
        prefHomeLongPress.summary = Config.getKeyCodeName(Config.getKeyCode(this, Config.KEY_HOME_LONG_PRESS))
        prefBackPress.summary = Config.getKeyCodeName(Config.getKeyCode(this, Config.KEY_BACK_PRESS))
        prefBackLongPress.summary = Config.getKeyCodeName(Config.getKeyCode(this, Config.KEY_BACK_LONG_PRESS))
        prefRecentPress.summary = Config.getKeyCodeName(Config.getKeyCode(this, Config.KEY_RECENT_PRESS))
        prefRecentLongPress.summary = Config.getKeyCodeName(Config.getKeyCode(this, Config.KEY_RECENT_LONG_PRESS))
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        val key = preference.key
        AlertDialog.Builder(this).setItems(resources.getStringArray(R.array.action_array)) { _, which ->
            when (which - 1) {
                -1 -> Config.setKeyCode(this, key, Config.getDefaultKeyCode(key))
                else -> Config.setKeyCode(this, key, which - 1)
            }
            preference.summary = Config.getKeyCodeName(which - 1)
        }.show()
        return true
    }

    private fun goAccess() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun updateAccessibilityServiceStatus() {
        prefEnabled.summary = resStr(if (isAccessibilitySettingsOn()) R.string.pval_enabled else R.string.pval_disabled)
    }

    private fun isAccessibilitySettingsOn(): Boolean {
        var accessibilityEnabled = 0
        val service = "$packageName/${ButtonEventService::class.java.canonicalName}"
        try {
            accessibilityEnabled = Settings.Secure.getInt(applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Exception) {

        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        updateAccessibilityServiceStatus()
    }

}
