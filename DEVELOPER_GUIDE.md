# 開発者向け？
どうやって現在再生している楽曲情報を取得したり、プレイヤーの操作しているのかという話ですが、
MediaSessionManagerから現在アクティブ状態になっているMediaSessionからMediaControllerを取得しています。

# MediaController
MediaSessionの操作、楽曲情報を担当するクラス。
これを経由することで今のMediaSessionから、情報を取得しています。

# 楽曲情報を提供したり、操作を受け付ける方法
MediaSessionを真面目に実装すれば対応します。

嘘です後で書きます