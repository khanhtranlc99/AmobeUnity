package com.tbx.admodule

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.initialization.AdapterStatus
import com.tbx.admodule.callback.NativeAdCallback
import com.tbx.admodule.impl.AdmobNativeManagerImpl
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @author Created by TrinhBX.
 * Mail: trinhbx196@gmail.com
 * Phone: +08 988324622
 * @since Date: 14/01/2025
 **/

object AdManager {
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    @JvmStatic
    fun initialize(
        activity: Activity,
        callBackInit: (StringBuilder) -> Unit
    ) {
        initGDPR(activity, callBackInit)
    }

    private fun initAdmobManager(
        context: Context,
        callBackInit: (StringBuilder) -> Unit
    ) {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }
        val stringBuilder = StringBuilder()
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("B16C7D0555AE3049E0224CCC24D1D207")).build()
        )
        MobileAds.initialize(context) {
            val statusMap: Map<String, AdapterStatus> = it.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                stringBuilder.append(
                    String.format(
                        Locale.getDefault(),
                        "Adapter name: %s, Description: %s, Latency: %d \n",
                        adapterClass,
                        status?.description,
                        status?.latency
                    )
                )
            }
            callBackInit.invoke(stringBuilder)

        }
    }

    private const val AD_APP_ID = "ca-app-pub-3940256099942544~3347511713"

    private fun initGDPR(activity: Activity, callBackInit: (StringBuilder) -> Unit) {
//        if (BuildConfig.DEBUG) {
//            GoogleConsentManager.resetConsent(activity)
//        }
        GoogleConsentManager.gatherConsentDebugWithGeography(activity = activity,
            hashedId = "9E6EFCDACDC588A12ECBBC0B32275D62",
            appId = AD_APP_ID,
            onConsentGatheringCompleteListener = { consentError ->
                if (consentError != null) {
                    initAdmobManager(activity, callBackInit)
                }
                if (GoogleConsentManager.isRequestAds) {
                    initAdmobManager(activity, callBackInit)
                }
            })
        if (GoogleConsentManager.isRequestAds) {
            initAdmobManager(activity, callBackInit)
        }
    }

    @JvmStatic
    private val admobNativeManager: AdmobNativeManager = AdmobNativeManagerImpl()

    @JvmStatic
    fun loadNativeAd(activity: Activity, adUnitId: String?, callback: NativeAdCallback?) {
        admobNativeManager.loadNativeAd(activity, adUnitId, callback)
    }

    @JvmStatic
    fun showNativeAd(adUnitId: String?) {
        admobNativeManager.showNativeAd(adUnitId)
    }

    @JvmStatic
    fun loadAndShowNativeAd(activity: Activity, adUnitId: String?, callback: NativeAdCallback?) {
        admobNativeManager.loadAndShowNativeAd(activity, adUnitId, callback)
    }

    @JvmStatic
    fun hideAdNative(adUnitId: String?) {
        admobNativeManager.hideAdNative(adUnitId)
    }

    @JvmStatic
    fun removeAdById(adUnitId: String?) {
        admobNativeManager.removeAdById(adUnitId)
    }

    @JvmStatic
    fun release() {
        admobNativeManager.removeAllAd()
        GoogleConsentManager.release()
        isMobileAdsInitializeCalled.set(false)
    }
}