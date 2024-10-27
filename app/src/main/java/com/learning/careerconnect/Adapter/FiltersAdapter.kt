package com.learning.careerconnect.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.GridViewBinding

class FiltersAdapter(
    private val items: ArrayList<String>,
    private val sets: MutableSet<Int>,
    private val type:String = "notTime"
) : RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    var currentPos = -1
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

        if ((type == "Time" && position == currentPos) || (type == "Time" && sets.contains(position))) {
            holder.binding.gridViewTextView.setBackgroundResource(R.drawable.filter_option_bg_greu_colour)
            currentPos=position
        } else {
            holder.binding.gridViewTextView.setBackgroundResource(R.drawable.filter_option_bg_white_colour)
        }
        if (type != "Time" && sets.contains(position)) {
            holder.binding.gridViewTextView.setBackgroundResource(R.drawable.filter_option_bg_greu_colour)
        }
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, item)

            if (type == "Time") {

                if (currentPos == position) {
                    currentPos = -1
                } else {
                    val prevPos = currentPos
                    currentPos = position
                    notifyItemChanged(prevPos)
                }
                notifyItemChanged(position)
            }
            else
            {
                if (holder.binding.gridViewTextView.background.constantState ==
                    ContextCompat.getDrawable(holder.binding.gridViewTextView.context, R.drawable.filter_option_bg_greu_colour)?.constantState) {
                    holder.binding.gridViewTextView.setBackgroundResource(R.drawable.filter_option_bg_white_colour)
                } else {
                    holder.binding.gridViewTextView.setBackgroundResource(R.drawable.filter_option_bg_greu_colour)
                }
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