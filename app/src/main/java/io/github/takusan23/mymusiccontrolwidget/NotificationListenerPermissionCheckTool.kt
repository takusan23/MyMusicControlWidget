package io.github.takusan23.mymusiccontrolwidget

import android.content.ComponentName
import android.content.Context
import android.media.session.MediaSessionManager

object NotificationListenerPermissionCheckTool {

    /**
     * 権限があるか
     *
     * ContextCompat#checkSelfPermission()が使えないので、実際に叩いてSecurityExceptionが出ないかチェック
     * */
    fun isGrantedNotificationListenerPermission(context: Context): Boolean {
        val mediaSessionManager = context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
        return try {
            mediaSessionManager.getActiveSessions(ComponentName(context, MusicNotificationListener::class.java))
            true
        } catch (e: Exception) {
            false
        }
    }


}