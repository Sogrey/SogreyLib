package org.sogrey.demo.sogreylibdemo.db;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.TableManager;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.SQLiteHelper;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import org.sogrey.db.DbCommand;
import org.sogrey.db.NoRetDbCommand;
import org.sogrey.db.OnDBCallback;
import org.sogrey.demo.sogreylibdemo.BuildConfig;
import org.sogrey.demo.sogreylibdemo.MyApplication;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作，基于<a href="https://github.com/litesuits/android-lite-orm">liteorm</a>
 * <p>
 * Created by Sogrey on 2018/8/17.
 */
public enum DBUtils implements SQLiteHelper.OnUpdateListener {
    INSTANCE;
    private static final String TAG = DBUtils.class.getSimpleName();
    private SQLiteDatabase mRDB;
    private LiteOrm mLiteOrm;

    DBUtils() {
        DataBaseConfig config = new DataBaseConfig(MyApplication.getContext());
        config.dbName = MyApplication.getContext().getExternalFilesDir("db") + File.separator + "app.db";//db文件位置
        config.dbVersion = 1;//数据库版本
        config.onUpdateListener = this;
        config.debugged = BuildConfig.DEBUG;
        //可替换为 newCascadeInstance支持级联操作
        mLiteOrm = LiteOrm.newSingleInstance(config);
        mRDB = mLiteOrm.getSQLiteHelper().getReadableDatabase();
    }

    @Override
    public void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DBUtils", "The database version upgrade from "
                + oldVersion + " to " + newVersion);
    }

    /**
     * 获取LiteOrm实例
     *
     * @return LiteOrm实例
     */
    public LiteOrm getLiteOrm() {
        return mLiteOrm;
    }

    /**
     * 保存数据
     *
     * @param o 要保存的数据
     */
    public void save(Object o) {
        if (o == null) return;
        mLiteOrm.save(o);
    }

    /**
     * 保存数据(异步)
     *
     * @param o 要保存的数据
     */
    public void saveAsyn(Object o) {
        new NoRetDbCommand() {
            @Override
            protected Void doInBackground() {
                save(o);
                return null;
            }
        }.execute();
    }

    /**
     * 保存一组数据
     *
     * @param collection 要保存的一组数据
     * @param <T>        数据实体类型
     */
    public <T> void save(List<T> collection) {
        if (null == collection || collection.isEmpty()) return;
        mLiteOrm.save(collection);
    }

    /**
     * 保存一组数据(异步)
     *
     * @param collection 要保存的一组数据
     * @param <T>        数据实体类型
     */
    public <T> void saveAsyn(List<T> collection) {
        new NoRetDbCommand() {
            @Override
            protected Void doInBackground() {
                save(collection);
                return null;
            }
        }.execute();
    }

    /**
     * 删除一个数据
     *
     * @param t   要删除的数据
     * @param <T> 数据实体类型
     */
    public <T> void delete(T t) {
        mLiteOrm.delete(t);
    }

    /**
     * 删除一个数据(异步)
     *
     * @param t   要删除的数据
     * @param <T> 数据实体类型
     */
    public <T> void deleteAsyn(T t) {
        new NoRetDbCommand() {
            @Override
            protected Void doInBackground() {
                delete(t);
                return null;
            }
        }.execute();
    }

    /**
     * 删除一个表
     *
     * @param cla 数据实体类
     * @param <T> 数据实体类型
     */
    public <T> void delete(Class<T> cla) {
        if (cla != null)
            mLiteOrm.delete(cla);
    }

    /**
     * 删除一个表(异步)
     *
     * @param cla 数据实体类
     * @param <T> 数据实体类型
     */
    public <T> void deleteAsyn(Class<T> cla) {
        new NoRetDbCommand() {
            @Override
            protected Void doInBackground() {
                delete(cla);
                return null;
            }
        }.execute();
    }

    /**
     * 删除集合中的数据
     *
     * @param list 要删除的一组数据
     * @param <T>  数据实体类型
     */
    public <T> void deleteList(List<T> list) {
        mLiteOrm.delete(list);
    }

    /**
     * 删除集合中的数据(异步)
     *
     * @param list 要删除的一组数据
     * @param <T>  数据实体类型
     */
    public <T> void deleteListAsyn(List<T> list) {
        new NoRetDbCommand() {
            @Override
            protected Void doInBackground() {
                deleteList(list);
                return null;
            }
        }.execute();
    }

    /**
     * 删除数据库
     */
    public void deleteDatabase() {
        mLiteOrm.deleteDatabase();
    }

    public void update(Object o) {
        mLiteOrm.update(o);
    }

    public <T> void update(Collection<T> list) {
        mLiteOrm.update(list);
    }

    public void update(WhereBuilder where, ColumnsValue columns, ConflictAlgorithm conflictAlgorithm) {
        mLiteOrm.update(where, columns, conflictAlgorithm);
    }

    /**
     * 查询所有数据
     *
     * @param tClass 数据实体类
     * @param <T>    数据实体类型
     * @return 返回所有查询到的数据
     */
    public <T> List<T> queryAll(Class<T> tClass) {
        if (tClass == null) return null;
        return mLiteOrm.query(tClass);
    }

    /**
     * 查询所有数据(异步)
     *
     * @param tClass   数据实体类
     * @param callback 异步执行结果监听
     * @param <T>      数据实体类型
     */
    @SuppressWarnings("unchecked")
    public <T> void queryAllAsyn(Class<T> tClass, OnDBCallback callback) {
        new DbCommand<List<T>>() {
            @Override
            protected List<T> doInBackground() {
                return queryAll(tClass);
            }

            @Override
            protected void onPostExecute(List<T> result) {
                if (callback != null) callback.onPost(result);
            }
        }.execute();
    }

    /**
     * 查询  某字段 等于 Value的值
     *
     * @param tClass 数据实体类
     * @param field  查询条件可带占位符
     * @param value  查询条件占位符对应值
     * @param <T>    数据实体类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> queryByWhere(Class<T> tClass, String field, Object... value) {
        return mLiteOrm.<T>query(new QueryBuilder(tClass).where(field, value));
    }

    /**
     * 查询  某字段 等于 Value的值（异步）
     *
     * @param tClass   数据实体类
     * @param callback 异步执行结果监听
     * @param field    查询条件可带占位符
     * @param value    查询条件占位符对应值
     * @param <T>      数据实体类型
     */
    @SuppressWarnings("unchecked")
    public <T> void queryByWhereAsyn(Class<T> tClass, OnDBCallback callback, String field, Object... value) {
        new DbCommand<List<T>>() {
            @Override
            protected List<T> doInBackground() {
                return mLiteOrm.<T>query(new QueryBuilder(tClass).where(field, value));
            }

            @Override
            protected void onPostExecute(List<T> result) {
                if (callback != null) callback.onPost(result);
            }
        }.execute();
    }

    /**
     * 查询  某字段 等于 Value的值  可以指定从1-20，就是分页
     *
     * @param tClass 数据实体类
     * @param field  查询条件可带占位符
     * @param value  查询条件占位符对应值
     * @param start  开始位置
     * @param length 查询长度
     * @param <T>    数据实体类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> queryByWhereLength(Class<T> tClass, String field, Object[] value, int start, int length) {
        return mLiteOrm.<T>query(new QueryBuilder(tClass).where(field, value).limit(start, length));
    }

    /**
     * 查询  某字段 等于 Value的值  可以指定从1-20，就是分页（异步）
     *
     * @param tClass   数据实体类
     * @param callback 异步执行结果监听
     * @param field    查询条件可带占位符
     * @param value    查询条件占位符对应值
     * @param start    开始位置
     * @param length   查询长度
     * @param <T>      数据实体类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> void queryByWhereLengthAsyn(Class<T> tClass, OnDBCallback callback, String field, Object[] value, int start, int length) {
        new DbCommand<List<T>>() {
            @Override
            protected List<T> doInBackground() {
                return mLiteOrm.<T>query(new QueryBuilder(tClass).where(field, value).limit(start, length));
            }

            @Override
            protected void onPostExecute(List<T> result) {
                if (callback != null) callback.onPost(result);
            }
        }.execute();
    }
    /////////////////////以上是基本操作/////////////////////////////////////
    /////////////////////以上是扩展操作/////////////////////////////////////
    /**
     * sql语句查询
     *
     * @param sql 完整sql语句
     * @return
     */
    public Cursor queryBySql(String sql) {
        return mRDB.rawQuery(sql, new String[]{});
    }

    /**
     * sql语句查询
     *
     * @param sql                完整sql语句
     * @param cancellationSignal
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Cursor queryBySql(String sql, CancellationSignal cancellationSignal) {
        return mRDB.rawQuery(sql, new String[]{}, cancellationSignal);
    }

    /**
     * sql语句查询
     *
     * @param sql 完整sql语句
     * @param cla 查询的库实体(需要与要查询的表保持一致)
     * @param <T> 数据实体类型
     * @return
     */
    private <T> ArrayList<T> queryBySql(String sql, Class<T> cla) {
        return queryByCursor(queryBySql(sql), cla);
    }

    /**
     * sql语句查询
     *
     * @param sql                完整sql语句
     * @param cancellationSignal 进程中取消操作的信号, 如果操作被取消, 当查询命令执行时会抛出 OperationCanceledException 异常
     * @param cla                查询的库实体 (需要与要查询的表保持一致)
     * @param <T>                数据实体类型
     * @return
     */
    private <T> ArrayList<T> queryBySql(String sql, CancellationSignal cancellationSignal, Class<T> cla) {
        return queryByCursor(queryBySql(sql, cancellationSignal), cla);
    }

    /**
     * 通过Cursor转换成对应的VO集合。注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     *
     * @param cursor 游标
     * @param clazz  查询的库实体 (需要与要查询的表保持一致)
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> ArrayList<T> queryByCursor(Cursor cursor, Class<T> clazz) {
        if (cursor == null) {
            return null;
        }
        ArrayList list = new ArrayList();
        Object obj;
        try {
            while (cursor.moveToNext()) {
                obj = setValues2Fields(cursor, clazz);

                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR @：cursor2VOList");
            return null;
        } finally {
            cursor.close();
        }
    }

    /**
     * 把值设置进类属性里
     *
     * @param cursor 游标
     * @param clazz  查询的库实体 (需要与要查询的表保持一致)
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private static Object setValues2Fields(Cursor cursor, Class clazz)
            throws Exception {

        String[] columnNames = cursor.getColumnNames();// 字段数组
        //init a instance from the VO`s class
        Object obj = clazz.newInstance();
        //return a field array from obj`s ALL(include private exclude inherite(from father)) field
        Field[] fields = clazz.getDeclaredFields();

        for (Field _field : fields) {
            //field`s type
            Class<? extends Object> typeClass = _field.getType();// 属性类型
            for (int j = 0; j < columnNames.length; j++) {
                String columnName = columnNames[j];
                typeClass = getBasicClass(typeClass);
                //if typeclass is basic class ,package.if not,no change
                boolean isBasicType = isBasicType(typeClass);

                if (isBasicType) {
                    if (columnName.equalsIgnoreCase(_field.getName())) {// 是基本类型
                        String _str = cursor.getString(cursor.getColumnIndex(columnName));
                        if (_str == null) {
                            break;
                        }
                        if (TextUtils.isEmpty(_str)) _str = "";
                        //if value is null,make it to ""
                        //use the constructor to init a attribute instance by the value
                        Constructor<? extends Object> cons = typeClass
                                .getConstructor(String.class);
                        Object attribute = cons.newInstance(_str);
                        _field.setAccessible(true);
                        //give the obj the attr
                        _field.set(obj, attribute);
                        break;
                    }
                } else {
                    Object obj2 = setValues2Fields(cursor, typeClass);// 递归
                    _field.set(obj, obj2);
                    break;
                }

            }
        }
        return obj;
    }

    /**
     * 判断是不是基本类型
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static boolean isBasicType(Class typeClass) {
        if (typeClass.equals(Integer.class) || typeClass.equals(Long.class)
                || typeClass.equals(Float.class)
                || typeClass.equals(Double.class)
                || typeClass.equals(Boolean.class)
                || typeClass.equals(Byte.class)
                || typeClass.equals(Short.class)
                || typeClass.equals(String.class)) {

            return true;

        } else {
            return false;
        }
    }

    /**
     * 获得包装类
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("all")
    public static Class<? extends Object> getBasicClass(Class typeClass) {
        Class _class = basicMap.get(typeClass);
        if (_class == null)
            _class = typeClass;
        return _class;
    }

    @SuppressWarnings("rawtypes")
    private static Map<Class, Class> basicMap = new HashMap<>();

    static {
        basicMap.put(int.class, Integer.class);
        basicMap.put(long.class, Long.class);
        basicMap.put(float.class, Float.class);
        basicMap.put(double.class, Double.class);
        basicMap.put(boolean.class, Boolean.class);
        basicMap.put(byte.class, Byte.class);
        basicMap.put(short.class, Short.class);
    }
    /////////////////////以上是扩展操作/////////////////////////////////////
}