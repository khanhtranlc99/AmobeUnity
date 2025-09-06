# Added to prevent warnings related to JSpecify nullness annotations.
-keep interface com.tbx.admodule.callback.NativeAdCallback {
    *;
}

-keep interface com.tbx.admodule.AdmobNativeManager {
    *;
}
-keep class com.tbx.admodule.AdManager{
    public <fields>;
    public <methods>;
}

-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn org.jspecify.nullness.**
-keep class com.google.android.ads.** { *; }