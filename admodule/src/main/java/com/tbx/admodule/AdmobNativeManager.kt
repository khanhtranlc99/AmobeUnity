package com.tbx.admodule

import android.app.Activity
import com.tbx.admodule.callback.NativeAdCallback


/**
 * @author Created by TrinhBX.
 * Mail: trinhbx196@gmail.com
 * Phone: +08 988324622
 * @since Date: 14/01/2025
 **/

interface AdmobNativeManager {
    fun loadNativeAd(activity: Activity, adUnitId: String?, callback: NativeAdCallback?)

    fun loadAndShowNativeAd(activity: Activity, adUnitId: String?, callback: NativeAdCallback?)

    fun showNativeAd(adUnitId: String?)

    fun hideAdNative(adUnitId: String?)

    fun removeAdById(adUnitId: String?)

    fun removeAllAd()
}