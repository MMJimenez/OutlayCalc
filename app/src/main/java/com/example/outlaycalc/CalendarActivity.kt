package com.example.outlaycalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

class CalendarActivity : AppCompatActivity() {

    var TAG = "milog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        var calendar = Calendar.getInstance()

        Log.v(TAG, "$calendar")
    }
}