package io.github.takusan23.mymusiccontrolwidget

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import io.github.takusan23.mymusiccontrolwidget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.activityMainPermissionButton.setOnClickListener {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        }

        viewBinding.activityMainPrivacyPolicyButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://github.com/takusan23/MyMusicControlWidget/blob/master/PRIVARY_POLICY.md".toUri())
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { root, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            root.updatePadding(
                left = systemInsets.left,
                top = systemInsets.top,
                right = systemInsets.right,
                bottom = systemInsets.bottom
            )
            insets
        }
    }

}