package com.learning.careerconnect.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.careerconnect.Activity.BaseActivity
import com.learning.careerconnect.Activity.DisplayJobInDetailActivity
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
    lateinit var locationAvailable:ArrayList<String>
    var currposition =1

    var ps: String? = null
    var ts: String? =  null
    var ss: String? =  null
    var tts: String? =  null
    var es: String? = null
    var ls: String? =  null
    var preferredJobTypeSet = mutableSetOf<Int>()
    var preferredJobTypeArr = ArrayList<String>()
    var skillsSet  = mutableSetOf<Int>()
    var typeOfJobSet = mutableSetOf<Int>()
    var locationSet = mutableSetOf<Int>()
    var timeSet = mutableSetOf<Int>()
    var timeArr = ArrayList<String>()
    var easyApplySet = mutableSetOf<Int>()
    var easyApplyArr = ArrayList<String>()

    var diaglog:Dialog?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchJobBinding.inflate(layoutInflater)
        jobMVVM = ViewModelProvider(requireActivity())[JobMVVM::class.java]
        arrayListJobsMain = ArrayList()
        rolesAvailable = ArrayList()
        locationAvailable = ArrayList()
        skillsAvailable = ArrayList()
        val sharedPreference =
            requireActivity().getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreference.getString(Constants.JWT_TOKEN_SP, "rk").toString()

        binding.filterOptionEnable.setOnClickListener {
            if(rolesAvailable.size <=0)
            {
                binding.linearLayoutProgressBar.visibility= View.VISIBLE
                jobMVVM.getAllTypeOfInfo(this,"Bearer ${token}",requireContext())
            }
            else
            {
                showFilterDialog()
            }

        }
        binding.constraintLayoutProgressBar.visibility = View.VISIBLE
        callingSearchJobFn()
        jobMVVM.observerForSearchAllJobs().observe(viewLifecycleOwner, Observer { result ->
            Log.d("rk","search jobs api")
            binding.refreshLayout.isRefreshing = false
            binding.linearLayoutProgressBarDisable.visibility = View.GONE
            binding.constraintLayoutProgressBar.visibility = View.GONE
            if (result.status == "success") {
                if(diaglog!=null)
                {
                    diaglog!!.cancel()
                    diaglog=null
                }
                totalItemCount= result.totalJobs!!.toInt()
                arrayListJobsMain.clear()
                for (i in 0..result.data!!.data!!.size - 1) {
                    result.data!!.data?.get(i)?.let { arrayListJobsMain.add(it) }
                }
                adapter(arrayListJobsMain,currposition,totalItemCount)
            }
        })
        jobMVVM.observerForgetAllTypeOfInfo().observe(viewLifecycleOwner, Observer { result ->
            Log.d("rk","Get all type of info  api")
            if (result.status == "success") {
                binding.linearLayoutProgressBar.visibility= View.GONE
                skillsAvailable = result.data!!.skill as ArrayList<String>
                rolesAvailable = result.data!!.role as ArrayList<String>
                locationAvailable = result.data!!.location as ArrayList<String>
                showFilterDialog()
            }
        })

        binding.refreshLayout.setOnRefreshListener {
            currposition=1
            callingSearchJobFn()
        }

        binding.filterOptionDisable.setOnClickListener {
            binding.linearLayoutProgressBarDisable.visibility = View.VISIBLE
            callingSearchJobFnAllNullAndMakeFiltersNull()

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
                val intent = Intent(requireActivity(),DisplayJobInDetailActivity::class.java)
                intent.putExtra(Constants.JOB_DATA,model)
                startActivity(intent)
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
                    binding.constraintLayoutProgressBar.visibility = View.VISIBLE
                    callingSearchJobFn()
                }
                else if(position == "backBtn")
                {
                    currposition--
                    binding.constraintLayoutProgressBar.visibility = View.VISIBLE
                    callingSearchJobFn()
                }
                else if(position != "...")
                {
                    currposition=position.toString().toInt()
                    callingSearchJobFn()
                }

            }
        })
    }

    fun callingSearchJobFn() {
        jobMVVM.searchAllJobs(
            fragment = this@SearchJobFragment,
            token = "Bearer $token",
            context = requireContext(),
            ts,
            "${currposition-1}",
            "10",
            ps,
            ls,
            ss,
            null,
            es,
            tts
        )
    }

    fun callingSearchJobFnAllNullAndMakeFiltersNull() {
        ps=null
        ts=null
        ls=null
        ss=null
        es=null
        tts=null
        preferredJobTypeSet.clear()
        typeOfJobSet.clear()
        locationSet.clear()
        skillsSet.clear()
        easyApplySet.clear()
        timeSet.clear()

        rolesAvailable.clear()
        skillsAvailable.clear()
        locationAvailable.clear()
        jobMVVM.searchAllJobs(
            fragment = this@SearchJobFragment,
            token = "Bearer $token",
            context = requireContext(),
            ts,
            "${currposition-1}",
            "10",
            ps,
            ls,
            ss,
            null,
            es,
            tts
        )
    }
    private fun showFilterDialog() {
        fillingDetails()
        diaglog = Dialog(requireActivity(), R.style.PauseDialog)
        diaglog!!.setContentView(R.layout.filters_search_job)
        diaglog!!.window!!.setBackgroundDrawable(ColorDrawable(0))

        val preferredJobType = diaglog!!.findViewById<View>(R.id.preferedJobTypeTV)
        val typeOfJob = diaglog!!.findViewById<View>(R.id.typeOfJobTV)
        val location = diaglog!!.findViewById<View>(R.id.locationTV)
        val skills = diaglog!!.findViewById<View>(R.id.skillsTV)
        val easyApply = diaglog!!.findViewById<View>(R.id.easyApplyTV)
        val time = diaglog!!.findViewById<View>(R.id.timeTV)
        val cancelBtn = diaglog!!.findViewById<View>(R.id.cancelBtn)
        val applyFilter = diaglog!!.findViewById<View>(R.id.applyFilter)
        val gridView = diaglog!!.findViewById<RecyclerView>(R.id.gridView)
        val progressBar = diaglog!!.findViewById<ProgressBar>(R.id.gridViewProgressBar)
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

            makeEveryThingSimilar(typeOfJob,location,skills,easyApply,time,preferredJobType)
        }
        skills.setOnClickListener {
            makeEveryThingSimilar(typeOfJob,preferredJobType,location,easyApply,time,skills)
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
            makeEveryThingSimilar(location,preferredJobType,skills,easyApply,time,typeOfJob)

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
            val adapter = FiltersAdapter(timeArr,timeSet,"Time")
            gridView.adapter = adapter
            adapter.setOnClickListener(object :
                FiltersAdapter.OnClickListener {
                override fun onClick(position: Int, model: String) {
                    if(timeSet.contains(position))
                    {
                        timeSet.clear()
                    }
                    else
                    {
                        timeSet.clear()
                        timeSet.add(position)
                    }
                }
            })
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,easyApply,location,time)
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
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,location,time,easyApply)
        }
        location.setOnClickListener {

            val adapter = FiltersAdapter(locationAvailable,locationSet)
            gridView.adapter = adapter
            adapter.setOnClickListener(object :
                FiltersAdapter.OnClickListener {
                override fun onClick(position: Int, model: String) {
                    if(locationSet.contains(position))
                    {
                        locationSet.remove(position)
                    }
                    else
                    {
                        locationSet.add(position)
                    }
                }
            })
            makeEveryThingSimilar(typeOfJob,preferredJobType,skills,easyApply,time,location)
        }
        cancelBtn.setOnClickListener {
            diaglog!!.cancel()
            diaglog=null
        }
        applyFilter.setOnClickListener {

            var preferedJobTypeString = ""
            var typeOfJobString=""
            var skillsString=""
            var timeString=""
            var easyApplyString=""
            var locationString=""
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
            for(ele in locationSet)
            {
                locationString+=locationAvailable[ele]
                locationString+=","
            }
            if(timeSet.size ==1)
            {
                if(timeSet.contains(0)) timeString="1"
                else if(timeSet.contains(1)) timeString="2"
                else timeString="3"
            }

            if(preferedJobTypeString.length>0)
                preferedJobTypeString=preferedJobTypeString.dropLast(1)
            if(typeOfJobString.length>0)
                typeOfJobString=typeOfJobString.dropLast(1)
            if(skillsString.length>0)
                skillsString=skillsString.dropLast(1)
            if(easyApplySet.size >0)
                easyApplyString="1"
            if(locationSet.size>0)
                locationString=locationString.dropLast(1)
            Log.d("rk",preferedJobTypeString)
            Log.d("rk",typeOfJobString)
            Log.d("rk",skillsString)
            Log.d("rk",timeString)
            Log.d("rk",easyApplyString)
            Log.d("rk",locationString)
            ps = if (preferedJobTypeString.isNotEmpty()) preferedJobTypeString else null
            ts = if (typeOfJobString.isNotEmpty()) typeOfJobString else null
            ss = if (skillsString.isNotEmpty()) skillsString else null
            tts = if (timeString.isNotEmpty()) timeString else null
            es= if (easyApplyString.isNotEmpty()) easyApplyString else null
            ls = if (locationString.isNotEmpty()) locationString else null


            progressBar.visibility = View.VISIBLE
            jobMVVM.searchAllJobs(
                this,"Bearer $token",requireContext(),ts,"0","10", ps,ls,ss,null,es,tts)
        }
        diaglog!!.show()
    }

    private fun makeEveryThingSimilar(v1:View,v2:View,v3:View,v4:View,v5:View,v7:View)
    {
        v7.setBackgroundResource(R.drawable.box_for_filters_selected)
        v1.setBackgroundResource(R.drawable.filter_box)
        v2.setBackgroundResource(R.drawable.filter_box)
        v3.setBackgroundResource(R.drawable.filter_box)
        v4.setBackgroundResource(R.drawable.filter_box)
        v5.setBackgroundResource(R.drawable.filter_box)
    }

    fun fillingDetails()
    {
        preferredJobTypeArr.clear()
        timeArr.clear()
        easyApplyArr.clear()
        preferredJobTypeArr.add("Internship")
        preferredJobTypeArr.add("FullTime")
        preferredJobTypeArr.add("PartTime")
        preferredJobTypeArr.add("Contract")
        timeArr.add("Last 24 Hours")
        timeArr.add("Last 3 Days")
        timeArr.add("Last 7 Days")
        easyApplyArr.add("YES")
    }
}