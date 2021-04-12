package com.example.cheerup2120.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cheerup2120.Models.Analytics
import com.example.cheerup2120.R
import java.util.zip.Inflater

class AnalyticsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items: List<Analytics> = ArrayList()

    fun setData(analyticsList: List<Analytics>){
        items = analyticsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnalyticsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_analytics_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is AnalyticsViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class AnalyticsViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val fio: TextView = itemView.findViewById(R.id.tvItemFIOAnalytics)
        val imgv: ImageView = itemView.findViewById(R.id.imgvItemAnalytics)

        fun bind(analytics: Analytics){
            fio.text = analytics.fio
//            imgv.setImageResource(analytics.image)
        }
    }
}