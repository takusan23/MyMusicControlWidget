package io.github.takusan23.mymusiccontrolwidget

import android.content.ComponentName
import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import kotlin.math.abs

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

    /**
     * 再生リストで指定した番号に移動する。
     * プレイヤーが移動する機能を実装していない場合は、指定回数、進む/戻るを押す。
     *
     * @param index 移動したい音楽の、再生リスト内での位置。インデックス
     * @param prevOrNextCount 未実装の場合、指定回数、進む/戻る を押すのでその回数。マイナスの場合は戻る
     */
    fun sendPlaylistMoveIndex(context: Context, index: Int, prevOrNextCount: Int) {
        val controller = getMediaController(context) ?: return
        val supportedActionFlags = controller.playbackState?.actions ?: return

        if ((supportedActionFlags and PlaybackState.ACTION_SKIP_TO_QUEUE_ITEM) != 0L) {
            // 指定したトラック番号に移動できる機能があればそれを使う
            val id = controller.queue?.get(index)?.queueId ?: return
            controller.transportControls.skipToQueueItem(id)
        } else {
            // 未実装
            // マイナス？
            val isNegative = prevOrNextCount <= -1
            // その前にシーク位置を0に戻す。これしないと戻るの最初の動作が曲の最初になるので
            sendControlPos(context, 0)
            repeat(abs(prevOrNextCount)) {
                if (isNegative) {
                    sendControlPrev(context)
                } else {
                    sendControlNext(context)
                }
            }
        }
    }

}