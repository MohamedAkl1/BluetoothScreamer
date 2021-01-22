package com.android.akl.bluetoothscreamer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.akl.bluetoothscreamer.R
import com.android.akl.bluetoothscreamer.adapter.WelcomeRecyclerAdapter.WelcomeViewHolder
import java.util.*

/**
 * Created by Mohamed Akl on 8/22/2018.
 */
class WelcomeRecyclerAdapter(context: Context, devicesNames: Set<String>?) : RecyclerView.Adapter<WelcomeViewHolder>() {

    private val names: MutableList<String> = devicesNames!!.toMutableList()
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context
    var selectedNames: MutableList<String> = ArrayList()
    var notSelectedNames: MutableList<String> = devicesNames!!.toMutableList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): WelcomeViewHolder {
        val view = mLayoutInflater.inflate(R.layout.welcome_recycler_item, viewGroup, false)
        return WelcomeViewHolder(view)
    }

    override fun onBindViewHolder(welcomeViewHolder: WelcomeViewHolder, i: Int) {
        welcomeViewHolder.pairedName.text = names.get(i)
        Toast.makeText(mContext, "added " + names[i], Toast.LENGTH_SHORT).show()
        welcomeViewHolder.addButton.setOnClickListener(View.OnClickListener {
            if ((welcomeViewHolder.addButton.text.toString().trim { it <= ' ' } == mContext.resources.getString(R.string.welcome_dialog_add_button))) {
                selectedNames.add(names[welcomeViewHolder.adapterPosition])
                Toast.makeText(mContext, "added", Toast.LENGTH_SHORT).show()
                welcomeViewHolder.addButton.setText(R.string.added)
                notSelectedNames.remove(names[welcomeViewHolder.adapterPosition])
            } else {
                welcomeViewHolder.addButton.setText(R.string.add)
                welcomeViewHolder.addButton.isEnabled = true
                Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show()
                selectedNames.removeAt(welcomeViewHolder.adapterPosition)
                notSelectedNames.add(names[welcomeViewHolder.adapterPosition])
            }
        })
    }

    override fun getItemCount(): Int {
        return names.size
    }

    inner class WelcomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pairedName: TextView = itemView.findViewById(R.id.welcome_dialog_bt_name_tv)
        var addButton: Button = itemView.findViewById(R.id.welcome_add_button)

    }
}