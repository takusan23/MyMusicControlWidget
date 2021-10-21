package io.github.takusan23.mymusiccontrolwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import kotlin.math.abs

/**
 * Implementation of App Widget functionality.
 */
class MusicControlWidget : AppWidgetProvider() {

    /** ブロードキャスト受け取り */
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: return

        when (intent?.extras?.getString("control")) {
            "pause" -> {
                MediaControlTool.sendControlPauseOrPlay(context)
            }
            "prev" -> {
                MediaControlTool.sendControlPrev(context)
            }
            "next" -> {
                MediaControlTool.sendControlNext(context)
            }
            "select" -> {
                val callNum = intent.extras?.getInt("select") ?: return
                // 0でもreturn
                if (callNum == 0) return
                // マイナス？
                val isMinus = callNum <= -1
                // 多分ID指定して再生とか出来ないので戻る/進むを押しまくる
                // その前にシーク位置を0に戻す。これしないと戻るの最初の動作が曲の最初になるので
                MediaControlTool.sendControlPos(context, 0)
                repeat(abs(callNum)) {
                    if (isMinus) {
                        MediaControlTool.sendControlPrev(context)
                    } else {
                        MediaControlTool.sendControlNext(context)
                    }
                }
            }
        }

    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        UpdateWidgetTool.updateWidget(context)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

