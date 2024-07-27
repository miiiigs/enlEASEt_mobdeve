package com.mobdeve.mco.enleaset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.mco.enleaset.R
import com.mobdeve.mco.enleaset.model.CourseOffering

class CourseOfferingAdapter (private val courseOfferingList: List<CourseOffering>) :
    RecyclerView.Adapter<CourseOfferingAdapter.CourseOfferingViewHolder>() {

    class CourseOfferingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseNameTextView: TextView = itemView.findViewById(R.id.tv_course_name)
        val professorNameTextView: TextView = itemView.findViewById(R.id.tv_prof_name)
        val slotsTextView: TextView = itemView.findViewById(R.id.tv_course_slots)
        val availabilityButton: Button = itemView.findViewById(R.id.bt_course_action)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseOfferingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_offering, parent,  false)
        return CourseOfferingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseOfferingViewHolder, position: Int) {
        val courseOffering = courseOfferingList[position]
        holder.courseNameTextView.text = courseOffering.course?.code
        holder.professorNameTextView.text = "${courseOffering.professor?.firstname} ${courseOffering.professor?.lastname}"
        holder.slotsTextView.text = "${courseOffering.slotsTaken}/${courseOffering.slots} SLOTS TAKEN"
        if(courseOffering.availability == "open"){
            holder.availabilityButton.text = "ADD COURSE"
        }else{
            holder.availabilityButton.text = "COURSE FULL"
        }
    }

    override fun getItemCount() = courseOfferingList.size
}