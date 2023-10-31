package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class EkranStartowy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekranstartowy)

        val buttonstart = findViewById<Button>(R.id.ButtonStart)

        buttonstart.setOnClickListener {
            val intent = Intent(this, EkranGlowny::class.java)
            startActivity(intent)
        }
    }
}