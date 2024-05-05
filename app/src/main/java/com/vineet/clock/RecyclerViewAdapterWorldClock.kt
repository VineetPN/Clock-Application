package com.vineet.clock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapterWorldClock(private val listZone: WorldClock.Companion, private val listTime: WorldClock.Companion ) :
    RecyclerView.Adapter<RecyclerViewAdapterWorldClock.viewHolder>() {

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTimeZone = itemView.findViewById<TextView>(R.id.text_view_country_timezone)
        val textViewTime = itemView.findViewById<TextView>(R.id.text_view_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_world_clock, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return listZone.listZone.count()
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapterWorldClock.viewHolder, position: Int) {
        holder.textViewTimeZone.text = listZone.listZone.get(position).toString()
        holder.textViewTime.text = listTime.listTime.get(position).toString()
    }
}