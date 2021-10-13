package com.example.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.timer.databinding.ThreadTimerBinding

class StartActivity : AppCompatActivity() {


    private var _binding: ThreadTimerBinding? = null
    val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ThreadTimerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

    }
}