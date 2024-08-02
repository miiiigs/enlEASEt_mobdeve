package com.mobdeve.mco.enleaset

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.mobdeve.mco.enleaset.model.Professor

class FilterDialogFragment: DialogFragment() {
    interface FilterDialogListener {
        fun onFilterApplied(
            availability: String?,
            day: String?,
            time: String?,
            professor: String?
        )
    }

    private lateinit var radioGroupAvailability: RadioGroup
    private lateinit var spinnerDay: Spinner
    private lateinit var spinnerTime: Spinner
    private lateinit var spinnerProfessor: Spinner
    private lateinit var buttonApplyFilters: Button
    private lateinit var buttonRemoveFilters: Button

    private var listener: FilterDialogListener? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var professorList = listOf<Professor>()
    private var timeSlots = listOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)

        radioGroupAvailability = view.findViewById(R.id.radioGroupAvailability)
        spinnerDay = view.findViewById(R.id.spinner_day)
        spinnerTime = view.findViewById(R.id.spinner_time)
        spinnerProfessor = view.findViewById(R.id.spinner_professor)
        buttonApplyFilters = view.findViewById(R.id.button_apply_filters)
        buttonRemoveFilters = view.findViewById(R.id.button_remove_filters)

        sharedPreferences = requireContext().getSharedPreferences("FilterPrefs", Context.MODE_PRIVATE)

        populateSpinners()
        loadFilterPreferences()

        buttonApplyFilters.setOnClickListener {
            val availability = when (radioGroupAvailability.checkedRadioButtonId) {
                R.id.radio_open -> "open"
                R.id.radio_closed -> "closed"
                R.id.radio_waitlist -> "waitlist"
                else -> null
            }
            val day = if (spinnerDay.selectedItemPosition != 0) spinnerDay.selectedItem?.toString() else null
            val time = if (spinnerTime.selectedItemPosition != 0) spinnerTime.selectedItem?.toString() else null
            val professor = if (spinnerProfessor.selectedItemPosition != 0) spinnerProfessor.selectedItem?.toString() else null

            saveFilterPreferences(availability, day, time, professor)
            listener?.onFilterApplied(availability, day, time, professor)
            dismiss()
        }

        buttonRemoveFilters.setOnClickListener {
            resetFilters()
            saveFilterPreferences(null, null, null, null)
            listener?.onFilterApplied(null, null, null, null)
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    fun setFilterDialogListener(listener: FilterDialogListener) {
        this.listener = listener
    }

    fun setProfessorList(professors: List<Professor>) {
        this.professorList = professors
    }

    fun setTimeSlots(timeSlots: List<String>) {
        this.timeSlots = timeSlots
    }

    private fun populateSpinners() {
        val initialTimeSlots = listOf("-","7:30 - 9:00", "09:15 - 10:45", "11:00 - 12:30", "12:45 - 14:15", "14:30 - 16:00", "16:15 - 17:45", "18:00 - 19:30", "19:45 - 21:15")

        val days = listOf("-", "MH","TF", "WS", "M", "T", "W", "H", "F", "S")
        val dayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, days)
        dayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerDay.adapter = dayAdapter

        val times = initialTimeSlots + timeSlots.filter { !initialTimeSlots.contains(it) }.sorted()
        val timeAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, times)
        timeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerTime.adapter = timeAdapter

        val professorNames = professorList.map { it.firstname + " " + it.lastname }.toMutableList()
        professorNames.add(0, "-")
        val professorAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, professorNames)
        professorAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerProfessor.adapter = professorAdapter
    }
    private fun saveFilterPreferences(availability: String?, day: String?, time: String?, professor: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("availability", availability)
        editor.putString("day", day)
        editor.putString("time", time)
        editor.putString("professor", professor)
        editor.apply()
    }
    private fun loadFilterPreferences() {
        val availability = sharedPreferences.getString("availability", "all")
        val day = sharedPreferences.getString("day", "-")
        val time = sharedPreferences.getString("time", "-")
        val professor = sharedPreferences.getString("professor", "-")

        when (availability) {
            "open" -> radioGroupAvailability.check(R.id.radio_open)
            "closed" -> radioGroupAvailability.check(R.id.radio_closed)
            "waitlist" -> radioGroupAvailability.check(R.id.radio_waitlist)
            else -> radioGroupAvailability.check(R.id.radio_all)
        }

        spinnerDay.setSelection(getSpinnerIndex(spinnerDay, day))
        spinnerTime.setSelection(getSpinnerIndex(spinnerTime, time))
        spinnerProfessor.setSelection(getSpinnerIndex(spinnerProfessor, professor))
    }
    private fun getSpinnerIndex(spinner: Spinner, value: String?): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == value) {
                return i
            }
        }
        return 0
    }
    private fun resetFilters() {
        radioGroupAvailability.check(R.id.radio_all)
        spinnerDay.setSelection(0)
        spinnerTime.setSelection(0)
        spinnerProfessor.setSelection(0)
    }
}