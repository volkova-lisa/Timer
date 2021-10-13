package com.example.timer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import java.util.*
import kotlin.collections.HashSet

class CountDownTimer(timeInSeconds: Long) {

    private var handler: Handler = Handler(Looper.getMainLooper())

    private val bundleKey : String = "COUNT_DOWN_TIMER_BUNDLE_EXTRA_TIME"
    private var onTickSubscribers: MutableSet<TimerOnUpdate> = Collections.synchronizedSet(HashSet())
    private lateinit var timerThread: Thread
    private var currentTime: Long = timeInSeconds
    private var isTimerStarted = false

    fun start() {
        if (!isTimerStarted) {
            isTimerStarted = true
            createTickThread()
            timerThread.start()
        } else {
            pause()
        }
    }

    private fun createTickThread() {
        timerThread = Thread {
            try {
                for (i in 0 until currentTime) {
                    Thread.sleep(1000)
                    if (currentTime > 0) {
                        currentTime--
                    } else {
                        return@Thread
                    }
                    notifySubscribers()
                }
            } catch (ignored: InterruptedException) {
            }
        }
    }

    private fun pause() {
        isTimerStarted = false
        timerThread.interrupt()
    }

    fun reset() {
        isTimerStarted = false
        currentTime = 0
        timerThread.interrupt()
    }

    fun subscribeOnTick(onTimerTickInterface: TimerOnUpdate) {
        onTickSubscribers.add(onTimerTickInterface)
    }

    fun unsubscribeOnTick(onTimerTickInterface: TimerOnUpdate) {
        onTickSubscribers.remove(onTimerTickInterface)
    }

    private fun notifySubscribers() {
        handler.post {
            onTickSubscribers.forEach { timerOnUpdate ->
                timerOnUpdate.onUpdateTimer(formatTime(currentTime * 1000))
            }
        }
    }

    private fun formatTime(millis: Long): String {
        val secs = millis / 1000
        return String.format("%02d:%02d:%02d", secs / 3600, secs % 3600 / 60, secs % 60)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(bundleKey)) {
            currentTime = savedInstanceState.getLong(bundleKey)
            start()
        }
    }

    fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putLong(bundleKey, currentTime)
        outPersistentState.putLong(bundleKey, currentTime)
    }

    interface TimerOnUpdate {
        fun onUpdateTimer(strTime: String)
    }
}