package com.example.outlaycalc.interfaces

import com.example.outlaycalc.models.Movement

interface CustomItemListener {
    fun onItemClick(selectedMovement: Movement, docRef: String)
}