package com.cancer.xapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cancer.core.app.event.XBus
import com.cancer.xapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var count = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        XBus.observe<XEvent.ShowUserName>(this) {
            binding.tvCount.text = it.userName
        }
        binding.btnAdd.setOnClickListener {
            XBus.post(XEvent.ShowUserName(count.toString()))
            count++
        }

    }


}