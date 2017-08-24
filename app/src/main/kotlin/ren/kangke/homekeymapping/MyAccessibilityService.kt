package ren.kangke.homekeymapping

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import java.lang.ref.WeakReference

/**
 * The accessibility service for change home key to be back.
 * Created by renkangke .
 */

class MyAccessibilityService : AccessibilityService() {

    companion object {
        /**
         * The interval time of home action.
         */
        val INTERVAL_TIME = 100L
        val HANDLER_HOME_DELAY_TIME_WHAT = 100001
    }

    private var mIsFirstHomeAction = false
    private var mFirstHomeEventTime: Long = 0
    private val mAccessibilityHandler: Handler = AccessibilityHandler(this)

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        val key = event.keyCode
        when(key) {
            KeyEvent.KEYCODE_HOME -> {
                mIsFirstHomeAction = !mIsFirstHomeAction
                if (mIsFirstHomeAction) {
                    mFirstHomeEventTime = System.currentTimeMillis()
                    mAccessibilityHandler.sendEmptyMessageDelayed(HANDLER_HOME_DELAY_TIME_WHAT, INTERVAL_TIME)
                } else {
                    val interval = System.currentTimeMillis() - mFirstHomeEventTime
                    if (interval > (INTERVAL_TIME * 5)) {
                        return super.onKeyEvent(event)
                    } else {
                        mAccessibilityHandler.removeMessages(HANDLER_HOME_DELAY_TIME_WHAT)
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
                        return true
                    }
                }
            }
            KeyEvent.KEYCODE_BACK -> {
                // TODO: back
            }

            KeyEvent.KEYCODE_APP_SWITCH -> {
                // TODO: recent app
            }
        }

        return super.onKeyEvent(event)
    }

    inner class AccessibilityHandler(serviceWeakReference: MyAccessibilityService) : Handler() {
        internal var mServiceWeakReference: WeakReference<MyAccessibilityService>? = null
        init {
            mServiceWeakReference = WeakReference(serviceWeakReference)
        }
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what) {
                HANDLER_HOME_DELAY_TIME_WHAT -> {
                    mServiceWeakReference?.get()?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
                    removeMessages(HANDLER_HOME_DELAY_TIME_WHAT)
                }
            }
        }
    }

}
