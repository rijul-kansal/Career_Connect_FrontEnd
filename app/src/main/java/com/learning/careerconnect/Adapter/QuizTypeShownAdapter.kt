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
        "AWS" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/aws.png",
        "CPP" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/cpp.png",
        "AI" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/ai.avif",
        "ML" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/ml.png",
        "JavaScript" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/js.png",
        "SQL" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/sql.png",
        "Android App development" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/androidappdev.png",
        "PYTHON" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/python.jpg",
        "JAVA" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/java.png",
        "Cyber Security" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/cs.jpg",
        "BLOCKCHAIN" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/block+chain.jpg",
        "IOT" to "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/iot.png",
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