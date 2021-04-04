package br.ufpe.cin.vrvs.podcastplayer.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.icu.util.TimeUnit
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES

object Utils {
    fun processUrl(url: String): String {
        if ("https" in url)
            return url
        return url.replace("http", "https")
    }

    fun setTint(resources: Resources, drawable: Drawable, @ColorRes color: Int) {
        DrawableCompat.setTint(
            DrawableCompat.wrap(drawable),
            ResourcesCompat.getColor(
                resources,
                color,
                null
            )
        )
    }

    fun toPrettyDate(timestamp: Long): String {
        val df = SimpleDateFormat("MMMM dd, yyyy")
        return df.format(Date().apply {  time = timestamp*1000L})
    }

    fun toPrettyDuration(ms: Long): String {
        val h = MILLISECONDS.toHours(ms)
        val m = MILLISECONDS.toMinutes(ms)%60
        val s = MILLISECONDS.toSeconds(ms)%60
        return when {
            h>0 -> {
                "$h h $m m $s s"
            }
            m > 0 -> {
                "$m m $s s"
            }
            else -> {
                "$s s"
            }
        }

    }
}