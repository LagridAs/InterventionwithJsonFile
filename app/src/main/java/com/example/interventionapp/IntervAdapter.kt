package com.example.interventionapp

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView


class IntervAdapter(private val dataList:MutableList<Intervention>):RecyclerView.Adapter<IntervAdapter.HolderInter>() {

    var onItemClick: ((Intervention) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderInter {
        val inflater = LayoutInflater.from(parent.context)
        return HolderInter(inflater, parent)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: HolderInter, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }



    /***********************HolderView**********************/

    inner class HolderInter(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_interv_view,
                parent, false
            )
        ) {

        private var numTextView:TextView?=null
        private var tyTextView:TextView?=null
        private var detBtn:Button?=null

        init {
            tyTextView=itemView.findViewById(R.id.interType)
            numTextView=itemView.findViewById(R.id.interNum)
            detBtn= itemView.findViewById(R.id.detailsBtn)
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(interv:Intervention) {
            tyTextView?.text= interv.Type?.intitule
            numTextView?.text= "Intervention: ${interv.numero}"
            detBtn?.setOnClickListener {
                onItemClick?.invoke(interv)
            }
        }

    }
}