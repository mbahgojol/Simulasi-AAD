package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var edCourseName: TextInputEditText
    private lateinit var edLecturer: TextInputEditText
    private lateinit var edNote: TextInputEditText
    private lateinit var spDay: Spinner
    private var startTime = "00:00"
    private var endTime = "00:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)

        edCourseName = findViewById(R.id.edCourseName)
        edLecturer = findViewById(R.id.edLecturer)
        edNote = findViewById(R.id.edNote)
        spDay = findViewById(R.id.spDay)

        findViewById<ImageButton>(R.id.btnStartTime).setOnClickListener {
            val dialogFragment = TimePickerFragment()
            dialogFragment.show(supportFragmentManager, "startTime")
        }
        findViewById<ImageButton>(R.id.btnEndTime).setOnClickListener {
            val dialogFragment = TimePickerFragment()
            dialogFragment.show(supportFragmentManager, "endTime")
        }

        viewModel.saved.observe(this) {
            if (it.getContentIfNotHandled() == true) finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val crouseName = edCourseName.text.toString()
                val lecturer = edLecturer.text.toString()
                val note = edNote.text.toString()
                val day = spDay.selectedItemPosition
                viewModel.insertCourse(crouseName, day, startTime, endTime, lecturer, note)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
            .apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }

        val formatter = SimpleDateFormat("HH:mm")
        val formattedDate = formatter.format(calendar.time)

        when (tag) {
            "endTime" -> {
                endTime = formattedDate
            }
            "startTime" -> {
                startTime = formattedDate
            }
        }
        findViewById<TextView>(R.id.tvStartTime).text = startTime
        findViewById<TextView>(R.id.tvEndTime).text = endTime
    }
}