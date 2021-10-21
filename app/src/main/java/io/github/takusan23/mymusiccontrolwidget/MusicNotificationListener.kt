package io.github.takusan23.mymusiccontrolwidget

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

/**
 * 通知監視サービス
 *
 * MediaSessionの変化を受け取る
 * */
class MusicNotificationListener : NotificationListenerService() {

    override fun onCreate() {
        super.onCreate()
        // 更新
        UpdateWidgetTool.updateWidget(this)
    }

    /** 通知更新時 */
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        // 更新
        UpdateWidgetTool.updateWidget(this)

    }

}