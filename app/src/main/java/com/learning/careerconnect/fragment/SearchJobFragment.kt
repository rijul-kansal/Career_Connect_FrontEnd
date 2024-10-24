package com.learning.careerconnect.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private fun showFilterDialog() {
//        Todo only allow single time
        var preferredJobTypeSet = mutableSetOf<Int>()
        var preferredJobTypeArr = ArrayList<String>()
        preferredJobTypeArr.add("Internship")
        preferredJobTypeArr.add("FullTime")
        preferredJobTypeArr.add("PartTime")
        preferredJobTypeArr.add("Contract")

        var skillsSet  = mutableSetOf<Int>()
        var typeOfJobSet = mutableSetOf<Int>()


        var timeSet = mutableSetOf<Int>()
        var timeArr = ArrayList<String>()
        timeArr.add("Last 24 Hours")
        timeArr.add("Last 3 Days")
        timeArr.add("Last 7 Days")

        var easyApplySet = mutableSetOf<Int>()
        var easyApplyArr = ArrayList<String>()
        easyApplyArr.add("YES")
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
        gridView.layoutManager = LinearLayoutManager(requireActivity())
        Glide
            .with(this)
            .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/cancel.png")
            .placeholder(R.drawable.career_connect_white_bg)
            .into(cancelBtn as ImageView)
        preferredJobType.setOnClickListener {
            val adapter = FiltersAdapter(preferredJobTypeArr,preferredJobTypeSet)
            gridView.adapter = adapter
            adapter.setOnClickListener(object :
                FiltersAdapter.OnClickListener {
                override fun onClick(position: Int, model: String) {
                   if(preferredJobTypeSet.contains(position))
                   {
                       preferredJobTypeSet.remove(position)
                   }
                    else
                   {
                       preferredJobTypeSet.add(position)
                   }
                }
            })

            makeEveryThingSimilar(typeOfJob,location,skills,companyName,easyApply,time,preferredJobType)
        }
        skills.setOnClickListener {
            makeEveryThingSimilar(typeOfJob,preferredJobType,location,companyName,easyApply,time,skills)
            val adapter = FiltersAdapter(skillsAvailable,skillsSet)
            gridView.adapter = adapter
            adapter.setOnClickListener(object :
                FiltersAdapter.OnClickListener {
                override fun onClick(position: Int, model: String) {
                    if(skillsSet.contains(position))
                    {
                        skillsSet.remove(position)
                    }
                    else
                    {
                        skillsSet.add(position)
                    }
                }
            })
        }
        typeOfJob.setOnClickListener {
            makeEveryThingSimilar(location,preferredJobType,skills,companyName,easyApply,time,typeOfJob)

            val adapter = FiltersAdapter(rolesAvailable,typeOfJobSet)
            gridView.adapter = adapter
            adapter.setOnClickListener(object :
                FiltersAdapter.OnClickListener {
                override fun onClick(position: Int, model: String) {
                    if(typeOfJobSet.contains(position))
                    {
                        typeOfJobSet.remove(position)
                    }
                    else
                    {
                        typeOfJobSet.add(position)
                    }
                }
            })

        }
        time.setOnClickListener {
            val adapter = FiltersAdapter(timeArr,timeSet)
            gridView.adapter = adapter
            adapter.setOnClickListener(object :
                FiltersAdapter.OnClickListener {
                override fun onClick(position: Int, model: String) {
                    if(timeSet.contains(position))
                    {
                        timeSet.remove(position)
                    }
                    else
                    {
                        timeSet.add(position)
                    }
                }
            })
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,companyName,easyApply,location,time)
        }
        easyApply.setOnClickListener {
            val adapter = FiltersAdapter(easyApplyArr,easyApplySet)
            gridView.adapter = adapter
            adapter.setOnClickListener(object :
                FiltersAdapter.OnClickListener {
                override fun onClick(position: Int, model: String) {
                    if(easyApplySet.contains(position))
                    {
                        easyApplySet.remove(position)
                    }
                    else
                    {
                        easyApplySet.add(position)
                    }
                }
            })
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,companyName,location,time,easyApply)
        }
        location.setOnClickListener {
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,companyName,easyApply,time,location)
        }
        companyName.setOnClickListener {
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,location,easyApply,time,companyName)
        }

        cancelBtn.setOnClickListener {
            diaglog.cancel()
        }
        applyFilter.setOnClickListener {

            var preferedJobTypeString = ""
            var typeOfJobString=""
            var skillsString=""
            var timeString=""
            var easyApplyString=""
            for(ele in preferredJobTypeSet)
            {
                preferedJobTypeString+=preferredJobTypeArr[ele]
                preferedJobTypeString+=","
            }
            for(ele in typeOfJobSet)
            {
                typeOfJobString+=rolesAvailable[ele]
                typeOfJobString+=","
            }
            for(ele in skillsSet)
            {
                skillsString+=skillsAvailable[ele]
                skillsString+=","
            }
            for(ele in timeSet)
            {
                timeString+=timeArr[ele]
                timeString+=","
            }

            if(preferedJobTypeString.length>0)
                preferedJobTypeString=preferedJobTypeString.dropLast(1)
            if(typeOfJobString.length>0)
                typeOfJobString=typeOfJobString.dropLast(1)
            if(skillsString.length>0)
                skillsString=skillsString.dropLast(1)
            if(timeString.length>0)
                timeString = timeString.dropLast(1)
            if(easyApplySet.size >0 )
            {
                easyApplyString="1"
            }

            Log.d("rk",preferedJobTypeString)
            Log.d("rk",typeOfJobString)
            Log.d("rk",skillsString)
            Log.d("rk",timeString)
            Log.d("rk",easyApplyString)
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