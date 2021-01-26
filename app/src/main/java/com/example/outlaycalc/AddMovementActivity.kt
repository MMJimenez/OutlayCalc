package com.example.outlaycalc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.outlaycalc.models.Movement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_movement.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class AddMovementActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movement)


        auth = FirebaseAuth.getInstance()

        title = "Añade un Nuevo Gasto"

        val query = db.collection("users").document(auth.currentUser!!.uid).collection("movements")

        val editTextDate = findViewById<EditText>(R.id.inputTxtDate)
        editTextDate.showSoftInputOnFocus = false


        val today = Date()
        showDate(dateToString(today))

        editTextDate.setOnClickListener {
            showDialog()
        }

        btnAdd.setOnClickListener {
            createMovement()
        }
    }

    fun createMovement() {
        val newMovement = Movement()
        newMovement.amount = inputTxtAmount.text.toString().toFloat()
        newMovement.description = inputTxtDescription.text.toString()
        newMovement.date = stringToDate(inputTxtDate.getText().toString())

        if (radioIngress.isChecked) {
            newMovement.outlay = false
        }
        Log.v(
            "miapp",
            "createMovement: ${newMovement.amount}, ${newMovement.description}, ${newMovement.outlay}, ${newMovement.date}"
        )
        db.collection("users").document(auth.currentUser?.uid!!).collection("movements")
            .add(newMovement)
            .addOnCompleteListener {
                Log.i("miApp", "datos enviados correctamente")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    fun dateToString(unformattedDate: Date): String {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val formatedDate = simpleDateFormat.format(unformattedDate)

        return formatedDate
    }

    fun stringToDate(dateOnString: String): Date {

        val defaultZoneId = ZoneId.systemDefault()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
        val dateOnLocalDate = LocalDate.parse(dateOnString, formatter)

        val dateOnDate = Date.from(dateOnLocalDate.atStartOfDay(defaultZoneId).toInstant())

        return dateOnDate
    }

        fun showDate(date: String) {
            if (date == null) {
                val date = Date()
            }
            inputTxtDate.setText(date)
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

            //ESTO ABRIA QUE CAMBIARLO
            val month = selectedMonth + 1
            val selectedDate = "$selectedDay/$month/$selectedYear"
            Log.v("miapp", "el dia del mes es: $selectedDate")

            inputTxtDate.setText(selectedDate)

            dialog.dismiss()

        }

        cancelBtn.setOnClickListener() {
            dialog.dismiss()
        }

        dialog.show()
    }
}
