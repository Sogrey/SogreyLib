package org.sogrey.base.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * Created by Sogrey on 2018/4/19.
 */

public abstract class CommonExpandListAdapter<P, C> extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<P> mParentLst;
    private final List<List<C>> mChildrenLst;
    private final int mParentLayoutId;
    private final int mChildrenLayoutId;

    public CommonExpandListAdapter(Context context, List<P> parent, List<List<C>> children, int parentLayoutId, int childLayoutId) {
        super();
        this.mContext = context;
        this.mParentLst = parent;
        this.mChildrenLst = children;
        this.mParentLayoutId = parentLayoutId;
        this.mChildrenLayoutId = childLayoutId;
    }

    @Override
    public int getGroupCount() {
        return mParentLst == null ? 0 : mParentLst.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (mChildrenLst == null || mChildrenLst.size() <= groupPosition || mChildrenLst.get(groupPosition) == null) ?
                0 : mChildrenLst.get(groupPosition).size();
    }

    @Override
    public P getGroup(int groupPosition) {
        return mParentLst == null ? null : mParentLst.get(groupPosition);
    }

    @Override
    public C getChild(int groupPosition, int childPosition) {
        return (mChildrenLst == null || mChildrenLst.size() <= groupPosition || mChildrenLst.get(groupPosition) == null) ?
                null : mChildrenLst.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mParentLst.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mChildrenLst.get(groupPosition).get(childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.get(this.mContext,
                convertView, parent, this.mParentLayoutId, groupPosition);
        convertParent(holder, mParentLst.get(groupPosition), groupPosition, isExpanded);
        return holder.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.get(this.mContext,
                convertView, parent, this.mChildrenLayoutId, groupPosition);
        convertChild(holder, mChildrenLst.get(groupPosition).get(childPosition), groupPosition, childPosition, isLastChild);
        return holder.getConvertView();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public abstract void convertParent(CommonViewHolder holder, P p, int groupPosition, boolean isExpanded);

    public abstract void convertChild(CommonViewHolder holder, C c, int groupPosition, int childPosition, boolean isLastChild);
}
