package com.vineet.clock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vineet.clock.databinding.ActivityNewTimeZoneListBinding

class NewTimeZoneList : AppCompatActivity(), IRecyclerViewCommunicator {
    companion object {
        fun finishActivity() {

        }
    }

    private lateinit var binding: ActivityNewTimeZoneListBinding
    private var recyclerViewNewAddition: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTimeZoneListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = RecyclerViewAdapterNewTimeZoneAddition(MainActivity.Companion)
        recyclerViewNewAddition = findViewById(R.id.recycler_view_new_timezone_addition)
        recyclerViewNewAddition?.adapter = adapter
        recyclerViewNewAddition?.layoutManager = LinearLayoutManager(this)

        adapter.finishActivityLiveData.observe(this) {
            finish()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.frame_layout, WorldClock())
//                .commit()
        }
    }


    override fun OnitemSelected(selectedValue: String) {
        Toast.makeText(this, selectedValue, Toast.LENGTH_LONG).show()
        finish()
    }

}