package com.cliffdevops.tronics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceAdapter(private val deviceList: List<DeviceItem>): RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_recycleview,
            parent, false)
        return DeviceViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val currentItem = deviceList[position]
        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView1.text = currentItem.name
        holder.textView2.text = currentItem.serial
    }

    override fun getItemCount() = deviceList.size
    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.deviceImage)
        val textView1: TextView = itemView.findViewById(R.id.lbldeviceName)
        val textView2: TextView = itemView.findViewById(R.id.lblSerial)
    }

}