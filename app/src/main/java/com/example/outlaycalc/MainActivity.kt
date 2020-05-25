package com.example.outlaycalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.outlaycalc.models.Movement
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {


    lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    lateinit var fireAdapter: FirestoreRecyclerAdapter<Movement, MovementsAdapter.MovementsViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
