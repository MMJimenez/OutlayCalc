package com.example.outlaycalc.adapters

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.outlaycalc.AmountSum
import com.example.outlaycalc.R
import com.example.outlaycalc.models.Movement
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_movement.*
import kotlinx.android.synthetic.main.item_movement.view.*
import kotlinx.android.synthetic.main.item_movement.view.iconOutlay

class MovementsAdapter(
    recyclerOptions: FirestoreRecyclerOptions<Movement>
) :
    FirestoreRecyclerAdapter<Movement, MovementsAdapter.MovementsViewHolder>(recyclerOptions) {

    var sum: AmountSum? = null
    var sumMovements = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movement, parent, false)
        return MovementsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovementsAdapter.MovementsViewHolder,
        position: Int,
        model: Movement
    ) {
        holder.bindData(model)

        holder.itemView.setOnClickListener {
            val documentId = snapshots.getSnapshot(position).id
        }
    }

    inner class MovementsViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bindData(movement: Movement) {
            itemView.txtDesiption.text = movement.description
            itemView.txtAmount.text = "${movement.amount} €"

            if (movement.outlay == false) {
                itemView.iconOutlay.setImageResource(R.drawable.ic_arrow_right)
                sumMovements += movement.amount!!
                sum?.sumatory(sumMovements)
                Log.v("miApp", "La suma es $sumMovements")
            } else {
                sumMovements -= movement.amount!!
                sum?.sumatory(sumMovements)
                Log.v("miApp", "La suma es $sumMovements")
            }




        }
    }
}