package com.learning.careerconnect.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.careerconnect.Activity.BaseActivity
import com.learning.careerconnect.Adapter.AppliedJobAdapter
import com.learning.careerconnect.Cashes.JobAppliedDB
import com.learning.careerconnect.MVVM.JobMVVM
import com.learning.careerconnect.Model.GetAllAppliedJobsOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentAppliedJobBinding

class AppliedJobFragment : Fragment() {

    private lateinit var binding: FragmentAppliedJobBinding
    private lateinit var jobMVVM:JobMVVM
    private lateinit var token:String

    lateinit var appliedJobsData:ArrayList<GetAllAppliedJobsOM.Data.Data?>

    private var isLoading=false
    lateinit var itemAdapter: AppliedJobAdapter

    var skip=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppliedJobBinding.inflate(inflater, container, false)
        jobMVVM = ViewModelProvider(this)[JobMVVM::class.java]
        val sharedPreference =
            requireActivity().getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreference.getString(Constants.JWT_TOKEN_SP, "rk").toString()

        addDataToArr()
        jobMVVM.observerForGetAllAppliedJobs().observe(viewLifecycleOwner, Observer {

            res->
            Log.d("rk","Get all applied jobs api")
            if(res.status == "success")
            {
                if(isLoading)
                {
                    appliedJobsData.removeAt(appliedJobsData.size-1)
                }
                for(i in 0..res.data.data.size-1)
                {
                    appliedJobsData.add(res.data.data[i])
                }

                if(isLoading)
                {
                    itemAdapter.notifyDataSetChanged()
                    isLoading=false
                }
                else
                    adapter(appliedJobsData)
            }
        })
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager
                if(!isLoading)
                {
                    if(linearLayoutManager!=null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == appliedJobsData.size-1)
                    {
                        isLoading=true
                        getMoreData()
                    }
                }

            }
        })
        return binding.root
    }

    private fun addDataToArr() {

        appliedJobsData = ArrayList()
        val dbHandler = JobAppliedDB(requireActivity())
        val data= dbHandler.read()
        Log.d("rk","Data $data")
        for( i in 0..data.size-1)
        {
            val _id = data[i]._id
            val aboutCompany = data[i].aboutCompany
            val ctc = data[i].costToCompany
            val descriptionAboutRole = data[i].descriptionAboutRole
            val durationOfInternship = data[i].durationOfInternship
            val lastDateToApply = (data[i].lastDateToApply)!!.toLong()
            val nameOfCompany = data[i].nameOfCompany
            val nameOfRole = data[i].nameOfRole
            val noOfOpening = (data[i].noOfOpening)!!.toInt()
            val noOfStudentsApplied = (data[i].noOfStudentsApplied)!!.toInt()
            val postedDate = (data[i].postedDate)!!.toLong()
            val roleCategory =data[i].roleCategory
            val startDate = data[i].startDate
            val typeOfJob = data[i].typeOfJob
            val type = data[i].type

            var companysLink :ArrayList<GetAllAppliedJobsOM.Data.Data.JobAppliedId.CompanyLink> = ArrayList()
            var splitArr = data[i].companyLinks!!.split("||")
            companysLink.add(GetAllAppliedJobsOM.Data.Data.JobAppliedId.CompanyLink("$1",splitArr[0],"companys website"))
            companysLink.add(GetAllAppliedJobsOM.Data.Data.JobAppliedId.CompanyLink("$1",splitArr[1],"image"))

            val location = data[i].location!!.split("||")
            val minimumQualification = data[i].minimumQualification!!.split("||")
            val perks = data[i].perks!!.split("||")
            val responsibilities = data[i].responsibilities!!.split("||")
            val skillsRequired = data[i].skillsRequired!!.split("||")


            appliedJobsData.add(GetAllAppliedJobsOM.Data.Data(null,null,GetAllAppliedJobsOM.Data.Data.JobAppliedId(
                null,
                _id = _id,
                aboutCompany = aboutCompany,
                companyLinks = companysLink,
                costToCompany = ctc,
                descriptionAboutRole = descriptionAboutRole,
                durationOfInternship = durationOfInternship,
                lastDateToApply = lastDateToApply,
                location = location,
                minimumQualification = minimumQualification,
                nameOfCompany =nameOfCompany,
                nameOfRole = nameOfRole,
                noOfOpening = noOfOpening,
                noOfStudentsApplied = noOfStudentsApplied,
                perks = perks,
                postedDate = postedDate,
                responsibilities = responsibilities,
                roleCategory = roleCategory,
                skillsRequired = skillsRequired,
                startDate = startDate,
                null,
                typeOfJob = typeOfJob
            ),null,type,null))
        }

        adapter(appliedJobsData)

    }

    fun adapter(lis: ArrayList<GetAllAppliedJobsOM.Data.Data?>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        itemAdapter = AppliedJobAdapter(lis,requireContext())
        binding.recyclerView.adapter = itemAdapter
        itemAdapter.setOnClickListener(object :
            AppliedJobAdapter.OnClickListener {
            override fun onClick(position: Int, model: GetAllAppliedJobsOM.Data.Data) {

            }
        })
    }

    private fun getMoreData() {
        appliedJobsData.add(null)
        itemAdapter.notifyItemChanged(appliedJobsData.size -1)
        skip+=10
        jobMVVM.getAllAppliedJobs(requireContext(),"Bearer $token",this,"$skip")
    }
    fun errorFn(message: String) {
        BaseActivity().toast(message, requireContext())
    }
}
