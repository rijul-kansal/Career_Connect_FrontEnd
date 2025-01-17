package com.learning.careerconnect.fragment.Profile

import android.app.Activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.learning.careerconnect.Activity.BaseActivity
import com.learning.careerconnect.Adapter.ProfileAdapter.LanguageKnownAdapter
import com.learning.careerconnect.Adapter.ProfileAdapter.LanguageKnownAdapterDialog
import com.learning.careerconnect.MVVM.ExtMVVM
import com.learning.careerconnect.MVVM.UserMVVM
import com.learning.careerconnect.Model.LoginOM
import com.learning.careerconnect.Model.SingleCityIM
import com.learning.careerconnect.Model.SingleStateIM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentPersonalInfoBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class PersonalInfoFragment : Fragment() {
    lateinit var binding :FragmentPersonalInfoBinding
    lateinit var userData:LoginOM
    var valueChanges = false
    lateinit var token:String
    lateinit var userVM: UserMVVM
    lateinit var extVM: ExtMVVM
    lateinit var dialog:Dialog
    lateinit var languageKnown : ArrayList<String>
    lateinit var itemAdapterForLanguageDialog:LanguageKnownAdapterDialog
    lateinit var languageItemAdapter:LanguageKnownAdapter

    private lateinit var getImageLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
        extVM= ViewModelProvider(this)[ExtMVVM::class.java]
        userVM= ViewModelProvider(this)[UserMVVM::class.java]

        val sharedPreference1 =
            requireActivity().getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreference1.getString(Constants.JWT_TOKEN_SP, "rk").toString()

        val sharedPreference = requireActivity().getSharedPreferences(Constants.GET_ME_SP_PN, Context.MODE_PRIVATE)
        val gson = Gson()
        val fileData = sharedPreference.getString(Constants.GET_ME_SP, null)
        val listType = object : TypeToken<LoginOM>() {}.type
        userData = gson.fromJson(fileData, listType)
        BaseActivity().requestPermission(requireActivity())
        populateData()


        getImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val fileUri = uri?.let { DocumentFile.fromSingleUri(requireContext(), it)?.uri }
            val fileName = uri?.let { DocumentFile.fromSingleUri(requireContext(), it)?.name }
            if(fileUri !=null)
            {
                Glide
                    .with(requireActivity())
                    .load(fileUri)
                    .placeholder(R.drawable.career_connect_white_bg)
                    .into(binding.profileImage)
                showProgressBar(requireActivity())
                val fileDir = requireContext().filesDir
                val file = fileName?.let { File(fileDir, it) }
                val inputStream = uri.let { requireActivity().contentResolver.openInputStream(it) }
                val outputStream = FileOutputStream(file)
                inputStream!!.copyTo(outputStream)
                val requestBody = file!!.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("image", file.name, requestBody)
                val descriptionRequestBody = "Image".toRequestBody("text/plain".toMediaTypeOrNull())
                userVM.uploadToS3(requireContext(),"Bearer $token ",part,descriptionRequestBody, this)
            }
        }


        binding.name.setOnClickListener {
            singleValueDialog(binding.name,"Name",binding.name.text.toString())
        }
        binding.mobileNo.setOnClickListener {
            singleValueDialog(binding.mobileNo,"Mobile Number",binding.mobileNo.text.toString())
        }
        binding.DOB.setOnClickListener {
            datePickerDialog(binding.DOB)
        }
        binding.gender.setOnClickListener {
            singleValueChosenDialog(binding.gender,"Gender",binding.gender.text.toString())
        }

        binding.profileImage.setOnClickListener {
            getImageLauncher.launch("image/*")
        }

        binding.currentLocation.setOnClickListener {
            threeValueDialog(binding.currentLocation,"Current Location",userData.data?.data?.currentLocation?.country , userData.data?.data?.currentLocation?.state,userData.data?.data?.currentLocation?.city)
        }

        binding.cardViewLanguage.setOnClickListener {
            shownLanguageAdapter("Languages Known")
        }

        userVM.observerForUploadToS3().observe(viewLifecycleOwner , Observer {
                result->
            cancelProgressBar()
            if(result.status == "success")
            {
                val sharedPreference = requireActivity().getSharedPreferences(Constants.GET_ME_SP_PN, Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                userData.data?.data!!.image = result.data!!.image
                Log.d("rk",userData.data!!.data!!.image.toString())
                val jsonStr = Gson().toJson(userData)
                editor.putString(Constants.GET_ME_SP, jsonStr)
                editor.apply()
            }
        })
        return binding.root
    }

    private fun populateData() {
        binding.name.text = userData.data!!.data!!.name
        binding.email.text = userData.data!!.data!!.email
        binding.mobileNo.text = userData.data!!.data!!.mobileNumber.toString()

        if(userData.data!!.data!!.image != null)
            binding.gender.text = userData.data!!.data!!.gender
        else
            binding.gender.text = "Add Gender"
        if(userData.data!!.data!!.dateOfBirth != null)
            binding.DOB.text = userData.data!!.data!!.dateOfBirth?.let { getDateTime(it) }
        else
            binding.DOB.text = "Add DOB"
        var location = "${userData.data!!.data!!.currentLocation?.city}," +
                "${userData.data!!.data!!.currentLocation?.state}," +
                "${userData.data!!.data!!.currentLocation?.country}"
        if(location != "null,null,null")
            binding.currentLocation.text = location
        else
            binding.currentLocation.text = "Add Location"

        var arr = userData.data!!.data!!.language as ArrayList<String>
        languageKnown = arr
        if(arr.size==0)
            arr.add("Add Languages")
        binding.language.layoutManager = LinearLayoutManager(requireActivity())
        languageItemAdapter = LanguageKnownAdapter(arr)
        resize(arr)

        Glide
            .with(requireActivity())
            .load(userData.data!!.data!!.image+"?timestamp=${System.currentTimeMillis()}")
            .placeholder(R.drawable.career_connect_white_bg)
            .into(binding.profileImage)
    }
    private fun resize(arr: java.util.ArrayList<String>) {
        val heightInDp = 60
        val heightInPx = (heightInDp * requireContext().resources.displayMetrics.density).toInt()
        val layoutparams = LinearLayout.LayoutParams(MATCH_PARENT, (arr.size+1) * heightInPx)
        layoutparams.setMargins(12,12,12,12)
        binding.cardViewLanguage.layoutParams=layoutparams
        binding.language.adapter = languageItemAdapter
    }
    fun singleValueDialog(actualView:TextView,generalText:String,actualValue:String){
        var dialog = Dialog(requireActivity())
        val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.single_input_allowed, null)
        val doneButton = view.findViewById<TextView>(R.id.doneBtn)
        val edText = view.findViewById<EditText>(R.id.etDialog)
        dialog.setContentView(view)
        edText.setText(actualValue)
        view.findViewById<TextView>(R.id.generalTV).setText(generalText)
        val window = dialog.window
        window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)
        doneButton.setOnClickListener {
            if(edText.text.toString() != actualValue)
            {
                valueChanges = true
                actualView.text = edText.text.toString()
            }
            Log.d("rk",valueChanges.toString())
            dialog.cancel()
        }
        dialog.show()
    }
    fun singleValueDialogForLanguage(generalText:String ) {
        var dialog = Dialog(requireActivity())
        val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.single_input_allowed, null)
        val doneButton = view.findViewById<TextView>(R.id.doneBtn)
        val edText = view.findViewById<EditText>(R.id.etDialog)
        dialog.setContentView(view)
        view.findViewById<TextView>(R.id.generalTV).setText(generalText)
        val window = dialog.window
        window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)
        doneButton.setOnClickListener {
            if(edText.text.length !=0)
            {
                if(userData.data?.data?.language!!.size > 0 && userData.data?.data?.language?.get(0) == "Add Languages")
                {
                    languageKnown.removeAt(0)
                    languageKnown.add(edText.text.toString())
                    itemAdapterForLanguageDialog.notifyDataSetChanged()
                }
                else
                {
                    languageKnown.add(edText.text.toString())
                    itemAdapterForLanguageDialog.notifyDataSetChanged()
                }
            }
            dialog.cancel()
        }
        dialog.show()
    }
    fun datePickerDialog(view1:TextView) {
        val c: Calendar = Calendar.getInstance()
        var mYear = c.get(Calendar.YEAR)
        var mMonth = c.get(Calendar.MONTH)
        var mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            OnDateSetListener { view, year, monthOfYear,
                                dayOfMonth ->
                val newDate = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                if(newDate != view1.text)
                {
                    valueChanges = true
                    view1.setText(newDate)
                }
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }
    fun singleValueChosenDialog(actualView:TextView,generalText:String,actualValue:String){
        var dialog = Dialog(requireActivity())
        val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.single_value_choosen_dialog, null)
        val doneButton = view.findViewById<TextView>(R.id.doneBtn)
        val edText = view.findViewById<AutoCompleteTextView>(R.id.chooseTypeET)
        val type = resources.getStringArray(R.array.typeOfGender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item_text_view, type)
        edText.setAdapter(arrayAdapter)
        edText.keyListener=null
        dialog.setContentView(view)

        view.findViewById<TextView>(R.id.generalTV).setText(generalText)
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)
        doneButton.setOnClickListener {
            if(edText.text.toString() != actualValue)
            {
                valueChanges = true
            }
            Log.d("rk",valueChanges.toString())
            actualView.text = edText.text.toString()
            dialog.cancel()
        }
        dialog.show()
    }
    fun threeValueDialog(actualView:TextView,generalText:String,actualValue1:String?,actualValue2:String?,actualValue3:String?){
        var dialog = Dialog(requireActivity())
        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.three_value_input_dialog, null)
        val doneButton = view.findViewById<TextView>(R.id.doneBtn)
        val edText1 = view.findViewById<AutoCompleteTextView>(R.id.chooseCountry)
        val edText2 = view.findViewById<AutoCompleteTextView>(R.id.chooseState)
        val edText3 = view.findViewById<AutoCompleteTextView>(R.id.chooseCity)
        dialog.setContentView(view)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item_text_view, resources.getStringArray(R.array.countries))
        edText1.setAdapter(arrayAdapter)
        if(actualValue1 !=null)
        {
            edText1.setText(actualValue1)
            edText2.setText(actualValue2)
            edText3.setText(actualValue3)
        }

        view.findViewById<TextView>(R.id.generalTV).setText(generalText)
        val window = dialog.window
        window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)


        edText1.setOnItemClickListener { parent, view, position, id ->
            extVM.getSingleState(this,requireContext(), SingleStateIM(edText1.text.toString()))
            showProgressBar(requireActivity())
        }
        edText2.setOnItemClickListener { parent, view, position, id ->
            extVM.getSingleCity(this,requireContext(), SingleCityIM(edText1.text.toString(),edText2.text.toString()))
            showProgressBar(requireActivity())
        }
        doneButton.setOnClickListener {

            if(edText1.text.toString().length !=0 &&
                edText2.text.toString().length !=0 &&
                edText3.text.toString().length !=0 &&
                (edText1.text.toString() != actualValue1 || edText2.text.toString() != actualValue2 || edText3.text.toString() != actualValue3))
            {
                valueChanges = true
                binding.currentLocation.text = "${edText1.text},${edText2.text},${edText3.text}"
            }
            Log.d("rk",valueChanges.toString())
            dialog.cancel()
        }
        extVM.observerForGetSingleStateSearch().observe(viewLifecycleOwner, Observer {
                result->
            cancelProgressBar()
            var arr=ArrayList<String>()

            for(i in result.data?.states!!)
            {
                if (i != null) {
                    i.name?.let { arr.add(it) }
                }
            }
            val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.drop_down_item_text_view, arr)
            edText2.setAdapter(arrayAdapter1)
        })
        extVM.observerForGetSingleCitySearch().observe(viewLifecycleOwner, Observer {
                result->
            cancelProgressBar()
            var arr=ArrayList<String>()

            for(i in result.data!!)
            {
                if (i != null) {
                    i.let { arr.add(it) }
                }
            }
            val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.drop_down_item_text_view, arr)
            edText3.setAdapter(arrayAdapter1)
        })
        dialog.show()
    }
    fun shownLanguageAdapter(generalText:String) {
        var arr = languageKnown
        var dialog = Dialog(requireActivity())
        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.recycle_view_dialog, null)
        view.findViewById<TextView>(R.id.generalTV).setText(generalText)
        var recycleView = view.findViewById<RecyclerView>(R.id.recycleViewDialog)
        dialog.setContentView(view)
        itemAdapterForLanguageDialog = LanguageKnownAdapterDialog(languageKnown)
        recycleView.layoutManager = LinearLayoutManager(requireActivity())

        recycleView.adapter = itemAdapterForLanguageDialog
        itemAdapterForLanguageDialog.setOnClickListener(object :
            LanguageKnownAdapterDialog.OnClickListener {
            override fun onClick(position: Int?, model: String) {
                if(model == "addContent")
                {
                    singleValueDialogForLanguage("Add your fav Language")

                }
                else if(model == "DoneBtn")
                {
                    if(languageKnown.size ==0)
                    {
                        languageKnown.add("Add Languages")
                    }
                    if(arr != languageKnown)
                    {
                        valueChanges = true
                    }
                    arr = languageKnown
                    resize(arr)
                    languageItemAdapter.notifyDataSetChanged()
                    dialog.cancel()
                }
                else
                {
                    languageKnown.removeAt(position!!)
                    itemAdapterForLanguageDialog.notifyDataSetChanged()
                }
            }
        })
        val window = dialog.window
        window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.white)
        dialog.show()
    }
    fun errorFn(message: String) {
        BaseActivity().toast(message, requireContext())
    }
    fun showProgressBar(activity: Activity) {
        dialog = Dialog(activity)
        dialog.setContentView(R.layout.progress_bar)
        dialog.show()
    }
    fun cancelProgressBar() {
        dialog.cancel()
    }
    private fun getDateTime(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(s )
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}