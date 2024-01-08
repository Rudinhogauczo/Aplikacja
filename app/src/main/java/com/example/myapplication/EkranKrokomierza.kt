package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class EkranKrokomierza : AppCompatActivity(), StepListener {

    private lateinit var krokomierz: Krokomierz
    private lateinit var wynikTextView: TextView
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ekrankrokomierza)

        wynikTextView = findViewById(R.id.KrokiTextView)
        pieChart = findViewById(R.id.pieChartKrok)

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(android.R.color.white)
        pieChart.transparentCircleRadius = 61f
        pieChart.setEntryLabelColor(android.R.color.black)


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
            wynikTextView.text = "$count / ${krokomierz.dailyGoal}"
            updatePieChart(count.toFloat())
        }
    }

    private fun updatePieChart(currentSteps: Float) {
        val remainingSteps = maxOf(0f, krokomierz.dailyGoal - currentSteps)

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(currentSteps, "Zrobione"))
        entries.add(PieEntry(remainingSteps, "Pozostało"))

        val dataSet = PieDataSet(entries, "Dzienny cel kroków")
        dataSet.setColors(Color.parseColor("#00B74F"), Color.parseColor("#C8102E"))
        dataSet.valueTextColor = android.R.color.black
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)

        pieChart.legend.isEnabled = false


        pieChart.data = data
        pieChart.invalidate()
    }
}