package com.simplemobiletools.clock.helpers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.widget.RemoteViews
import com.simplemobiletools.clock.R
import com.simplemobiletools.clock.activities.SplashActivity
import com.simplemobiletools.clock.extensions.config
import com.simplemobiletools.clock.extensions.getFormattedDate
import com.simplemobiletools.clock.extensions.getFormattedTime
import com.simplemobiletools.clock.extensions.scheduleNextWidgetUpdate
import com.simplemobiletools.commons.extensions.applyColorFilter
import com.simplemobiletools.commons.extensions.getLaunchIntent
import com.simplemobiletools.commons.extensions.setText
import java.util.*

class MyWidgetDateTimeProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        performUpdate(context)
        context.scheduleNextWidgetUpdate()
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        context.scheduleNextWidgetUpdate()
    }

    private fun performUpdate(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
        appWidgetManager.getAppWidgetIds(getComponentName(context)).forEach {
            RemoteViews(context.packageName, getProperLayout(context)).apply {
                updateTexts(context, this)
                updateColors(context, this)
                setupAppOpenIntent(context, this)
                appWidgetManager.updateAppWidget(it, this)
            }
        }
    }

    private fun getProperLayout(context: Context): Int{
        return R.layout.widget_date_time_with_shadow
    }
//    private fun getProperLayout(context: Context) = if (context.config.useTextShadow) {
//        if (isOreoPlus()) {
//            R.layout.widget_date_time_with_shadow
//            Log.i("xmg", "111111111111111111111111111111")
//        } else {
//            R.layout.widget_date_time_with_shadow_pre_oreo
//            Log.i("xmg", "2222222222222222222222222222222")
//        }
//    } else {
//        if (isOreoPlus()) {
//            R.layout.widget_date_time
//            Log.i("xmg", "3333333333333333333333333333333")
//        } else {
//            R.layout.widget_date_time_pre_oreo
//            Log.i("xmg", "4444444444444444444444444444")
//        }
//    }

    private fun updateTexts(context: Context, views: RemoteViews) {
        val timeText = context.getFormattedTime(getPassedSeconds(), false, false).toString()
//        val nextAlarm = getFormattedNextAlarm(context)
        views.apply {
            //时间
            setText(R.id.widget_time, timeText)
            //年月日
            setText(R.id.widget_date, context.getFormattedDate(Calendar.getInstance()))
            //第三行文字
            setText(R.id.widget_next_alarm, getTipsText())

//            setVisibleIf(R.id.widget_alarm_holder, nextAlarm.isNotEmpty())
        }
    }

    private fun updateColors(context: Context, views: RemoteViews) {
        val config = context.config
        val widgetTextColor = config.widgetTextColor

        views.apply {
            applyColorFilter(R.id.widget_background, config.widgetBgColor)
            setTextColor(R.id.widget_time, widgetTextColor)
            setTextColor(R.id.widget_date, widgetTextColor)
            setTextColor(R.id.widget_next_alarm, widgetTextColor)

//            if (context.config.useTextShadow) {
//                val bitmap = getMultiplyColoredBitmap(R.drawable.ic_clock_shadowed, widgetTextColor, context)
//                setImageViewBitmap(R.id.widget_next_alarm_image, bitmap)
//            } else {
//                setImageViewBitmap(R.id.widget_next_alarm_image, context.resources.getColoredBitmap(R.drawable.ic_alarm_vector, widgetTextColor))
//            }
        }
    }

    private fun getComponentName(context: Context) = ComponentName(context, this::class.java)

    private fun setupAppOpenIntent(context: Context, views: RemoteViews) {
        (context.getLaunchIntent() ?: Intent(context, SplashActivity::class.java)).apply {
            putExtra(OPEN_TAB, TAB_CLOCK)
            val pendingIntent = PendingIntent.getActivity(context, OPEN_APP_INTENT_ID, this, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.ll_bottom_layout, pendingIntent)
        }

        val in1 = Intent(context, SplashActivity::class.java)
        in1.putExtra(ACTION_TYPE, ACTION_ALARM)
        val pi1 = PendingIntent.getActivity(context, 1111, in1, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.tv_left_btn, pi1)

        val in2 = Intent(context, SplashActivity::class.java)
        in2.putExtra(ACTION_TYPE, ACTION_CALENDAR)
        val pi2 = PendingIntent.getActivity(context, 1112, in2, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.tv_right_btn, pi2)
    }

    private fun getTipsText(): String {
        val tips = arrayOf(
            "永远相信美好的事情即将发生",
            "心态放平",
            "雨过天晴",
            "风雨之后才能见到彩虹",
            "走过昨天，拥抱今天，期待明天",
            "每一个早晨，都是新的开始",
            "山高水长，岁月静好",
            "天气不错，心情也很好",
            "今天比昨天更好",
            "山穷水尽疑无路",
            "柳暗花明又一村",
            "再遥远的距离，也会越走越近")
        return tips[Random().nextInt(12)]
    }

    private fun getFormattedNextAlarm(context: Context): String {
//        return Settings.System.getString(
//            context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED
//        )

        return "天气不错"
//        val nextAlarm = context.getNextAlarm()
//        if (nextAlarm.isEmpty()) {
//            return ""
//        }
//
//        val isIn24HoursFormat = !nextAlarm.endsWith(".")
//        return when {
//            context.config.use24HourFormat && !isIn24HoursFormat -> {
//                val dayTime = nextAlarm.split(" ")
//                val times = dayTime[1].split(":")
//                val hours = times[0].toInt()
//                val minutes = times[1].toInt()
//                val seconds = 0
//                val isAM = dayTime[2].startsWith("a", true)
//                val newHours = when {
//                    hours == 12 && isAM -> 0
//                    hours == 12 && !isAM -> 12
//                    !isAM -> hours + 12
//                    else -> hours
//                }
//                formatTime(false, true, newHours, minutes, seconds)
//            }
//            !context.config.use24HourFormat && isIn24HoursFormat -> {
//                val times = nextAlarm.split(" ")[1].split(":")
//                val hours = times[0].toInt()
//                val minutes = times[1].toInt()
//                val seconds = 0
//                context.formatTo12HourFormat(false, hours, minutes, seconds)
//            }
//            else -> nextAlarm
//        }
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle?) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        performUpdate(context)
    }

    private fun getMultiplyColoredBitmap(resourceId: Int, newColor: Int, context: Context): Bitmap {
        val options = BitmapFactory.Options()
        options.inMutable = true
        val bmp = BitmapFactory.decodeResource(context.resources, resourceId, options)
        val paint = Paint()
        val filter = PorterDuffColorFilter(newColor, PorterDuff.Mode.MULTIPLY)
        paint.colorFilter = filter
        val canvas = Canvas(bmp)
        canvas.drawBitmap(bmp, 0f, 0f, paint)
        return bmp
    }
}
