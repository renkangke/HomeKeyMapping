@file:Suppress("DEPRECATION")

package ren.kangke.homekeymapping

import android.annotation.SuppressLint
import android.os.Bundle
import com.rarnu.kt.android.BackActivity
import com.rarnu.kt.android.resStr
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * Created by rarnu on 9/4/17.
 */
class AboutActivity : BackActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_about)
        actionBar?.title = resStr(R.string.app_about)
        btnDonateAlipay.setOnClickListener { DonateUtil.donateAlipay(this) }

        try {
            val appInfo = packageManager.getPackageInfo(packageName, 0)
            tvAppVersion.text = "${appInfo.versionName}(${appInfo.versionCode})"
        } catch (e: Exception) {

        }
    }

}