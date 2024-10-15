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
    lateinit var itemAdapter: SearchJobAdapter
    var totalItemCount:Int = 0
    lateinit var token:String

    var currposition =1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchJobBinding.inflate(layoutInflater)
        jobMVVM = ViewModelProvider(requireActivity())[JobMVVM::class.java]
        arrayListJobsMain = ArrayList()
        val sharedPreference =
            requireActivity().getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreference.getString(Constants.JWT_TOKEN_SP, "rk").toString()

        callingSearchJobFn(null,null,null,null,null,null,null)
        jobMVVM.observerForSearchAllJobs().observe(viewLifecycleOwner, Observer { result ->
            binding.refreshLayout.isRefreshing = false
            if (result.status == "success") {
                totalItemCount= result.totalJobs!!.toInt()
                Log.d("rk",totalItemCount.toString())
                arrayListJobsMain.clear()
                for (i in 0..result.data!!.data!!.size - 1) {
                    result.data!!.data?.get(i)?.let { arrayListJobsMain.add(it) }
                }
                adapter(arrayListJobsMain,currposition,totalItemCount)
            }
        })

        binding.refreshLayout.setOnRefreshListener {
            currposition=1
            callingSearchJobFn(null,null,null,null,null,null,null)
        }
        return binding.root
    }

    fun errorFn(message: String) {
        BaseActivity().toast(message, requireContext())
    }

    fun adapter(lis: ArrayList<SearchAllJobsOM.Data.Data>,pos:Int,totalItemCount:Int) {
        binding.recycleView.layoutManager = LinearLayoutManager(requireActivity())
        itemAdapter = SearchJobAdapter(lis, requireContext(),totalItemCount,pos)
        binding.recycleView.adapter = itemAdapter
        itemAdapter.setOnClickListener(object :
            SearchJobAdapter.OnClickListener {
            override fun onClick(position: Int, model: SearchAllJobsOM.Data.Data) {
                model.nameOfCompany?.let { BaseActivity().toast(it,requireContext()) }
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

        itemAdapter.setOnClickListenerDots(object : SearchJobAdapter.OnClickListenerDots {
            override fun onClick(position: CharSequence) {
                BaseActivity().toast("$position",requireContext())
                if(position == "forwardBtn")
                {
                    currposition++
                    callingSearchJobFn(null,null,null,null,null,null,null)
                }
                else if(position == "backBtn")
                {
                    currposition--
                    callingSearchJobFn(null,null,null,null,null,null,null)
                }
                else if(position != "...")
                {
                    currposition=position.toString().toInt()
                    callingSearchJobFn(null,null,null,null,null,null,null)
                }

            }
        })
    }

    fun callingSearchJobFn(preferredJobType:String?,typeOfJob:String?,location:String?,skill:String?,compantNames:String?,easyApply:String?,time:String?) {
        jobMVVM.searchAllJobs(
            fragment = this@SearchJobFragment,
            token = "Bearer $token",
            context = requireContext(),
            preferredJobType,
            "${currposition-1}",
            "10",
            typeOfJob,
            location,
            skill,
            compantNames,
            easyApply,
            time
        )
    }
}