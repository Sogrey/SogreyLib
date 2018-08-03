package org.sogrey.utils;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * android 设置相关，跳转到相关设置页面
 * Created by Sogrey on 2018/8/1.
 */

public class AndroidSettingsUtils {
    //    Android系统设置选项的包名
//    以com.android.settings开头的形式
    public static final String ANDROID_SETTINGS_Settings = "com.android.settings.Settings";// 手机系统设置
    public static final String ANDROID_SETTINGS_WirelessSettings = "com.android.settings.WirelessSettings";// 无线和网络设置
    public static final String ANDROID_SETTINGS_AccessibilitySettings = "com.android.settings.AccessibilitySettings";// 辅助功能设置
    public static final String ANDROID_SETTINGS_ActivityPicker = "com.android.settings.ActivityPicker";// 选择活动
    public static final String ANDROID_SETTINGS_ApnSettings = "com.android.settings.ApnSettings";// APN设置
    public static final String ANDROID_SETTINGS_ApplicationSettings = "com.android.settings.ApplicationSettings";// 应用程序设置
    public static final String ANDROID_SETTINGS_BandMode = "com.android.settings.BandMode";// 设置GSM/UMTS波段
    public static final String ANDROID_SETTINGS_BatteryInfo = "com.android.settings.BatteryInfo";//电池信息
    public static final String ANDROID_SETTINGS_DateTimeSettings = "com.android.settings.DateTimeSettings";// 日期和时间设置
    public static final String ANDROID_SETTINGS_DateTimeSettingsSetupWizard = "com.android.settings.DateTimeSettingsSetupWizard";// 日期和时间设置2
    public static final String ANDROID_SETTINGS_DevelopmentSettings = "com.android.settings.DevelopmentSettings";// 开发者选项
    public static final String ANDROID_SETTINGS_DeviceAdminSettings = "com.android.settings.DeviceAdminSettings";// 设备管理器
    public static final String ANDROID_SETTINGS_DeviceInfoSettings = "com.android.settings.DeviceInfoSettings";// 关于手机
    public static final String ANDROID_SETTINGS_Display = "com.android.settings.Display";// 显示—设置显示字体大小及预览
    public static final String ANDROID_SETTINGS_DisplaySettings = "com.android.settings.DisplaySettings";// 显示设置
    public static final String ANDROID_SETTINGS_DockSettings = "com.android.settings.DockSettings";// 底座设置
    public static final String ANDROID_SETTINGS_IccLockSettings = "com.android.settings.IccLockSettings";// SIM卡锁定设置
    public static final String ANDROID_SETTINGS_InstalledAppDetails = "com.android.settings.InstalledAppDetails";// 应用程序的详细信息页面
    public static final String ANDROID_SETTINGS_LanguageSettings = "com.android.settings.LanguageSettings";// 语言和输入法设置
    public static final String ANDROID_SETTINGS_LocalePicker = "com.android.settings.LocalePicker";// 选择手机语言
    public static final String ANDROID_SETTINGS_LocalePickerInSetupWizard = "com.android.settings.LocalePickerInSetupWizard";// 选择手机语言2
    public static final String ANDROID_SETTINGS_ManageApplications = "com.android.settings.ManageApplications";// 应用管理
    public static final String ANDROID_SETTINGS_MasterClear = "com.android.settings.MasterClear";// 恢复出厂设置
    public static final String ANDROID_SETTINGS_MediaFormat = "com.android.settings.MediaFormat";// 格式化手机闪存
    public static final String ANDROID_SETTINGS_PhysicalKeyboardSettings = "com.android.settings.PhysicalKeyboardSettings";// 设置键盘
    public static final String ANDROID_SETTINGS_PrivacySettings = "com.android.settings.PrivacySettings";// 隐私设置
    public static final String ANDROID_SETTINGS_ProxySelector = "com.android.settings.ProxySelector";// 代理设置
    public static final String ANDROID_SETTINGS_RadioInfo = "com.android.settings.RadioInfo";// 手机信息
    public static final String ANDROID_SETTINGS_RunningServices = "com.android.settings.RunningServices";// 正在运行的程序（服务）
    public static final String ANDROID_SETTINGS_SecuritySettings = "com.android.settings.SecuritySettings";// 安全设置
    public static final String ANDROID_SETTINGS_SettingsSafetyLegalActivity = "com.android.settings.SettingsSafetyLegalActivity";// 安全信息
    public static final String ANDROID_SETTINGS_SoundSettings = "com.android.settings.SoundSettings";// 声音设置
    public static final String ANDROID_SETTINGS_TestingSettings = "com.android.settings.TestingSettings";// 测试
    public static final String ANDROID_SETTINGS_TetherSettings = "com.android.settings.TetherSettings";// 绑定与便携式热点
    public static final String ANDROID_SETTINGS_TextToSpeechSettings = "com.android.settings.TextToSpeechSettings";// 文字转语音设置
    public static final String ANDROID_SETTINGS_UsageStats = "com.android.settings.UsageStats";// 使用情况统计
    public static final String ANDROID_SETTINGS_UserDictionarySettings = "com.android.settings.UserDictionarySettings";// 用户词典
    public static final String ANDROID_SETTINGS_VoiceInputOutputSettings = "com.android.settings.VoiceInputOutputSettings";// 语音输入与输出设置

    //    以android.provider.Settings.开头的形式
    public static final String ACTION_ADD_ACCOUNT = android.provider.Settings.ACTION_ADD_ACCOUNT;//创建新账户界面
    public static final String ACTION_ACCESSIBILITY_SETTINGS = android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS;//系统的辅助功能界面
    public static final String ACTION_AIRPLANE_MODE_SETTINGS = android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS;//飞行模式设置界面
    public static final String ACTION_APN_SETTINGS = android.provider.Settings.ACTION_APN_SETTINGS;//APNs设置界面
    public static final String ACTION_APPLICATION_DETAILS_SETTINGS = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;//根据包名跳转到系统自带的应用程序信息界面
    public static final String ACTION_APPLICATION_DEVELOPMENT_SETTINGS = android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS;//开发人员选项界面
    public static final String ACTION_APPLICATION_SETTINGS = android.provider.Settings.ACTION_APPLICATION_SETTINGS;//应用程序列表界面
    public static final String ACTION_BLUETOOTH_SETTINGS = android.provider.Settings.ACTION_BLUETOOTH_SETTINGS;//蓝牙设置界面
    public static final String ACTION_DATA_ROAMING_SETTINGS = android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS;//移动网络设置界面
    public static final String ACTION_DATE_SETTINGS = android.provider.Settings.ACTION_DATE_SETTINGS;//日期时间设置界面
    public static final String ACTION_DEVICE_INFO_SETTINGS = android.provider.Settings.ACTION_DEVICE_INFO_SETTINGS;//手机状态界面
    public static final String ACTION_DISPLAY_SETTINGS = android.provider.Settings.ACTION_DISPLAY_SETTINGS;//手机显示界面
    public static final String ACTION_INPUT_METHOD_SETTINGS = android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS;//语言和输入设备界面
    public static final String ACTION_INPUT_METHOD_SUBTYPE_SETTINGS = android.provider.Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS;//语言选择界面（多国语言选择，API 11及以上）
    public static final String ACTION_INTERNAL_STORAGE_SETTINGS = android.provider.Settings.ACTION_INTERNAL_STORAGE_SETTINGS;//存储设置界面（内部存储）
    public static final String ACTION_LOCALE_SETTINGS = android.provider.Settings.ACTION_LOCALE_SETTINGS;//语言选择界面（仅有English和中文两种选择）
    public static final String ACTION_LOCATION_SOURCE_SETTINGS = android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;//位置服务界面（管理已安装的应用程序）
    public static final String ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS = android.provider.Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS;//跳转到应用程序界面（所有的）
    public static final String ACTION_MANAGE_APPLICATIONS_SETTINGS = android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS;//应用程序列表界面（已安装的）
    public static final String ACTION_MEMORY_CARD_SETTINGS = android.provider.Settings.ACTION_MEMORY_CARD_SETTINGS;//存储设置（记忆卡存储）
    public static final String ACTION_NETWORK_OPERATOR_SETTINGS = android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS;//选择网络运营商界面
    public static final String ACTION_NFCSHARING_SETTINGS = android.provider.Settings.ACTION_NFCSHARING_SETTINGS;//* 显示NFC共享设置，API 14及以上*
    public static final String ACTION_NFC_SETTINGS = android.provider.Settings.ACTION_NFC_SETTINGS;//显示NFC设置，API 16及以上
    public static final String ACTION_PRINT_SETTINGS = android.provider.Settings.ACTION_PRINT_SETTINGS;//打印设置
    public static final String ACTION_PRIVACY_SETTINGS = android.provider.Settings.ACTION_PRIVACY_SETTINGS;//备份和重置界面
    public static final String ACTION_QUICK_LAUNCH_SETTINGS = android.provider.Settings.ACTION_QUICK_LAUNCH_SETTINGS;//快速启动设置界面
    public static final String ACTION_SEARCH_SETTINGS = android.provider.Settings.ACTION_SEARCH_SETTINGS;//搜索设置界面
    public static final String ACTION_SECURITY_SETTINGS = android.provider.Settings.ACTION_SECURITY_SETTINGS;//安全设置界面
    public static final String ACTION_SETTINGS = android.provider.Settings.ACTION_SETTINGS;//系统设置界面
    public static final String ACTION_SOUND_SETTINGS = android.provider.Settings.ACTION_SOUND_SETTINGS;//声音设置界面
    public static final String ACTION_SYNC_SETTINGS = android.provider.Settings.ACTION_SYNC_SETTINGS;//账户同步界面
    public static final String ACTION_USER_DICTIONARY_SETTINGS = android.provider.Settings.ACTION_USER_DICTIONARY_SETTINGS;//用户字典界面
    public static final String ACTION_WIFI_IP_SETTINGS = android.provider.Settings.ACTION_WIFI_IP_SETTINGS;//IP设定界面
    public static final String ACTION_WIFI_SETTINGS = android.provider.Settings.ACTION_WIFI_SETTINGS;//Wi-Fi列表设置界面
    public static final String ACTION_WIRELESS_SETTINGS = android.provider.Settings.ACTION_WIRELESS_SETTINGS;//允许无线控制，Wi-Fi，蓝牙和移动网络的配置

    Context mContext;

    public AndroidSettingsUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 跳转到设置
     *
     * @param action AndroidSettingsUtils#ACTION_*，例如跳到Wi-Fi列表设置界面：{@link AndroidSettingsUtils#ACTION_WIFI_SETTINGS}
     */
    public void go2SettingsByAction(String action) {
        try {
            Intent wifiSettingsIntent = new Intent(action);
            mContext.startActivity(wifiSettingsIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳到设置页面
     * @param settingsClz AndroidSettingsUtils#ANDROID_SETTINGS_*，例如跳到Wi-Fi列表设置界面：{@link AndroidSettingsUtils#ANDROID_SETTINGS_WirelessSettings}
     */
    public void go2Settings(String settingsClz) {
        Intent intent = new Intent();
        //   第一个参为包名，第二个各个设置的类名(可以参考下面，包名不用改变)
        ComponentName cm = new ComponentName("com.android.settings",
                settingsClz);
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
