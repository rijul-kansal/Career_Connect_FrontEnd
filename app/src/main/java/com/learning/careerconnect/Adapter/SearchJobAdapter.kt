package com.learning.careerconnect.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.learning.careerconnect.Model.SearchJobOM
import com.learning.careerconnect.databinding.SearchAllJobAdapterViewBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.learning.careerconnect.R
import java.sql.Date
import java.text.SimpleDateFormat

class SearchJobAdapter(
    private val items: ArrayList<SearchJobOM.Data.Data>,
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
        holder.binding.postedDate.text = item.postedDate?.let { convertLongToTime(it) }
        holder.binding.noOfApplicant.text = item.noOfStudentsApplied.toString()
        Glide
            .with(context)
            .load(item.companyLinks?.get(2)?.name)
            .placeholder(R.drawable.career_connect_white_bg)
            .into(holder.binding.imageOfCompany)
        if(item.typeOfJob == "Internship" || item.typeOfJob == "Contract")
        {
            holder.binding.stipned.text = item.costToCompany
            holder.binding.duration.text = item.durationOfInternship
        }
        else
        {
            holder.binding.stipned.visibility= View.INVISIBLE
            holder.binding.duration.visibility= View.INVISIBLE
        }
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
        fun onClick(position: Int, model: SearchJobOM.Data.Data)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("YYYY.MM.DD")
        return format.format(date)
    }

    class ViewHolder(val binding: SearchAllJobAdapterViewBinding) : RecyclerView.ViewHolder(binding.root)
}