package com.example.outlaycalc.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.outlaycalc.R
import com.example.outlaycalc.interfaces.CustomItemListener
import com.example.outlaycalc.models.Movement
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_movement.view.*
import kotlinx.android.synthetic.main.item_movement.view.iconOutlay
import kotlinx.android.synthetic.main.item_movement.view.txtAmount
import kotlinx.android.synthetic.main.item_movement.view.txtDesiption
import kotlinx.android.synthetic.main.item_movement_3.view.*
import java.text.SimpleDateFormat
import java.util.*

class MovementsAdapter(
    recyclerOptions: FirestoreRecyclerOptions<Movement>, val clickAction: CustomItemListener
) :
    FirestoreRecyclerAdapter<Movement, MovementsAdapter.MovementsViewHolder>(recyclerOptions) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movement_3, parent, false)
        return MovementsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovementsAdapter.MovementsViewHolder,
        position: Int,
        model: Movement
    ) {
        holder.bindData(model)

        holder.itemView.setOnClickListener {
            val docId = snapshots.getSnapshot(position).id
            Log.d("GetId", docId)
            clickAction.onItemClick(model, docId)
        }

    }

    inner class MovementsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindData(movement: Movement) {

            itemView.txtDesiption.text = movement.description
            itemView.txtAmount.text = "${movement.amount} €"
            itemView.txtDate.text = dateToString(movement.date)

            if (!movement.outlay) {
                itemView.iconOutlay.setImageResource(R.drawable.ic_arrow_right)
            } else {
                itemView.iconOutlay.setImageResource(R.drawable.ic_arrow_left)
            }
        }
    }

    fun dateToString(unformattedDate: Date): String {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val formatedDate = simpleDateFormat.format(unformattedDate)

        return formatedDate
    }
}