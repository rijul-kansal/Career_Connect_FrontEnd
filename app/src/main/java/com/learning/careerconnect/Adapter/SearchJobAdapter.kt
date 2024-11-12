package com.learning.careerconnect.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.SearchAllJobAdapterViewBinding
import com.learning.careerconnect.databinding.SearchAllJobsFooterBinding
import java.sql.Date
import java.text.SimpleDateFormat


private const val FOOTER_VIEW_TYPE = 2

class SearchJobAdapter(
    private val items: ArrayList<SearchAllJobsOM.Data.Data>,
    private val context: Context,
    private val totalItem: Int,
    private val currpos:Int
) : ListAdapter<SearchAllJobsOM.Data.Data, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SearchAllJobsOM.Data.Data>() {
        override fun areItemsTheSame(
            oldItem: SearchAllJobsOM.Data.Data,
            newItem: SearchAllJobsOM.Data.Data
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: SearchAllJobsOM.Data.Data,
            newItem: SearchAllJobsOM.Data.Data
        ): Boolean {
            return oldItem == newItem
        }
    }

    private var onClickListener: OnClickListener? = null
    private var onClickListenerDots: OnClickListenerDots? = null
    private val dots: ArrayList<TextView> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) {
            FOOTER_VIEW_TYPE
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FOOTER_VIEW_TYPE) {
            val binding = SearchAllJobsFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            FooterViewHolder(binding)
        } else {
            val binding = SearchAllJobAdapterViewBinding.inflate(
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
            holder.bind(totalItem,currpos)
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1 // Add one for the footer
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    fun setOnClickListenerDots(listener: OnClickListenerDots?) {
        this.onClickListenerDots = listener
    }

    inner class FooterViewHolder(binding: SearchAllJobsFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val ll = binding.indicatorLayout
        private var backBtn = binding.backBtn
        private var forwardBtn = binding.forwardBtn
        fun bind(totalItems: Int, currpos:Int) {
            dots.clear()
            ll.removeAllViews()
            val totalDots = if (totalItems % 10 == 0) {
                totalItems / 10
            } else {
                (totalItems / 10) + 1
            }
            if(totalItems<=10)
            {
                backBtn.visibility = View.GONE
                forwardBtn.visibility = View.GONE
            }
            else if(currpos == 1)
            {
                backBtn.visibility = View.INVISIBLE
                forwardBtn.visibility = View.VISIBLE
            }
            else if(currpos == totalDots)
            {
                backBtn.visibility = View.VISIBLE
                forwardBtn.visibility = View.INVISIBLE
            }
            if (totalDots <= 5) {
                for (i in 0 until totalDots) {
                    val textView = createDotTextView("${i + 1}")
                    dots.add(textView)
                    ll.addView(textView)
                }
            }
            else {
                if(currpos<=3)
                {
                    for (i in 0 until 5) {
                        val textView = createDotTextView("${i + 1}")
                        dots.add(textView)
                        ll.addView(textView)
                    }
                    val textView = createDotTextView("...")
                    dots.add(textView)
                    val textView1 = createDotTextView("$totalDots")
                    dots.add(textView1)
                    ll.addView(textView)
                    ll.addView(textView1)
                }
                else if(currpos>=totalDots-4)
                {
                    val textView1 = createDotTextView("1")
                    dots.add(textView1)
                    ll.addView(textView1)
                    val textViewD = createDotTextView("...")
                    dots.add(textViewD)
                    ll.addView(textViewD)
                    for (i in totalDots-5 .. totalDots-1) {
                        val textView = createDotTextView("${i + 1}")
                        dots.add(textView)
                        ll.addView(textView)
                    }
                }
                else
                {
                    val textView1 = createDotTextView("1")
                    dots.add(textView1)
                    ll.addView(textView1)
                    val textViewD = createDotTextView("...")
                    dots.add(textViewD)
                    ll.addView(textViewD)
                    for (i in currpos-2 .. currpos) {
                        val textView = createDotTextView("${i + 1}")
                        dots.add(textView)
                        ll.addView(textView)
                    }
                    val textViewDD = createDotTextView("...")
                    dots.add(textViewDD)
                    ll.addView(textViewDD)
                    val textViewT = createDotTextView("$totalDots")
                    dots.add(textViewT)
                    ll.addView(textViewT)

                }
            }
            if(dots.isNotEmpty())
            {
                if(totalDots<=5)
                {
                    dots[currpos-1].setBackgroundResource(R.drawable.box_for_pagination_selected)
                }
                else
                {
                    if(currpos<=3)
                        dots[currpos-1].setBackgroundResource(R.drawable.box_for_pagination_selected)
                    else if(totalDots-5 <currpos)
                        dots[currpos-totalDots+6].setBackgroundResource(R.drawable.box_for_pagination_selected)
                    else
                        dots[3].setBackgroundResource(R.drawable.box_for_pagination_selected)
                }

            }
            dots.forEachIndexed { index, textView ->
                textView.setOnClickListener {
                    onClickListenerDots?.onClick(textView.text)
                }
            }

            forwardBtn.setOnClickListener {
                onClickListenerDots?.onClick("forwardBtn")
            }
            backBtn.setOnClickListener {
                onClickListenerDots?.onClick("backBtn")
            }
        }

        private fun createDotTextView(index: String): TextView {
            var textView =  TextView(context)
            textView.text = index
            textView.setTextColor(R.color.black)
            textView.setBackgroundResource(R.drawable.box_for_pagination_not_selected)
            textView.setPadding(30, 30, 30, 30)
            textView.textSize = 16F
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 20, 0)
            textView.setLayoutParams(params)
            return textView
        }
    }

    inner class GeneralViewHolder(private val binding: SearchAllJobAdapterViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchAllJobsOM.Data.Data) {
            val location = item.location?.joinToString(",") ?: ""

            binding.nameOfRole.text = item.nameOfRole
            binding.nameOfCompany.text = item.nameOfCompany
            binding.location.text = location
            binding.typeOfJob.text = item.typeOfJob
            binding.postedDate.text = item.postedDate?.let { "Posted date: ${convertLongToTime(it)}" }

            Glide.with(context)
                .load(item.companyLinks?.getOrNull(1)?.link)
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.imageOfCompany)

            itemView.setOnClickListener {
                onClickListener?.onClick(adapterPosition, item)
            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: SearchAllJobsOM.Data.Data)
    }

    interface OnClickListenerDots {
        fun onClick(position: CharSequence)
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd")
        return format.format(date)
    }
}
