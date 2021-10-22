# 開発者向け？
どうやって現在再生している楽曲情報を取得したり、プレイヤーの操作しているのかという話ですが、
MediaSessionManagerから現在アクティブ状態になっているMediaSessionからMediaControllerを取得しています。

# MediaController
MediaSessionの操作、楽曲情報を担当するクラス。
これを経由することで今のMediaSessionから、情報を取得しています。
別にBroadcastとか使っているわけじゃないです。MediaSessionの仕組みを利用しているので大抵の音楽アプリで動くはずです。

# このアプリが他アプリのMediaSessionのMediaControllerへアクセスするまで
`MediaSessionManager`にある`getActiveSessions`を利用して現在アクティブ状態になっているMediaSessionのMediaControllerへアクセスします。
このメソッドは、システムレベルの権限 or 通知領域へのアクセス のどちらかが必要ですが、root権限が無いので後者を選ぶしか無い。

# 音楽アプリ開発者向け？楽曲情報を提供したり、操作を受け付ける方法
~~MediaSessionを真面目に実装すれば対応します。~~

## 楽曲情報
MediaSessionCompat#setMetadata()に以下の内容を入れると対応します。

- MediaMetadata.METADATA_KEY_TITLE
    - 文字列
    - タイトル
- MediaMetadata.METADATA_KEY_ALBUM
    - 文字列
    - アルバム名
- MediaMetadata.METADATA_KEY_ARTIST
    - 文字列
    - アーティスト名
- MediaMetadata.METADATA_KEY_ALBUM_ART
    - Bitmap
    - アルバムアート
    - Uriは非対応です

## キュー（再生リスト）
MediaController#getQueueから取得しています。

- MediaDescription.getTitle
    - 一行目。タイトル
- MediaDescription.getSubtitle
    - 二行目。アーティスト名

## アプリ起動

**MediaController#getSessionActivityが一部のアプリでは動かないので、MediaController#getPackageNameからアプリを起動するようになっています。**

## プレイヤー操作
PlaybackStateCompat#setActionsで以下の操作を受け付けていて、
実装がある必要(MediaSessionCompat.Callbackで該当メソッドをオーバーライドしている)があります。

- ACTION_PLAY
- ACTION_PAUSE
- ACTION_SKIP_TO_NEXT
- ACTION_SKIP_TO_PREVIOUS
- ACTION_SEEK_TO
    - ない場合は前の曲ボタンが正常に動きません。（だいたいのアプリが前の曲ボタン投下一回目が曲の最初に移動なので）

このアプリでは、キューから項目を押して再生する機能がありますが、アプリ次第で実装が違う気がする(指定して再生にもonPlayFromMediaIdとかonPlayFromUriとかあるので)気がするので、
再生したい楽曲になるまで **次の曲ボタン or 前の曲ボタン** を繰り返し押すことで実装しています。