package com.android.akl.bluetoothscreamer.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.android.akl.bluetoothscreamer.R
import android.widget.TextView
import java.util.ArrayList

class BTNamesRecyclerAdapter(context: Context?, names1: List<String>?) : RecyclerView.Adapter<BTNamesRecyclerAdapter.ViewHolder>() {

    private val names: MutableList<String> = names1 as MutableList<String>
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = mLayoutInflater.inflate(R.layout.bt_name_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bTNameTextView.text = names[i]
    }

    override fun getItemCount(): Int {
        return names.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bTNameTextView: TextView = itemView.findViewById(R.id.bt_device_name_tv)
        var mButton: Button = itemView.findViewById(R.id.edit_device_edit_text)
    }
}