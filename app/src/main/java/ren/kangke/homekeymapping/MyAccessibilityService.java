package ren.kangke.homekeymapping;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import java.lang.ref.WeakReference;

/**
 * The accessibility service for change home key to be back.
 * Created by renkangke .
 */

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";

    /**
     * The interval time of home action.
     */
    public static final int INTERVAL_TIME = 300;
    public static final int HANDLER_HOME_DELAY_TIME_WHAT = 100001;

    private boolean mIsFirstHomeAction = false;
    private long mFirstHomeEventTime = 0;

    final Handler mAccessibilityHandler = new AccessibilityHandler(this);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.i(TAG, "onKeyEvent:" + event.getKeyCode());
        int key = event.getKeyCode();
        switch (key) {
            case KeyEvent.KEYCODE_HOME:
                mIsFirstHomeAction = !mIsFirstHomeAction;
                if (mIsFirstHomeAction) {
                    mFirstHomeEventTime = System.currentTimeMillis();
                    mAccessibilityHandler.sendEmptyMessageDelayed(HANDLER_HOME_DELAY_TIME_WHAT, INTERVAL_TIME);
                } else {
                    long interval = System.currentTimeMillis() - mFirstHomeEventTime;
                    if (interval > INTERVAL_TIME) {
                        return super.onKeyEvent(event);
                    } else {
                        mAccessibilityHandler.removeMessages(HANDLER_HOME_DELAY_TIME_WHAT);
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        return true;
                    }
                }
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    private static class AccessibilityHandler extends Handler {
        WeakReference<MyAccessibilityService> mServiceWeakReference;

        AccessibilityHandler(MyAccessibilityService serviceWeakReference) {
            mServiceWeakReference = new WeakReference<>(serviceWeakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_HOME_DELAY_TIME_WHAT:
                    if (mServiceWeakReference != null && mServiceWeakReference.get() != null) {
                        mServiceWeakReference.get().performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                    }
                    removeMessages(HANDLER_HOME_DELAY_TIME_WHAT);
                    break;
            }
        }
    }

}
