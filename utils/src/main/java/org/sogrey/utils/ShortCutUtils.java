package org.sogrey.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;

/**
 * 快捷方式
 * Created by Sogrey on 2018/8/8.
 */

public class ShortCutUtils {
    private static final String TAG = ShortCutUtils.class.getSimpleName();

    /**
     * 系统创建Activity快捷方式
     *
     * @param context     上下文
     * @param activiyName 快捷方式指向的activity 完整包名
     * @param title       标题
     * @param resIcon     图标资源ID
     */
    protected void createShortCut(Context context, String activiyName, String title, int resIcon) {
        Intent shortcutIntent = new Intent();
        //设置点击快捷方式时启动的Activity,因为是从Lanucher中启动，所以包名类名要写全。
        shortcutIntent.setComponent(new ComponentName(context.getPackageName(), activiyName));

        Log.i(TAG, "createShortCut: " + context.getPackageName());

        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent resultIntent = new Intent();

//        /** @deprecated */
//        @Deprecated
//        public static final String EXTRA_SHORTCUT_ICON = "android.intent.extra.shortcut.ICON";
//        /** @deprecated */
//        @Deprecated
//        public static final String EXTRA_SHORTCUT_ICON_RESOURCE = "android.intent.extra.shortcut.ICON_RESOURCE";
//        /** @deprecated */
//        @Deprecated
//        public static final String EXTRA_SHORTCUT_INTENT = "android.intent.extra.shortcut.INTENT";
//        /** @deprecated */
//        @Deprecated
//        public static final String EXTRA_SHORTCUT_NAME = "android.intent.extra.shortcut.NAME";

        //设置快捷方式图标
        resultIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",
                Intent.ShortcutIconResource.fromContext(context, resIcon));
        //启动的Intent
        resultIntent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
        //设置快捷方式的名称
        resultIntent.putExtra("android.intent.extra.shortcut.NAME", title);
        resultIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(resultIntent);
    }

    /**
     * 7.0系统创建Activity快捷方式
     *
     * @param context      上下文
     * @param activiyClazz 快捷方式指向的activity
     * @param shortCutId   快捷方式ID
     * @param shortLabel   短标题
     * @param longLabel    长标题
     * @param resIcon      图标资源ID
     */
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void createShortCut7(Context context, Class activiyClazz, String shortCutId, String shortLabel, String longLabel, int resIcon) {
        //获取系统服务得到ShortcutManager对象
        ShortcutManager systemService = context.getSystemService(ShortcutManager.class);
        if (Build.VERSION.SDK_INT >= 25) {

            //设置Intent跳转逻辑
            Intent intent = new Intent(context, activiyClazz);
            intent.setAction(Intent.ACTION_VIEW);

            //设置ID
            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, shortCutId)
                    //设置短标题
                    .setShortLabel(shortLabel)
                    //设置长标题
                    .setLongLabel(longLabel)
                    //设置icon
                    .setIcon(Icon.createWithResource(context, resIcon))
                    //设置Intent
                    .setIntent(intent)
                    .build();
            //这样就可以通过长按图标显示出快捷方式了
            if (systemService != null) {
                systemService.setDynamicShortcuts(Collections.singletonList(shortcutInfo));
            }
        }
    }


    /**
     * 8.0系统创建Activity快捷方式
     *
     * @param context          上下文
     * @param activiyClazz     快捷方式指向的activity
     * @param shortCutId       快捷方式ID
     * @param shortLabel       短标题
     * @param longLabel        长标题
     * @param resIcon          图标资源ID
     * @param callBackReceiver 添加快捷方式的确认弹框回调BroadcastReceiver class
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createShortCut8(Context context, Class activiyClazz, String shortCutId, String shortLabel, String longLabel,
                                       int resIcon, Class callBackReceiver) {
        ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        boolean requestPinShortcutSupported = false;
        if (shortcutManager != null) {
            requestPinShortcutSupported = shortcutManager.isRequestPinShortcutSupported();
        }
        Log.i(TAG, "启动器是否支持固定快捷方式: " + requestPinShortcutSupported);
        if (requestPinShortcutSupported) {
            Intent shortcutInfoIntent = new Intent(context, activiyClazz);
            shortcutInfoIntent.setAction(Intent.ACTION_VIEW);

            ShortcutInfo info = new ShortcutInfo.Builder(context, shortCutId)
                    .setIcon(Icon.createWithResource(context, resIcon))
                    .setShortLabel(shortLabel)
                    .setLongLabel(longLabel)
                    .setIntent(shortcutInfoIntent)
                    .build();

            //当添加快捷方式的确认弹框弹出来时，将被回调CallBackReceiver里面的onReceive方法
            PendingIntent shortcutCallbackIntent =
                    PendingIntent.getBroadcast(context, 0, new Intent(context, callBackReceiver), PendingIntent.FLAG_UPDATE_CURRENT);

            shortcutManager.requestPinShortcut(info, shortcutCallbackIntent.getIntentSender());

        }
    }
}
