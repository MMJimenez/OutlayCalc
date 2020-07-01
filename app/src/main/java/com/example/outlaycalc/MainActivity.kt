package com.example.outlaycalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.outlaycalc.adapters.MovementsAdapter
import com.example.outlaycalc.models.Movement
import com.example.outlaycalc.models.MovementsList
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    lateinit var fireAdapter: FirestoreRecyclerAdapter<Movement, MovementsAdapter.MovementsViewHolder>
    var TAG = "miApp"

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

        val query = db.collection("users").document(auth.currentUser!!.uid).collection("movements")
            .orderBy("date", Query.Direction.DESCENDING)

        val fireOptions =
            FirestoreRecyclerOptions.Builder<Movement>().setQuery(query, Movement::class.java)
                .build()

        val mLay = LinearLayoutManager(this)
        mainRecyclerView.layoutManager = mLay

        fireAdapter = MovementsAdapter(fireOptions)
        mainRecyclerView.adapter = fireAdapter

        //(fireAdapter as MovementsAdapter).sum = this

        floating_action_button.setOnClickListener {
            startActivity(Intent(this, AddMovementActivity::class.java))
            var dataMovement = db.collection("users").document(auth.currentUser!!.uid)

            Log.v("miApp", "dataMovement: ${dataMovement}")


        }

        //Add Movements from FireBase to Array
        getCollections()


    }

    override fun onStart() {
        super.onStart()
        fireAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        fireAdapter.stopListening()
    }

    fun totalAmount() {
        var amountsArray = arrayListOf<Movement>()
    }

    fun getCollections() {

        var money: Double = 0.0

        var myMovement: Movement? = null
        var myMovementsList: MovementsList? = null

        var movementQuery =
            db.collection("users").document(auth.currentUser!!.uid).collection("movements")
        movementQuery.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.v(TAG, "${document.id} => ${document.data}")

                    val movementObject = document.toObject(Movement::class.java)
                    Log.v(TAG, "documentObject: ${movementObject.outlay.toString()}")

                    if (movementObject.outlay) {
                        movementObject.amount?.let { money -= it }
                        Log.e(TAG, "Ha entrado por outlays: ${movementObject.amount} money: $money")
                    } else {
                        movementObject.amount?.let { money += it }
                        Log.e(TAG, "Ha entrado por ingress: ${movementObject.amount} money: $money")
                    }
                }
                txtSumMovements.text = money.toString()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }
}
