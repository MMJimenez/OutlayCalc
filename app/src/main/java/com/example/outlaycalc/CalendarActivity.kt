package com.example.outlaycalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.time.Month
import java.util.*
import java.util.Calendar.*
import java.util.Locale.ENGLISH

class CalendarActivity : AppCompatActivity() {

    var TAG = "miapp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)


        var today = Calendar.getInstance()

        setDateText(today)
        getDaysOfTheMonth(today)
    }

    fun setDateText(today: Calendar) {
        val monthText = findViewById<TextView>(R.id.monthName)

        val month = today.getDisplayName(MONTH, SHORT, ENGLISH)
        val year = today.get(YEAR)

        monthText.text = "$month $year"
        Log.v(TAG, "$month $year")
    }

    fun getDaysOfTheMonth(today: Calendar) {
        val firstDayOfWeek = today.getMinimalDaysInFirstWeek()
//        val lastDayOfWeek = today
        Log.v(TAG, "$firstDayOfWeek y ")
    }
}