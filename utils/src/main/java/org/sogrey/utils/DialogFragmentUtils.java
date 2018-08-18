package org.sogrey.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sogrey on 2018/8/13.
 */

public class DialogFragmentUtils extends AppCompatDialogFragment {
    private static final String TAG = "DialogFragmentUtils";
    private Context mContext;
    private Activity mActivity = null;
    private Fragment mFragment = null;
    //标题
    private String mTitle = "提示";
    private int mTitleSize = 20;
    private int mTitleColor = R.color.colorPrimaryDark;
    //内容
    private String mMessage = "暂无内容";
    //按钮
    private String mPositiveBtnStr = "确定";
    private String mNegativeBtnStr = "取消";
    private String mNeutralBtnStr = "";//中立按钮默认无文字
    private int mPositiveBtnColor = R.color.colorPrimary;
    private int mNegativeBtnColor = R.color.colorPrimary;
    private int mNeutralBtnColor = R.color.colorSecondary;
    private int mPositiveBtnStrSize = 14;
    private int mNegativeBtnStrSize = 14;
    private int mNeutralBtnStrSize = 14;
    //背景透明度
    private float mDimAmout = 0.0f;
    //能否点击取消
    private boolean mCancelOutside = true;
    private boolean mCancelBackup = true;

    ///////////////////////////////////////////构造/////////////////////////////////////////////////////

    /**
     * 用volatile修饰的变量，
     * 线程在每次使用变量的时候，都会读取变量修改后的最的值。
     * volatile很容易被误用，用来进行原子性操作。
     */
    private static volatile DialogFragmentUtils DialogFragmentUtils = null;

    //私有化构造函数：
    private void PayDialogFragment() {
    }

    /**
     * 单例模式：创建  Fragment：
     *
     * @return
     */
    public static DialogFragmentUtils getInstance() {
        if (DialogFragmentUtils == null) {
            synchronized (DialogFragmentUtils.class) {
                if (DialogFragmentUtils == null) {
                    DialogFragmentUtils = new DialogFragmentUtils();
                }
            }
        }
        return DialogFragmentUtils;
    }

    //activity调用
    //DialogFragmentUtils editNameDialog = DialogFragmentUtils.getInstance();
    //                   editNameDialog.show(getFragmentManager(), "PayDialog");

    /////////////////////////////////////////setter//////////////////////////////////////////////////


    /**
     * 设置中立按钮文字
     *
     * @param neutralBtnStr 默认:"等等再说"
     * @return
     */
    public DialogFragmentUtils setNeutralBtnStr(String neutralBtnStr) {
        mNeutralBtnStr = neutralBtnStr;
        return this;
    }

    /**
     * 设置中立按钮颜色
     *
     * @param neutralBtnColor 默认:R.color.secondary_text
     * @return
     */
    public DialogFragmentUtils setNeutralBtnColor(int neutralBtnColor) {
        mNeutralBtnColor = neutralBtnColor;
        return this;
    }

    /**
     * 设置中立按钮尺寸
     *
     * @param neutralBtnStrSize 默认:14
     * @return
     */
    public DialogFragmentUtils setNeutralBtnStrSize(int neutralBtnStrSize) {
        mNeutralBtnStrSize = neutralBtnStrSize;
        return this;
    }

    /**
     * 设置能否点击外部取消对话框
     *
     * @param cancelOutside 默认:true 可以取消
     * @return
     */
    public DialogFragmentUtils setCancelOutside(boolean cancelOutside) {
        mCancelOutside = cancelOutside;
        return this;
    }

    /**
     * 设置能否点击"后退键"取消对话框
     *
     * @param cancelBackup 默认:true 可以取消
     * @return
     */
    public DialogFragmentUtils setCancelBackup(boolean cancelBackup) {
        mCancelBackup = cancelBackup;
        return this;
    }

    /**
     * 设置标题字号
     *
     * @param titleSize 默认:20
     * @return
     */
    public DialogFragmentUtils setTitleSize(int titleSize) {
        mTitleSize = titleSize;
        return this;
    }

    /**
     * 设置标题颜色
     *
     * @param titleColor 默认:R.color.primary_text
     * @return
     */
    public DialogFragmentUtils setTitleColor(int titleColor) {
        mTitleColor = titleColor;
        return this;
    }

    /**
     * 设置积极按钮颜色
     *
     * @param positiveBtnColor 默认:R.color.primary
     * @return
     */
    public DialogFragmentUtils setPositiveBtnColor(int positiveBtnColor) {
        mPositiveBtnColor = positiveBtnColor;
        return this;
    }

    /**
     * 设置消极按钮颜色
     *
     * @param negativeBtnColor 默认:R.color.primary
     * @return
     */
    public DialogFragmentUtils setNegativeBtnColor(int negativeBtnColor) {
        mNegativeBtnColor = negativeBtnColor;
        return this;
    }

    /**
     * 设置积极按钮(文字)尺寸
     *
     * @param positiveBtnStrSize 默认:14
     * @return
     */
    public DialogFragmentUtils setPositiveBtnStrSize(int positiveBtnStrSize) {
        mPositiveBtnStrSize = positiveBtnStrSize;
        return this;
    }

    /**
     * 设置消极按钮(文字)尺寸
     *
     * @param negativeBtnStrSize 默认14
     * @return
     */
    public DialogFragmentUtils setNegativeBtnStrSize(int negativeBtnStrSize) {
        mNegativeBtnStrSize = negativeBtnStrSize;
        return this;
    }

    /**
     * 设置上下文 context
     *
     * @param context
     * @return
     */
    public DialogFragmentUtils setContext(Context context) {
        this.mContext = context;
        return this;
    }

    /**
     * activity调用对话框时设置
     *
     * @param activity
     * @return
     */
    public DialogFragmentUtils setActivity(Activity activity) {
        mActivity = activity;
        return this;
    }

    /**
     * fragment调用对话框时设置
     *
     * @param fragment
     * @return
     */
    public DialogFragmentUtils setFragment(Fragment fragment) {
        mFragment = fragment;
        return this;
    }

    /**
     * 编写标题文字
     *
     * @param title 默认:"提示"
     * @return
     */
    public DialogFragmentUtils setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    /**
     * 设置对话框弹出背景透明度
     *
     * @param dimAmout 不透明程度 0.0f(透明)-1.0f(不透明)
     * @return
     */
    public DialogFragmentUtils setDimAmout(float dimAmout) {
        this.mDimAmout = dimAmout;
        return this;
    }

    /**
     * 编写内容文字
     *
     * @param message 默认为空
     * @return
     */
    public DialogFragmentUtils setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    /**
     * 编写积极按钮文字
     *
     * @param positiveBtnStr 默认:"确定"
     * @return
     */
    public DialogFragmentUtils setPositiveBtnStr(String positiveBtnStr) {
        this.mPositiveBtnStr = positiveBtnStr;
        return this;
    }

    /**
     * 编写消极按钮文字
     *
     * @param negativeBtnStr 默认:"取消"
     * @return
     */
    public DialogFragmentUtils setNegativeBtnStr(String negativeBtnStr) {
        this.mNegativeBtnStr = negativeBtnStr;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog mAlertDialog = new AlertDialog.Builder(getActivity())
                .setCustomTitle(this.customTitle(getActivity(), mTitle, mTitleSize, mTitleColor, false))
                .setMessage(mMessage)
                .setPositiveButton(mPositiveBtnStr, (dialog, which) -> {
                    //监听回调
                    if (mFragment != null) {
                        DialogFragmentUtilsInterface positiveBtnCallback = (DialogFragmentUtilsInterface) mFragment;
                        positiveBtnCallback.doPositive();
                        dismiss();
                    }

                    if (mActivity != null) {
                        DialogFragmentUtilsInterface positiveBtnCallback = (DialogFragmentUtilsInterface) mActivity;
                        positiveBtnCallback.doPositive();
                        dismiss();
                    }

                })
                .setNegativeButton(mNegativeBtnStr, (dialog, which) -> {
                    //监听回调
                    if (mFragment != null) {
                        DialogFragmentUtilsInterface negativeBtnCallback = (DialogFragmentUtilsInterface) mFragment;
                        negativeBtnCallback.doNegative();
                        dismiss();
                    }

                    if (mActivity != null) {
                        DialogFragmentUtilsInterface negativeBtnCallback = (DialogFragmentUtilsInterface) mActivity;
                        negativeBtnCallback.doNegative();
                        dismiss();
                    }
                })
                .setNeutralButton(mNeutralBtnStr, (dialog, which) -> {
                    //监听回调
                    if (mFragment != null) {
                        DialogFragmentUtilsInterface negativeBtnCallback = (DialogFragmentUtilsInterface) mFragment;
                        negativeBtnCallback.doNeutral();
                        dismiss();
                    }

                    if (mActivity != null) {
                        DialogFragmentUtilsInterface negativeBtnCallback = (DialogFragmentUtilsInterface) mActivity;
                        negativeBtnCallback.doNeutral();
                        dismiss();
                    }
                })
                .create();

        //禁止点击 dialog 外部取消弹窗
        mAlertDialog.setCanceledOnTouchOutside(mCancelOutside);

        //设置这个对话框不能被用户按[返回键]而取消掉
        if (!mCancelBackup) {
            mAlertDialog.setCancelable(false);//设置这个对话框不能被用户按[返回键]而取消掉
            //这里的setOnKeyListener作用就是屏蔽用户按下KeyEvent.KEYCODE_SEARCH
            mAlertDialog.setOnKeyListener((dialog, keyCode, event) -> (keyCode == KeyEvent.KEYCODE_SEARCH));
        }

        //先判断是否在前台
        if (!this.isAppToBackground(getActivity())) {
            mAlertDialog.show();
            mAlertDialog.getWindow().setAttributes(this.getLayoutParamsForDimAmount(mAlertDialog, mDimAmout));
        }

        //注意：这里这些属性的获取都一定是要在Dialog调用完show()方法之后，即Dialog展示出来之后。要不就会NullPointException。
        this.setDialogButtonAll(mAlertDialog, getActivity(),
                mPositiveBtnColor, mPositiveBtnStrSize,
                mNegativeBtnColor, mNegativeBtnStrSize,
                mNeutralBtnColor, mNeutralBtnStrSize);

        return mAlertDialog;
    }

    public interface DialogFragmentUtilsInterface {
        /**
         * 点击positive按钮的回调
         */
        void doPositive();

        /**
         * 点击negative按钮的回调
         */
        void doNegative();

        /**
         * 点击neutral按钮的回调
         */
        void doNeutral();
    }


    ///////////////////////////////////////////工具方法/////////////////////////////////////////////

    /**
     * 设置LayoutParams的dimAmount属性
     * <br>dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的，1.0f时候，背景全部变黑暗。
     *
     * @param dimAmout 不透明程度 0.0f(透明)-1.0f(不透明)
     * @return
     */
    @NonNull
    private WindowManager.LayoutParams getLayoutParamsForDimAmount(Dialog alertDialog, float dimAmout) {
        //设置弹出对话框背景变暗程度
        WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
        //在show()方法后面设置dimAmount属性，才会生效。进度条也是。
        layoutParams.dimAmount = dimAmout;
        return layoutParams;
    }


    /**
     * 判断当前应用程序(如Activity)处于前台还是后台 (可见/不可见)
     * return false 在前台
     * return true 在后台
     */
    @SuppressWarnings("deprecated")
    private boolean isAppToBackground(final Context context) {
        String top = null;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //For versions less than lollipop
//            ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
//            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(5);
//
//            if (taskInfo != null && !taskInfo.isEmpty()) {
//                top = taskInfo.get(0).topActivity.getPackageName();
//                Log.v(TAG, "top app = " + top);
//                ComponentName topActivity = taskInfo.get(0).topActivity;
//                if (!topActivity.getPackageName().equals(context.getPackageName())) {
//                    return true;
//                }
//            }
//        } else
            { //For versions Lollipop and above
            List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(context.getApplicationContext());
            Collections.sort(processes, new AndroidProcesses.ProcessComparator());
            for (int i = 0; i <= processes.size() - 1; i++) {
                if (processes.get(i).name.equalsIgnoreCase(AppUtil.getPackageName(context))) { //always the package name above/below this package is the top app
                    if ((i + 1) <= processes.size() - 1) { //If processes.get(i+1) available, then that app is the top app
                        top = processes.get(i + 1).name;
                        AndroidAppProcess topActivity = processes.get(i + 1);
                        if (!topActivity.getPackageName().equals(context.getPackageName())) {
                            return true;
                        }
                    } else if (i != 0) { //If the last package name is "com.google.android.gms" then the package name above this is the top app
                        top = processes.get(i - 1).name;
                        AndroidAppProcess topActivity = processes.get(i + 1);
                        if (!topActivity.getPackageName().equals(context.getPackageName())) {
                            return true;
                        }
                    } else {
                        if (i == processes.size() - 1) { //If only one package name available
                            top = processes.get(i).name;
                            AndroidAppProcess topActivity = processes.get(i);
                            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                                return true;
                            }
                        }
                    }
                    Log.v(TAG, "top app = " + top);
                }
            }
        }
        return false;
    }

    /**
     * 设置按钮字体颜色,字体大小
     *
     * @param alertDialog
     * @param posColor     积极按钮
     * @param posSize
     * @param negColor     消极按钮
     * @param negSize
     * @param neutralColor 中立按钮
     * @param neutralSize
     */
    private void setDialogButtonAll(AlertDialog alertDialog, Context context,
                                    int posColor, int posSize,
                                    int negColor, int negSize,
                                    int neutralColor, int neutralSize) {

        Button btnPositive =
                alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        Button btnNegative =
                alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
        Button btnNeutral =
                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        if (btnPositive != null) {
            btnPositive.setTextColor(ContextCompat.getColor(context, posColor));
            btnPositive.setTextSize(posSize);
        }

        if (btnNegative != null) {
            btnNegative.setTextColor(ContextCompat.getColor(context, negColor));
            btnNegative.setTextSize(negSize);
        }

        if (btnNeutral != null) {
            btnNeutral.setTextColor(ContextCompat.getColor(context, neutralColor));
            btnNeutral.setTextSize(neutralSize);
        }
    }

    /**
     * 设置自定义title
     *
     * @param context
     * @param title
     * @param size
     * @param color
     * @param bold    是否粗体
     * @return
     */
    private View customTitle(Context context, String title, int size, int color, boolean bold) {
        //自定义标题
        TextView mDialogTitle = new TextView(context);
        mDialogTitle.setText(title);    //内容
        mDialogTitle.setTextSize(size);//字体大小
        mDialogTitle.setPadding(64, 64, 64, 48);//位置-->int left, int top, int right, int bottom
        mDialogTitle.setTextColor(ContextCompat.getColor(context, color));//颜色

        TextPaint tp = mDialogTitle.getPaint();
        tp.setFakeBoldText(bold);

        return mDialogTitle;
    }

}
