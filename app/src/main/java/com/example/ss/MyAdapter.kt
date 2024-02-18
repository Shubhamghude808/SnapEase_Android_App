package com.example.ss

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val userlist: ArrayList<Userdata>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.history_item,
            parent,false)

        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem=userlist[position]
        holder.hist.text= currentitem.hist

    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val hist: TextView=itemView.findViewById(R.id.item)

    }

}