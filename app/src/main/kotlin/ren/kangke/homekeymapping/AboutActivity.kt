package ren.kangke.homekeymapping

import android.app.Fragment
import android.os.Bundle
import android.view.Menu
import com.rarnu.base.app.BaseActivity
import com.rarnu.base.app.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_about.view.*

/**
 * Created by rarnu on 9/4/17.
 */
class AboutActivity: BaseActivity() {
    override fun customTheme(): Int = 0

    override fun getActionBarCanBack(): Boolean = true

    override fun getIcon(): Int = R.drawable.icon

    override fun replaceFragment(): Fragment = AboutFragment()

    class AboutFragment: BaseFragment() {
        override fun getBarTitle(): Int = R.string.app_about

        override fun getBarTitleWithPath(): Int = 0

        override fun getCustomTitle(): String? = null

        override fun getFragmentLayoutResId(): Int = R.layout.fragment_about

        override fun getFragmentState(): Bundle? = null

        override fun getMainActivityName(): String? = null

        override fun initComponents() {

        }

        override fun initEvents() {
            innerView.btnDonateAlipay.setOnClickListener { DonateUtil.donateAlipay(activity) }
        }

        override fun initLogic() {

        }

        override fun initMenu(menu: Menu) {

        }

        override fun onGetNewArguments(bn: Bundle?) {

        }

    }

}