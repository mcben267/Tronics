package com.cliffdevops.tronics.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cliffdevops.tronics.R
import com.cliffdevops.tronics.model.LogItem
import com.squareup.picasso.Picasso

class LogAdapter(
    private val logList: List<LogItem>,
    private val itemListener: OnItemClickListener
) :
    RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.logs_recycleview,
            parent, false
        )
        return LogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val currentItem = logList[position]
        holder.textView1.text = currentItem.name
        holder.textView2.text = currentItem.serial
        holder.textView3.text = currentItem.status
        holder.textView4.text = currentItem.timestamp
        Picasso.get().load(currentItem.imageResource)
            .placeholder(R.drawable.ic_account)
            .into(holder.imageView);
    }

    override fun getItemCount() = logList.size
    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.deviceImage)
        val textView1: TextView = itemView.findViewById(R.id.lbldeviceName)
        val textView2: TextView = itemView.findViewById(R.id.lblSerial)
        val textView3: TextView = itemView.findViewById(R.id.lblStatus)
        val textView4: TextView = itemView.findViewById(R.id.lblTimeStamp)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
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

