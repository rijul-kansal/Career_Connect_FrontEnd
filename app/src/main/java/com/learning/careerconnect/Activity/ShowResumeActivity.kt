package com.learning.careerconnect.Activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivityShowResumeBinding
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo

class ShowResumeActivity : AppCompatActivity() {
    lateinit var  link:String
    lateinit var binding:ActivityShowResumeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.RESUME_LINK))
        {
            link = intent.getStringExtra(Constants.RESUME_LINK).toString()
            Log.d("rk",link)

        }
        val uniqueLink = "$link?timestamp=${System.currentTimeMillis()}"
        PdfViewerActivity.launchPdfFromUrl(
            context = this,
            pdfUrl = uniqueLink,
            pdfTitle = "PDF Title",
            saveTo = saveTo.ASK_EVERYTIME,
            enableDownload = true
        )
        binding.pdfView.initWithUrl(
            url = uniqueLink,
            lifecycleCoroutineScope = lifecycleScope,
            lifecycle = lifecycle
        )

    }
}