package com.example.outlaycalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_mod_movement.*

class ModMovementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_movement)

        val bundle: Bundle? = intent.extras
        val recivedDocRef: String? = bundle?.getString("intentDocRef")

        //btnLog.setOnClickListener {
            Log.d("GetId", "$recivedDocRef dentro de ModMovement")
        //}
    }
}