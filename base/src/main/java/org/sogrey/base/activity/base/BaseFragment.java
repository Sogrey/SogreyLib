package org.sogrey.base.activity.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sogrey.base.BaseApplication;

import java.lang.reflect.Field;


/**
 * 基本fragment;
 *
 * @author Sogrey.
 */
public abstract class BaseFragment extends Fragment {

    private LayoutInflater inflater;
    protected View contentView;
    public Activity mContext;
    protected ViewGroup container;
    private static final int DELAY = 300;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState
    ) {
        this.inflater = inflater;
        this.container = container;
        onCreateView(savedInstanceState);
        if (contentView == null)
            return super.onCreateView(inflater, container, savedInstanceState);
        initDatas();
        return contentView;
    }

    public abstract void initDatas();

    protected abstract void onCreateView(Bundle savedInstanceState);

//    {
//        setContentView() here
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contentView = null;
        container = null;
        inflater = null;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(FragmentActivity context) {
        this.mContext = context;
    }


    public void setContentView(int layoutResID) {
        contentView = LayoutInflater.from(this.mContext)
                .inflate(layoutResID, container, false);
        setContentView(contentView);
    }

    public void setContentView(View view) {
        contentView = view;
//        ButterKnife.bind(this,contentView);
    }

    public View getContentView() {
        return contentView;
    }

    public View findViewById(int id) {
        if (contentView != null)
            return contentView.findViewById(id);
        return null;
    }

    // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang
    // -illegalstateexception-activity-has-been-destroyed
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Activity 跳转
     *
     * @param cls 要跳转到的Activity
     */
    public void startIntent(Class<?> cls) {
        startIntent(cls, 0, 0, false);
    }

    public void startIntent(Class<?> cls, boolean isFinish) {
        startIntent(cls, 0, 0, isFinish);
    }

    /**
     * Activity 跳转
     *
     * @param cls   要跳转到的Activity
     * @param delay 自定義延時（毫秒）
     */
    public void startIntent(Class<?> cls, int delay) {
        startIntent(cls, 0, 0, delay, false);
    }

    public void startIntent(Class<?> cls, int delay, boolean isFinish) {
        startIntent(cls, 0, 0, delay, isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls     要跳转到的Activity
     * @param animIn  进入动画
     * @param animOut 退出动画
     */
    public void startIntent(Class<?> cls, final int animIn, final int animOut) {
        startIntent(cls, animIn, animOut, -1, false);
    }

    public void startIntent(Class<?> cls, final int animIn, final int animOut, boolean isFinish) {
        startIntent(cls, animIn, animOut, -1, isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls     要跳转到的Activity
     * @param animIn  进入动画
     * @param animOut 退出动画
     * @param delay   自定義延時（毫秒）
     */
    public void startIntent(Class<?> cls, final int animIn, final int animOut, int delay) {
        startIntent(cls, null, animIn, animOut, delay, false);
    }

    public void startIntent(
            Class<?> cls, final int animIn, final int animOut, int delay,
            boolean isFinish
    ) {
        startIntent(cls, null, animIn, animOut, delay, isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls     要跳转到的Activity
     * @param bundle  传递参数
     * @param animIn  进入动画
     * @param animOut 退出动画
     */
    public void startIntent(Class<?> cls, Bundle bundle, final int animIn, final int animOut) {
        startIntent(cls, bundle, animIn, animOut, -1, false);
    }

    public void startIntent(
            Class<?> cls, Bundle bundle, final int animIn, final int animOut, boolean
            isFinish
    ) {
        startIntent(cls, bundle, animIn, animOut, -1, isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls     要跳转到的Activity
     * @param bundle  传递参数
     * @param animIn  进入动画
     * @param animOut 退出动画
     * @param delay   自定義延時（毫秒）
     */
    public void startIntent(
            Class<?> cls, Bundle bundle, final int animIn, final int animOut,
            int delay, final boolean isFinish
    ) {
//        if (manager.hasActivityCls(cls)) {
//            return;
//        }
//        manager.putActivityCls(cls);
        final Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        new Handler().postDelayed(() -> {

            startActivity(intent);
            if (animIn != 0 && animOut != 0) {
                try {
                    mContext.overridePendingTransition(animIn, animOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isFinish)
                mContext.finish();
        }, delay < 0 ? DELAY : delay);
    }

    /**
     * Activity 跳转(需要返回结果)
     *
     * @param cls         要跳转到的Activity
     * @param requestCode 请求码
     */
    public void startIntentForResult(Class<?> cls, int requestCode) {
        startIntentForResult(cls, requestCode, 0, 0);
    }

    /**
     * Activity 跳转(需要返回结果)
     *
     * @param cls         要跳转到的Activity
     * @param requestCode 请求码
     * @param delay       自定義延時（毫秒）
     */
    public void startIntentForResult(Class<?> cls, int requestCode, int delay) {
        startIntentForResult(cls, requestCode, 0, 0, delay);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls         要跳转到的Activity
     * @param requestCode 请求码
     * @param animIn      进入动画
     * @param animOut     退出动画
     */
    public void startIntentForResult(
            Class<?> cls, int requestCode, int animIn,
            int animOut
    ) {
        startIntentForResult(cls, null, requestCode, animIn, animOut);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls         要跳转到的Activity
     * @param requestCode 请求码
     * @param animIn      进入动画
     * @param animOut     退出动画
     * @param delay       自定義延時（毫秒）
     */
    public void startIntentForResult(
            Class<?> cls, int requestCode, int animIn,
            int animOut, int delay
    ) {
        startIntentForResult(cls, null, requestCode, animIn, animOut, delay);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls         要跳转到的Activity
     * @param bundle      參數
     * @param requestCode 请求码
     * @param animIn      进入动画
     * @param animOut     退出动画
     */
    public void startIntentForResult(
            Class<?> cls, Bundle bundle, int requestCode,
            int animIn, int animOut
    ) {
        startIntentForResult(cls, bundle, requestCode, animIn, animOut, -1);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls         要跳转到的Activity
     * @param bundle      传递参数
     * @param requestCode 请求码
     * @param animIn      进入动画
     * @param animOut     退出动画
     * @param delay       自定義延時（毫秒）
     */
    public void startIntentForResult(
            Class<?> cls, Bundle bundle, final int requestCode,
            final int animIn, final int animOut, int delay
    ) {
//        if (manager.hasActivityCls(cls)) {
//            return;
//        }
//        manager.putActivityCls(cls);
        final Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        new Handler().postDelayed(() -> {

            startActivityForResult(intent, requestCode);
            if (animIn != 0 && animOut != 0) {
                try {
                    mContext.overridePendingTransition(animIn, animOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay < 0 ? DELAY : delay);
    }

    protected String getResString(int resId) {
        return mContext.getResources().getString(resId);
    }

    protected String getResString(int resId, Object... o) {
        return mContext.getResources().getString(resId, o);
    }

    public Drawable getResDrawable(@DrawableRes int id) {
        return getResDrawable(mContext, id);
    }

    public Drawable getResDrawable(Context context, @DrawableRes int id) {
        if(context==null) context = BaseApplication.getInstance();
        return ContextCompat.getDrawable(context, id);
    }

    public int getResColor(@ColorRes int id) {
        return ContextCompat.getColor(mContext, id);
    }

    public int getResColor(Context context, @DrawableRes int id) {
        if(context==null) context = BaseApplication.getInstance();
        @SuppressLint("ResourceType") int color = ContextCompat.getColor(context, id);
        return color;
    }
}
