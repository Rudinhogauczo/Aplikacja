package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class NoweKonto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nowekonto)

        val spinnerOptions = findViewById<Spinner>(R.id.WyborTypu)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        adapter.add("Chcę zredukować wagę")
        adapter.add("Chcę zachować wagę")
        adapter.add("Chcę zwiększyć wagę")

        spinnerOptions.adapter = adapter
    }
}