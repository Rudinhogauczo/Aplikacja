package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EkranKrokomierza : AppCompatActivity(), StepListener {

    private lateinit var krokomierz: Krokomierz
    private lateinit var wynikTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekrankrokomierza)

        wynikTextView = findViewById(R.id.wynikTextView)

        krokomierz = Krokomierz(this, this)
    }

    override fun onStart() {
        super.onStart()
        krokomierz.start()
    }

    override fun onStop() {
        super.onStop()
        krokomierz.stop()
    }

    override fun onStep(count: Int) {
        runOnUiThread {
            wynikTextView.text = "Kroki: $count"
        }
    }
}