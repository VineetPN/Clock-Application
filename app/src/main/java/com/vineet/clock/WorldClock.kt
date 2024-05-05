package com.vineet.clock

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Intents
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.gson.JsonElement
import com.vineet.clock.databinding.FragmentWorldClockBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class WorldClock : Fragment() {

    private lateinit var binding: FragmentWorldClockBinding
    private var recyclerView: RecyclerView? = null
    private var timeZone: String = ""
    private var time: String = ""
    private val REQUEST_CODE_SELECTION = 101


    companion object {
        var selectedTimeZone: String = ""
        var listOfSelectedTimeZones: HashMap<String, String>? = null
        var listZone = mutableListOf<String>()
        var listTime = mutableListOf<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentWorldClockBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECTION && resultCode == Activity.RESULT_OK) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        AlertDialog.Builder(requireContext()).setMessage(selectedTimeZone).show()
        binding.buttonAddNewTimezone.setOnClickListener() {
            // Get the class of the child fragment then replace and commit
            try {
                val intent = Intent(requireContext(), NewTimeZoneList::class.java)
                startActivityForResult(intent, REQUEST_CODE_SELECTION)
            } catch (e: Exception) {
                Log.e("buttonAddNewTimezone.setOnClickListener", "onViewCreated: ${e.message}")
            }
        }


        if (selectedTimeZone != "") {
            SetSelectedTimeZone(selectedTimeZone, false)
        }


    }

    private fun SetSelectedTimeZone(selectedItem: String, makeAPICall: Boolean) {
        if (makeAPICall) {
            if (!listZone.contains(selectedItem)) {
                val serviceBuilder = ServiceBuilder.buildService(IServiceFunctions::class.java)
                val reqestCall = serviceBuilder.getTimeZone(selectedItem)
                reqestCall.enqueue(object : Callback<JsonElement> {
                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("GETTime", "onFailure: ${t.message}")
                    }

                    override fun onResponse(
                        call: Call<JsonElement>, response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            val jsonElement = response.body()!!
                            if (jsonElement != null) {
                                if (jsonElement.isJsonObject) {
                                    val jsonObject = jsonElement.asJsonObject
                                    time = jsonObject.get("time").toString()
                                    timeZone = jsonObject.get("timeZone").toString()
                                    AddTimeZoneToDataClassAndDisplay(
                                        timeZone.split('\"')[1].toString(),
                                        time.split('\"')[1].toString()
                                    )
                                }
                            }
                        }
                    }

                })
            }
        }

    }


    override fun onResume() {
        super.onResume()
        if (!RecyclerViewAdapterNewTimeZoneAddition.isSelectionDone) {
            val adapterFragmentChange = RecyclerViewAdapterWorldClock(WorldClock, WorldClock)
            recyclerView = view?.findViewById(R.id.recyclerView)
            recyclerView?.adapter = adapterFragmentChange
            recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        }
        if (selectedTimeZone != "") {
            if (RecyclerViewAdapterNewTimeZoneAddition.isSelectionDone) {
                SetSelectedTimeZone(selectedTimeZone, true)
                RecyclerViewAdapterNewTimeZoneAddition.isSelectionDone = false
                return
            }
        }
    }

    private fun AddTimeZoneToDataClassAndDisplay(timeZone: String, time: String) {
        listOfSelectedTimeZones?.put(timeZone, time)
        listZone.add(timeZone)
        listTime.add(time)
        val adapter = RecyclerViewAdapterWorldClock(WorldClock, WorldClock)
        recyclerView = view?.findViewById(R.id.recyclerView)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

    }

}