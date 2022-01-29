package com.pb.app.widget;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


/**
 * Implementation of App Widget functionality.
 */
public class TimeWidget extends AppWidgetProvider {

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("xmg", "TimeWidget  onReceive  action="+intent.getAction());

        Uri data = intent.getData();
        int resID = -1;
        if(data != null) {
            resID = Integer.parseInt(data.getSchemeSpecificPart());
        }
        /**通过远程对象设置文字*/
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.time_widget);
        switch (resID) {
            case R.id.tv_top_left_btn:
                Toast.makeText(context, "1111", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_top_right_btn:
                Toast.makeText(context, "2222", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_bottom_btn:
                Toast.makeText(context, "3333", Toast.LENGTH_SHORT).show();
                break;
        }
        // 获得appwidget管理实例，用于管理appwidget以便进行更新操作
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        // 相当于获得所有本程序创建的appwidget
        ComponentName componentName = new ComponentName(context, TimeWidget.class);
        // 更新appwidget
        appWidgetManager.updateAppWidget(componentName, remoteViews);

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i("xmg", "TimeWidget  onUpdate");

        /**构造RemoteViews实例*/
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.time_widget);
        //添加点击事件
        remoteViews.setOnClickPendingIntent(R.id.tv_top_left_btn, getPendingIntent(context, R.id.tv_top_left_btn));
        remoteViews.setOnClickPendingIntent(R.id.tv_top_right_btn, getPendingIntent(context, R.id.tv_top_right_btn));
        remoteViews.setOnClickPendingIntent(R.id.tv_bottom_btn, getPendingIntent(context, R.id.tv_bottom_btn));
        // 更新Appwidget
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);


        Time time = new Time();
        time.setToNow();
        //使用Service更新时间
        Intent intent = new Intent(context, TimeService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        //使用Alarm定时更新界面数据
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC, time.toMillis(true), 60*1000, pendingIntent);


        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private PendingIntent getPendingIntent(Context context, int resID){
        Intent intent = new Intent();
        //注意这个intent构造的是显式intent，直接将这个广播发送给MyAppWidgetProvider，使用Action的方式接收不到
        intent.setClass(context, TimeWidget.class);
        intent.setData(Uri.parse("hx:" + resID));
        return PendingIntent.getBroadcast(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onEnabled(Context context) {
        Log.i("xmg", "TimeWidget  onEnabled");
//        context.startService(new Intent(context, TimeService.class));//开启服务
    }


    @Override
    public void onDisabled(Context context) {
        Log.i("xmg", "TimeWidget  onDisabled");
//        context.stopService(new Intent(context, TimeService.class));//stop服务
    }
}