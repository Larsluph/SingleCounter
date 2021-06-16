package com.larsluph.singlecounter

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var counter = 0
        set(value) {
            field = value
            updateText()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val plus = findViewById<Button>(R.id.buttonCounterPlus)
        val minus = findViewById<Button>(R.id.buttonCounterMinus)

        findViewById<TextView>(R.id.counterView).setOnLongClickListener{
            buildPopup(getString(R.string.title_set), InputType.TYPE_NUMBER_FLAG_SIGNED) { counter = it }
            true
        }

        plus.setOnClickListener { counter += 1 }
        plus.setOnLongClickListener {
            buildPopup(getString(R.string.title_add), InputType.TYPE_CLASS_NUMBER) { counter += it }
            true
        }
        minus.setOnClickListener { counter-- }
        minus.setOnLongClickListener {
            buildPopup(getString(R.string.title_sub), InputType.TYPE_CLASS_NUMBER) { counter -= it }
            true
        }
    }

    override fun onPause() {
        super.onPause()
        saveCounter()
    }

    override fun onResume() {
        super.onResume()
        readCounter()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.reset) {
            counter = 0
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateText() {
        findViewById<TextView>(R.id.counterView).text = counter.toString()
    }

    private fun saveCounter() {
        val sp = getSharedPreferences("com.larsluph.simplecounter", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putInt("CounterData", counter)
        editor.apply()
        updateText()
    }

    private fun readCounter() {
        val sp = getSharedPreferences("com.larsluph.simplecounter", MODE_PRIVATE)
        counter = sp.getInt("CounterData", 0)
        updateText()
    }

    private fun buildPopup(title: String, inputType: Int, callback: (Int) -> Unit) {
        val input = EditText(this)
        input.inputType = inputType
        AlertDialog.Builder(this)
                .setTitle(title)
                .setView(input)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    try {
                        callback(input.text.toString().toInt())
                    } catch (numberFormatException: NumberFormatException) {}
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                .show()
    }
}
