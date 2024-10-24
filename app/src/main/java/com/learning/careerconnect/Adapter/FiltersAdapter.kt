package com.learning.careerconnect.Adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableResource
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.GridViewBinding

class FiltersAdapter(
    private val items: ArrayList<String>
) : RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GridViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.gridViewTextView.text = item

        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, item)

            if (holder.binding.gridViewTextView.background.constantState ==
                ContextCompat.getDrawable(holder.binding.gridViewTextView.context, R.drawable.main_colour_border_bg_btn_inside_grey)?.constantState) {
                holder.binding.gridViewTextView.setBackgroundResource(R.drawable.filter_option_bg_white_colour)
            } else {
                holder.binding.gridViewTextView.setBackgroundResource(R.drawable.main_colour_border_bg_btn_inside_grey)
            }

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
    class ViewHolder(val binding: GridViewBinding) : RecyclerView.ViewHolder(binding.root)
}