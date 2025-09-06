package com.tbx.admodule

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlin.let

open class CustomNativeView : FrameLayout {
    private var templateType = 0
    private var nativeAdView: NativeAdView? = null
    private var primaryView: TextView? = null
    private var secondaryView: TextView? = null
    private var ratingBar: RatingBar? = null
    private var tertiaryView: TextView? = null
    private var iconView: ImageView? = null
    private var callToActionView: Button? = null
    private var callToActionOverlay: TextView? = null
    private var mediaView: MediaView? = null
    private var background: ViewGroup? = null
    private var textAd: TextView? = null
    private var imgClose: ImageView? = null

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val attributes =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomNativeView, 0, 0
            )
        templateType = getTemplateType(attributes)
        attributes.recycle()
        initLayout(context, templateType)
    }

    private fun initLayout(context: Context, layoutRes: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(layoutRes, this)
    }

    private fun getTemplateType(attributes: TypedArray): Int {
        return attributes.getResourceId(
            R.styleable.CustomNativeView_ad_view_layout,
            R.layout.admob_native_media_view
        )
    }

    private fun initIds() {
        nativeAdView = findViewById<NativeAdView>(R.id.native_ad_view)
        primaryView = findViewById<TextView>(R.id.primary)
        tertiaryView = findViewById<TextView>(R.id.body)
//        secondaryView = findViewById<TextView>(R.id.secondary)
        ratingBar =  findViewById<RatingBar?>(R.id.rating_bar)
        callToActionView = findViewById<Button>(R.id.cta)
        callToActionOverlay = findViewById<TextView>(R.id.tvCtaOverlay)
        iconView = findViewById<ImageView>(R.id.icon)
        background = findViewById<ViewGroup>(R.id.background)
        mediaView = findViewById<MediaView>(R.id.media_view)
        textAd = findViewById<TextView>(R.id.ad_notification_view)
        imgClose = findViewById<ImageView>(R.id.imvDismiss)
        imgClose?.setOnClickListener {
            mediaView?.isGone = true
            imgClose?.isGone = true
        }
    }

    fun adHasOnlyStore(nativeAd: NativeAd?): Boolean {
        val store = nativeAd?.store
        val advertiser = nativeAd?.advertiser
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
    }

    fun setTemplateType(int: Int) {
        templateType = int
        removeAllViews()
        initLayout(this.context, templateType)
        initIds()
    }

    fun getTemplateType(): Int {
        return templateType
    }

    fun setBackgroundNative(backgroundRes: Int) {
        background?.setBackgroundResource(backgroundRes)
    }

    fun setBackgroundTint(colorRes: Int) {
        background?.let {
            ViewCompat.setBackgroundTintList(
                it,
                ColorStateList.valueOf(colorRes)
            )
        }
    }

    fun setPrimaryColor(textColor: Int) {
        primaryView?.setTextColor(textColor)
    }

    fun setPrimarySize(textSize: Float) {
        primaryView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setSecondaryColor(textColor: Int) {
        secondaryView?.setTextColor(textColor)
    }

    fun setSecondarySize(textSize: Float) {
        secondaryView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setBodyColor(textColor: Int) {
        tertiaryView?.setTextColor(textColor)
    }

    fun setBodySize(textSize: Float) {
        tertiaryView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setCallTextColor(textColor: Int) {
        callToActionView?.setTextColor(textColor)
    }

    fun setCallTextSize(textSize: Float) {
        callToActionView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setCallBackground(bgRes: Int) {
        callToActionView?.setBackgroundResource(bgRes)
    }

    fun setTextADColor(textColor: Int) {
        textAd?.setTextColor(textColor)
    }

    fun setTextADBackground(bgRes: Int) {
        textAd?.setBackgroundResource(bgRes)
    }

    fun setTextAdTint(colorRes: Int) {
        textAd?.let {
            ViewCompat.setBackgroundTintList(
                it,
                ColorStateList.valueOf(colorRes)
            )
        }
    }

    fun setTextAd(text: String) {
        textAd?.text = text
    }

    fun setTextAdSize(textSize: Float) {
        textAd?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initIds()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("CustomNativeView", "onDetachedFromWindow: ")
        nativeAdView = null
        primaryView = null
        secondaryView = null
        ratingBar = null
        tertiaryView = null
        iconView = null
        callToActionView = null
        callToActionOverlay = null
        mediaView = null
        background = null
        textAd = null
        imgClose = null
    }

    fun release() {
        Log.d("CustomNativeView", "release: ${nativeAdView != null}")
        nativeAdView?.destroy()
    }

    fun bindNativeAd(nativeAd: NativeAd?) {
        nativeAdView = findViewById<NativeAdView>(R.id.native_ad_view)
        val store = nativeAd?.store
        val advertiser = nativeAd?.advertiser
        val headline = nativeAd?.headline
        val body = nativeAd?.body
        val cta = nativeAd?.callToAction
        val starRating = nativeAd?.starRating
        val icon = nativeAd?.icon

        nativeAdView?.callToActionView = callToActionView
        nativeAdView?.headlineView = primaryView
        mediaView?.visibility = View.VISIBLE
        imgClose?.visibility = View.VISIBLE
        nativeAdView?.mediaView = mediaView
        secondaryView?.visibility = View.VISIBLE
        secondaryView?.visibility = View.VISIBLE
        val secondaryText: String?

        if (adHasOnlyStore(nativeAd)) {
            nativeAdView?.storeView = secondaryView
            secondaryText = store
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView?.advertiserView = secondaryView
            secondaryText = advertiser
        } else {
            secondaryText = ""
        }
        primaryView?.text = headline
        callToActionView?.let { button ->
            val ctaText = try {
                cta?.lowercase()?.let {
                    it.replaceFirst(it.first(), it.first().uppercaseChar())
                }
            } catch (e: Exception) {
                null
            }
            button.text = ctaText ?: cta
            button.isAllCaps = false
            callToActionOverlay?.text = ctaText ?: cta
        }

        if (secondaryView != null) {
            secondaryView?.text = secondaryText
            secondaryView?.visibility = View.VISIBLE
            ratingBar?.visibility = View.GONE
        } else if ((starRating != null) && (starRating > 0)) {
            ratingBar?.alpha = 1f
            ratingBar?.visibility = View.VISIBLE
            ratingBar?.max = 5
            ratingBar?.stepSize = 0.1f
            ratingBar?.rating = starRating.toFloat()
            nativeAdView?.starRatingView = ratingBar
        }
        if (icon != null) {
            iconView?.visibility = View.VISIBLE
            iconView?.setImageDrawable(icon.drawable)
        } else {
            iconView?.visibility = View.INVISIBLE
        }
        tertiaryView?.text = body
        nativeAdView?.bodyView = tertiaryView
        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        nativeAdView?.setNativeAd(nativeAd ?: return)
    }
}