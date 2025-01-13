package com.learning.careerconnect.Adapter.ProfileAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.learning.careerconnect.databinding.PlusAndDoneBtnBinding
import com.learning.careerconnect.databinding.TextDeleteLayoutBinding


private const val FOOTER_VIEW_TYPE = 2
class LanguageKnownAdapterDialog(
    private val items: ArrayList<String>
) : ListAdapter<String, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem== newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }

    private var onClickListener: OnClickListener? = null


    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) {
            FOOTER_VIEW_TYPE
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FOOTER_VIEW_TYPE) {
            val binding = PlusAndDoneBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FooterViewHolder(binding)
        } else {
            val binding = TextDeleteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GeneralViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GeneralViewHolder) {
            val item = items[position]
            holder.bind(item)
        } else if (holder is FooterViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1 // Add one for the footer
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }



    inner class FooterViewHolder(private val binding: PlusAndDoneBtnBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.plusSignIV.setOnClickListener {
                onClickListener?.onClick(null,"addContent")
            }

            binding.doneBtn.setOnClickListener {
                onClickListener?.onClick(null,"DoneBtn")
            }
        }
    }

    inner class GeneralViewHolder(private val binding: TextDeleteLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {

            binding.textView.text = "${adapterPosition+1}) $item"
            binding.imageViewDel.setOnClickListener {
                onClickListener?.onClick(adapterPosition,item)
            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int?, model: String)
    }
}