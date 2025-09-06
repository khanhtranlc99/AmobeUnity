package com.tbx.admodule.models

import android.os.Parcelable
import androidx.annotation.Keep

/**
 * @author
 * Created by Trinh BX.
 * Mail: trinhbx196@gmail.com.
 * Phone: +084 988 324 622.
 * @since 19/02/2025
 */
/**
 * ads_unit : get adUnit by placement
 * enable  : turn on / turn off placement
 * show_direct : show direct now or time ads ( open - inter )
 * ctr_config : config information ctr control
 * banner_config : banner config control default value  BannerConfig(position = null, collapsible = false)
 * native_config : native config control default value
 * NativeConfig(
 *             timeDelay = 0L, countTime = 0L, nativeType = NativeType.NORMAL.nativeType,
 *             nativeKeyPlacement = null, sizeButtons = listOf(15, 25, 40)
 *)
 * back_up : back up ads control default value
 * BackupAdmob(backUpUnit = null, backupEnable = false, timeReload = 3L)
 */

//@Keep
//@Parcelize
//data class PlacementItem(
//    @Json(name = "ads_unit") val adsUnit: String? = null,
//    @Json(name = "enable") val enable: Boolean = true,
//    @Json(name = "show_direct") val showDirect: Boolean = true,
//    @Json(name = "ctr_config") val ctrConfig: CTRConfig = CTRConfig(),
//    @Json(name = "banner_config") val bannerConfig: BannerConfig = BannerConfig.DEFAULT,
//    @Json(name = "native_config") val nativeConfig: NativeConfig = NativeConfig.DEFAULT,
//    @Json(name = "back_up") val backupAdmob: BackupAdmob = BackupAdmob.DEFAULT
//) : Parcelable