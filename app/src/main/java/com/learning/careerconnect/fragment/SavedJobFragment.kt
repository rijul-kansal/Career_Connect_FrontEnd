package com.learning.careerconnect.fragment

import android.content.Context
import android.content.Intent
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
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.learning.careerconnect.Activity.BaseActivity
import com.learning.careerconnect.Activity.DisplayJobInDetailActivity
import com.learning.careerconnect.Adapter.SavedJobAdapter
import com.learning.careerconnect.MVVM.JobMVVM
import com.learning.careerconnect.Model.GetAllSavedLaterJobsOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentSavedJobBinding

class SavedJobFragment : Fragment() {
    lateinit var binding:FragmentSavedJobBinding
    lateinit var itemAdapter:SavedJobAdapter
    private var isLoading=false
    private lateinit var token:String
    var skip=0
    var isFirst = true
    var isDataAvalable = true
    lateinit var jobsArr: ArrayList<GetAllSavedLaterJobsOM. Data. Data. JobId?>

    private lateinit var jobMVVM: JobMVVM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // TODO add next page fn
        binding = FragmentSavedJobBinding.inflate(inflater, container, false)
        jobMVVM = ViewModelProvider(this)[JobMVVM::class.java]
        val sharedPreferenceToken=
            requireActivity().getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreferenceToken.getString(Constants.JWT_TOKEN_SP, "rk").toString()
        val sharedPreference = requireActivity().getSharedPreferences(Constants.FULL_JOBID_SP, Context.MODE_PRIVATE)
        val gson = Gson()
        val fileData = sharedPreference.getString(Constants.FULL_JOBID_ARR, null)
        val listType = object : TypeToken<ArrayList<GetAllSavedLaterJobsOM.Data.Data.JobId>>() {}.type
        jobsArr = gson.fromJson(fileData, listType)
        jobsArr.reverse()
        adapter(jobsArr)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager
                if(!isLoading)
                {
                    if(linearLayoutManager!=null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == jobsArr.size-1)
                    {
                        isLoading=true
                        getMoreData()
                    }
                }

            }
        })

        jobMVVM.observerForGetAllSavedLaterJobsFull().observe(viewLifecycleOwner , Observer {
            res->
            Log.d("rk","result"+res.toString())
            if(res.status == "success")
            {
                if(res.data?.data?.size!! <= 0) isDataAvalable = false
                if(isLoading)
                {
                    jobsArr.removeAt(jobsArr.size-1)
                }
                for(i in res.data?.data!!) if (i != null) {
                    jobsArr.add(i.jobId)
                }

                if(isLoading)
                {
                    itemAdapter.notifyDataSetChanged()
                    isLoading=false
                }
                else
                    adapter(jobsArr)
            }
        })
        return binding.root
    }

    fun errorFn(message: String) {
        BaseActivity().toast(message, requireContext())
    }

    fun adapter(lis: ArrayList<GetAllSavedLaterJobsOM.Data.Data.JobId?>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        itemAdapter = SavedJobAdapter(lis,requireContext())
        binding.recyclerView.adapter = itemAdapter
        itemAdapter.setOnClickListener(object :
            SavedJobAdapter.OnClickListener {
            override fun onClick(position: Int, model: GetAllSavedLaterJobsOM.Data.Data.JobId) {
                var intent= Intent(requireActivity(), DisplayJobInDetailActivity::class.java)
                intent.putExtra(Constants.JOB_DATA1,model)
                intent.putExtra(Constants.TYPE_OF_FRAGMENT,"SavedJobFragment")
                startActivity(intent)

            }
        })
    }

    private fun getMoreData() {
        Log.d("rk","hello")
        if(isFirst)
        {
            skip+=jobsArr.size
            isFirst=false
        }
        else
            skip+=10

        if(isDataAvalable)
        {
            jobsArr.add(null)
            itemAdapter.notifyItemChanged(jobsArr.size -1)
            jobMVVM.getAllSavedLaterJobsFull(requireContext(),null,"Bearer $token",this,"$skip")

        }
    }
}