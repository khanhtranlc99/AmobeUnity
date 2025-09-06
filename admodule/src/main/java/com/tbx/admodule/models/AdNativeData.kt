package com.tbx.admodule.models

import com.google.android.gms.ads.nativead.NativeAd
import com.tbx.admodule.callback.NativeAdCallback


/**
 * @author Created by TrinhBX.
 * Mail: trinhbx196@gmail.com
 * Phone: +08 988324622
 * @since Date: 14/01/2025
 **/

data class AdNativeData(
    val unitAd: String,
    var nativeAd: NativeAd? = null,
    var state: AdState = AdState.NONE,
    var timestamp: Long = 0,
    var nativeAdCallback: NativeAdCallback? = null
)