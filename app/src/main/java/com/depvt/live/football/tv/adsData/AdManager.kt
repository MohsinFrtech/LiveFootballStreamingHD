package com.depvt.live.football.tv.adsData

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chartboost.sdk.Chartboost
import com.chartboost.sdk.ads.Interstitial
import com.chartboost.sdk.callbacks.InterstitialCallback
import com.chartboost.sdk.events.*
import com.chartboost.sdk.privacy.model.CCPA
import com.chartboost.sdk.privacy.model.COPPA
import com.chartboost.sdk.privacy.model.GDPR
import com.depvt.live.football.tv.utils.Logger
import com.depvt.live.football.tv.utils.interfaces.AdManagerListener
import com.depvt.live.football.tv.utils.objects.Constants
import com.facebook.ads.*
import com.facebook.ads.AdError
import com.facebook.ads.AdSize
import com.startapp.sdk.ads.banner.Banner
import com.startapp.sdk.ads.banner.BannerListener
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.LayoutFbNativeAdBinding
import com.depvt.live.football.tv.models.AppAd
import com.unity3d.ads.*
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

class AdManager(
    private val context: Context,
    private val activity: Activity,
    val adManagerListener: AdManagerListener
) {

    private val logger = Logger()
    private var adView: NativeAdView? = null
    private var nativeAdLayout: NativeAdLayout? = null
    private var chartboostInterstitial: Interstitial? = null
    private var fbNativeAd: NativeAd? = null
    private var facebookbinding: LayoutFbNativeAdBinding? = null
    private var bottomBanner: BannerView? = null
    private var bottomAdUnitId = "Banner_Android"
    private val taG = "AdManagerClass"
    private var bannerAdValue = ""
    private var interstitialAdValue = ""
    private var nativeAdValue = ""
    private var startAppAd: StartAppAd? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var facebookinterstitial: com.facebook.ads.InterstitialAd? = null
    private var adProvider = ""
    private var topBannerUnity: BannerView? = null

    ///function will return provider
    fun checkProvider(list: List<AppAd>, location: String): String {
        adProvider = "none"
        for (listItem in list) {
            if (listItem.enable == true) {
                if (!listItem.ad_locations.isNullOrEmpty()) {
                    for (adLocation in listItem.ad_locations!!) {
                        if (adLocation.title.equals(location, true)) {
                            if (listItem.ad_provider.equals(Constants.admob, true)) {
                                adProvider = Constants.admob
                                checkAdValue(adLocation.title, listItem.ad_key, adProvider)
                            } else if (listItem.ad_provider.equals(Constants.facebook, true)) {
                                adProvider = Constants.facebook
                                checkAdValue(adLocation.title, listItem.ad_key, adProvider)
                            } else if (listItem.ad_provider.equals(Constants.unity, true)) {
                                adProvider = Constants.unity
                                checkAdValue(adLocation.title, listItem.ad_key, adProvider)
                            } else if (listItem.ad_provider.equals(Constants.chartBoost, true)) {
                                adProvider = Constants.chartBoost
                                checkAdValue(adLocation.title, listItem.ad_key, adProvider)
                            } else if (listItem.ad_provider.equals(Constants.startApp, true)) {
                                adProvider = Constants.startApp
                                checkAdValue(adLocation.title, listItem.ad_key, adProvider)
                            }

                        }


                    }

                }


            }

        }

        ////If provider exist then initialize sdk of the particular provider
        return adProvider
    }

    private fun checkAdValue(adLocation: String?, adKey: String?, provider: String) {

        if (adLocation.equals(Constants.adMiddle, true) || adLocation.equals(
                Constants.adBefore,
                true
            )
            || adLocation.equals(Constants.adAfter, true)
        ) {
            interstitialAdValue = adKey.toString()
            if (provider.equals(Constants.chartBoost, true)) {
                if (interstitialAdValue.contains(Constants.mySecretCheckDel)) {
                    val yourArray: List<String> =
                        interstitialAdValue.split(Constants.mySecretCheckDel)
                    Constants.chartBoostAppID = yourArray[0].trim()
                    Constants.chartBoostAppSig = yourArray[1].trim()
                }
            } else if (provider.equals(Constants.admob, true)) {
                Constants.admobInterstitial = interstitialAdValue
            } else if (provider.equals(Constants.facebook, true)) {
                Constants.facebookPlacementIdInterstitial = interstitialAdValue
            } else if (provider.equals(Constants.startApp, true)) {
                Constants.startAppId = interstitialAdValue
            } else if (provider.equals(Constants.unity, true)) {
                Constants.unityGameID = interstitialAdValue
            }

        } else if (adLocation.equals(Constants.nativeAdLocation, true)) {
            nativeAdValue = adKey.toString()

            if (provider.equals(Constants.admob, true)) {
                Constants.nativeAdmob = nativeAdValue

            } else if (provider.equals(Constants.facebook, true)) {
                Constants.nativeFacebook = nativeAdValue
            }

        } else if (adLocation.equals(Constants.adLocation1, true)
            || adLocation.equals(Constants.adLocation2top, true)
            || adLocation.equals(Constants.adLocation2bottom, true)
            || adLocation.equals(Constants.adLocation2topPermanent, true)
        ) {
            bannerAdValue = adKey.toString()
            if (provider.equals(Constants.admob, true)) {
                Constants.admobBannerId = bannerAdValue
            } else if (provider.equals(Constants.facebook, true)) {
                Constants.fbPlacementIdBanner = bannerAdValue
            } else if (provider.equals(Constants.unity, true)) {
                Constants.unityGameID = bannerAdValue

            } else if (provider.equals(Constants.startApp, true)) {
                Constants.startAppId = bannerAdValue

            }


        }
    }

    fun loadAdProvider(
        provider: String,
        adLocation: String,
        adView: LinearLayout?,
        linearLayout: LinearLayout?,
        relativeLayout: RelativeLayout?,
        startAppBanner: Banner?
    ) {


        if (provider.equals(Constants.admob, true)) {
//            adMobSdkInitializationOrAdmobAd(adLocation,adView,linearLayout,relativeLayout,startAppBanner)
        } else if (provider.equals(Constants.facebook, true)) {
            facebookSdkInitialization(
                adLocation,
                adView,
                linearLayout,
                relativeLayout,
                startAppBanner
            )
        } else if (provider.equals(Constants.unity, true)) {

            unitySdkInitialization(adLocation, adView, linearLayout, relativeLayout, startAppBanner)
        } else if (provider.equals(Constants.chartBoost, true)) {
            chartboostSdkInitialization(
                adLocation,
                adView,
                linearLayout,
                relativeLayout,
                startAppBanner
            )
        } else if (provider.equals(Constants.startApp, true)) {
            startAppSdkInitialization(
                adLocation,
                adView,
                linearLayout,
                relativeLayout,
                startAppBanner
            )
        }
    }


    private fun unitySdkInitialization(
        adLocation: String,
        adView: LinearLayout?,
        linearLayout: LinearLayout?,
        relativeLayout: RelativeLayout?,
        banner: Banner?
    ) {


        if (Constants.isUnitySdkInit) {

            loadAdAtParticularLocation(
                adLocation,
                Constants.unity, adView, linearLayout, relativeLayout, banner
            )
        } else {
            UnityAds.initialize(
                context,
                Constants.unityGameID,
                Constants.unityTestMode,
                object : IUnityAdsInitializationListener {
                    override fun onInitializationComplete() {
                        if (UnityAds.isInitialized()) {

                            Constants.isUnitySdkInit = true
                            loadAdAtParticularLocation(
                                adLocation,
                                Constants.unity, adView, linearLayout, relativeLayout, banner
                            )

                        }

                    }

                    override fun onInitializationFailed(
                        p0: UnityAds.UnityAdsInitializationError?,
                        p1: String?
                    ) {
                        Constants.isUnitySdkInit = false

                    }
                })

        }
    }


    private fun setUpUnityBannerBottom(relativeLayout: RelativeLayout?) {

        relativeLayout?.removeAllViews()
        bottomBanner = BannerView(activity, bottomAdUnitId, UnityBannerSize(320, 50))
        val bannerListener: BannerView.IListener = object : BannerView.IListener {
            override fun onBannerLoaded(bannerAdView: BannerView) {
                // Called when the banner is loaded.
                logger.printLog(taG, "unityLoaded")

                if (bottomBanner != null) {
                    showBanner(relativeLayout, bottomBanner!!)
                }

            }

            override fun onBannerFailedToLoad(
                bannerAdView: BannerView,
                errorInfo: BannerErrorInfo
            ) {
                logger.printLog(taG, "unityFailed" + "  " + errorInfo.errorMessage)
            }

            override fun onBannerClick(bannerAdView: BannerView) {

            }

            override fun onBannerLeftApplication(bannerAdView: BannerView) {
            }
        }
        bottomBanner?.listener = bannerListener
        bottomBanner?.load()
    }


    private fun setUpUnityBanner(relativeLayout: RelativeLayout?) {
        // Listener for banner events:
        relativeLayout?.removeAllViews()
        topBannerUnity = BannerView(activity, bottomAdUnitId, UnityBannerSize(320, 50))
        val bannerListener: BannerView.IListener = object : BannerView.IListener {
            override fun onBannerLoaded(bannerAdView: BannerView) {
                // Called when the banner is loaded.
                logger.printLog(taG, "unityLoaded")

                if (topBannerUnity != null) {
                    showBanner(relativeLayout, topBannerUnity!!)
                }

            }

            override fun onBannerFailedToLoad(
                bannerAdView: BannerView,
                errorInfo: BannerErrorInfo
            ) {
                logger.printLog(taG, "unityFailed" + "  " + errorInfo.errorMessage)
            }

            override fun onBannerClick(bannerAdView: BannerView) {

            }

            override fun onBannerLeftApplication(bannerAdView: BannerView) {
            }
        }
        topBannerUnity?.listener = bannerListener
        topBannerUnity?.load()


    }

    private fun showBanner(s: RelativeLayout?, insideBanner: BannerView) {
//        s?.removeAllViews()
        s?.addView(insideBanner)
    }


    ///Function to initialize admob sdk...
    private fun adMobSdkInitializationOrAdmobAd(
        locationName: String,
        adView: LinearLayout?,
        linearLayout: LinearLayout?,
        relativeLayout: RelativeLayout?,
        banner: Banner?
    ) {

    }


    fun showAds(adProviderShow: String) {
        if (adProviderShow.equals(Constants.admob, true)) {
            showAdmobInterstitial()
        } else if (adProviderShow.equals(Constants.unity, true)) {
            showUnityAd()
        } else if (adProviderShow.equals(Constants.chartBoost, true)) {
            showChartBoost()
        } else if (adProviderShow.equals(Constants.facebook, true)) {
            showFacebookAdInterstitial()
        } else if (adProviderShow.equals(Constants.startApp, true)) {

//                showStartAppAd()
        }

    }


    private fun showUnityAd() {
        val showListener: IUnityAdsShowListener = object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(
                placementId: String,
                error: UnityAds.UnityAdsShowError,
                message: String
            ) {
                adManagerListener.onAdFinish()

            }

            override fun onUnityAdsShowStart(placementId: String) {

            }

            override fun onUnityAdsShowClick(placementId: String) {

            }

            override fun onUnityAdsShowComplete(
                placementId: String,
                state: UnityAds.UnityAdsShowCompletionState
            ) {
                adManagerListener.onAdFinish()

            }
        }
        UnityAds.show(
            activity,
            "Interstitial_Android",
            UnityAdsShowOptions(),
            showListener
        )

    }

    private fun loadAdAtParticularLocation(
        locationName: String,
        adProviderName: String,
        adView: LinearLayout?,
        linearLayout: LinearLayout?,
        relativeLayout: RelativeLayout?,
        banner: Banner?
    ) {

        if (locationName.equals(Constants.adLocation1, true) ||
            locationName.equals(Constants.adLocation2top, true) ||
            locationName.equals(Constants.adLocation2bottom, true)
            || locationName.equals(Constants.adLocation2topPermanent, true)
        ) {

            if (adProviderName.equals(Constants.admob, true)) {
                loadAdmobBanner(adView)
            } else if (adProviderName.equals(Constants.facebook, true)) {
                loadFaceBookBannerAd(context, linearLayout)
            } else if (adProviderName.equals(Constants.unity, true)) {
                if (locationName.equals(Constants.adLocation1, true)) {
                    setUpUnityBanner(relativeLayout)
                }
                if (locationName.equals(Constants.adLocation2top, true)) {
                    setUpUnityBanner(relativeLayout)
                }

                if (locationName.equals(Constants.adLocation2bottom, true)) {
                    setUpUnityBannerBottom(relativeLayout)
                }
            } else if (adProviderName.equals(Constants.startApp, true)) {
                setStartAppBanner(banner)
            }


        } else {

            if (adProviderName.equals(Constants.admob, true)) {
//                loadAdmobInterstitialAd()
            } else if (adProviderName.equals(Constants.unity, true)) {
                loadUnityAdInterstitial()
            } else if (adProviderName.equals(Constants.chartBoost, true)) {
                loadChartBoost()
            } else if (adProviderName.equals(Constants.facebook, true)) {
                loadFacebookInterstitialAd()
            } else if (adProviderName.equals(Constants.startApp, true)) {
                loadStartAppAd()
            }

        }

    }


    private fun loadStartAppAd() {
        startAppAd = StartAppAd(context)
        startAppAd?.loadAd(object : AdEventListener {

            override fun onReceiveAd(p0: com.startapp.sdk.adsbase.Ad) {

                Log.d("StartAppAd", "ad" + p0.toString())
                showStartAppAd()

            }

            override fun onFailedToReceiveAd(p0: com.startapp.sdk.adsbase.Ad?) {
                Log.d("StartAppAd", "ad" + p0?.errorMessage)

            }
        })
    }


    private fun showStartAppAd() {
        startAppAd?.showAd(object : AdDisplayListener {
            override fun adHidden(ad: com.startapp.sdk.adsbase.Ad) {


            }

            override fun adDisplayed(ad: com.startapp.sdk.adsbase.Ad) {


            }

            override fun adClicked(ad: com.startapp.sdk.adsbase.Ad) {

            }

            override fun adNotDisplayed(ad: com.startapp.sdk.adsbase.Ad) {
            }
        })
    }

    private fun loadUnityAdInterstitial() {
        val loadListener: IUnityAdsLoadListener = object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String) {
                adManagerListener.onAdLoad("success")

            }

            override fun onUnityAdsFailedToLoad(
                placementId: String,
                error: UnityAds.UnityAdsLoadError,
                message: String
            ) {

                adManagerListener.onAdLoad("failed")


            }
        }


        UnityAds.load("Interstitial_Android", loadListener)
    }


    private fun chartboostSdkInitialization(
        adLocation: String,
        adView: LinearLayout?,
        linearLayout: LinearLayout?,
        relativeLayout: RelativeLayout?,
        banner: Banner?
    ) {
        if (Constants.isChartboostSdkInit) {
            loadAdAtParticularLocation(
                adLocation,
                Constants.chartBoost, adView, linearLayout, relativeLayout, banner
            )

        } else {
            Chartboost.addDataUseConsent(context, GDPR(GDPR.GDPR_CONSENT.BEHAVIORAL))
            Chartboost.addDataUseConsent(context, CCPA(CCPA.CCPA_CONSENT.OPT_IN_SALE))
            Chartboost.addDataUseConsent(context, COPPA(true))

            Chartboost.startWithAppId(
                context, Constants.chartBoostAppID, Constants.chartBoostAppSig
            ) { startError: StartError? ->
                if (startError == null) {
                    Constants.isChartboostSdkInit = true
                    loadAdAtParticularLocation(
                        adLocation,
                        Constants.chartBoost, adView, linearLayout, relativeLayout, banner
                    )

                    // handle success
                } else {

                    // handle failure
                }
            }
        }


    }


    private fun facebookSdkInitialization(
        adLocation: String,
        adView: LinearLayout?,
        linearLayout: LinearLayout?,
        relativeLayout: RelativeLayout?,
        banner: Banner?
    ) {
        if (Constants.isInitFacebookSdk) {
            loadAdAtParticularLocation(
                adLocation,
                Constants.facebook, adView, linearLayout, relativeLayout, banner
            )
        } else {
            AudienceNetworkAds
                .buildInitSettings(context)
                .withInitListener {
                    if (it.isSuccess) {
                        Constants.isInitFacebookSdk = true
                        loadAdAtParticularLocation(
                            adLocation,
                            Constants.facebook, adView, linearLayout, relativeLayout, banner
                        )

                    } else {

                        Constants.isInitFacebookSdk = false
                    }

                }
                .initialize()


        }


    }


    ////startapp sdk initialization....
    private fun startAppSdkInitialization(
        adLocation: String,
        adView: LinearLayout?,
        linearLayout: LinearLayout?,
        relativeLayout: RelativeLayout?,
        banner: Banner?
    ) {


        if (Constants.isStartAppSdkInit) {
            loadAdAtParticularLocation(
                adLocation,
                Constants.startApp, adView, linearLayout, relativeLayout, banner
            )
        } else {
            try {
                StartAppSDK.init(context, Constants.startAppId, false)
                StartAppAd.disableSplash()
                StartAppSDK.setTestAdsEnabled(true)
                Constants.isStartAppSdkInit = true
                loadAdAtParticularLocation(
                    adLocation,
                    Constants.startApp, adView, linearLayout, relativeLayout, banner
                )

            } catch (e: Exception) {
                Log.d("StartAppAd", "adnot" + e.message)

                logger.printLog(taG, "StartAppError" + e.message)
            }
        }


    }


    private fun setStartAppBanner(bannerView: Banner?) {
        val banner = Banner(activity, object : BannerListener {
            override fun onReceiveAd(p0: View?) {
                bannerView?.addView(p0)
                bannerView?.visibility = View.VISIBLE
                bannerView?.showBanner()
            }

            override fun onFailedToReceiveAd(p0: View?) {
                bannerView?.visibility = View.GONE

            }

            override fun onImpression(p0: View?) {

            }

            override fun onClick(p0: View?) {

            }


        })
        banner.loadAd()
    }


    //loadChartBoost
    private fun loadChartBoost() {
        chartboostInterstitial = Interstitial("location", object : InterstitialCallback {
            override fun onAdDismiss(event: DismissEvent) {
                adManagerListener.onAdFinish()
            }

            override fun onAdLoaded(event: CacheEvent, error: CacheError?) {
                adManagerListener.onAdLoad("success")
            }

            override fun onAdRequestedToShow(event: ShowEvent) {}
            override fun onAdShown(event: ShowEvent, error: ShowError?) {


            }

            override fun onAdClicked(event: ClickEvent, error: ClickError?) {
                adManagerListener.onAdFinish()
            }

            override fun onImpressionRecorded(event: ImpressionEvent) {}


        })
        chartboostInterstitial?.cache()
    }

    ///Show chartboost ads
    private fun showChartBoost(
    ) {

        if (chartboostInterstitial?.isCached() == true) { // check is cached is not mandatory
            chartboostInterstitial?.show()
        } else {
            adManagerListener.onAdFinish()
        }

    }

    ///setAdmobBanner....
    private fun loadAdmobBanner(adViewLayout: LinearLayout?) {

    }

    ///LoadFacebook banner ad.....
    private fun loadFaceBookBannerAd(context: Context?, adContainer: LinearLayout?) {
        val adView =
            com.facebook.ads.AdView(context, Constants.fbPlacementIdBanner, AdSize.BANNER_HEIGHT_50)
        // AdSettings.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        // Add the ad view to your activity layout
        adContainer?.removeAllViews()
        adContainer?.addView(adView)

        val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {
            override fun onError(ad: Ad?, adError: AdError) {
                // Ad error callback

            }

            override fun onAdLoaded(ad: Ad?) {
                // Ad loaded callback

                adContainer?.visibility = View.VISIBLE
            }

            override fun onAdClicked(ad: Ad?) {
                // Ad clicked callback
            }

            override fun onLoggingImpression(ad: Ad?) {
                // Ad impression logged callback
            }
        }
        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build())


    }


    ///Admob load function...
    private fun loadAdmobInterstitialAd() {


    }


    ////showAdmobInterstitial
    private fun showAdmobInterstitial() {


    }


    fun loadFacebookNativeAd(nativeAdLayout: NativeAdLayout) {
        fbNativeAd = NativeAd(context, Constants.nativeFacebook)
        val nativeAdListener: NativeAdListener = object : NativeAdListener {
            override fun onMediaDownloaded(ad: Ad) {

                // Native ad finished downloading all assets
            }

            override fun onError(ad: Ad?, adError: AdError) {


                // Native ad failed to load
            }

            override fun onAdLoaded(ad: Ad) {
                // Native ad is loaded and ready to be displayed
                if (fbNativeAd != null) {
                    inflateFbNativeAd(fbNativeAd!!, nativeAdLayout)

                }
            }

            override fun onAdClicked(ad: Ad) {
                // Native ad clicked
            }

            override fun onLoggingImpression(ad: Ad) {
                // Native ad impression
            }
        }

        // Request an ad
        fbNativeAd?.buildLoadAdConfig()
            ?.withAdListener(nativeAdListener)
            ?.build().let {
                fbNativeAd?.loadAd(
                    it
                )
            }
    }


    //    ////To inflate facebook native view
    private fun inflateFbNativeAd(
        fbNativeAd: NativeAd, nativeAdLayout2: NativeAdLayout
    ) {
        fbNativeAd.unregisterView()
        nativeAdLayout = nativeAdLayout2
        val inflater = LayoutInflater.from(context)
        val fbAdView =
            inflater.inflate(
                R.layout.layout_fb_native_ad,
                nativeAdLayout,
                false
            ) as LinearLayout?
        nativeAdLayout?.addView(fbAdView)
        facebookbinding = fbAdView?.let { DataBindingUtil.bind(it) }
        // Add the AdOptionsView
        val adOptionsView = AdOptionsView(context, fbNativeAd, nativeAdLayout)
        facebookbinding?.adChoicesContainer?.removeAllViews()
        facebookbinding?.adChoicesContainer?.addView(adOptionsView, 0)
        // Set the Text.
        facebookbinding?.nativeAdTitle?.text = fbNativeAd.advertiserName
        facebookbinding?.nativeAdBody?.text = fbNativeAd.adBodyText
        facebookbinding?.nativeAdSocialContext?.text = fbNativeAd.adSocialContext
        if (fbNativeAd.hasCallToAction()) {
            facebookbinding?.nativeAdCallToAction?.visibility = View.VISIBLE
        } else {
            facebookbinding?.nativeAdCallToAction?.visibility = View.INVISIBLE
        }
        facebookbinding?.nativeAdCallToAction?.text = fbNativeAd.adCallToAction
        facebookbinding?.nativeAdSponsoredLabel?.text = fbNativeAd.sponsoredTranslation

        val clickableViews = ArrayList<View>()
        facebookbinding?.nativeAdTitle?.let { clickableViews.add(it) }
        facebookbinding?.nativeAdCallToAction?.let { clickableViews.add(it) }


        fbNativeAd.registerViewForInteraction(
            fbAdView,
            facebookbinding?.nativeAdMedia,
            facebookbinding?.nativeAdIcon,
            clickableViews
        )
    }

    fun loadAdmobNativeAd(view_holder: ViewHolder, nativeAdView: NativeAdView) {


    }


    ///load facebook interstitial....
    private fun loadFacebookInterstitialAd() {
        facebookinterstitial =
            InterstitialAd(context, Constants.facebookPlacementIdInterstitial)
        val adListener = object : InterstitialAdListener {

            override fun onInterstitialDisplayed(p0: Ad?) {

            }

            override fun onAdClicked(p0: Ad?) {
                adManagerListener.onAdFinish()
            }

            override fun onInterstitialDismissed(p0: Ad?) {
                adManagerListener.onAdFinish()
            }

            override fun onError(p0: Ad?, p1: AdError?) {
                adManagerListener.onAdLoad("failed")
            }

            override fun onAdLoaded(p0: Ad?) {
                adManagerListener.onAdLoad("success")
            }

            override fun onLoggingImpression(p0: Ad?) {

            }


        }
        val loadAdConfig = facebookinterstitial!!.buildLoadAdConfig()
            .withAdListener(adListener)
            .build()

        facebookinterstitial!!.loadAd(loadAdConfig)

    }


    /// show facebook interstitial....
    private fun showFacebookAdInterstitial() {
        if (facebookinterstitial != null) {
            if (facebookinterstitial!!.isAdLoaded) {

                try {
                    facebookinterstitial!!.show()
                } catch (e: Throwable) {

                    logger.printLog(taG, "Exception" + e.message)
                }


            } else {

                adManagerListener.onAdFinish()
            }
        } else {

            adManagerListener.onAdFinish()
        }


    }

}