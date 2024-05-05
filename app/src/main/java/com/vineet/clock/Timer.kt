package com.vineet.clock

import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.core.view.isVisible
import com.vineet.clock.databinding.FragmentStopwatchBinding
import com.vineet.clock.databinding.FragmentTimerBinding
import kotlinx.coroutines.Delay
import kotlin.concurrent.fixedRateTimer
import java.util.Timer
import java.util.concurrent.TimeUnit


class Timer : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0
    private var selectedSecond: Int = 0
    private var timer: Timer? = null
    private var remainingTime: Int = 0
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.iphone_alarm)
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vibrator: Vibrator =
            requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        binding.textViewTimer.isVisible = false
        binding.buttonEndTimer.isVisible = false
        binding.numberPickerHours.minValue = 0
        binding.numberPickerHours.maxValue = 72
        binding.numberPickerMinutes.maxValue = 60
        binding.numberPickerMinutes.minValue = 0
        binding.numberPickerSeconds.minValue = 0
        binding.numberPickerSeconds.maxValue = 60

        binding.numberPickerHours.setOnValueChangedListener { picker, oldVal, newVal ->

            vibrator.vibrate(100)
            selectedHour = newVal
        }
        binding.numberPickerMinutes.setOnValueChangedListener { picker, oldVal, newVal ->
            vibrator.vibrate(100)
            selectedMinute = newVal

        }
        binding.buttonEndTimer.setOnClickListener() {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.isLooping = false
                mediaPlayer.stop()
            }
            binding.buttonEndTimer.isVisible = false
        }

        binding.numberPickerSeconds.setOnValueChangedListener { picker, oldVal, newVal ->
            vibrator.vibrate(100)
            selectedSecond = newVal
        }
        binding.buttonTimerStart.setOnClickListener() {
            if (binding.buttonTimerStart.text == "Start") {
                if (selectedSecond != 0 || selectedHour != 0 || selectedMinute != 0) {
                    HandleUIElements(false)
                    binding.buttonTimerStart.text = "Pause"
                    binding.buttonTimerStart.background =
                        resources.getDrawable(R.drawable.roundedbuttonred, null)
                    binding.buttonTimerReset.isEnabled = true
                    val totalSeconds =
                        (selectedHour * 3600) + (selectedMinute * 60) + selectedSecond
                    Thread.sleep(1000)
                    StartCountDown(totalSeconds)
                }
            } else if (binding.buttonTimerStart.text == "Pause") {
                binding.buttonTimerStart.text = "Resume"
                timer?.cancel()
                binding.buttonTimerReset.isEnabled = true
            } else if (binding.buttonTimerStart.text == "Resume") {
                binding.buttonTimerStart.text = "Pause"
                StartCountDown(remainingTime)
                binding.buttonTimerReset.isEnabled = true
            }


        }
        binding.buttonTimerReset.setOnClickListener() {
            HandleUIElements(true)
            binding.buttonTimerReset.isEnabled = true
            timer?.cancel()
            binding.buttonTimerStart.text = "Start"
            binding.textViewTimer.text = "00:00:00"
        }
    }

    private fun StartCountDown(totalSeconds: Int) {
        var tSeconds = totalSeconds
        timer = fixedRateTimer("timer", period = 1000L) {
            activity?.runOnUiThread {
                tSeconds--
                remainingTime = tSeconds

                var hoursToShow: String =
                    String.format("%02d", TimeUnit.SECONDS.toHours(tSeconds.toLong()))
                var minutesToShow: String = String.format(
                    "%02d",
                    TimeUnit.SECONDS.toMinutes(tSeconds.toLong()) % TimeUnit.HOURS.toMinutes(1)
                )
                var secondsToShow: String = String.format(
                    "%02d",
                    TimeUnit.SECONDS.toSeconds(tSeconds.toLong()) % TimeUnit.MINUTES.toSeconds(1)
                )
                binding.textViewTimer.text = "$hoursToShow:$minutesToShow:$secondsToShow"
                if (TimeUnit.SECONDS.toHours(tSeconds.toLong())
                        .toString() == "0" && (TimeUnit.SECONDS.toMinutes(tSeconds.toLong()) % TimeUnit.HOURS.toMinutes(
                        1
                    )).toString() == "0" && (TimeUnit.SECONDS.toSeconds(tSeconds.toLong()) % TimeUnit.MINUTES.toSeconds(
                        1
                    )).toString() == "0"
                ) {
                    timer?.cancel()
                    binding.textViewTimer.text = "$hoursToShow:$minutesToShow:$secondsToShow"
                    mediaPlayer.isLooping = true
                    mediaPlayer.start()
                    binding.buttonEndTimer.isVisible = true

                }
            }
        }

    }

    private fun DisplayMessageBox() {
        val view =
            AlertDialog.Builder(requireContext()).setView(R.layout.activity_custom_alertdialog)
        view.create()
    }

    private fun HandleUIElements(makePickerVisible: Boolean) {
        if (makePickerVisible) {
            binding.textViewTimer.isVisible = false
            binding.numberPickerHours.isVisible = true
            binding.numberPickerMinutes.isVisible = true
            binding.numberPickerSeconds.isVisible = true
            binding.textViewColon1.isVisible = true
            binding.textViewColon2.isVisible = true
        } else {
            binding.numberPickerHours.isVisible = false
            binding.numberPickerMinutes.isVisible = false
            binding.numberPickerSeconds.isVisible = false
            binding.textViewColon1.isVisible = false
            binding.textViewColon2.isVisible = false
            binding.textViewTimer.isVisible = true

        }

    }

}