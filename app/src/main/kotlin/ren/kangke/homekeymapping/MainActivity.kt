package ren.kangke.homekeymapping

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import com.rarnu.base.app.BaseActivity

/**
 * Created by renkangke .
 */

class MainActivity : BaseActivity() {

    override fun customTheme(): Int = 0

    override fun getActionBarCanBack(): Boolean = false

    override fun getIcon(): Int = R.drawable.icon

    override fun replaceFragment(): Fragment = KeyBindFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val service = Intent(this, ButtonEventService::class.java)
        startService(service)
    }

}
