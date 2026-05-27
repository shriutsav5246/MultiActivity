package com.utsav.multiactivity

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class UtilityAdapter(
    private val items: List<UtilityItem>
) : RecyclerView.Adapter<UtilityAdapter.UtilityViewHolder>() {

    class UtilityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view as MaterialCardView
        val container: LinearLayout = view.findViewById(R.id.cardContainer)
        val title: TextView = view.findViewById(R.id.tvUtilityTitle)
        val subtitle: TextView = view.findViewById(R.id.tvUtilitySubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UtilityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_utility_card, parent, false)

        return UtilityViewHolder(view)
    }

    override fun onBindViewHolder(holder: UtilityViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title
        holder.subtitle.text = item.subtitle

        holder.title.setTextColor(Color.parseColor(item.color))
        holder.card.strokeColor = Color.parseColor(item.color)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, item.targetActivity)
            holder.itemView.context.startActivity(intent)
        }

        applyFloatingAnimation(holder, position)
    }

    override fun getItemCount(): Int = items.size

    private fun applyFloatingAnimation(
        holder: UtilityViewHolder,
        position: Int
    ) {
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 120f
        holder.itemView.scaleX = 0.94f
        holder.itemView.scaleY = 0.94f

        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(700)
            .setStartDelay((position * 90).toLong())
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
}