package com.learning.careerconnect.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.careerconnect.databinding.JobLongTextNoRecycleViewBinding

class JobResponsibilitiesAdapter(
    private val items: ArrayList<String>
) : RecyclerView.Adapter<JobResponsibilitiesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = JobLongTextNoRecycleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.corespondingText.text = item
        holder.binding.number.text = "${position+1}"
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class ViewHolder(val binding: JobLongTextNoRecycleViewBinding) : RecyclerView.ViewHolder(binding.root)
}