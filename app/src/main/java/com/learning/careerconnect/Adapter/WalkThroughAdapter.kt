package com.learning.careerconnect.Adapter

import android.widget.ImageView

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.learning.careerconnect.R


class WalkThroughAdapter(var context: Context) : PagerAdapter() {

    var headings = intArrayOf(
        R.string.walk_through_screen_h1,
        R.string.walk_through_screen_h2,
        R.string.walk_through_screen_h3,
        R.string.walk_through_screen_h4
    )
    var images = intArrayOf(
        R.drawable.walk_through_screen_1,
        R.drawable.walk_through_image_2,
        R.drawable.walk_through_screen_3,
        R.drawable.walk_throught_screen_4,
    )
    var descriptions = intArrayOf(
        R.string.walk_through_screen_d1,
        R.string.walk_through_screen_d2,
        R.string.walk_through_screen_d3,
        R.string.walk_through_screen_d4
    )
    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.walk_through_layout, container, false)
        val slideTitleImage = view.findViewById<View>(R.id.imageView) as ImageView
        val slideHeading = view.findViewById<View>(R.id.title) as TextView
        val slideDescription = view.findViewById<View>(R.id.description) as TextView

        // Set image
        Glide
            .with(context)
            .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/walkThroughScreen1.svg")
            .centerCrop()
            .placeholder(R.drawable.career_connect_white_bg)
            .into(slideTitleImage);
//        slideTitleImage.setImageResource(images[position])
//        slideTitleImage.setImageURI("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/walkThroughScreen1.svg" as Uri)

        // Set heading and description
        slideHeading.setText(headings[position])
        slideDescription.setText(descriptions[position])

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}