package com.android.akl.bluetoothscreamer

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*

class DisconnectionActivity : AppCompatActivity() {
    var mConstraintLayout: ConstraintLayout? = null
    var mTextView: TextView? = null
    var mButton: Button? = null
    var temp = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disconnection)

        mConstraintLayout = findViewById(R.id.disconnection_activity_layout)
        mTextView = findViewById(R.id.disconnection_activity_tv)
        mButton = findViewById(R.id.disconnection_activity_button)

        val deviceName = intent.extras?.getString("device_name")
        mTextView?.text = "$deviceName disconnected"

        val player = MediaPlayer.create(applicationContext, R.raw.siren_sound)
        player.setVolume(10f, 10f)
        player.start()

        val handler = Handler()
        handler.postDelayed({
            temp = when(temp){
                0 ->{
                    mConstraintLayout?.setBackgroundColor(Color.RED)
                    1
                }
                else ->{
                    mConstraintLayout?.setBackgroundColor(Color.WHITE)
                    0
                }
            }
        }, 1000)

        mButton?.setOnClickListener(View.OnClickListener {
            player.stop()
            finish()
        })
    }
}