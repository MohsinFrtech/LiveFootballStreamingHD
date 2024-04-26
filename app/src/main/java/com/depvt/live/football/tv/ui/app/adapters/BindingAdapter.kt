package com.depvt.live.football.tv.ui.app.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.depvt.live.football.tv.R

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView,url:String?)
{
    if (url!=null)
    {

            Glide.with(imageView.context)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView)

    }


}


@BindingAdapter("imageEvent")
fun loadEvent(imageView: ImageView,url:String?)
{
    if (url!=null)
    {

        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(imageView)

    }


}