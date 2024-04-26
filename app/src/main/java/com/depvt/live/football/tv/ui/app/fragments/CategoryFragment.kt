package com.depvt.live.football.tv.ui.app.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.adsData.AdManager
import com.depvt.live.football.tv.databinding.CategoriesFragmentBinding
import com.depvt.live.football.tv.models.Channel
import com.depvt.live.football.tv.room.RoomDatabase
import com.depvt.live.football.tv.room.RoomTable
import com.depvt.live.football.tv.ui.app.adapters.ChannelAdapter
import com.depvt.live.football.tv.utils.interfaces.AdManagerListener
import com.depvt.live.football.tv.utils.interfaces.NavigateData
import com.depvt.live.football.tv.utils.objects.Constants
import com.depvt.live.football.tv.viewModel.OneViewModel
import com.unity3d.scar.adapter.common.Utils
import java.util.*

class CategoryFragment:Fragment(), AdManagerListener, NavigateData {

    private val modelEvent by lazy {
        activity?.let { ViewModelProvider(it)[OneViewModel::class.java] }
    }
    private var adManager: AdManager?=null
    private var adStatus=false
    private  var liveChannelsList: MutableList<Channel> = ArrayList()
    private  var liveListWithAds: MutableList<Channel> = ArrayList()
    private var  nativeFieldVal=""
    private var navDirections: NavDirections?=null
    private var adProviderName="none"
    private var nativeAdProviderName="none"
    private var bindingCategory: CategoriesFragmentBinding? = null
    private var notificationlist: MutableList<RoomTable> =
        ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.categories_fragment, container, false)
        bindingCategory = DataBindingUtil.bind(view)
        bindingCategory?.lifecycleOwner=this
        bindingCategory?.model=modelEvent
        adManager= activity?.let { AdManager(requireContext(), it,this) }

        val input = "e"
        val output = input.uppercase(Locale.getDefault())

//        bindingCategory?.appText?.text = context?.resources?.getString(R.string.appText,"Liv",output," Football")

        setUpViewModel()
        clickListener()

        return view
    }



    private fun clickListener() {

        bindingCategory?.bellIcon?.setOnClickListener { findNavController().navigate(R.id.notification) }
    }

    private fun setUpViewModel() {

//        try {
//            bindingCategory?.let { getListOfNotifications(requireContext(), it) }
//        }
//        catch (e:Exception)
//        {
//            Log.d("Exception",""+e.message)
//        }
        modelEvent?.dataModelList?.observe(viewLifecycleOwner) {

            liveChannelsList = ArrayList<Channel>()
            adStatus=false
            Constants.middleAdProvider="none"
            if (!it.extra_3.isNullOrEmpty())
            {
                nativeFieldVal= it.extra_3!!
            }
            if (!it.app_ads.isNullOrEmpty())
            {
                adProviderName= adManager?.checkProvider(it.app_ads!!, Constants.adMiddle).toString()

                Constants.middleAdProvider=adProviderName

                if (!adProviderName.equals(Constants.startApp,true))
                {
                    adManager?.loadAdProvider(adProviderName,Constants.adMiddle,null,bindingCategory?.fbAdView,
                        bindingCategory?.unityBannerView,bindingCategory?.startAppBanner)
                }

                nativeAdProviderName=adManager?.checkProvider(it.app_ads!!, Constants.nativeAdLocation).toString()

//                val bannerProvider=adManager?.checkProvider(it.app_ads!!, Constants.adLocation1)
//
//                if (bannerProvider != null) {
//                    bindingCategory?.fbAdView?.let { it2 ->
//                        bindingCategory?.startAppBanner?.let { it3 ->
//                            adManager?.loadAdProvider(bannerProvider, Constants.adLocation1,
//                                null, it2,bindingCategory?.unityBannerView, it3
//                            )
//                        }
//                    }
//                }

            }


            if (!it.events.isNullOrEmpty()) {


                for (live_event in it.events!!) {
                    if (live_event.live!!) {
                        if (!live_event.channels.isNullOrEmpty()) {

                            ////Iterating through list of channels
                            for (live_channel in live_event.channels!!) {
                                if (live_channel.live!!) {
                                    liveChannelsList.add(live_channel)
                                }

                            }

                        }

                    }

                }


                if (liveChannelsList.isNotEmpty())
                {
                    bindingCategory?.noMatchText?.visibility=View.GONE

                    val channelRemoveDuplicate: MutableList<Channel> = ArrayList<Channel>()
                    for (element in liveChannelsList) {
                        // If this element is not present in newList
                        // then add it
                        if (!channelRemoveDuplicate.contains(element)) {
                            channelRemoveDuplicate.add(element)
                        }
                    }
                    ///checking for duplication....

                    setAdapter(channelRemoveDuplicate)
                }
                else
                {

                    bindingCategory?.noMatchText?.visibility=View.VISIBLE
                }
            }
            else
            {
                bindingCategory?.channelRecycler?.visibility=View.GONE
                bindingCategory?.noMatchText?.visibility=View.VISIBLE

                ///if event list is empty from backend...
            }

        }
    }


    private fun getListOfNotifications(context: Context?, binding: CategoriesFragmentBinding) {

        Thread {
            // do your stuff
            val database by lazy { context?.let { RoomDatabase.getDatabase(it) } }
            if (notificationlist.isNotEmpty()) {
                notificationlist.clear()
            }

            @Suppress("UNCHECKED_CAST")
            notificationlist = database?.wordDao()?.getAll() as MutableList<RoomTable>
            Utils.runOnUiThread {
                if (notificationlist.isNotEmpty()) {

                    binding.bellIcon.setImageDrawable( context?.let { ContextCompat.getDrawable(it,R.drawable.ic_bell) })



                } else {
                    binding.bellIcon.setImageDrawable( context?.let { ContextCompat.getDrawable(it,R.drawable.notification1) })

                }
                // do onPostExecute stuff
            }
        }.start()

    }

    private fun setAdapter(liveCategoriesChannel: MutableList<Channel>) {

        if (nativeAdProviderName.equals(Constants.admob,true))
        {
            liveListWithAds = checkNativeAd(liveCategoriesChannel) as MutableList<Channel>

        }
        else if (nativeAdProviderName.equals(Constants.facebook,true))
        {
            liveListWithAds = checkNativeAd(liveCategoriesChannel) as MutableList<Channel>

        }
        else
        {

           liveListWithAds=liveCategoriesChannel
        }


        val adapter= adManager?.let { it1 ->
            ChannelAdapter(requireContext(),this,liveListWithAds,nativeAdProviderName,
                it1,nativeFieldVal,"event")
        }
        bindingCategory?.channelRecycler?.layoutManager= LinearLayoutManager(context)
        bindingCategory?.channelRecycler?.adapter=adapter
        adapter?.submitList(liveListWithAds)
        searchEvents(liveCategoriesChannel)


    }


    private fun checkNativeAd(list: List<Channel>): List<Channel?> {
        val tempChannels: MutableList<Channel?> =
            ArrayList()
        for (i in list.indices) {
            if (list[i].live!!) {
                val diff = i % 5
                if (diff == 2) {

                    tempChannels.add(null)
                }
                tempChannels.add(list[i])
                if (list.size == 2) {
                    if (i == 1) {
                        tempChannels.add(null)

                    }
                }
            }
        }
        return tempChannels
    }

    private fun searchEvents(list: List<Channel>) {
        bindingCategory?.eventEdittext?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                search(s.toString(), list as MutableList<Channel>)
            }
        })
    }
    private fun search(
        text: String,
        liveAndNonemptyEvents: MutableList<Channel>
    ) {
        //new array list that will hold the filtered data
        val localList: MutableList<Channel> =
            ArrayList<Channel>()
        //looping through existing elements
        for (s in liveAndNonemptyEvents) {
            //if the existing elements contains the search input
            if (s.name?.trim()?.lowercase(Locale.ROOT)?.contains(text.lowercase(Locale.ROOT))!!) {
                //adding the element to filtered list
                localList.add(s)
            }
        }
        val adapter= context?.let { adManager?.let { it1 ->
            ChannelAdapter(it,this,localList,nativeAdProviderName,
                it1,nativeFieldVal,"event")
        } }
        bindingCategory?.channelRecycler?.layoutManager= LinearLayoutManager(context)
        bindingCategory?.channelRecycler?.adapter=adapter
        adapter?.submitList(localList)

    }

    override fun onAdLoad(value: String) {
        adStatus = value.equals("success",true)

    }

    override fun onAdFinish() {
        if (navDirections!=null)
        {
            findNavController().navigate(navDirections!!)
        }
    }

    override fun navigation(viewId: NavDirections) {
        try {
            navDirections=viewId
            if (adStatus)
            {
                if (!adProviderName.equals("none",true)) {
                    adManager?.showAds(adProviderName)
                }
                else
                {
                    findNavController().navigate(viewId)
                }
            }
            else {
                findNavController().navigate(viewId)
            }
        }
        catch (e:Exception)
        {
            Log.d("Exception","mess")
        }

    }

}