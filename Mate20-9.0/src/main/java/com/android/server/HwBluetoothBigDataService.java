package com.android.server;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.SystemProperties;
import android.os.UserHandle;
import com.android.server.wifipro.WifiProCommonUtils;
import java.util.Iterator;
import java.util.List;

public class HwBluetoothBigDataService {
    public static final String BIGDATA_RECEIVER_PACKAGENAME = "com.android.bluetooth";
    public static final String BLUETOOTH_BIGDATA = "com.android.bluetooth.bluetoothBigdata";
    private static final String TAG = "HwBluetoothBigDataService";
    private static HwBluetoothBigDataService mHwBluetoothBigDataService = null;
    boolean isChinaArea = "CN".equalsIgnoreCase(SystemProperties.get(WifiProCommonUtils.KEY_PROP_LOCALE, ""));

    public static synchronized HwBluetoothBigDataService getHwBluetoothBigDataService() {
        HwBluetoothBigDataService hwBluetoothBigDataService;
        synchronized (HwBluetoothBigDataService.class) {
            if (mHwBluetoothBigDataService == null) {
                mHwBluetoothBigDataService = new HwBluetoothBigDataService();
            }
            hwBluetoothBigDataService = mHwBluetoothBigDataService;
        }
        return hwBluetoothBigDataService;
    }

    public void sendBigDataEvent(Context mContext, String bigDataEvent) {
        if (this.isChinaArea && getAppName(mContext, Binder.getCallingPid()) != null) {
            Intent intent = new Intent();
            intent.setAction(bigDataEvent);
            intent.setPackage(BIGDATA_RECEIVER_PACKAGENAME);
            intent.putExtra("appName", getAppName(mContext, Binder.getCallingPid()));
            mContext.sendBroadcastAsUser(intent, UserHandle.of(UserHandle.getCallingUserId()), BLUETOOTH_BIGDATA);
        }
    }

    public String getAppName(Context mContext, int pid) {
        if (pid <= 0) {
            return null;
        }
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService("activity");
        if (activityManager == null) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null || appProcesses.size() == 0) {
            return null;
        }
        String packageName = null;
        Iterator<ActivityManager.RunningAppProcessInfo> it = appProcesses.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ActivityManager.RunningAppProcessInfo appProcess = it.next();
            if (appProcess.pid == pid && appProcess.importance == 100) {
                packageName = appProcess.processName;
                break;
            }
        }
        return packageName;
    }
}
