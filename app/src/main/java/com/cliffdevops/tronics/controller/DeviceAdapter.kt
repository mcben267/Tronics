package com.cliffdevops.tronics.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cliffdevops.tronics.R
import com.cliffdevops.tronics.model.DeviceItem
import com.squareup.picasso.Picasso

class DeviceAdapter(
    private val deviceList: List<DeviceItem>,
    private val itemListener: OnItemClickListener
) :
    RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.device_recycleview,
            parent, false
        )
        return DeviceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val currentItem = deviceList[position]
        holder.textView1.text = currentItem.name
        holder.textView2.text = currentItem.serial
        holder.textView3.text = currentItem.vendor
        Picasso.get().load(currentItem.imageResource)
            .placeholder(R.drawable.ic_account)
            .into(holder.imageView);
    }

    override fun getItemCount() = deviceList.size
    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.deviceImage)
        val textView1: TextView = itemView.findViewById(R.id.lbldeviceName)
        val textView2: TextView = itemView.findViewById(R.id.lblSerial)
        val textView3: TextView = itemView.findViewById(R.id.lblVendor)

        init {
            itemView.setOnClickListener(this)
//
            //}
        }

        override fun onClick(v: View?) {
            val deviceList: List<DeviceItem>
            val position: Int = adapterPosition

            if (position != RecyclerView.NO_POSITION) {
                itemListener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}