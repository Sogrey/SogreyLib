package org.sogrey.base.activity;

import android.app.Activity;
import android.text.TextUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map.Entry;


/**
 * 管理类;
 *
 * @author Sogrey.
 */
public class ActivityManager {

    private static ActivityManager activityManager;

    public static ActivityManager getActivityManager() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    private final HashMap<String, SoftReference<Activity>> taskMap = new HashMap<>();
    private final HashMap<String, SoftReference<Class<?>>> taskMapCls = new HashMap<>();


    public final void putActivity(Activity atv) {
        taskMap.put(atv.toString(), new SoftReference<>(atv));
    }

    public final void removeActivity(Activity atv) {
        taskMap.remove(atv.toString());
    }

    public final void clear() {
        taskMap.clear();
    }

    public final void clearButThis(String activityName) {
        for (Entry<String, SoftReference<Activity>> stringSoftReferenceEntry : taskMap
                .entrySet()) {
            SoftReference<Activity> activityReference = stringSoftReferenceEntry
                    .getValue();
            Activity activity = activityReference.get();
            if (activity != null && !TextUtils.equals(activity.getLocalClassName(), activityName)) {
                activity.finish();
            }
        }
        clear();
    }

    public final void putActivityCls(Class<?> cls) {
        taskMapCls.put(cls.getName(), new SoftReference<>(cls));
    }

    public final boolean hasActivityCls(Class<?> cls) {
        return taskMapCls.containsKey(cls.getName());
    }

    public final void removeActivityCls(Class<?> cls) {
        taskMapCls.remove(cls.getName());
    }

    public final void clearActivityCls() {
        taskMapCls.clear();
    }

    public final void exit() {
        for (Entry<String, SoftReference<Activity>> stringSoftReferenceEntry : taskMap
                .entrySet()) {
            SoftReference<Activity> activityReference = stringSoftReferenceEntry
                    .getValue();
            Activity activity = activityReference.get();
            if (activity != null) {
                activity.finish();
            }
        }

        taskMap.clear();
    }
}
