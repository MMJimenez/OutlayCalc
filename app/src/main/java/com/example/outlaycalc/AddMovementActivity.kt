package com.example.outlaycalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.outlaycalc.models.Movement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_movement.*

class AddMovementActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movement)


        auth = FirebaseAuth.getInstance()

        title = "Añade un Nuevo Gasto"

        val query = db.collection("users").document(auth.currentUser!!.uid).collection("movements")

        btnAdd.setOnClickListener {
            createMovement()
        }
    }

    fun createMovement() {
        val newMovement = Movement()
        newMovement.amount = inputTxtAmount.text.toString().toFloat()
        newMovement.description = inputTxtDescription.text.toString()
        if (radioIngress.isChecked) {
            newMovement.outlay = false
        }
        Log.v(
            "miApp",
            "createMovement: ${newMovement.amount}, ${newMovement.description}, ${newMovement.outlay}"
        )
        db.collection("users").document(auth.currentUser?.uid!!).collection("movements")
            .add(newMovement)
            .addOnCompleteListener {
                Log.i("miApp", "datos enviados correctamente")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    fun showDialog() {
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.fragment_calendar)

        //set preferences
        val calendar = dialog.findViewById<DatePicker>(R.id.calendarPicker)
        val acceptBtn = dialog.findViewById<Button>(R.id.acceptBtn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)

        acceptBtn.setOnClickListener() {
            val selectedDay = calendar.dayOfMonth
            val selectedMonth = calendar.month
            val selectedYear = calendar.year
            changeDate(selectedDay, selectedMonth, selectedYear)
            Log.v("miapp", "el dia del mes es: $selectedDay")
            dialog.dismiss()

        }

        cancelBtn.setOnClickListener() {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun changeDate(day: Int, monthArray: Int, year: Int) {
        val dateTextView2 = findViewById<TextView>(R.id.dateText2)
        val month = monthArray + 1
        dateTextView2.text = "$day-$month-$year"
    }
}
