package com.example.top_10_downloader_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class myAdapter (private val items:ArrayList<feedEntry>): RecyclerView.Adapter<myAdapter.MyViewHolder>(){
    class MyViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        val textvFeed:TextView= itemView.textv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      val itemView = LayoutInflater.from(parent.context).inflate(
          R.layout.item_row, parent, false
      )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val title = items[position].name
        holder.textvFeed.text= title
    }

    override fun getItemCount(): Int {
    return items.size
    }

}