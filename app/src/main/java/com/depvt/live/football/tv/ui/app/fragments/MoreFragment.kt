package com.depvt.live.football.tv.ui.app.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.depvt.live.football.tv.BuildConfig
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.MoreScreenLayoutBinding
import com.depvt.live.football.tv.utils.objects.SharedPreference

class MoreFragment : Fragment() {

    private var preference:SharedPreference?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.more_screen_layout, container, false)
        val binding: MoreScreenLayoutBinding? = DataBindingUtil.bind(view)
        preference= SharedPreference(requireContext())


            //        ////Rate us click listener....


        ///Notification enable or disable

        binding?.mainRateLay?.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + context?.packageName)
                    )
                )
            } catch (e: ActivityNotFoundException) {

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context?.packageName)
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    Log.d("Exception", "" + e.message)
                }

            }
        }


//        binding?.switchLayout?.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                FirebaseMessaging.getInstance().subscribeToTopic("event")
//                preference?.saveBool(Constants.preferenceKey,true)
//            } else {
//                FirebaseMessaging.getInstance().unsubscribeFromTopic("event")
//                preference?.saveBool(Constants.preferenceKey,false)
//
//            }
//        }

        ////Contact us click listener....
        binding?.mainFeedBackLay?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, Array(1) { "" })
            intent.putExtra(Intent.EXTRA_SUBJECT, resources?.getString(R.string.app_name))
            startActivity(Intent.createChooser(intent, "Send Email..."))
        }
        /////Terms and condition....

        binding?.mainShareLay?.setOnClickListener{

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT, "Please download this app for live  streaming.\n" +
                        "https://play.google.com/store/apps/details?id=" + context?.packageName
            )
            intent.type = "text/plain"
            startActivity(intent)
        }
        binding?.mainTermLay?.setOnClickListener {
            try {
                val url = ""
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: ActivityNotFoundException) {
                Log.d("Exception", "" + e.message)
            }
        }
//
        binding?.appVersion?.text = ""+ BuildConfig.VERSION_NAME
        return view

    }

}