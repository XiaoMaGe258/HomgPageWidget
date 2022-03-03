package com.simplemobiletools.clock.activities

import android.content.Intent
import com.simplemobiletools.clock.helpers.*
import com.simplemobiletools.commons.activities.BaseSplashActivity
import android.content.ContentUris
import android.net.Uri

import android.provider.CalendarContract
import java.util.*
import android.provider.AlarmClock
import android.util.Log


class SplashActivity : BaseSplashActivity() {
    override fun initActivity() {
        when {
//            intent?.action == "android.intent.action.SHOW_ALARMS" -> {
//                Intent(this, MainActivity::class.java).apply {
//                    putExtra(OPEN_TAB, TAB_ALARM)
//                    startActivity(this)
//                }
//            }
            intent.extras?.containsKey(OPEN_TAB) == true -> {
                Intent(this, MainActivity::class.java).apply {
                    putExtra(OPEN_TAB, intent.getIntExtra(OPEN_TAB, TAB_CLOCK))
                    putExtra(TIMER_ID, intent.getIntExtra(TIMER_ID, INVALID_TIMER_ID))
                    startActivity(this)
                }
            }
            intent.getStringExtra(ACTION_TYPE) == ACTION_ALARM -> {
                val i = Intent(AlarmClock.ACTION_SHOW_ALARMS)
                startActivity(i)
            }
            intent.getStringExtra(ACTION_TYPE) == ACTION_CALENDAR -> {
                val builder: Uri.Builder = CalendarContract.CONTENT_URI.buildUpon()
                builder.appendPath("time")
                ContentUris.appendId(builder, Date().getTime())
                val calIntent = Intent(Intent.ACTION_VIEW).setData(builder.build())
                startActivity(calIntent)
            }
            else -> startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
