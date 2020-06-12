package com.example.outlaycalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.outlaycalc.adapters.MovementsAdapter
import com.example.outlaycalc.interfaces.AmountSum
import com.example.outlaycalc.interfaces.EditMovements
import com.example.outlaycalc.models.Movement
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity(),
    AmountSum, EditMovements {


    lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    lateinit var fireAdapter: FirestoreRecyclerAdapter<Movement, MovementsAdapter.MovementsViewHolder>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuLogOut -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, SingInActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        auth = FirebaseAuth.getInstance()

        val query = db.collection("users").document(auth.currentUser!!.uid).collection("movements").orderBy("date", Query.Direction.DESCENDING)

        val fireOptions =
            FirestoreRecyclerOptions.Builder<Movement>().setQuery(query, Movement::class.java).build()

        val mLay = LinearLayoutManager(this)
        mainRecyclerView.layoutManager = mLay

        fireAdapter = MovementsAdapter(fireOptions)
        mainRecyclerView.adapter = fireAdapter

        (fireAdapter as MovementsAdapter).sum = this

        floating_action_button.setOnClickListener{
            startActivity(Intent(this, AddMovementActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        fireAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        fireAdapter.stopListening()
    }

    override fun sumatory(money: Double) {
        val decimal = BigDecimal(money).setScale(2, RoundingMode.HALF_EVEN)
        txtSumMovements.text = "$decimal €"
        Log.e("miApp", "sumMovements $decimal")
    }

    override fun edit(position: Int) {
        Log.v("miApp", "La celda desde el MainActivity es: $position")
    }

}
