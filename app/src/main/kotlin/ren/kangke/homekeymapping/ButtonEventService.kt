package ren.kangke.homekeymapping

import android.accessibilityservice.AccessibilityService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import java.lang.ref.WeakReference

/**
 * The accessibility service for change home key to be back.
 * Created by renkangke .
 */

class ButtonEventService : AccessibilityService() {

    companion object {
        /**
         * The interval time of home action.
         */
        val INTERVAL_TIME = 100L
        private val HANDLER_HOME_DELAY_TIME_WHAT   = 100001
        private val HANDLER_RECENT_DELAY_TIME_WHAT = 100002
        private val HANDLER_BACK_DELAY_TIME_WHAT   = 100003

        fun getDelayTimeWhat(keyCode: Int): Int = when(keyCode) {
            KeyEvent.KEYCODE_HOME -> HANDLER_HOME_DELAY_TIME_WHAT
            KeyEvent.KEYCODE_BACK -> HANDLER_BACK_DELAY_TIME_WHAT
            KeyEvent.KEYCODE_APP_SWITCH -> HANDLER_RECENT_DELAY_TIME_WHAT
            else -> 0
        }

        fun getPressAction(ctx: Context, keyCode: Int): Int = when(keyCode) {
            KeyEvent.KEYCODE_HOME -> {
                var code = Config.getKeyCode(ctx, Config.KEY_HOME_PRESS)
                if (code == -1) code = Config.getDefaultKeyCode(Config.KEY_HOME_PRESS)
                code
            }
            KeyEvent.KEYCODE_BACK -> {
                var code = Config.getKeyCode(ctx, Config.KEY_BACK_PRESS)
                if (code == -1) code = Config.getDefaultKeyCode(Config.KEY_BACK_PRESS)
                code
            }
            KeyEvent.KEYCODE_APP_SWITCH -> {
                var code = Config.getKeyCode(ctx, Config.KEY_RECENT_PRESS)
                if (code == -1) code = Config.getDefaultKeyCode(Config.KEY_RECENT_PRESS)
                code
            }
            else -> 0
        }

        fun getLongPressAction(ctx: Context, msgWhat: Int): Int = when(msgWhat) {
            HANDLER_HOME_DELAY_TIME_WHAT -> {
                var code = Config.getKeyCode(ctx, Config.KEY_HOME_LONG_PRESS)
                if (code == -1) code = Config.getDefaultKeyCode(Config.KEY_HOME_LONG_PRESS)
                code
            }
            HANDLER_BACK_DELAY_TIME_WHAT -> {
                var code = Config.getKeyCode(ctx, Config.KEY_BACK_LONG_PRESS)
                if (code == -1) code = Config.getDefaultKeyCode(Config.KEY_BACK_LONG_PRESS)
                code
            }
            HANDLER_RECENT_DELAY_TIME_WHAT -> {
                var code = Config.getKeyCode(ctx, Config.KEY_RECENT_LONG_PRESS)
                if (code == -1) code = Config.getDefaultKeyCode(Config.KEY_RECENT_LONG_PRESS)
                code
            }
            else -> 0
        }
    }

    private var mIsFirstAction = false
    private var mFirstEventTime = 0L

    private val mAccessibilityHandler: Handler = AccessibilityHandler(this, this)

    override fun onInterrupt() { }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) { }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        val key = event.keyCode
        val what = getDelayTimeWhat(key)
        if (what != 0) {
            mIsFirstAction = !mIsFirstAction
            if (mIsFirstAction) {
                mFirstEventTime = System.currentTimeMillis()
                mAccessibilityHandler.sendEmptyMessageDelayed(what, INTERVAL_TIME * 3)
            } else {
                val interval = System.currentTimeMillis() - mFirstEventTime
                if (interval < (INTERVAL_TIME * 3)) {
                    mAccessibilityHandler.removeMessages(what)
                    val action = getPressAction(this, key)
                    if (action != 0) {
                        performGlobalAction(action)
                    }
                }
            }
            return true
        }
        return super.onKeyEvent(event)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = Service.START_STICKY_COMPATIBILITY

    class AccessibilityHandler(ctx: Context, serviceWeakReference: ButtonEventService) : Handler() {
        var mContext = ctx
        var mServiceWeakReference: WeakReference<ButtonEventService> = WeakReference(serviceWeakReference)
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val action = getLongPressAction(mContext, msg.what)
            if (action != 0) {
                mServiceWeakReference.get()?.performGlobalAction(action)
            }
            removeMessages(msg.what)

        }
    }

}
