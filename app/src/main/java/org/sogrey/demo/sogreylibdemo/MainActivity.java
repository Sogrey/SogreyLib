package org.sogrey.demo.sogreylibdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.sogrey.base.activity.base.BaseActivity;
import org.sogrey.utils.AndroidSettingsUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutRedId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

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

        new AndroidSettingsUtils(this).go2Settings(AndroidSettingsUtils.ACTION_WIFI_SETTINGS);
//        new AndroidSettingsUtils(this).go2Settings("com.android.settings.wifi.WifiSettings");
    }
}
