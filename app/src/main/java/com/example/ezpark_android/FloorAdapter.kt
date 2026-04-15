package com.example.ezpark_android

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FloorAdapter(
    private val floors: List<String>,
    private val onFloorSelected: (String) -> Unit
) : RecyclerView.Adapter<FloorAdapter.FloorViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_ID.toInt()

    inner class FloorViewHolder(val tvFloor: TextView) : RecyclerView.ViewHolder(tvFloor) {
        fun bind(floor: String, isSelected: Boolean) {
            tvFloor.text = floor
            tvFloor.isSelected = isSelected
            tvFloor.setTextSize(TypedValue.COMPLEX_UNIT_SP, if (isSelected) 16f else 14f)
            tvFloor.setOnClickListener {
                val prev = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(prev)
                notifyItemChanged(selectedPosition)
                onFloorSelected(floor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloorViewHolder {
        val tv = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_floor_button, parent, false) as TextView
        return FloorViewHolder(tv)
    }

    override fun onBindViewHolder(holder: FloorViewHolder, position: Int) {
        holder.bind(floors[position], position == selectedPosition)
    }

    override fun getItemCount() = floors.size
}
