package com.learning.careerconnect.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.careerconnect.Model.GetAllAppliedJobsOM
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.AppliedJobsRecycleViewBinding
import com.learning.careerconnect.databinding.LoadingProgressBarBinding

private const val FOOTER_VIEW_TYPE = 2
class AppliedJobAdapter(
    private val items: ArrayList<GetAllAppliedJobsOM.Data.Data?>,
    private val context:Context
) : ListAdapter<GetAllAppliedJobsOM.Data.Data, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<GetAllAppliedJobsOM.Data.Data>() {
        override fun areItemsTheSame(
            oldItem: GetAllAppliedJobsOM.Data.Data,
            newItem: GetAllAppliedJobsOM.Data.Data
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: GetAllAppliedJobsOM.Data.Data,
            newItem: GetAllAppliedJobsOM.Data.Data
        ): Boolean {
            return oldItem == newItem
        }
    }

    private var onClickListener: OnClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) {
            FOOTER_VIEW_TYPE
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FOOTER_VIEW_TYPE) {
            val binding = LoadingProgressBarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            FooterViewHolder(binding)
        } else {
            val binding = AppliedJobsRecycleViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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
        return items.size
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }


    inner class FooterViewHolder(binding: LoadingProgressBarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    inner class GeneralViewHolder(private val binding: AppliedJobsRecycleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GetAllAppliedJobsOM.Data.Data?) {


            binding.nameOfRole.text = item!!.jobAppliedId!!.nameOfRole
            binding.nameOfCompany.text = item!!.jobAppliedId!!.nameOfCompany
            binding.status.text = "Status: "+item.type
            Log.d("rk","link ${item.jobAppliedId!!.companyLinks?.get(1)!!.link}\n")
            Glide.with(context)
                .load(item.jobAppliedId!!.companyLinks?.get(1)!!.link)
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.imageOfCompany)

            itemView.setOnClickListener {
                onClickListener?.onClick(adapterPosition, item)
            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: GetAllAppliedJobsOM.Data.Data)
    }
}