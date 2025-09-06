package com.tbx.admanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.tbx.admanager.databinding.ActivityMainBinding
import com.tbx.admodule.AdManager
import com.tbx.admodule.callback.NativeAdCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val nativeKey = "ca-app-pub-3940256099942544/2247696110"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnShowAd.isEnabled = false
        binding.btnDestroy.isEnabled = false
        binding.btnHide.isEnabled = false
        AdManager.initialize(activity = this) {
            binding.btnShowAd.isEnabled = true
            binding.btnDestroy.isEnabled = true
            binding.btnHide.isEnabled = true
        }
        binding.btnShowAd.setOnClickListener {
            AdManager.loadNativeAd(this, nativeKey, object : NativeAdCallback {
                override fun onNativeClicked() {
                    Log.e("AD", "CLICKED")
                }

                override fun onNativeFailed(adUnitId: String?, errorCode: String?) {
                    Log.e("AD", "FAILED")
                }

                override fun onNativeLoaded(adUnitId: String?) {
                    Log.e("AD", "LOADED")
                }

                override fun onNativeShowFailed(adUnitId: String?) {
                    Log.e("AD", "SHOW FAILED")
                }

                override fun onNativeCalculateHeight(height: Int) {
                    Log.e("AD", "HEIGHT $height")
                }

                override fun onNativeShowed(adUnitId: String?) {
                    Log.e("AD", "SHOWED")
                }

                override fun onRevenueReceivedNative(
                    valueMicros: Long,
                    currencyCode: String,
                    adUnitId: String
                ) {
                    Log.e("AD", "REVENUE")
                }
            })
        }
        binding.btnHide.setOnClickListener {
            lifecycleScope.launch (Dispatchers.IO){
                AdManager.hideAdNative(nativeKey)
            }
        }
        binding.btnDestroy.setOnClickListener {
            lifecycleScope.launch (Dispatchers.IO){
                AdManager.showNativeAd(nativeKey)
            }
        }
    }

    override fun onDestroy() {
        Log.e("AD", "DESTROY")
        AdManager.removeAdById(nativeKey)
        super.onDestroy()
    }
}