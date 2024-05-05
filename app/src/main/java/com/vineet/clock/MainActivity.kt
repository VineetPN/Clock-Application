package com.vineet.clock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.navigation.NavigationBarView
import com.vineet.clock.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var InflateFragmentOnce : Int = 0
    companion object {
        var ListTimeZones: List<String>? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationBar.setOnItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        if(InflateFragmentOnce == 0) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, WorldClock())
                .commitNow()
            InflateFragmentOnce += 1
        }
    }
    override fun onResume() {
        super.onResume()
        // Call the API to store the timezones into Data class

        val serviceBuilder = ServiceBuilder.buildService(IServiceFunctions::class.java)
        val requestCall = serviceBuilder.getAllTimeZones()

        requestCall.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if(response.isSuccessful) {
                    MainActivity.ListTimeZones = response.body()!!
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("On Failure of GetAllTieZones", "onFailure: ${t.message}")
            }

        })

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_navi_world_clock -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_layout, WorldClock())
                    commit()
                }
                return true
            }

            R.id.bottom_navi_alarm -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_layout, Alarm())
                    commit()
                }
                return true
            }

            R.id.bottom_navi_stop_watch -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_layout, Stopwatch())
                    commit()
                }
                return true
            }

            R.id.bottom_navi_timer -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_layout, Timer())
                    commit()
                }
                return true
            }

            else -> {
                return false
            }
        }
    }
}