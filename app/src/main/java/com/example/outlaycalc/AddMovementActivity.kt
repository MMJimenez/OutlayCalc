package com.example.outlaycalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.outlaycalc.models.Movement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_movement.*

class AddMovementActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth// ...

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
            }
    }
}
