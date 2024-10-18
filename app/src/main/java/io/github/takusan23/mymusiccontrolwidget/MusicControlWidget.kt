package io.github.takusan23.mymusiccontrolwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent

/**
 * Implementation of App Widget functionality.
 */
class MusicControlWidget : AppWidgetProvider() {

    /** ブロードキャスト受け取り */
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: return

        when (intent?.extras?.getString("control")) {
            "pause" -> MediaControlTool.sendControlPauseOrPlay(context)
            "prev" -> MediaControlTool.sendControlPrev(context)
            "next" -> MediaControlTool.sendControlNext(context)
            "select" -> {
                val index = intent.extras?.getInt("index") ?: return
                val prevOrNextCount = intent.extras?.getInt("prev_next_count") ?: return
                MediaControlTool.sendPlaylistMoveIndex(context, index, prevOrNextCount)
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

