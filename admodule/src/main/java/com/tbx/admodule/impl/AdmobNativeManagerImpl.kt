package com.tbx.admodule.impl

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MediaAspectRatio
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.tbx.admodule.AdmobNativeManager
import com.tbx.admodule.CustomNativeView
import com.tbx.admodule.R
import com.tbx.admodule.callback.NativeAdCallback
import com.tbx.admodule.models.AdNativeData
import com.tbx.admodule.models.AdState


/**
 * @author Created by TrinhBX.
 * Mail: trinhbx196@gmail.com
 * Phone: +08 988324622
 * @since Date: 14/01/2025
 **/

class AdmobNativeManagerImpl : AdmobNativeManager {
    companion object {
        private const val TIME_INTERVAL = 10000L
        private const val AD_KEY_TEST = "ca-app-pub-3940256099942544/2247696110"
    }

    private val listAdmobNative = mutableMapOf<String, AdNativeData>()
    private var adViewContainer: CustomNativeView? = null
    private var activity: Activity? = null
    private var adHandler: Handler? = null

    override fun loadNativeAd(activity: Activity, unitId: String?, callback: NativeAdCallback?) {
        val adUnitId = unitId ?: AD_KEY_TEST
        if (listAdmobNative[adUnitId] == null) {
            listAdmobNative[adUnitId] = AdNativeData(adUnitId)
        }
        listAdmobNative[adUnitId]?.let { adNativeData ->
            if (adNativeData.state == AdState.LOADING || System.currentTimeMillis() - adNativeData.timestamp < TIME_INTERVAL) {
                return
            }
            adNativeData.state = AdState.LOADING
            AdLoader.Builder(activity, adUnitId).forNativeAd { ads: NativeAd ->
                this.activity = activity
                adHandler = Handler(Looper.getMainLooper())
                adNativeData.timestamp = System.currentTimeMillis()
                adNativeData.nativeAd?.destroy()
                adNativeData.nativeAd = ads
                adNativeData.nativeAdCallback = callback
                adNativeData.state = AdState.LOADED
                callback?.onNativeLoaded(adUnitId)
                ads.setOnPaidEventListener { adValue ->
                    val revenue = adValue.valueMicros
                    val currencyCode = adValue.currencyCode
                    callback?.onRevenueReceivedNative(revenue, currencyCode, adUnitId)
                }

            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    adNativeData.state = AdState.FAILED
                    callback?.onNativeFailed(adUnitId, p0.message)
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    callback?.onNativeClicked()
                }
            }).withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setMediaAspectRatio(MediaAspectRatio.ANY)
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT).build()
            ).build().loadAd(AdRequest.Builder().build())
        }
    }

    private fun addNativeViewToActivity(activity: Activity, callback: NativeAdCallback?) {
        adHandler?.post {
            adViewContainer?.release()
            (adViewContainer?.parent as? ViewGroup)?.removeView(adViewContainer)
            adViewContainer =
                activity.layoutInflater.inflate(
                    R.layout.admob_native_media_container,
                    null,
                    false
                ) as? CustomNativeView
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )
            activity.addContentView(adViewContainer, layoutParams)
            adViewContainer?.post {
                val height = adViewContainer?.height ?: 0
                callback?.onNativeCalculateHeight(height)
            }
        }
    }

    override fun loadAndShowNativeAd(
        activity: Activity,
        adUnitId: String?,
        callback: NativeAdCallback?
    ) {
        loadNativeAd(
            activity,
            adUnitId,
            callback
        )
    }

    override fun showNativeAd(adUnitId: String?) {
        val newAdUnitId = adUnitId ?: AD_KEY_TEST
        val nativeAdData = listAdmobNative[newAdUnitId]
        if (nativeAdData?.nativeAd == null) {
            adHandler?.post {
                adViewContainer?.isInvisible = true
            }
            nativeAdData?.nativeAdCallback?.onNativeShowFailed(adUnitId)
        } else {
            activity?.let {
                addNativeViewToActivity(activity = it, nativeAdData.nativeAdCallback)
                adHandler?.post {
                    adViewContainer?.isInvisible = false
                    adViewContainer?.bindNativeAd(nativeAdData.nativeAd)
                }
                nativeAdData.nativeAdCallback?.onNativeShowed(adUnitId)
            }
        }
    }

    override fun hideAdNative(adUnitId: String?) {
        adHandler?.post {
            adViewContainer?.isInvisible = true
        }
    }

    override fun removeAdById(adUnitId: String?) {
        adHandler?.post {
            adViewContainer?.isInvisible = true
            adViewContainer?.release()
            (adViewContainer?.parent as? ViewGroup)?.removeView(adViewContainer)
        }
        adHandler?.removeCallbacksAndMessages(null)
        adHandler = null
        listAdmobNative[adUnitId]?.nativeAd?.destroy()
        listAdmobNative.remove(adUnitId)
        adViewContainer = null
        activity = null
    }

    override fun removeAllAd() {
        listAdmobNative.forEach {
            it.value.nativeAd?.destroy()
            it.value.nativeAd = null
        }
        listAdmobNative.clear()
    }
}