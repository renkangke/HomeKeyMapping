package ren.kangke.homekeymapping

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.provider.Settings
import android.text.TextUtils
import android.view.Menu
import com.rarnu.base.app.BasePreferenceFragment

/**
 * Created by rarnu on 9/2/17.
 */
class KeyBindFragment: BasePreferenceFragment(), Preference.OnPreferenceClickListener {


    private var prefEnabled: Preference? = null
    private var prefInterval: Preference? = null
    private var prefHomePress: Preference? = null
    private var prefHomeLongPress: Preference? = null
    private var prefBackPress: Preference? = null
    private var prefBackLongPress: Preference? = null
    private var prefRecentPress: Preference? = null
    private var prefRecentLongPress: Preference? = null
    private var prefAbout: Preference? = null
    private var prefOpenSource: Preference? = null

    override fun getBarTitle(): Int = R.string.app_name

    override fun getBarTitleWithPath(): Int = 0

    override fun getCustomTitle(): String? = null

    override fun getFragmentLayoutResId(): Int = R.xml.key_bind_preference

    override fun getFragmentState(): Bundle? = null

    override fun getMainActivityName(): String? = null

    override fun initComponents() {
        prefEnabled = findPreference(Config.KEY_ENABLED)
        prefInterval = findPreference(Config.KEY_INTERVAL)
        prefHomePress = findPreference(Config.KEY_HOME_PRESS)
        prefHomeLongPress = findPreference(Config.KEY_HOME_LONG_PRESS)
        prefBackPress = findPreference(Config.KEY_BACK_PRESS)
        prefBackLongPress = findPreference(Config.KEY_BACK_LONG_PRESS)
        prefRecentPress = findPreference(Config.KEY_RECENT_PRESS)
        prefRecentLongPress = findPreference(Config.KEY_RECENT_LONG_PRESS)
        prefAbout = findPreference(Config.KEY_ABOUT)
        prefOpenSource = findPreference(Config.KEY_OPENSOURCE)
    }


    override fun initEvents() {
        prefEnabled?.setOnPreferenceClickListener {
            goAccess()
            true
        }

        prefInterval?.setOnPreferenceClickListener {
            AlertDialog.Builder(activity).setItems(resources.getStringArray(R.array.interval_array), {
                _, which ->
                val iv = (which + 1) * 100
                Config.setKeyCode(activity, Config.KEY_INTERVAL, iv)
                prefInterval?.summary = Config.getKeyCode(activity, Config.KEY_INTERVAL).toString()
            }).show()
            true
        }

        prefHomePress?.onPreferenceClickListener = this
        prefHomeLongPress?.onPreferenceClickListener = this
        prefBackPress?.onPreferenceClickListener = this
        prefBackLongPress?.onPreferenceClickListener = this
        prefRecentPress?.onPreferenceClickListener = this
        prefRecentLongPress?.onPreferenceClickListener = this

        prefAbout?.setOnPreferenceClickListener {
            // TODO: about
            true
        }
        prefOpenSource?.setOnPreferenceClickListener {
            // TODO: opensource
            true
        }

    }

    override fun initLogic() {
        updateAccessibilityServiceStatus()
        prefInterval?.summary = Config.getKeyCode(activity, Config.KEY_INTERVAL).toString()
        prefHomePress?.summary = Config.getKeyCodeName(Config.getKeyCode(activity, Config.KEY_HOME_PRESS))
        prefHomeLongPress?.summary = Config.getKeyCodeName(Config.getKeyCode(activity, Config.KEY_HOME_LONG_PRESS))
        prefBackPress?.summary = Config.getKeyCodeName(Config.getKeyCode(activity, Config.KEY_BACK_PRESS))
        prefBackLongPress?.summary = Config.getKeyCodeName(Config.getKeyCode(activity, Config.KEY_BACK_LONG_PRESS))
        prefRecentPress?.summary = Config.getKeyCodeName(Config.getKeyCode(activity, Config.KEY_RECENT_PRESS))
        prefRecentLongPress?.summary = Config.getKeyCodeName(Config.getKeyCode(activity, Config.KEY_RECENT_LONG_PRESS))
    }

    override fun initMenu(menu: Menu) {

    }

    override fun onGetNewArguments(bn: Bundle?) { }

    private fun isAccessibilitySettingsOn(): Boolean {
        var accessibilityEnabled = 0
        val service = "${activity.packageName}/${ButtonEventService::class.java.canonicalName}"
        try {
            accessibilityEnabled = Settings.Secure.getInt(activity.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Exception) {

        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(activity.applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
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

    private fun updateAccessibilityServiceStatus() {
        prefEnabled?.summary = getString(if (isAccessibilitySettingsOn()) R.string.pval_enabled else R.string.pval_disabled)
    }

    override fun onResume() {
        super.onResume()
        updateAccessibilityServiceStatus()
    }

    private fun goAccess() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        val key = preference.key
        AlertDialog.Builder(activity).setItems(resources.getStringArray(R.array.action_array), {
            _, which ->
            when(which - 1) {
                -1 -> Config.setKeyCode(activity, key, Config.getDefaultKeyCode(key))
                else -> Config.setKeyCode(activity, key, which - 1)
            }
            preference.summary = Config.getKeyCodeName(which - 1)
        }).show()
        return true
    }
}