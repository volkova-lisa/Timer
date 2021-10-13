package com.example.timer

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.timer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val START_TIME_IN_MILLIS: Long = 600000

    private var _binding: ActivityMainBinding? = null
    val mBinding get() = _binding!!

    lateinit var startButton: Button
    var startPressed : Boolean = true
    lateinit var resetButton : Button
    lateinit var countDownTextView : TextView
    lateinit var countDownTimer: CountDownTimer
    var timerRunning : Boolean = false
    var timeLeftInMillis = START_TIME_IN_MILLIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        startButton = mBinding.startBtn
        resetButton = mBinding.resetBtn
        countDownTextView = mBinding.countdown
        countDownTimer = CountDownTimer(10)
        countDownTimer.subscribeOnTick(object : CountDownTimer.TimerOnUpdate {
            override fun onUpdateTimer(strTime: String) {
                countDownTextView.setText(strTime)
            }
        })

        startButton.setOnClickListener {
            countDownTimer.start()
            if (startPressed) startButton.text = "Stop"
            else startButton.text = "Start"
            startPressed = !startPressed
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        countDownTimer.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        countDownTimer.onRestoreInstanceState(savedInstanceState)
    }
}