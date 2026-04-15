package com.example.ezpark_android

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView

class ParkingTypeAdapter(
    private val types: List<ParkingType>,
    private val onTypeSelected: (ParkingType) -> Unit
) : RecyclerView.Adapter<ParkingTypeAdapter.ParkingTypeViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_ID.toInt()

    inner class ParkingTypeViewHolder(val root: FrameLayout) : RecyclerView.ViewHolder(root) {
        val ivIcon: ImageView = root.findViewById(R.id.ivParkingTypeIcon)

        fun bind(type: ParkingType, isSelected: Boolean) {
            root.isSelected = isSelected

            if (type.iconRes != null) {
                ivIcon.setImageResource(type.iconRes)
                val tintColor = if (isSelected) {
                    ContextCompat.getColor(root.context, R.color.green)
                } else {
                    ContextCompat.getColor(root.context, R.color.gray_60)
                }
                ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(tintColor))
                ivIcon.visibility = android.view.View.VISIBLE
            } else {
                ivIcon.visibility = android.view.View.GONE
            }

            root.setOnClickListener {
                val prev = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(prev)
                notifyItemChanged(selectedPosition)
                onTypeSelected(type)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingTypeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parking_type, parent, false) as FrameLayout
        return ParkingTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParkingTypeViewHolder, position: Int) {
        holder.bind(types[position], position == selectedPosition)
    }

    override fun getItemCount() = types.size
}
