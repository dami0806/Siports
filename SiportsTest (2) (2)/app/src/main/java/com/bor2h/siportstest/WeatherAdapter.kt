package com.bor2h.siportstest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WeatherAdapter(private val context: Context) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    var datas = mutableListOf<WeatherData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_weather,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

        if (position == 0) {
            holder.day.setTextColor(ContextCompat.getColor(context, R.color.siports))
            holder.date.setTextColor(ContextCompat.getColor(context, R.color.siports))
        }

        if (holder.day.text == "일") {
            holder.day.setTextColor(ContextCompat.getColor(context, R.color.holiday))
            holder.date.setTextColor(ContextCompat.getColor(context, R.color.holiday))
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView = itemView.findViewById(R.id.tv_day)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        private val rainfallAm: TextView = itemView.findViewById(R.id.tv_rainfall_am)
        private val rainfallPm: TextView = itemView.findViewById(R.id.tv_rainfall_pm)
        private val imageAm: ImageView = itemView.findViewById(R.id.iv_weather_am)
        private val imagePm: ImageView = itemView.findViewById(R.id.iv_weather_pm)
        private val lowest: TextView = itemView.findViewById(R.id.tv_lowest_temperature)
        private val highest: TextView = itemView.findViewById(R.id.tv_highest_temperature)

        fun bind(item: WeatherData) {
            day.text = item.day
            date.text = item.date
            rainfallAm.text = item.rainfallAm
            rainfallPm.text = item.rainfallPm
            lowest.text = item.lowTemperature
            highest.text = item.highTemperature
            Glide.with(itemView).load(item.imageAm).into(imageAm)
            Glide.with(itemView).load(item.imagePm).into(imagePm)
        }
    }
}