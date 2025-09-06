package com.tbx.admodule

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.tbx.admodule.models.ErrorCode
import kotlin.let

object GoogleConsentManager {
    private var consentInformation: ConsentInformation? = null

    fun interface OnConsentGatheringCompleteListener {
        fun consentGatheringComplete(error: ErrorCode?)
    }

    /** Helper variable to determine if the app can request ads. */
    val isRequestAds: Boolean
        get() = consentInformation?.canRequestAds() != false

    /** Helper variable to determine if the privacy options form is required. */
    val isPrivacyOptions: Boolean
        get() =
            consentInformation?.privacyOptionsRequirementStatus ==
                    ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    /**
     * Helper method to call the UMP SDK methods to request consent information and load/show a
     * consent form if necessary.
     */
    fun gatherConsentDebugWithGeography(
        activity: Activity,
        geography: Int = ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA,
        hashedId: String? = null,
        appId: String? = null,
        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener
    ) {
        if (consentInformation == null) {
            consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        }
        //the below code is for testing purpose only. Remove it in production build.
        val debugSettings =
            ConsentDebugSettings.Builder(activity)
                .setDebugGeography(geography)
        hashedId?.let {
            debugSettings.addTestDeviceHashedId(it)
        }
        //set tag for under age of consent
        //request consent
        val params =
            ConsentRequestParameters.Builder()
                .setConsentDebugSettings(debugSettings.build())
        appId?.let {
            params.setAdMobAppId(it)
        }
        requestConsentInfoUpdate(
            activity = activity,
            params = params.build(),
            onConsentGatheringCompleteListener = onConsentGatheringCompleteListener
        )
    }

    fun gatherConsent(
        activity: Activity,
        underAge: Boolean = false,
        appId: String? = null,
        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener
    ) {
        if (consentInformation == null) {
            consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        }
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(underAge)
        appId?.let {
            params.setAdMobAppId(it)
        }
        requestConsentInfoUpdate(
            activity = activity,
            params = params.build(),
            onConsentGatheringCompleteListener = onConsentGatheringCompleteListener
        )
    }

    private fun requestConsentInfoUpdate(
        activity: Activity,
        params: ConsentRequestParameters,
        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener
    ) {
        consentInformation?.requestConsentInfoUpdate(
            activity,
            params,
            { // The consent information state was updated, ready to check if a form is available.
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { error ->
                    onConsentGatheringCompleteListener.consentGatheringComplete(
                        ErrorCode(
                            code = error?.errorCode,
                            message = error?.message
                        )
                    )

                }
            },
            { formError ->
                onConsentGatheringCompleteListener.consentGatheringComplete(
                    ErrorCode(
                        code = formError.errorCode,
                        message = formError.message
                    )
                )
            }
        )
    }

    fun showPrivacyOptionsForm(
        activity: Activity,
        onConsentFormDismissedListener: ConsentForm.OnConsentFormDismissedListener
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
    }

    fun resetConsent(context: Context) {
        if (consentInformation == null) {
            consentInformation = UserMessagingPlatform.getConsentInformation(context)
        }
        consentInformation?.reset()
    }

    fun release() {
        consentInformation = null
    }
}