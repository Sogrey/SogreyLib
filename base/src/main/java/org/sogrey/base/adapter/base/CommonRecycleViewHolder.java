package org.sogrey.base.adapter.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 通用RecycleView適配器 ViewHolder
 * Created by Sogrey on 2016/4/1.
 */
public abstract class CommonRecycleViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mView;
    private Context mContext;
    private int mPostion;
    private View mConvertView;

    public CommonRecycleViewHolder(View itemView) {
        super(itemView);
        this.mConvertView = itemView;
        this.mView = new SparseArray<>();
        this.mContext = getContext();
    }

    public abstract Context getContext();

    /**
     * 通过viewId获取控件对象
     *
     * @param viewId 控件ID
     * @return View
     * @author Sogrey
     * @date 2015年5月11日
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        /* 依据key(viewId)获取View控件 */
        View view = mView.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
			/* 添加到控件队列：key:viewId,value:View */
            mView.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 获取convertView
     *
     * @return convertView
     * @author Sogrey
     * @date 2015年5月11日
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置可见状态<br>
     *
     * @param viewId
     * @param visibility {@link View#VISIBLE},{@link View#GONE},{@link View#INVISIBLE}
     * @return
     * @author Sogrey
     * @date 2015年8月14日
     */
    public CommonRecycleViewHolder setVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    /**
     * 获取可见状态
     *
     * @param viewId
     * @return {@link View#VISIBLE},{@link View#GONE},{@link View#INVISIBLE}
     * @author Sogrey
     * @date 2015年8月14日
     */
    public int getVisibility(int viewId) {
        return getView(viewId).getVisibility();
    }

    /**
     * 设置是否可点击<br>
     *
     * @param viewId
     * @param clickable
     * @return
     * @author Sogrey
     * @date 2015年8月14日
     */
    public CommonRecycleViewHolder setClickable(int viewId, boolean clickable) {
        getView(viewId).setClickable(clickable);
        return this;
    }

    /**
     * 是否可长按
     *
     * @param viewId
     * @param clickable
     * @return
     * @author Sogrey
     * @date 2015年8月14日
     */
    public CommonRecycleViewHolder setLongClickable(int viewId, boolean clickable) {
        getView(viewId).setLongClickable(clickable);
        return this;
    }

    /**
     * 设置是否可用
     *
     * @param viewId
     * @param enabled
     * @return
     * @author Sogrey
     * @date 2015年8月14日
     */
    public CommonRecycleViewHolder setEnabled(int viewId, boolean enabled) {
        getView(viewId).setEnabled(enabled);
        return this;
    }

    /**
     * 给TextView,EditText,Button设置内容
     *
     * @param viewId  id of the TextView,EditText or Button
     * @param content the content to set
     * @return CommonRecycleViewHolder
     * @author Sogrey
     * @date 2015年5月11日
     */
    public CommonRecycleViewHolder setText(int viewId, String content) {
        View v = getView(viewId);
        if (v instanceof Button) {
            ((Button) v).setText(content);
        } else if (v instanceof EditText) {
            ((EditText) v).setText(content);
        } else if (v instanceof TextView) {
            ((TextView) v).setText(content);
        }
        return this;
    }

    /**
     * 为ImageView设置资源-Bitmap
     *
     * @param viewId id of the ImageView
     * @param bm     the Bitmap to set
     * @return CommonRecycleViewHolder
     * @author Sogrey
     * @date 2015年5月11日
     */
    public CommonRecycleViewHolder setImg_ImageView(int viewId, Bitmap bm) {
        ((ImageView) getView(viewId)).setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置资源-Drawable
     *
     * @param viewId   id of the ImageView
     * @param drawable the Drawable to set
     * @return CommonRecycleViewHolder
     * @author Sogrey
     * @date 2015年5月11日
     */
    public CommonRecycleViewHolder setImg_ImageView(int viewId, Drawable drawable) {
        ((ImageView) getView(viewId)).setImageDrawable(drawable);
        return this;
    }

    /**
     * 为ImageView设置资源-ResourceId
     *
     * @param viewId id of the ImageView
     * @param resId  the id of resource to set
     * @return CommonRecycleViewHolder
     * @author Sogrey
     * @date 2015年5月11日
     */
    public CommonRecycleViewHolder setImg_ImageView(int viewId, int resId) {
        ((ImageView) getView(viewId)).setImageResource(resId);
        return this;
    }

    /**
     * 为ImageView设置资源-Uri
     *
     * @param viewId id of the ImageView
     * @param uri    the Uri to set
     * @return CommonRecycleViewHolder
     * @author Sogrey
     * @date 2015年5月11日
     */
    public CommonRecycleViewHolder setImg_ImageView(int viewId, Uri uri) {
        ((ImageView) getView(viewId)).setImageURI(uri);
        return this;
    }

    /**
     * 为ImageView设置资源-Url(网络路径)
     *
     * @author Sogrey
     * @date 2015年5月11日
     * @param viewId
     *            id of the ImageView
     * @param url
     *            the url of resource to set
     * @return CommonRecycleViewHolder
     */
//    public CommonRecycleViewHolder setImg_ImageView(int viewId, String url) {
////		ImageLoader.getInstance()
////				.displayImage(url, (ImageView) getView(viewId));
//        GlideUtils.setImage(mContext,(ImageView) getView(viewId),url);
//        return this;
//    }

    /**
     * 设置CheckBox选中与否
     *
     * @param viewId the id of the CheckBox
     * @param b      is checked
     * @return
     * @author Sogrey
     * @date 2015年5月11日
     */
    public CommonRecycleViewHolder setChecked_CheckBox(int viewId, boolean b) {
        ((CheckBox) getView(viewId)).setChecked(b);
        return this;
    }

    /**
     * 设置RadioButton选中与否
     *
     * @param viewId the id of the RadioButton
     * @param b      is checked
     * @return
     * @author Sogrey
     * @date 2015年5月11日
     */
    public CommonRecycleViewHolder setChecked_RadioButton(int viewId, boolean b) {
        ((RadioButton) getView(viewId)).setChecked(b);
        return this;
    }

    /**
     * 设置评星数
     *
     * @param viewId
     * @param ratingbar_size
     * @author Sogrey
     * @date 2015-10-26下午5:28:13
     */
    public CommonRecycleViewHolder setSize_RatingBar(int viewId, int ratingbar_size) {
        ((RatingBar) getView(viewId)).setProgress(ratingbar_size);
        return this;
    }

    /**
     * 设置评星（float）
     *
     * @param viewId
     * @param rating
     * @return
     * @author Sogrey
     * @date 2015-11-13上午11:06:17
     */
    public CommonRecycleViewHolder setRating_RatingBar(int viewId, float rating) {
        ((RatingBar) getView(viewId)).setRating(rating);
        return this;
    }

    /**
     * 设置View背景drawable
     *
     * @param viewId
     * @param background Drawable资源
     * @return
     * @author Sogrey
     * @date 2015年5月27日
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public CommonRecycleViewHolder setBackGround_View(int viewId, Drawable background) {
        getView(viewId).setBackground(background);
        return this;
    }

    /**
     * 设置View背景颜色
     *
     * @param viewId
     * @param color  颜色值
     * @return
     * @author Sogrey
     * @date 2015年5月27日
     */
    public CommonRecycleViewHolder setBackGroundColor_View(int viewId, int color) {
        getView(viewId).setBackgroundColor(color);
        return this;
    }

    /**
     * 设置View背景drawable
     *
     * @param viewId
     * @param background Drawable资源
     * @return
     * @author Sogrey
     * @date 2015年5月27日
     */
    @Deprecated
    public CommonRecycleViewHolder setBackGroundDrawable_View(int viewId,
                                                              Drawable background) {
        getView(viewId).setBackgroundDrawable(background);
        return this;
    }

    /**
     * 设置View背景资源
     *
     * @param viewId
     * @param resid  资源ID
     * @return
     * @author Sogrey
     * @date 2015年5月27日
     */
    public CommonRecycleViewHolder setBackGroundResource_View(int viewId, int resid) {
        getView(viewId).setBackgroundResource(resid);
        return this;
    }

    /**
     * 设置View 点击事件
     *
     * @param viewId the id of the view
     * @param l      the OnLongClickListener of view
     * @return
     * @author Sogrey
     * @date 2015年7月8日
     */
    public CommonRecycleViewHolder setOnclickListener(int viewId, View.OnClickListener l) {
        setOnclickListener(getView(viewId), l);
        return this;
    }

    /**
     * 设置View 点击事件
     *
     * @param v the view is clicked
     * @param l the OnLongClickListener of view
     * @return
     * @author Sogrey
     * @date 2015年7月8日
     */
    public CommonRecycleViewHolder setOnclickListener(View v, View.OnClickListener l) {
        v.setOnClickListener(l);
        return this;
    }

    /**
     * 设置View 长按事件
     *
     * @param viewId the id of the view
     * @param l      the OnLongClickListener of view
     * @return
     * @author Sogrey
     * @date 2015年7月8日
     */
    public CommonRecycleViewHolder setOnLongclickListener(int viewId,
                                                          View.OnLongClickListener l) {
        setOnLongclickListener(getView(viewId), l);
        return this;
    }

    /**
     * 设置View 长按事件
     *
     * @param v the view is clicked
     * @param l the OnLongClickListener of view
     * @return
     * @author Sogrey
     * @date 2015年7月8日
     */
    public CommonRecycleViewHolder setOnLongclickListener(View v, View.OnLongClickListener l) {
        v.setOnLongClickListener(l);
        return this;
    }

    public void setOnFocusChangeListener(int viewId, View.OnFocusChangeListener onFocusChangeListener) {
        View view = getView(viewId);
        if (view instanceof EditText) {
            view.setOnFocusChangeListener(onFocusChangeListener);
        }
    }
}
