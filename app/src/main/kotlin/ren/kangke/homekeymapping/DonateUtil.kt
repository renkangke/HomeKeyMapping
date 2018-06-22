package ren.kangke.homekeymapping

import android.app.Activity
import android.content.Context
import android.didikee.donate.AlipayDonate
import android.didikee.donate.WeiXinDonate
import android.graphics.BitmapFactory
import android.os.Environment
import com.rarnu.kt.android.resStr
import com.rarnu.kt.android.toast

@Suppress("unused")
/**
 * Created by rarnu on 9/4/17.
 */
object DonateUtil {

    private const val ALIPAY_CODE = "FKX03943MMSC4JXU3WIE15"

    fun donateAlipay(ctx: Context) {
        val hasClient = AlipayDonate.hasInstalledAlipayClient(ctx)
        if (hasClient) {
            AlipayDonate.startAlipayClient(ctx as Activity, ALIPAY_CODE)
        } else {
            with(ctx) { toast(resStr(R.string.donate_no_alipay)) }
        }
    }

    fun donateWechat(ctx: Context) {
        val hasClient = WeiXinDonate.hasInstalledWeiXinClient(ctx)
        if (hasClient) {
            val qrIs = ctx.resources.openRawResource(R.raw.weixin_donate)
            val qrPath = "${Environment.getExternalStorageDirectory().absolutePath}/.donate/weixin.png"
            WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(qrIs))
            WeiXinDonate.donateViaWeiXin(ctx as Activity, qrPath)
        } else {
            with(ctx) { toast(resStr(R.string.donate_no_wechat)) }

        }
    }

}