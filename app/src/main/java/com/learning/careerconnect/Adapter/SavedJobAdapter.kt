package com.learning.careerconnect.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.careerconnect.Model.GetAllSavedLaterJobsOM
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.AppliedJobsRecycleViewBinding
import com.learning.careerconnect.databinding.LoadingProgressBarBinding

private const val FOOTER_VIEW_TYPE = 2
class SavedJobAdapter(
    private val items: ArrayList<GetAllSavedLaterJobsOM.Data.Data.JobId?>,
    private val context: Context
) : ListAdapter<GetAllSavedLaterJobsOM.Data.Data.JobId, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<GetAllSavedLaterJobsOM.Data.Data.JobId?>() {
        override fun areItemsTheSame(
            oldItem: GetAllSavedLaterJobsOM.Data.Data.JobId,
            newItem: GetAllSavedLaterJobsOM.Data.Data.JobId
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: GetAllSavedLaterJobsOM.Data.Data.JobId,
            newItem: GetAllSavedLaterJobsOM.Data.Data.JobId
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

        fun bind(item: GetAllSavedLaterJobsOM.Data.Data.JobId?) {


            binding.nameOfRole.text = item!!.nameOfRole
            binding.nameOfCompany.text = item!!.nameOfCompany
            binding.status.visibility = View.INVISIBLE
            Log.d("rk", "link ${item!!.companyLinks?.get(1)!!.link}\n")
            Glide.with(context)
                .load(item!!.companyLinks?.get(1)!!.link)
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.imageOfCompany)

            itemView.setOnClickListener {
                onClickListener?.onClick(adapterPosition, item)
            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: GetAllSavedLaterJobsOM.Data.Data.JobId)
    }
}