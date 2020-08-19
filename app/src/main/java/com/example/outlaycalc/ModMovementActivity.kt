package com.example.outlaycalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.outlaycalc.models.Movement
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_movement.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mod_movement.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

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

    private fun printMovementData(modMovement: Movement) {

        modInputTxtAmount.setText(modMovement.amount.toString())
        modInputTxtDescription.setText(modMovement.description)

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
        modMovement.date = dateOfMovement
        if (modRadioIngress.isChecked) {
            modMovement.outlay = false
        }
        docPath.set(modMovement)
            .addOnCompleteListener {
                Log.i("miApp", "ha modificado datos correctamente")
                startActivity(Intent(this, MainActivity::class.java))
            }
    }

    fun deleteMovement(docPath: DocumentReference) {
        docPath.delete()
            .addOnCompleteListener {
                startActivity(Intent(this, MainActivity::class.java))
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

}