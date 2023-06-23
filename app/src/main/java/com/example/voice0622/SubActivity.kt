package com.example.voice0622

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import com.example.voice0622.databinding.ActivitySubBinding


class SubActivity : AppCompatActivity() {

    val binding by lazy { ActivitySubBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val tv_sub_1: TextView = findViewById(R.id.tv_sub_1)
        val btnback: Button = findViewById(R.id.btnback)

//        if (intent.hasExtra("name")){
//            tv_sub_1.setText(intent.getStringExtra("name"))
//        }
        binding.btnback.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}