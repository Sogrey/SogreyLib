package org.sogrey.db.literorm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.litesuits.orm.BuildConfig;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.assit.SQLiteHelper;

/**
 * Created by Sogrey on 2017/9/4.
 */

public class LiteOrmDBHelper {
    private static final String DB_NAME ="App.db";//数据库名
    private static final boolean DEBUGGED  = BuildConfig.DEBUG;//是否开启调试输出日志
    private static final int DB_VERSION = 2;//版本号

    /** 单例模式 对象 */
    private static LiteOrmDBHelper sInstance;

    /**
     * 单例模式 <br>
     * 一个类最多只能有一个实例 <br>
     * 1、有一个私有静态成员 <br>
     * 2、有一个公开静态方法getInstance得到这个私有静态成员 <br>
     * 3、有一个私有的构造方法（不允许被实例化） <br>
     */
    public static LiteOrmDBHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LiteOrmDBHelper.class) {
                if (sInstance == null) {
                    sInstance = new LiteOrmDBHelper(context);
                }
            }
        }
        return sInstance;
    }

    private LiteOrm liteOrm = null;
    private LiteOrmDBHelper(Context context) {
        if (liteOrm == null) {
            DataBaseConfig config =  new DataBaseConfig(context,
                    DB_NAME,DEBUGGED, DB_VERSION, _OnUpdateListener);
            liteOrm = LiteOrm.newSingleInstance(config);
//            liteOrm = LiteOrm.newCascadeInstance(context, DB_NAME);
        }
//        liteOrm.openOrCreateDatabase(Constant.PATH_RESOURCES_DB,null);
        liteOrm.setDebugged(DEBUGGED); // open the log
    }

    public LiteOrm getLiteOrm() {
        return liteOrm;
    }

    private SQLiteHelper.OnUpdateListener _OnUpdateListener =
            (db, oldVersion, newVersion) -> Log.e("DBStudent", "The database version upgrade from "
            + oldVersion + " to " + newVersion);
}
