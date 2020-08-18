package com.example.outlaycalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.outlaycalc.models.Movement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mod_movement.*
import java.math.BigDecimal
import java.math.RoundingMode

class ModMovementActivity : AppCompatActivity() {

    val TAG = "recivedDoc"

    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_movement)

        auth = FirebaseAuth.getInstance()

        val bundle: Bundle? = intent.extras
        val recivedDocRef: String? = bundle?.getString("intentDocRef")

        Log.d("GetId", "$recivedDocRef dentro de ModMovement")

        recivedDocRef?.let {
            getMovement(it)
        }
    }

    fun getMovement(docId: String) {
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

        if (modMovement.outlay) {
            modRadioOutlay.isChecked = true
        } else {
            modRadioIngress.isChecked = true
        }
    }


}