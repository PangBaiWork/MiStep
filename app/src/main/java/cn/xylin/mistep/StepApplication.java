package cn.xylin.mistep;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Process;

import java.util.ArrayList;

import cn.xylin.mistep.activitys.Main;
import cn.xylin.mistep.utils.MyCrash;
import cn.xylin.mistep.utils.NotificationUtil;
import cn.xylin.mistep.utils.Shared;
import cn.xylin.mistep.utils.WLOG;


/**
 * @author XyLin
 * @date 2020年11月17日 23:01:00
 * StepApplication.java
 **/
public class StepApplication extends Application {
    private static final ArrayList<Activity> ACTIVITYS = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        WLOG.initLogFile(this);
        MyCrash.getInstance().initContext(this);
        Shared.getShared().addShared(this, Main.USER_SETTING);
        NotificationUtil.get().init(this);
    }

    public static void add(Activity appActivity) {
        ACTIVITYS.add(appActivity);
    }

    public static void remove(Activity appActivity) {
        ACTIVITYS.remove(appActivity);
    }

    public static Activity get() {
        return ACTIVITYS.get(ACTIVITYS.size() - 1);
    }

    public static void finishAll() {
        int size = ACTIVITYS.size();
        for (int index = 0; index < size; index++) {
            if ((index + 1) < size) {
                ACTIVITYS.get(index).finish();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ACTIVITYS.get(index).finishAndRemoveTask();
                }
            }
        }
        ACTIVITYS.clear();
        //Util.TASK_SERVICE.shutdownNow();
        Process.killProcess(Process.myPid());
    }
}
