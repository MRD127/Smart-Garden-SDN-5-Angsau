package com.example.smartgarden

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val data: MutableList<HistoryItem>,
    private val onDeleteClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val cahaya: TextView = itemView.findViewById(R.id.tvCahaya)
        val kelembaban: TextView = itemView.findViewById(R.id.tvKelembaban)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = data[position]
        holder.tanggal.text = "Tanggal: ${item.tanggal}"
        holder.cahaya.text = "Intensitas cahaya: ${item.intensitasCahaya}"
        holder.kelembaban.text = "Kelembaban tanah: ${item.kelembabanTanah}"

        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }

    override fun getItemCount(): Int = data.size
}
