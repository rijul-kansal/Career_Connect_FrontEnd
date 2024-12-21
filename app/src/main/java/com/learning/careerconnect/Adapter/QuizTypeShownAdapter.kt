package com.learning.careerconnect.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.QuizTypeShownUiBinding


class QuizTypeShownAdapter(
    private val items: ArrayList<String>,
    private val context: Context,
) : RecyclerView.Adapter<QuizTypeShownAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    val map = mapOf(
        "Python" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/python.png",
        "Java" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/java.png",
        "C++" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/cpp.jpg",
        "AWS" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/aws.png",
        "AndroidAppDevelopment" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/android.jpeg",
        "IoT" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/iot.png",
        "Machine Learning" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/ml.jpg",
        "Blockchain" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/block+chain.jpeg"
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuizTypeShownUiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide
            .with(context)
            .load(map[item])
            .placeholder(R.drawable.career_connect_white_bg)
            .into(holder.binding.imageView)
        holder.binding.root.setOnClickListener {
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
        fun onClick(position: Int, model: String)
    }
    class ViewHolder(val binding: QuizTypeShownUiBinding) : RecyclerView.ViewHolder(binding.root)
}