package com.android.akl.bluetoothscreamer

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class DisconnectionActivity : AppCompatActivity() {
    private lateinit var mConstraintLayout: ConstraintLayout
    var mTextView: TextView? = null
    var mButton: Button? = null
    var temp = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disconnection)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        mConstraintLayout = findViewById(R.id.disconnection_activity_layout)
        mTextView = findViewById(R.id.disconnection_activity_tv)
        mButton = findViewById(R.id.disconnection_activity_button)

        val deviceName = intent.extras?.getString("device_name")
        mTextView?.text = "$deviceName disconnected"

        val player = playSound()

        mButton?.setOnClickListener {
            player.stop()
            finish()
        }
    }

    private fun playSound(): MediaPlayer {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0)
        val player = MediaPlayer.create(applicationContext, RingtoneManager.getActualDefaultRingtoneUri(applicationContext, RingtoneManager.TYPE_RINGTONE))
        player.start()

        val handler = Handler()
        handler.postDelayed({
            temp = when (temp) {
                0 -> {
                    mConstraintLayout.setBackgroundColor(Color.RED)
                    1
                }
                else -> {
                    mConstraintLayout.setBackgroundColor(Color.WHITE)
                    0
                }
            }
        }, 1000)
        return player
    }
}