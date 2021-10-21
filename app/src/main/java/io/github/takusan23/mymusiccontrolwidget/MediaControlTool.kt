package io.github.takusan23.mymusiccontrolwidget

import android.content.ComponentName
import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState

object MediaControlTool {

    fun getMediaController(context: Context): MediaController? {
        // MediaSession取得
        val mediaSessionManager = context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
        val activeMediaSessionList = mediaSessionManager.getActiveSessions(ComponentName(context, MusicNotificationListener::class.java))
        // ない
        if (activeMediaSessionList.isEmpty()) {
            return null
        }
        return activeMediaSessionList[0]
    }

    fun sendControlPauseOrPlay(context: Context) {
        if (getMediaController(context)?.playbackState?.state == PlaybackState.STATE_PLAYING) {
            getMediaController(context)?.transportControls?.pause()
        } else {
            getMediaController(context)?.transportControls?.play()
        }
    }

    fun sendControlNext(context: Context) {
        getMediaController(context)?.transportControls?.skipToNext()

    }

    fun sendControlPrev(context: Context) {
        getMediaController(context)?.transportControls?.skipToPrevious()
    }

    fun sendControlPos(context: Context, pos: Long) {
        getMediaController(context)?.transportControls?.seekTo(pos)
    }

}