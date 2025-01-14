package com.learning.careerconnect.Adapter.ProfileAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.careerconnect.Model.LoginOM
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.EducationLayoutBinding

class EducationDisplayAdapter(
    private val items: List<LoginOM.Data.Data.Education?>?,
    private val context: Context
) : RecyclerView.Adapter<EducationDisplayAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EducationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        if (item != null) {
            holder.binding.cgpa.text = "CGPA :"+item.CGPA
        }
        if (item != null) {
            holder.binding.gradYear.text = "Grad Year :"+item.graduationYear
        }
        Glide.with(context)
            .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/clg.jpg")
            .placeholder(R.drawable.career_connect_white_bg)
            .into(holder.binding.clgIV)

        if (item != null) {
            holder.binding.clgName.text = item.collegeName
        }
        if (item != null) {
            holder.binding.courcename.text = item.course
        }
    }
    override fun getItemCount(): Int {
        return items?.size ?: 0
    }
    class ViewHolder(val binding: EducationLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}