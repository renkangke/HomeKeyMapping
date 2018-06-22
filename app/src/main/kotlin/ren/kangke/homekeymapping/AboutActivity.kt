package ren.kangke.homekeymapping

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import com.rarnu.kt.android.resStr
import com.rarnu.kt.android.showActionBack
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * Created by rarnu on 9/4/17.
 */
class AboutActivity : Activity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_about)
        actionBar.title = resStr(R.string.app_about)
        showActionBack()

        btnDonateAlipay.setOnClickListener { DonateUtil.donateAlipay(this) }

        try {
            val appInfo = packageManager.getPackageInfo(packageName, 0)
            tvAppVersion.text = "${appInfo.versionName}(${appInfo.versionCode})"
        } catch (e: Exception) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

}