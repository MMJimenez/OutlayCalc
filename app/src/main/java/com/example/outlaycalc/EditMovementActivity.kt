package com.example.outlaycalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import com.example.outlaycalc.models.Movement
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_movement.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import java.util.*

class EditMovementActivity : AppCompatActivity() {

    val TAG = "recivedDoc"

    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_movement)

        modInputTxtDate.showSoftInputOnFocus = false
        modInputTxtDate.setOnClickListener {
            showDialog()
        }

        auth = FirebaseAuth.getInstance()

        val bundle: Bundle? = intent.extras
        val recivedDocRef: String? = bundle?.getString("intentDocRef")

        Log.d("GetId", "$recivedDocRef dentro de ModMovement")

        recivedDocRef?.let {
            getMovement(it)
        }
    }


    private fun getMovement(docId: String) {
        val movementToModify =
            db.collection("users").document(auth.currentUser?.uid!!).collection("movements")
                .document(docId)
        movementToModify.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                    val movementObject = document.toObject(Movement::class.java)
                    movementObject?.let {

                        printMovementData(it)

                        btnModify.setOnClickListener {
                            setModifiedMovement(movementToModify, movementObject.date)
                        }
                        btnDelete.setOnClickListener {
                            deleteAlertDialog(movementToModify)
                        }
                        Log.d(TAG, "doc mutable: ${it.description}")
                    }

                } else {
                    Log.d(TAG, "No such document")
                }
            }
    }

    fun printMovementData(modMovement: Movement) {

        modInputTxtAmount.setText(modMovement.amount.toString())
        modInputTxtDescription.setText(modMovement.description)
        modInputTxtDate.setText(dateToString(modMovement.date))

        if (modMovement.outlay) {
            modRadioOutlay.isChecked = true
        } else {
            modRadioIngress.isChecked = true
        }
    }

    private fun setModifiedMovement(docPath: DocumentReference, dateOfMovement: Date) {

        val modMovement = Movement()
        modMovement.amount = modInputTxtAmount.text.toString().toFloat()
        modMovement.description = modInputTxtDescription.text.toString()
        modMovement.date = stringToDate(modInputTxtDate.text.toString())

        if (modRadioIngress.isChecked) {
            modMovement.outlay = false
        }
        docPath.set(modMovement)
            .addOnCompleteListener {
                Log.i(
                    "miApp",
                    "ha modificado datos correctamente ${modMovement.amount.toString()}, ${modMovement.description}, ${modMovement.date}"
                )
                startActivity(Intent(this, MainActivity::class.java))
            }
    }

    fun deleteMovement(docPath: DocumentReference) {
        docPath.delete()
            .addOnCompleteListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    fun deleteAlertDialog(docPath: DocumentReference) {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.title))
            .setMessage(resources.getString(R.string.supporting_text))
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
            }
            .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                deleteMovement(docPath)
            }
            .show()
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

            val selectedDate = "${formatday(selectedDay)}/${formatMonth(selectedMonth)}/$selectedYear"
            Log.v("miapp", "el dia del mes es: $selectedDate")

            modInputTxtDate.setText(selectedDate)

            dialog.dismiss()

        }

        cancelBtn.setOnClickListener() {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun formatMonth(arrayMonth: Int): String {
        var month: String
        when (arrayMonth) {
            0 -> month = "01"
            1 -> month = "02"
            2 -> month = "03"
            3 -> month = "04"
            4 -> month = "05"
            5 -> month = "06"
            6 -> month = "07"
            7 -> month = "08"
            8 -> month = "09"
            9 -> month = "10"
            10 -> month = "11"
            else -> {
                month = "12"
            }
        }
        return month
    }

    fun formatday(day: Int): String {
        var dayformatted = day.toString()
        if (dayformatted.length == 1) {
            dayformatted = "0$dayformatted"
        }

        return dayformatted
    }

}