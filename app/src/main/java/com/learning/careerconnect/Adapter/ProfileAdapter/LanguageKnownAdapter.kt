package com.learning.careerconnect.Adapter.ProfileAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.careerconnect.databinding.TextViewLayoutBinding

class LanguageKnownAdapter(
    private val items: ArrayList<String>
) : RecyclerView.Adapter<LanguageKnownAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TextViewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.textView.text= "${position+1}) "+item
    }
    override fun getItemCount(): Int {
        return items.size
    }
    class ViewHolder(val binding: TextViewLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}