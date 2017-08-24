package ren.kangke.homekeymapping

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by renkangke .
 */

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val service = Intent(this, MyAccessibilityService::class.java)
        startService(service)
        updateAccessibilityServiceStatus()
    }

    private fun updateAccessibilityServiceStatus() {
        if (isAccessibilitySettingsOn(this)) {
            textView.text = "已开启辅助服务,请保证应用不要被清理"
            textView.setOnClickListener(null)
        } else {
            textView.text = "还没有开启辅助服务，去我去设置"
            textView.setOnClickListener {
                goAccess()
            }
        }
    }

    private fun goAccess() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun isAccessibilitySettingsOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        val service = "$packageName/${MyAccessibilityService::class.java.canonicalName}"
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Exception) {

        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(mContext.applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
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
