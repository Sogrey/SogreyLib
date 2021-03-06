package org.sogrey.views.dragview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import org.sogrey.views.RTextView;

/**
 * 描述：可拖动控件<br/>
 * 1.偷懒的Drawable方法
 * <br/>
 * setDrawableLeftResource(int resourceId)<br/>
 * setDrawableTopResource(int resourceId)<br/>
 * setDrawableRightResource(int resourceId)<br/>
 * setDrawableBottomResource(int resourceId)<br/>
 * <p>
 * 2.设置方向<br/>
 * 0是纵向中间界限 左右<br/>
 * 1是横向中间界限 上下<br/>
 * 2是一律向左<br/>
 * 3是一律向右<br/>
 * 4是一律向上<br/>
 * 5是一律向下<br/>
 * 6是不做操作<br/>
 * <br/>
 * setOrientation(int orientation)<br/>
 * <br/>
 * 3.设置不允许超过的边界
 * <br/>
 * setBoundary(int leftBoundary, int topBoundary, int rightBoundary, int bottomBoundary)
 * <br/>
 * 4.点击事件
 * <br/>
 * onDragViewClickListener
 * <br/>
 * onDragViewLongClickListener<br/>
 * Created by Sogrey on 2018/8/30.
 */

public class DKDragView extends RTextView {
    private static final String TAG = "DragView";
    private int startX, startY, targetX, targetY, ORIENTATION;
    private long startTime;
    private Context context;
    private onDragViewClickListener onDragViewClickListener;
    private onDragViewLongClickListener onDragViewLongClickListener;
    private boolean move, hasAnimation;
    private int leftBoundary, topBoundary, rightBoundary, bottomBoundary;
    private int windowWidth, windowHeight;
    private long animationDuration = 500;

    public DKDragView(Context context) {
        this(context, null);
    }

    public DKDragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DKDragView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        hasAnimation = true;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        windowWidth = dm.widthPixels;
        windowHeight = dm.heightPixels;
    }

    /**
     * 点击事件
     */
    public interface onDragViewClickListener {
        void onClick();
    }

    /**
     * 长按事件
     */
    public interface onDragViewLongClickListener {
        void onLongClick();
    }

    /**
     * 设置是否开启动画
     * @param hasAnimation
     * @return
     */
    public DKDragView setAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
        return this;
    }

    /**
     * 设置动画时间
     * @param duration
     * @return
     */
    public DKDragView setDuration(long duration) {
        animationDuration = duration;
        return this;
    }

    /**
     * 0是纵向中间界限 左右
     * 1是横向中间界限 上下
     * 2是一律向左
     * 3是一律向右
     * 4是一律向上
     * 5是一律向下
     * 6是不做操作
     */
    public DKDragView setOrientation(int orientation) {
        this.ORIENTATION = orientation;
        return this;
    }

    /**
     * 设置边界限制
     * @param leftBoundary
     * @param topBoundary
     * @param rightBoundary
     * @param bottomBoundary
     * @return
     */
    public DKDragView setBoundary(int leftBoundary, int topBoundary, int rightBoundary, int bottomBoundary) {
        this.leftBoundary = leftBoundary;
        this.topBoundary = topBoundary;
        this.rightBoundary = rightBoundary;
        this.bottomBoundary = bottomBoundary;
        return this;
    }

    public DKDragView setOnDragViewClickListener(onDragViewClickListener onDragViewClickListener) {
        this.onDragViewClickListener = onDragViewClickListener;
        return this;
    }

    public DKDragView setOnDragViewLongClickListener(onDragViewLongClickListener onDragViewLongClickListener) {
        this.onDragViewLongClickListener = onDragViewLongClickListener;
        return this;
    }

    public DKDragView setDrawableRightResource(int resourceId) {
        Drawable drawable = ContextCompat.getDrawable(context, resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(null, null, drawable, null);
        return this;
    }

    public DKDragView setDrawableLeftResource(int resourceId) {
        Drawable drawable = ContextCompat.getDrawable(context, resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(drawable, null, null, null);
        return this;
    }

    public DKDragView setDrawableTopResource(int resourceId) {
        Drawable drawable = ContextCompat.getDrawable(context, resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(null, drawable, null, null);
        return this;
    }

    public DKDragView setDrawableBottomResource(int resourceId) {
        Drawable drawable = ContextCompat.getDrawable(context, resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(null, null, null, drawable);
        return this;
    }

    public DKDragView setTextColorResource(int resourceId) {
        int color = ContextCompat.getColor(context, resourceId);
        setTextColor(color);
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                startTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - startX;
                int offsetY = y - startY;
                int l = getLeft() + offsetX;
                int t = getTop() + offsetY;
                int r = getRight() + offsetX;
                int b = getBottom() + offsetY;
                if (getLeft() + offsetX < leftBoundary) {
                    l = leftBoundary;
                    r = leftBoundary + getWidth();
                }
                if (getTop() + offsetY < topBoundary) {
                    t = topBoundary;
                    b = topBoundary + getHeight();
                }
                if (getRight() + offsetX > windowWidth - rightBoundary) {
                    l = windowWidth - rightBoundary - getWidth();
                    r = windowWidth - rightBoundary;
                }
                if (getBottom() + offsetY > windowHeight - bottomBoundary) {
                    t = windowHeight - bottomBoundary - getHeight();
                    b = windowHeight - bottomBoundary;
                }
                layout(l, t, r, b);
                if (Math.abs(x - startX) > 2f && Math.abs(y - startY) > 2f) {
                    move = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (Math.abs(x - startX) < 2f && Math.abs(y - startY) < 2f && !move) {
                    if (System.currentTimeMillis() - startTime > 1000) {
                        onDragViewLongClickListener.onLongClick();
                    } else {
                        onDragViewClickListener.onClick();
                    }
                }
                switch (ORIENTATION) {
                    case 0:
                        if (getRight() - (getRight() - getLeft()) / 2 <= windowWidth / 2) {
                            targetX = -(getLeft() - leftBoundary);
                        } else {
                            targetX = windowWidth - rightBoundary - getRight();
                        }
                        break;
                    case 1:
                        if (getBottom() - (getBottom() - getTop()) / 2 <= windowHeight / 2) {
                            targetY = -(getTop() - topBoundary);
                        } else {
                            targetY = windowHeight - bottomBoundary - getBottom();
                        }
                        break;
                    case 2:
                        targetX = -(getLeft() - leftBoundary);
                        break;
                    case 3:
                        targetX = windowWidth - rightBoundary - getRight();
                        break;
                    case 4:
                        targetY = -(getTop() - topBoundary);
                        break;
                    case 5:
                        targetY = windowHeight - bottomBoundary - getBottom();
                        break;
                    case 6:
                        break;

                }
                if (hasAnimation) {
                    startTranslateAnimation();
                } else {
                    vertifyLayout();
                }
                break;
        }
        return true;
    }

    private void startTranslateAnimation() {
        TranslateAnimation animation = new TranslateAnimation(0, targetX, 0, targetY);
        animation.setDuration(animationDuration);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
                vertifyLayout();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animation);
    }

    private void vertifyLayout() {
        switch (ORIENTATION) {
            case 0:
                if (targetX >= 0) {
                    layout(windowWidth - getWidth() - rightBoundary, getTop(),
                            windowWidth - rightBoundary, getBottom());
                } else {
                    layout(leftBoundary, getTop(), leftBoundary + getWidth(), getBottom());
                }
                break;
            case 1:
                if (targetY >= 0) {
                    layout(getLeft(), windowHeight - bottomBoundary - getHeight(), getRight(),
                            windowHeight - bottomBoundary);
                } else {
                    layout(getLeft(), topBoundary, getRight(), topBoundary + getHeight());
                }
                break;
            case 2:
                layout(leftBoundary, getTop(), leftBoundary + getWidth(), getBottom());
                break;
            case 3:
                layout(windowWidth - rightBoundary - getWidth(), getTop(), windowWidth - rightBoundary, getBottom());
                break;
            case 4:
                layout(getLeft(), topBoundary, getRight(), topBoundary + getHeight());
                break;
            case 5:
                layout(getLeft(), windowHeight - bottomBoundary - getHeight(), getRight(),
                        windowHeight - bottomBoundary);
                break;
            case 6:
                break;
        }
    }
}
