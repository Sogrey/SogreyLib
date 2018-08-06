package org.sogrey.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Sogrey on 2017/10/19.
 */

public class AlertUtils {
    public AlertUtils() {
    }

    @SuppressWarnings("unchecked")
    public AlertDialog alert(Context context, String title, String message,
                             String ok, DialogInterface.OnClickListener okListener,
                             String cancle, DialogInterface.OnClickListener cancleListener,
                             String ignore, DialogInterface.OnClickListener ignoreListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!TextUtils.isEmpty(ok)) {
            builder.setPositiveButton(ok, okListener);
        }
        if (!TextUtils.isEmpty(cancle)) {
            builder.setNegativeButton(cancle, cancleListener);
        }
        if (!TextUtils.isEmpty(ignore)) {
            builder.setNeutralButton(ignore, ignoreListener);
        }

        // 不需要绑定按键事件
        // 屏蔽keycode等于84之类的按键
        builder.setOnKeyListener((dialog, keyCode, event) -> {
            Log.v("alert", "keyCode==" + keyCode + "event=" + event);
            return true;
        });
        // 禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        return dialog;
    }

    public AlertDialog alert(Context context, String title, String message,
                             String ok, DialogInterface.OnClickListener okListener,
                             String cancle, DialogInterface.OnClickListener cancleListener) {
        return alert(context, title, message, ok, okListener, cancle, cancleListener, null, null);
    }

    public AlertDialog alert(Context context, String title, String message,
                             String ok, DialogInterface.OnClickListener okListener) {
        return alert(context, title, message, ok, okListener, null, null, null, null);
    }

    @Deprecated
    public AlertDialog progressDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        // 不需要绑定按键事件
        // 屏蔽keycode等于84之类的按键
        builder.setOnKeyListener((dialog, keyCode, event) -> {
            Log.v("alert", "keyCode==" + keyCode + "event=" + event);
            return true;
        });
        // 禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialog_progress = inflater.inflate(R.layout.dialog_progress, null);
        TextView txtMsg = dialog_progress.findViewById(R.id.txt_dialog_progress_msg);
        txtMsg.setText(message);
        dialog.setContentView(dialog_progress);
        dialog.show();
        return dialog;
    }

    /**
     * 进度对话框
     *
     * @param context        上下文
     * @param title          标题
     * @param message        消息内容
     * @param ok             确定钮
     * @param okListener     确定事件
     * @param cancle         取消钮
     * @param cancleListener 取消事件
     * @param ignore         忽略钮
     * @param ignoreListener 忽略事件
     * @return
     */
    public AlertDialog alertProgress(Context context, String title, String message,
                                     String ok, DialogInterface.OnClickListener okListener,
                                     String cancle, DialogInterface.OnClickListener cancleListener,
                                     String ignore, DialogInterface.OnClickListener ignoreListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!TextUtils.isEmpty(ok)) {
            builder.setPositiveButton(ok, okListener);
        }
        if (!TextUtils.isEmpty(cancle)) {
            builder.setNegativeButton(cancle, cancleListener);
        }
        if (!TextUtils.isEmpty(ignore)) {
            builder.setNeutralButton(ignore, ignoreListener);
        }

        // 不需要绑定按键事件
        // 屏蔽keycode等于84之类的按键
        builder.setOnKeyListener((dialog, keyCode, event) -> {
            Log.v("alert", "keyCode==" + keyCode + "event=" + event);
            return true;
        });
        // 禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_progress_h, null);
        TextView tv = contentView.findViewById(R.id.txt_progress);
        tv.setText(context.getResources().getString(R.string.txt_progress, "0%"));
        ProgressBar prg = contentView.findViewById(R.id.progressBar);
        prg.setProgress(0);
        dialog.setView(contentView);
        dialog.show();
        return dialog;
    }

    /**
     * 更新上面进度框
     *
     * @param dialog   传入上面方法返回值（AlertDialog对象）
     * @param resTemp  内容（带有占位符，表示进度）
     * @param progress 进度 0-100
     */
    public void updateProgress(AlertDialog dialog, int resTemp, int progress) {
        if (dialog != null) {
            TextView tv = dialog.getWindow().findViewById(R.id.txt_progress);
            ProgressBar prg = dialog.getWindow().findViewById(R.id.progressBar);
            if (prg.getProgress() >= progress) return;
            if (tv != null)
                tv.setText(dialog.getContext().getResources().getString(resTemp, -1 == progress ? "" : progress + "%"));//R.string.txt_uploading
            if (prg != null)
                prg.setProgress(-1 == progress ? 0 : progress);
        }
    }

    /**
     * 进度对话框
     *
     * @param context        上下文
     * @param title          标题
     * @param message        消息内容
     * @param ok             确定钮
     * @param okListener     确定事件
     * @param cancle         取消钮
     * @param cancleListener 取消事件
     * @return
     */
    public AlertDialog alertProgress(Context context, String title, String message,
                                     String ok, DialogInterface.OnClickListener okListener,
                                     String cancle, DialogInterface.OnClickListener cancleListener) {
        return alertProgress(context, title, message, ok, okListener, cancle, cancleListener, null, null);
    }

    /**
     * 进度对话框
     *
     * @param context    上下文
     * @param title      标题
     * @param message    消息内容
     * @param ok         确定钮
     * @param okListener 确定事件
     * @return
     */
    public AlertDialog alertProgress(Context context, String title, String message,
                                     String ok, DialogInterface.OnClickListener okListener) {
        return alertProgress(context, title, message, ok, okListener, null, null, null, null);
    }

    /**
     * 等待框
     *
     * @param context        上下文
     * @param title          标题
     * @param message        消息内容
     * @param ok             确定钮
     * @param okListener     确定事件
     * @param cancle         取消钮
     * @param cancleListener 取消事件
     * @param ignore         忽略钮
     * @param ignoreListener 忽略事件
     */
    public AlertDialog alertWaiting(Context context, String title, String message,
                                    String ok, DialogInterface.OnClickListener okListener,
                                    String cancle, DialogInterface.OnClickListener cancleListener,
                                    String ignore, DialogInterface.OnClickListener ignoreListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
//        if (!TextUtils.isEmpty(message)) {
//            builder.setMessage(message);
//        }
        if (!TextUtils.isEmpty(ok)) {
            builder.setPositiveButton(ok, okListener);
        }
        if (!TextUtils.isEmpty(cancle)) {
            builder.setNegativeButton(cancle, cancleListener);
        }
        if (!TextUtils.isEmpty(ignore)) {
            builder.setNeutralButton(ignore, ignoreListener);
        }

        // 不需要绑定按键事件
        // 屏蔽keycode等于84之类的按键
        builder.setOnKeyListener((dialog, keyCode, event) -> {
            Log.v("alert", "keyCode==" + keyCode + "event=" + event);
            return true;
        });
        // 禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_progress_c, null);
        TextView tv = contentView.findViewById(R.id.txt_dialog_progress_msg);
        if (!TextUtils.isEmpty(message))
            tv.setText(message);
        dialog.setView(contentView);
//        dialog.show();//手动显示
        return dialog;
    }

    /**
     * 等待框
     *
     * @param context        上下文
     * @param title          标题
     * @param message        消息内容
     * @param ok             确定钮
     * @param okListener     确定事件
     * @param cancle         取消钮
     * @param cancleListener 取消事件
     */
    public AlertDialog alertWaiting(Context context, String title, String message,
                                    String ok, DialogInterface.OnClickListener okListener,
                                    String cancle, DialogInterface.OnClickListener cancleListener) {
        return alertWaiting(context, title, message, ok, okListener, cancle, cancleListener, "", null);
    }

    /**
     * 等待框
     *
     * @param context    上下文
     * @param title      标题
     * @param message    消息内容
     * @param ok         确定钮
     * @param okListener 确定事件
     */
    public AlertDialog alertWaiting(Context context, String title, String message,
                                    String ok, DialogInterface.OnClickListener okListener) {
        return alertWaiting(context, title, message, ok, okListener, "", null);
    }

    /**
     * 等待框
     *
     * @param context 上下文
     * @param title   标题
     * @param message 消息内容
     */
    public AlertDialog alertWaiting(Context context, String title, String message) {
        return alertWaiting(context, title, message, "", null);
    }
}
