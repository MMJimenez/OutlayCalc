package com.example.outlaycalc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.outlaycalc.R
import com.example.outlaycalc.models.Movement
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_movement.view.*

class MovementsAdapter(
    recyclerOptions: FirestoreRecyclerOptions<Movement>
) :
    FirestoreRecyclerAdapter<Movement, MovementsAdapter.MovementsViewHolder>(recyclerOptions) {

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

    }

    inner class MovementsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindData(movement: Movement) {
            itemView.txtDesiption.text = movement.description
            itemView.txtAmount.text = "${movement.amount} €"

            if (!movement.outlay) {
                itemView.iconOutlay.setImageResource(R.drawable.ic_arrow_right)
            } else {
                itemView.iconOutlay.setImageResource(R.drawable.ic_arrow_left)
            }
        }
    }
}