package com.vineet.clock

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapterNewTimeZoneAddition(private val listOfTimeZone: MainActivity.Companion) :
    RecyclerView.Adapter<RecyclerViewAdapterNewTimeZoneAddition.viewHolder>() {
    private val listener: IRecyclerViewCommunicator? = null

    companion object {
        var isSelectionDone: Boolean = false
    }

    val finishActivityLiveData = MutableLiveData<Unit>()

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewAllTimeZones = itemView.findViewById<TextView>(R.id.text_view_new_time_addition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyler_view_new_time_zone_addition, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (listOfTimeZone.ListTimeZones != null) {
            listOfTimeZone.ListTimeZones?.size!!
        } else
            0
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.textViewAllTimeZones.text = listOfTimeZone.ListTimeZones?.get(position)
        holder.textViewAllTimeZones.setOnClickListener() {

            WorldClock.selectedTimeZone = listOfTimeZone.ListTimeZones?.get(position).toString()

            isSelectionDone = true

            finishActivityLiveData.value = Unit
        }
    }
}