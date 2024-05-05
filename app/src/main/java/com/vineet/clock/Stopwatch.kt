package com.vineet.clock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vineet.clock.databinding.FragmentStopwatchBinding
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class Stopwatch : Fragment() {
    private lateinit var binding: FragmentStopwatchBinding
    private var timer: Timer? = null
    companion object {
        private var isRunning: Boolean = false
        private var iSeconds: Int = 0
        private var iHours: Int = 0
        private var iMinutes: Int = 0
        private var specialFlag: Boolean = false // This is a special flag which is used to manage only the bottom navigation bar switching scenarios
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStart.setOnClickListener() {
            specialFlag = false
            StartStopWatch()
        }
        binding.buttonPause.setOnClickListener() {
            ResetStopWatch()
        }

    }

    override fun onStart() {
        super.onStart()
        if(isRunning || specialFlag) {

            var secondsToShow: String = String.format("%02d", iSeconds)
            var minutesToShow: String = String.format("%02d", iMinutes)
            var hoursToShow: String = String.format("%02d", iHours)
            binding.textViewStopwatchTime.text = "$hoursToShow:$minutesToShow:$secondsToShow"
            if(!specialFlag) {
                StopWatchLogic()
            }
        }
    }

    private fun ResetStopWatch(){
        timer?.cancel()
        isRunning = false
        binding.textViewStopwatchTime.text = "00:00:00"
        binding.buttonStart.background = resources.getDrawable(R.drawable.roundedbutton, null)
        binding.buttonPause.isEnabled = false
        binding.buttonStart.text = "Start"
        iSeconds = 0
        iHours = 0
        iMinutes = 0
    }
    private fun StartStopWatch() {
        if(binding.buttonStart.text == "Stop"){
            timer?.cancel()
            isRunning = false
            specialFlag = true
            binding.buttonStart.text = "Start"
            binding.buttonStart.background = resources.getDrawable(R.drawable.roundedbutton, null)
            binding.buttonPause.text = "Reset"
            binding.buttonPause.isEnabled = true
        }
        else {
            isRunning = true
            StopWatchLogic()
        }
    }
    private fun StopWatchLogic() {
        timer = fixedRateTimer("timer", period = 1000L) {
            activity?.runOnUiThread {
                binding.buttonStart.text = "Stop"
                binding.buttonStart.background = resources.getDrawable(R.drawable.roundedbuttonred, null)
                binding.buttonPause.isEnabled = true

                binding.buttonPause.text = "Reset"
                binding.buttonPause.isEnabled = true

                iSeconds++
                var seconds = iSeconds
                if (seconds == 60) {
                    iMinutes++
                    iSeconds = 0
                }
                if (iMinutes == 60) {
                    iHours++
                    iMinutes = 0
                }
                //timeOnStopWatch.postValue()
                var secondsToShow: String = String.format("%02d", iSeconds)
                var minutesToShow: String = String.format("%02d", iMinutes)
                var hoursToShow: String = String.format("%02d", iHours)
                binding.textViewStopwatchTime.text = "$hoursToShow:$minutesToShow:$secondsToShow"
            }
        }
    }

}