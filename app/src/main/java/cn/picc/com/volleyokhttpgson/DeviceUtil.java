package cn.picc.com.volleyokhttpgson;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class DeviceUtil {
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }

    public static String getImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * ANDROID_ID 是设备首次启动时由系统随机生成的一串64位的十六进制数字
     * 缺点：
     * ①.设备刷机wipe数据或恢复出厂设置时ANDROID_ID值会被重置。
     * ②.现在网上已有修改设备ANDROID_ID值的APP应用。
     * ③.某些厂商定制的系统可能会导致不同的设备产生相同的ANDROID_ID。
     * ④.某些厂商定制的系统可能导致设备返回ANDROID_ID值为空。
     * ⑤.CDMA设备，ANDROID_ID和DeviceId返回的值相同
     *
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        return UUID.nameUUIDFromBytes(ANDROID_ID.getBytes()).toString();
    }

    public static int getVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.versionCode;
        }
        return 0;
    }

    public static String getMacAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }
}
