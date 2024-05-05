package com.vineet.clock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog

class list_view : AppCompatActivity() {
    private val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        findByID()
        button.setOnClickListener {
            showSpinnerDialog()
        }
    }

    private fun showSpinnerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_list_view, null)
        val listView: ListView = dialogView.findViewById(R.id.listView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            button.text = selectedItem // Set the selected item text to the button
            dialog.dismiss()
        }
    }

    fun findByID() {
        button = findViewById(R.id.button_add_new_timezone)
    }
}