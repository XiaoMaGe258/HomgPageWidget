package com.pb.app.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mayong on 2021/12/7
 */
public class TimeService extends Service {

    private Timer mTimer;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("ss");

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("xmg", "TimeService onCreate");

    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("xmg", "TimeService onStartCommand 服务启动");
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateView();
            }
        }, 0, 1000);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateView(){
//        Log.i("xmg", "updateView");
        String time = sdf.format(new Date());
        String date = sdf1.format(new Date());
        String time2 = sdf2.format(new Date());
//        Time time = new Time();
//        time.setToNow();
//        int hour = time.hour;
//        int min = time.minute;
//        int second = time.second;
//        int year = time.year;
//        int month = time.month + 1;
//        int day = time.monthDay;

        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.time_widget);
        rv.setTextViewText(R.id.tv_widget_time, time);
        rv.setTextViewText(R.id.tv_widget_date, date);
        rv.setTextViewText(R.id.tv_widget_time2, time2);

        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName cn = new ComponentName(getApplicationContext(), TimeWidget.class);
        manager.updateAppWidget(cn, rv);

    }

//    private void UpdateWidget(Context context) {
//        //不用Calendar，Time对cpu负荷较小
//        Time time = new Time();
//        time.setToNow();
//        int hour = time.hour;
//        int min = time.minute;
//        int second = time.second;
//        int year = time.year;
//        int month = time.month + 1;
//        int day = time.monthDay;
//        String strTime = String.format("%02d:%02d:%02d %04d-%02d-%02d", hour, min, second, year, month, day);
//        RemoteViews updateView = new RemoteViews(context.getPackageName(),
//                R.layout.time_widget_layout);
//
//        //时间图像更新
//        String packageString = "org.owl.mythou";
//        String timePic = "time";
//        int hourHbit = hour / 10;
//        updateView.setImageViewResource(R.id.hourHPic, getResources().getIdentifier(timePic + hourHbit, "drawable", packageString));
//        int hourLbit = hour % 10;
//        updateView.setImageViewResource(R.id.hourLPic, getResources().getIdentifier(timePic + hourLbit, "drawable", packageString));
//        int minHbit = min / 10;
//        updateView.setImageViewResource(R.id.MinuteHPic, getResources().getIdentifier(timePic + minHbit, "drawable", packageString));
//        int minLbit = min % 10;
//        updateView.setImageViewResource(R.id.MinuteLPic, getResources().getIdentifier(timePic + minLbit, "drawable", packageString));
//
//        //星期几
//        updateView.setTextViewText(R.id.weekInfo, getWeekString(time.weekDay + 1));
//
//        //日期更新，根据日期，计算使用的图片
//        String datePic = "date";
//        int year1bit = year / 1000;
//        updateView.setImageViewResource(R.id.Year1BitPic, getResources().getIdentifier(datePic + year1bit, "drawable", packageString));
//        int year2bit = (year % 1000) / 100;
//        updateView.setImageViewResource(R.id.Year2BitPic, getResources().getIdentifier(datePic + year2bit, "drawable", packageString));
//        int year3bit = (year % 100) / 10;
//        updateView.setImageViewResource(R.id.Year3BitPic, getResources().getIdentifier(datePic + year3bit, "drawable", packageString));
//        int year4bit = year % 10;
//        updateView.setImageViewResource(R.id.Year4BitPic, getResources().getIdentifier(datePic + year4bit, "drawable", packageString));
//        //月
//        int mouth1bit = month / 10;
//        updateView.setImageViewResource(R.id.mouth1BitPic, getResources().getIdentifier(datePic + mouth1bit, "drawable", packageString));
//        int mouth2bit = month % 10;
//        updateView.setImageViewResource(R.id.mouth2BitPic, getResources().getIdentifier(datePic + mouth2bit, "drawable", packageString));
//        //日
//        int day1bit = day / 10;
//        updateView.setImageViewResource(R.id.day1BitPic, getResources().getIdentifier(datePic + day1bit, "drawable", packageString));
//        int day2bit = day % 10;
//        updateView.setImageViewResource(R.id.day2BitPic, getResources().getIdentifier(datePic + day2bit, "drawable", packageString));
//
//        //点击widget，启动日历
//        Intent launchIntent = new Intent();
//        launchIntent.setComponent(new ComponentName("com.mythou.mycalendar",
//                "com.mythou.mycalendar.calendarMainActivity"));
//        launchIntent.setAction(Intent.ACTION_MAIN);
//        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        PendingIntent intentAction = PendingIntent.getActivity(context, 0,
//                launchIntent, 0);
//        updateView.setOnClickPendingIntent(R.id.SmallBase, intentAction);
//        AppWidgetManager awg = AppWidgetManager.getInstance(context);
//        awg.updateAppWidget(new ComponentName(context, TimeWidgetSmall.class),
//                updateView);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("xmg", "TimeService Destroy");
        try {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        } catch (Exception e) {
            //do nothing
        }
    }

}
