package org.sogrey.base.activity.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.sogrey.base.BaseApplication;
import org.sogrey.base.R;
import org.sogrey.base.activity.ActivityManager;

import java.util.LinkedHashMap;

/**
 * 基本Activity类;
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final int DELAY=100;
    public    Context   mContext;
    private ActivityManager manager=ActivityManager.getActivityManager();
    private LinkedHashMap<String,Boolean> mLinkedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        //        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
//            SystemBarTintManager tintManager=new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.bg_statusbar);//通知栏所需颜色
        }
        mContext=this;
        manager.putActivity(this);
        if (mLinkedMap==null) {
            mLinkedMap= new LinkedHashMap<>();
        }
        setContentView(getLayoutRedId());
        init();
    }

    /**
     * 设置布局文件
     *
     * @return
     */
    protected abstract int getLayoutRedId();

    /**
     * 初始化
     *
     * @author Sogrey
     * @date 2016年3月11日
     */
    protected abstract void init();


    @TargetApi(19)
    private void setTranslucentStatus(boolean b) {
        Window                     win      =getWindow();
        WindowManager.LayoutParams winParams=win.getAttributes();
        final int                  bits     =WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (b) {
            winParams.flags|=bits;
        } else {
            winParams.flags&=~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowManager.LayoutParams params=getWindow().getAttributes();
        if (params.softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            // 隐藏软键盘
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            params.softInputMode=WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        }
    }

    /**
     * 退出提示; 子类实现
     */

    public void showExitDialog() {
    }

    /**
     * 退出应用
     */
    public void exit() {
        if (mLinkedMap!=null) {
            mLinkedMap.clear();
            mLinkedMap=null;
        }
        manager.exit();
        exitProgrames();
    }

    /**
     * 退出应用 - 真退出
     */
    public void exitProgrames() {
        Intent startMain=new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    /**
     * 退出应用 - 假退出
     */
    public void exit2Home() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
        finish();
    }


    @Override
    protected void onDestroy() {
        manager.removeActivity(this);
        manager.removeActivityCls(this.getClass());
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            if (doSomeThingBeforeDestroy())
                return true;
            if (isShowExitDialog()) {//彈出退出確認對話框
                showExitDialog();
            } else {//不彈退出確認對話框
                if (System.currentTimeMillis()- BaseApplication.getInstance().dataFrist<
                    2000) {
                    exit();
                } else {
                    Toast.makeText(this, "再次点击返回键退出", Toast.LENGTH_SHORT).show();
                    BaseApplication.getInstance().dataFrist=System.currentTimeMillis();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    /**
     * 返回退出时要做的事，返回true会跳过后续退出的操作
     * @return
     */
    protected boolean doSomeThingBeforeDestroy() {return false;}

    public void finishThis() {
            finish();
            overridePendingTransition(R.anim.base_slide_remain,R.anim.base_slide_right_out);
    }

    public void finishThisDelay() {
        new Handler().postDelayed(this::finishThis,1000);
    }

    public void finishThisRemain() {
            finish();
            overridePendingTransition(R.anim.base_slide_remain,R.anim.base_slide_remain);
    }

    public void finishThisRemainDelay() {
        new Handler().postDelayed(this::finishThisRemain,1000);
    }

    /**
     * 若要自定義Activity退出時是顯示提示框還是toast,默認toast,若要修改需複寫此方法返回true。
     *
     * @return if true show exit dialog,Otherwise show toast.
     */
    public boolean isShowExitDialog() {
        return false;
    }

    public void putNetWorkFlag(String key,boolean val) {
        if (mLinkedMap==null) {
            mLinkedMap= new LinkedHashMap<>();
        }
        mLinkedMap.put(key,val);
    }

    public boolean getNetWorkFlag(String key,boolean val) {
        if (mLinkedMap==null) {
            mLinkedMap= new LinkedHashMap<>();
        }
        if (mLinkedMap.containsKey(key)) {
            val=mLinkedMap.get(key);
        } else {
            val=false;
        }
        return val;
    }

    public void clearAllNetWorkFlag() {
        if (mLinkedMap==null) {
            mLinkedMap= new LinkedHashMap<>();
        }
        mLinkedMap.clear();
        mLinkedMap=null;
    }

    /**
     * Activity 跳转
     *
     * @param cls
     *         要跳转到的Activity
     */
    public void startIntent(Class<?> cls) {
        startIntent(cls,0,0,false);
    }

    public void startIntent(Class<?> cls,boolean isFinish) {
        startIntent(cls,0,0,isFinish);
    }

    /**
     * Activity 跳转
     *
     * @param cls
     *         要跳转到的Activity
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntent(Class<?> cls,int delay) {
        startIntent(cls,0,0,delay,false);
    }

    public void startIntent(Class<?> cls,int delay,boolean isFinish) {
        startIntent(cls,0,0,delay,isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     */
    public void startIntent(Class<?> cls,final int animIn,final int animOut) {
        startIntent(cls,animIn,animOut,-1,false);
    }

    public void startIntent(Class<?> cls,final int animIn,final int animOut,boolean isFinish) {
        startIntent(cls,animIn,animOut,-1,isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntent(Class<?> cls,final int animIn,final int animOut,int delay) {
        startIntent(cls,null,animIn,animOut,delay,false);
    }

    public void startIntent(
            Class<?> cls,final int animIn,final int animOut,int delay,
            boolean isFinish
    ) {
        startIntent(cls,null,animIn,animOut,delay,isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param bundle
     *         传递参数
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     */
    public void startIntent(Class<?> cls,Bundle bundle,final int animIn,final int animOut) {
        startIntent(cls,bundle,animIn,animOut,-1,false);
    }

    public void startIntent(
            Class<?> cls,Bundle bundle,final int animIn,final int animOut,boolean
            isFinish
    ) {
        startIntent(cls,bundle,animIn,animOut,-1,isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param bundle
     *         传递参数
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntent(
            Class<?> cls,Bundle bundle,final int animIn,final int animOut,
            int delay,final boolean isFinish
    ) {
//        if (manager.hasActivityCls(cls)) {
//            return;
//        }
        manager.putActivityCls(cls);
        final Intent intent=new Intent();
        intent.setClass(this,cls);
        if (bundle!=null)
            intent.putExtras(bundle);
        new Handler().postDelayed(() -> {

            startActivity(intent);
            if (animIn!=0&&animOut!=0) {
                try {
                    overridePendingTransition(animIn,animOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isFinish)
                finish();
        },delay<0 ? DELAY : delay);
    }

    /**
     * Activity 跳转(需要返回结果)
     *
     * @param cls
     *         要跳转到的Activity
     * @param requestCode
     *         请求码
     */
    public void startIntentForResult(Class<?> cls,int requestCode) {
        startIntentForResult(cls,requestCode,0,0);
    }

    /**
     * Activity 跳转(需要返回结果)
     *
     * @param cls
     *         要跳转到的Activity
     * @param requestCode
     *         请求码
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntentForResult(Class<?> cls,int requestCode,int delay) {
        startIntentForResult(cls,requestCode,0,0,delay);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param requestCode
     *         请求码
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     */
    public void startIntentForResult(
            Class<?> cls,int requestCode,int animIn,
            int animOut
    ) {
        startIntentForResult(cls,null,requestCode,animIn,animOut);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param requestCode
     *         请求码
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntentForResult(
            Class<?> cls,int requestCode,int animIn,
            int animOut,int delay
    ) {
        startIntentForResult(cls,null,requestCode,animIn,animOut,delay);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param bundle
     *         參數
     * @param requestCode
     *         请求码
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     */
    public void startIntentForResult(
            Class<?> cls,Bundle bundle,int requestCode,
            int animIn,int animOut
    ) {
        startIntentForResult(cls,bundle,requestCode,animIn,animOut,-1);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param bundle
     *         传递参数
     * @param requestCode
     *         请求码
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntentForResult(
            Class<?> cls,Bundle bundle,final int requestCode,
            final int animIn,final int animOut,int delay
    ) {
//        if (manager.hasActivityCls(cls)) {
//            return;
//        }
        manager.putActivityCls(cls);
        final Intent intent=new Intent();
        intent.setClass(this,cls);
        if (bundle!=null)
            intent.putExtras(bundle);
        new Handler().postDelayed(() -> {

            startActivityForResult(intent,requestCode);
            if (animIn!=0&&animOut!=0) {
                try {
                    overridePendingTransition(animIn,animOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },delay<0 ? DELAY : delay);
    }

    public String getResString(@NonNull int stringId){
       return getResources().getString(stringId);
    }
    protected String getResString(int resId,Object... o){
        return getResources().getString(resId,o);
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
