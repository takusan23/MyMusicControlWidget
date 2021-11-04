package io.github.takusan23.mymusiccontrolwidget

import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService


/**
 * WidgetのListViewで使うAdapter
 * */
class MusicControlListViewWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return ListViewWidgetFactory()
    }

    private inner class ListViewWidgetFactory : RemoteViewsFactory {

        private val queueList = arrayListOf<MediaSession.QueueItem>()

        override fun onCreate() {

        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        // データ取得
        override fun onDataSetChanged() {
            queueList.clear()
            val mediaController = MediaControlTool.getMediaController(this@MusicControlListViewWidgetService)
            queueList.addAll(mediaController?.queue ?: listOf())
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        // ListViewの各View
        override fun getViewAt(p0: Int): RemoteViews {
            val remoteView = RemoteViews(applicationContext.packageName, R.layout.widget_list_item_layout)
            if (queueList.size > p0) {
                val queueItem = queueList[p0]
                remoteView.apply {
                    setTextViewText(R.id.widget_list_item_title, queueItem.description.title)
                    setTextViewText(R.id.widget_list_item_sub_title, queueItem.description.subtitle)
                    // いま再生しているのがどこなのか
                    val currentPos = queueList.indexOfFirst { it.description.title == MediaControlTool.getMediaController(this@MusicControlListViewWidgetService)?.metadata?.getString(MediaMetadata.METADATA_KEY_TITLE) }
                    // 再生中なら色変える
                    if (currentPos == p0) {
                        setViewVisibility(R.id.widget_list_item_image, View.VISIBLE)
                    } else {
                        setViewVisibility(R.id.widget_list_item_image, View.GONE)
                    }
                    // 押したら移動
                    // 引いてPendingIntentに飛ばす
                    val intent = Intent().apply {
                        putExtra("control", "select")
                        putExtra("select", p0 - currentPos)
                    }
                    setOnClickFillInIntent(R.id.widget_list_item_root, intent)
                }
            }
            return remoteView
        }

        override fun getCount(): Int {
            return queueList.size
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun onDestroy() {

        }

    }

}
