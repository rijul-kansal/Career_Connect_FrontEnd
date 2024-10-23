package com.learning.careerconnect.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.careerconnect.Activity.BaseActivity
import com.learning.careerconnect.Adapter.FiltersAdapter
import com.learning.careerconnect.Adapter.SearchJobAdapter
import com.learning.careerconnect.MVVM.JobMVVM
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentSearchJobBinding


class SearchJobFragment : Fragment() {
    lateinit var binding: FragmentSearchJobBinding
    lateinit var jobMVVM: JobMVVM
    lateinit var arrayListJobsMain: ArrayList<SearchAllJobsOM.Data.Data>
    lateinit var itemAdapter: SearchJobAdapter
    var totalItemCount:Int = 0
    lateinit var token:String
    lateinit var rolesAvailable:ArrayList<String>
    lateinit var skillsAvailable:ArrayList<String>
    var currposition =1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchJobBinding.inflate(layoutInflater)
        jobMVVM = ViewModelProvider(requireActivity())[JobMVVM::class.java]
        arrayListJobsMain = ArrayList()
        rolesAvailable = ArrayList()
        skillsAvailable = ArrayList()
        val sharedPreference =
            requireActivity().getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreference.getString(Constants.JWT_TOKEN_SP, "rk").toString()

        binding.filterOptionEnable.setOnClickListener {
            binding.linearLayoutProgressBar.visibility= View.VISIBLE
            jobMVVM.getAllTypeOfInfo(this,"Bearer ${token}",requireContext())
        }

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
        jobMVVM.observerForgetAllTypeOfInfo().observe(viewLifecycleOwner, Observer { result ->
            if (result.status == "success") {
                Log.d("rk",result.toString())
                binding.linearLayoutProgressBar.visibility= View.INVISIBLE
                skillsAvailable = result.data!!.skill as ArrayList<String>
                rolesAvailable = result.data!!.role as ArrayList<String>
                showFilterDialog()
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
    private fun showFilterDialog()
    {
        val diaglog = Dialog(requireActivity(), R.style.PauseDialog)
        diaglog.setContentView(R.layout.filters_search_job)
        diaglog.window!!.setBackgroundDrawable(ColorDrawable(0))

        val preferredJobType = diaglog.findViewById<View>(R.id.preferedJobTypeTV)
        val typeOfJob = diaglog.findViewById<View>(R.id.typeOfJobTV)
        val location = diaglog.findViewById<View>(R.id.locationTV)
        val skills = diaglog.findViewById<View>(R.id.skillsTV)
        val companyName = diaglog.findViewById<View>(R.id.companyNamesTV)
        val easyApply = diaglog.findViewById<View>(R.id.easyApplyTV)
        val time = diaglog.findViewById<View>(R.id.timeTV)
        val cancelBtn = diaglog.findViewById<View>(R.id.cancelBtn)
        val applyFilter = diaglog.findViewById<View>(R.id.applyFilter)
        val gridView = diaglog.findViewById<RecyclerView>(R.id.gridView)
        Glide
            .with(this)
            .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/cancel.png")
            .placeholder(R.drawable.career_connect_white_bg)
            .into(cancelBtn as ImageView)
        preferredJobType.setOnClickListener {
            val arr = ArrayList<String>()
            arr.add("Internship")
            arr.add("FullTime")
            arr.add("PartTime")
            arr.add("Contract")
           gridView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = FiltersAdapter(arr)
            gridView.adapter = adapter

            adapter.setOnClickListener(object :
                FiltersAdapter.OnClickListener {
                override fun onClick(position: Int, model: String) {
                    Log.d("rk","pos $position name $model ")
                }
            })

            makeEveryThingSimilar(typeOfJob,location,skills,companyName,easyApply,time,preferredJobType)
        }
        location.setOnClickListener {
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,companyName,easyApply,time,location)
        }
        skills.setOnClickListener {
            makeEveryThingSimilar(typeOfJob,preferredJobType,location,companyName,easyApply,time,skills)
        }
        companyName.setOnClickListener {
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,location,easyApply,time,companyName)
        }
        easyApply.setOnClickListener {
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,companyName,location,time,easyApply)
        }
        time.setOnClickListener {
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,companyName,easyApply,location,time)
        }
        typeOfJob.setOnClickListener {
            makeEveryThingSimilar(location,preferredJobType,skills,companyName,easyApply,time,typeOfJob)
        }
        cancelBtn.setOnClickListener {
            diaglog.cancel()
        }
        diaglog.show()
    }

    private fun makeEveryThingSimilar(v1:View,v2:View,v3:View,v4:View,v5:View,v6:View,v7:View)
    {
        v7.setBackgroundResource(R.drawable.box_for_filters_selected)
        v1.setBackgroundResource(R.drawable.filter_box)
        v2.setBackgroundResource(R.drawable.filter_box)
        v3.setBackgroundResource(R.drawable.filter_box)
        v4.setBackgroundResource(R.drawable.filter_box)
        v5.setBackgroundResource(R.drawable.filter_box)
        v6.setBackgroundResource(R.drawable.filter_box)
    }
}