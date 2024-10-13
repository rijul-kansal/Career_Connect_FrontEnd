package com.learning.careerconnect.Adapter

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.databinding.SearchAllJobAdapterViewBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.learning.careerconnect.R
import java.sql.Date
import java.text.SimpleDateFormat

class SearchJobAdapter(
    private val items: ArrayList<SearchAllJobsOM.Data.Data>,
    private val context: Context
) : RecyclerView.Adapter<SearchJobAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchAllJobAdapterViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        var location =""
        for(i in item.location!!)
        {
            location+=(i+",")
        }

        holder.binding.nameOfRole.text = item.nameOfRole
        holder.binding.nameOfCompany.text = item.nameOfCompany
        holder.binding.location.text = location
        holder.binding.typeOfJob.text = item.typeOfJob
        holder.binding.postedDate.text = item.postedDate?.let { " posted date"+convertLongToTime(it) }
        Glide
            .with(context)
            .load(item.companyLinks?.get(2)?.name)
            .placeholder(R.drawable.career_connect_white_bg)
            .into(holder.binding.imageOfCompany)
        // Set click listeners for different views
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }



    interface OnClickListener {
        fun onClick(position: Int, model: SearchAllJobsOM.Data.Data)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("YYYY.MM.DD")
        return format.format(date)
    }

    class ViewHolder(val binding: SearchAllJobAdapterViewBinding) : RecyclerView.ViewHolder(binding.root)
}