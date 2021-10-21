package io.github.takusan23.mymusiccontrolwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViewsService

object UpdateWidgetTool {

    /**
     * MediaSession経由で情報を受け取り、ウイジェットを更新する
     *
     * ボタン押したときの処理は[MusicControlWidget]のブロードキャスト受け取りで！
     * */
    fun updateWidget(context: Context) {
        if (!NotificationListenerPermissionCheckTool.isGrantedNotificationListenerPermission(context)) {
            // 権限なし
            return
        }

        // 現在の状態取得
        val mediaController = MediaControlTool.getMediaController(context) ?: return
        val title = mediaController.metadata?.getString(MediaMetadata.METADATA_KEY_TITLE) ?: "不明なタイトル"
        val album = mediaController.metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM) ?: "不明なアルバム"
        val artist = mediaController.metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: "不明なアーティスト"
        val bitmap = mediaController.metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
        val isPlaying = mediaController.playbackState?.state == PlaybackState.STATE_PLAYING
        val playingIconId = if (isPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_outline_play_arrow_24

        // View作成
        val views = RemoteViews(context.packageName, R.layout.music_control_widget).apply {
            setTextViewText(R.id.widget_title, title)
            setTextViewText(R.id.widget_album, album)
            setTextViewText(R.id.widget_artist, artist)
            setBitmap(R.id.widget_artwork, "setImageBitmap", bitmap)
            setInt(R.id.widget_play, "setImageResource", playingIconId)

            // ブロードキャスト送信
            val playPendingIntent = createPendingIntent(context, 1, "pause")
            val nextPendingIntent = createPendingIntent(context, 2, "next")
            val prevPendingIntent = createPendingIntent(context, 3, "prev")

            setOnClickPendingIntent(R.id.widget_play, playPendingIntent)
            setOnClickPendingIntent(R.id.widget_prev, prevPendingIntent)
            setOnClickPendingIntent(R.id.widget_next, nextPendingIntent)

            // ListView
            val remoteViewsFactoryIntent = Intent(context, MusicControlListViewWidgetService::class.java)
            setRemoteAdapter(R.id.widget_listview, remoteViewsFactoryIntent)
            // ListView更新
            val listViewClickPendingIntent = createPendingIntent(context, 10, "select", true)
            setPendingIntentTemplate(R.id.widget_listview, listViewClickPendingIntent)

        }


        // 更新
        val componentName = ComponentName(context, MusicControlWidget::class.java)
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(componentName)
        ids.forEach { id ->
            manager.updateAppWidget(id, views)
            // ListView更新
            manager.notifyAppWidgetViewDataChanged(id, R.id.widget_listview)
        }

    }


    fun createPendingIntent(context: Context, requestCode: Int, control: String, isMutable: Boolean = false): PendingIntent? {
        val intent = Intent(context, MusicControlWidget::class.java).apply {
            putExtra("control", control)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, requestCode, intent, if (isMutable) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

}