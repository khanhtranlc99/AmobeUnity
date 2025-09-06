package com.tbx.admodule.callback

/**
 * @author Created by TrinhBX.
 * Mail: trinhbx196@gmail.com
 * Phone: +08 988324622
 * @since Date: 14/01/2025
 **/

interface NativeAdCallback {
    fun onNativeLoaded(adUnitId: String?)
    fun onNativeFailed(adUnitId: String?, errorCode: String?)
    fun onRevenueReceivedNative(valueMicros:Long, currencyCode:String, adUnitId:String)
    fun onNativeClicked()
    fun onNativeShowed(adUnitId: String?)
    fun onNativeShowFailed(adUnitId: String?)
    fun onNativeCalculateHeight(height:Int)
}