package org.sogrey.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Sogrey on 2016/9/21.
 */
public class IntentUtils {

    //VPN设置
    private static final String ACTION_VPN_SETTINGS = "android.net.vpn.SETTINGS";
    private static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1000;

    private static IntentUtils mIntentUtils;

    private IntentUtils() {
    }

    public static synchronized IntentUtils getInstance() {
        if (mIntentUtils == null) {
            mIntentUtils = new IntentUtils();
        }
        return mIntentUtils;
    }


    //前往设置VPN
    public void GoToVPNSetting(Context context) {
        Intent intent = new Intent(ACTION_VPN_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 跳转到手势密码校验界面
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void jumpToGesturePassCheck(Context context) {
        KeyguardManager keyguardManager =
                (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        Intent intent =
                keyguardManager.createConfirmDeviceCredentialIntent("finger", "指纹识别");
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
    }

    /**
     * 真退出应用
     *
     * @param context
     */
    public void exitProgrames(Context context) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 假退出
     *
     * @param context
     */
    public void exitProgrames2Home(Context context) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(i);
        ((Activity) context).finish();
    }

    /**
     * 根据包名跳转到系统自带的应用程序信息界面
     *
     * @param context 上下文
     */
    public void goApplicationSetting(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        context.startActivity(intent);
    }

    /**
     * 跳转设置页面
     *
     * @param context 上下文
     * @param action  <ul>
     *                <li>{@link android.provider.Settings#ACTION_ACCESSIBILITY_SETTINGS} 跳转系统的辅助功能界面</li>
     *                <li>{@link android.provider.Settings#ACTION_ADD_ACCOUNT} 显示添加帐户创建一个新的帐户屏幕。【测试跳转到微信登录界面】</li>
     *                <li>{@link android.provider.Settings#ACTION_AIRPLANE_MODE_SETTINGS} 飞行模式</li>
     *                <li>{@link android.provider.Settings#ACTION_WIRELESS_SETTINGS} 无线网和网络设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_APN_SETTINGS} 跳转 APN设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_APPLICATION_DEVELOPMENT_SETTINGS} 跳转开发人员选项界面</li>
     *                <li>{@link android.provider.Settings#ACTION_APPLICATION_SETTINGS} 跳转应用程序列表界面</li>
     *                <li>{@link android.provider.Settings#ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS} 跳转到应用程序界面【所有的】</li>
     *                <li>{@link android.provider.Settings#ACTION_MANAGE_APPLICATIONS_SETTINGS} 跳转 应用程序列表界面【已安装的】</li>
     *                <li>{@link android.provider.Settings#ACTION_BLUETOOTH_SETTINGS} 跳转系统的蓝牙设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_DATA_ROAMING_SETTINGS} 跳转到移动网络设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_DATE_SETTINGS} 跳转日期时间设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_DEVICE_INFO_SETTINGS} 跳转手机状态界面</li>
     *                <li>{@link android.provider.Settings#ACTION_DISPLAY_SETTINGS} 跳转手机显示界面</li>
     *                <li>{@link android.provider.Settings#ACTION_DREAM_SETTINGS} 【API 18及以上 没测试，不一定存在】</li>
     *                <li>{@link android.provider.Settings#ACTION_INPUT_METHOD_SETTINGS} 跳转语言和输入设备</li>
     *                <li>{@link android.provider.Settings#ACTION_INPUT_METHOD_SUBTYPE_SETTINGS} 【API 11及以上】  //  跳转 语言选择界面 【多国语言选择】</li>
     *                <li>{@link android.provider.Settings#ACTION_INTERNAL_STORAGE_SETTINGS}  跳转存储设置界面【内部存储】</li>
     *                <li>{@link android.provider.Settings#ACTION_MEMORY_CARD_SETTINGS} 跳转 存储设置 【记忆卡存储】</li>
     *                <li>{@link android.provider.Settings#ACTION_LOCALE_SETTINGS} 跳转语言选择界面【仅有English 和 中文两种选择】</li>
     *                <li>{@link android.provider.Settings#ACTION_LOCATION_SOURCE_SETTINGS} 跳转位置服务界面【管理已安装的应用程序。】</li>
     *                <li>{@link android.provider.Settings#ACTION_NETWORK_OPERATOR_SETTINGS} 跳转到 显示设置选择网络运营商。</li>
     *                <li>{@link android.provider.Settings#ACTION_NFCSHARING_SETTINGS} 显示NFC共享设置。 【API 14及以上】</li>
     *                <li>{@link android.provider.Settings#ACTION_NFC_SETTINGS} 显示NFC设置。这显示了用户界面,允许NFC打开或关闭。【API 16及以上】</li>
     *                <li>{@link android.provider.Settings#ACTION_PRIVACY_SETTINGS} 跳转到备份和重置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_QUICK_LAUNCH_SETTINGS} 跳转快速启动设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_SEARCH_SETTINGS} 跳转到 搜索设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_SECURITY_SETTINGS} 跳转到安全设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_SETTINGS} 跳转到设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_SOUND_SETTINGS} 跳转到声音设置界面</li>
     *                <li>{@link android.provider.Settings#ACTION_SYNC_SETTINGS}跳转账户同步界面</li>
     *                <li>{@link android.provider.Settings#ACTION_USER_DICTIONARY_SETTINGS}跳转用户字典界面</li>
     *                <li>{@link android.provider.Settings#ACTION_WIFI_IP_SETTINGS}跳转到IP设定界面</li>
     *                <li>{@link android.provider.Settings#ACTION_WIFI_SETTINGS}跳转Wifi列表设置</li>
     *                <ul/>
     */
    public void go2Settings(Context context, String action) {
        Intent intent = new Intent(action);
        context.startActivity(intent);
    }

}
