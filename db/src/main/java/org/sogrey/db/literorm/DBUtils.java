package org.sogrey.db.literorm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.litesuits.orm.BuildConfig;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.assit.SQLiteHelper;

import java.io.File;
import java.util.List;

/**
 * Created by Sogrey on 2018/8/17.
 */

public enum DBUtils implements SQLiteHelper.OnUpdateListener {
    INSTANCE;
    private LiteOrm mLiteOrm;
    private Context mContext;
    DBUtils() {

    }

    public void init(Context context){
        mContext = context;
        DataBaseConfig config = new DataBaseConfig(mContext);
        config.dbName = mContext.getExternalFilesDir("lib") + File.separator + "db.db";
        config.dbVersion = 1;
        config.onUpdateListener = this;
        config.debugged = BuildConfig.DEBUG;
        //可替换为 newCascadeInstance支持级联操作
        mLiteOrm = LiteOrm.newSingleInstance(config);
    }

    @Override public void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DBUtils", "The database version upgrade from "
                + oldVersion + " to " + newVersion);
    }

    public void save(Object o) {
        if (o == null) {
            return;
        }

        mLiteOrm.save(o);
    }

    public <T> void save(List<T> collection) {
        if (null==collection||collection.isEmpty()) {
            return;
        }

        mLiteOrm.save(collection);
    }

    public <T> void delete(Class<T> tClass) {
        if (tClass == null) {
            return;
        }

        mLiteOrm.delete(tClass);
    }

    public <T> List<T> queryAll(Class<T> tClass) {
        if (tClass == null) {
            return null;
        }

        return mLiteOrm.query(tClass);
    }

}