package org.sogrey.db;

import java.util.List;

/**
 * DB异步操作返回监听
 * Created by Sogrey on 2018/8/18.
 */

public interface OnDBCallback<T> {
    /**
     * 把查询结果抛回UI线程
     * @param result
     */
    void onPost(List<T> result);
}
