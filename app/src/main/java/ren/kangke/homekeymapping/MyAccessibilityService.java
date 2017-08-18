package ren.kangke.homekeymapping;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by renkangke .
 */

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";
    public static final int INTERVAL_TIME = 300;

    boolean mIsFirstHome = false;
    long mFirstHomeEvent = 0;

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
                mIsFirstHome = !mIsFirstHome;
                if (mIsFirstHome) {
                    mFirstHomeEvent = System.currentTimeMillis();
                } else {
                    long interval = System.currentTimeMillis() - mFirstHomeEvent;
                    if (interval > INTERVAL_TIME) {
                        return super.onKeyEvent(event);
                    } else {
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        return true;
                    }
                }
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(TAG, "action" + event.getAction() + "," + event.getPackageName() + ",type:" + event.getEventType() + ", des:" + event.getContentDescription());
    }

    @Override
    public void onInterrupt() {

    }

}
