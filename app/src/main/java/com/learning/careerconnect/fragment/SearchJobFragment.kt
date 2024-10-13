package com.learning.careerconnect.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.careerconnect.Activity.BaseActivity
import com.learning.careerconnect.Adapter.SearchJobAdapter
import com.learning.careerconnect.MVVM.JobMVVM
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentSearchJobBinding


class SearchJobFragment : Fragment() {
    lateinit var binding: FragmentSearchJobBinding
    lateinit var jobMVVM: JobMVVM
    lateinit var arrayListJobsMain: ArrayList<SearchAllJobsOM.Data.Data>
    var x:Int=0
    var y:Int=0
    lateinit var itemAdapter: SearchJobAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchJobBinding.inflate(layoutInflater)
        jobMVVM = ViewModelProvider(requireActivity())[JobMVVM::class.java]
        arrayListJobsMain = ArrayList()
        val sharedPreference =
            requireActivity().getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        val token = sharedPreference.getString(Constants.JWT_TOKEN_SP, "rk")

        jobMVVM.searchAllJobs(
            fragment = this,
            token = "Bearer $token",
            context = requireContext(),
            null,
            null,
            "100",
            null,
            null,
            null,
            null,
            null,
            null
        )
        jobMVVM.observerForSearchAllJobs().observe(viewLifecycleOwner, Observer { result ->
            if (result.status == "success") {
                arrayListJobsMain.clear()
                Log.d("rk", result.toString())
                for (i in 0..result.data!!.data!!.size - 1) {
                    result.data!!.data?.get(i)?.let { arrayListJobsMain.add(it) }
                }
                adapter(arrayListJobsMain)
            }
        })
        return binding.root
    }

    fun errorFn(message: String) {
        BaseActivity().toast(message, requireContext())
    }

    fun adapter(lis: ArrayList<SearchAllJobsOM.Data.Data>) {
        binding.recycleView.layoutManager = LinearLayoutManager(requireActivity())
        itemAdapter = SearchJobAdapter(lis, requireContext())
        binding.recycleView.adapter = itemAdapter
        itemAdapter.setOnClickListener(object :
            SearchJobAdapter.OnClickListener {
            override fun onClick(position: Int, model: SearchAllJobsOM.Data.Data) {
            }
        })

        binding.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalHeight = recyclerView.computeVerticalScrollRange() - recyclerView.height
                binding.verticalProgressbar.max = totalHeight
                binding.verticalProgressbar.progress = recyclerView.computeVerticalScrollOffset()
            }
        })


    }


}