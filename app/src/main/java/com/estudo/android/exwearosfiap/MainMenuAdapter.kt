package com.estudo.android.exwearosfiap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainMenuAdapter(
    context: Context,
    dataArgs: ArrayList<MenuItem>,
    callback: AdapterCallback?
) :
    RecyclerView.Adapter<MainMenuAdapter.RecyclerViewHolder>() {
    private var dataSource = ArrayList<MenuItem>()

    interface AdapterCallback {
        fun onItemClicked(menuPosition: Int?)
    }

    private val callback: AdapterCallback?
    private val context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.main_menu_item, parent, false)
        return RecyclerViewHolder(view)
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var menuContainer: LinearLayout
        var menuItem: TextView

        init {
            menuContainer = view.findViewById(R.id.menu_container)
            menuItem = view.findViewById(R.id.menu_item)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerViewHolder,
        position: Int
    ) {
        val data_provider = dataSource[position]
        holder.menuItem.setText(data_provider.text)
        holder.menuContainer.setOnClickListener { callback?.onItemClicked(position) }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    init {
        this.context = context
        dataSource = dataArgs
        this.callback = callback
    }
}