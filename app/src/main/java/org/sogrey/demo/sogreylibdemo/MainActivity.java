package org.sogrey.demo.sogreylibdemo;

import android.provider.Settings;
import android.util.Log;
import android.view.View;

import org.sogrey.base.activity.base.BaseActivity;
import org.sogrey.camera.CameraActivity;
import org.sogrey.demo.sogreylibdemo.bean.Person;
import org.sogrey.demo.sogreylibdemo.db.DBUtils;
import org.sogrey.utils.IntentUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutRedId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            personList.add(new Person("person"+i,10+i));
        }

        DBUtils.INSTANCE.save(personList);
    }

    public void btnClickk(View view) {
//        第一种
//        Intent intent = new Intent();
//        intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
//        startActivity(intent);
//        第二种
//        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
//        第三种
//        Intent i = new Intent();
//        if(android.os.Build.VERSION.SDK_INT >= 11){
//            //Honeycomb
//            i.setClassName("com.android.settings", "com.android.settings.Settings$WifiSettingsActivity");
//        }else{
//            //other versions
//            i.setClassName("com.android.settings"
//                    , "com.android.settings.wifi.WifiSettings");
//        }
//        startActivity(i);
//        第四种
//        Intent wifiSettingsIntent = new Intent(AndroidSettingsUtils.ACTION_WIFI_SETTINGS);
//        startActivity(wifiSettingsIntent);

//        new AndroidSettingsUtils(this).go2SettingsByAction(AndroidSettingsUtils.ACTION_WIFI_SETTINGS);
//        new AndroidSettingsUtils(this).go2Settings("com.android.settings.wifi.WifiSettings");

//        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

//        Intent intent =  new Intent(Settings.ACTION_DREAM_SETTINGS);
//        startActivity(intent);

        IntentUtils.getInstance().go2Settings(this, Settings.ACTION_DEVICE_INFO_SETTINGS );

    }

    public void btnCamera(View view) {
//        startIntent(CameraActivity.class,R.anim.base_slide_right_in,R.anim.base_slide_remain);
//        IntentUtils.getInstance().go2Settings(this, Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS );


      ArrayList<Person> personArrayList =  DBUtils.INSTANCE.queryBySql("select * from tb_person where age%3=1",Person.class);
      if(personArrayList!=null&&!personArrayList.isEmpty()){
          for (Person person : personArrayList) {
              Log.e("XXX",person.toString());
          }
      }
    }
}
