package com.mobdeve.mco.enleaset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.mco.enleaset.R
import com.mobdeve.mco.enleaset.model.Professor

class ProfessorAdapter (private val professorList: List<Professor>) : RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {

    class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val professorNameTextView: TextView = itemView.findViewById(R.id.tv_prof_name)
        val professorDepartmentTextView: TextView = itemView.findViewById(R.id.tv_prof_department)
        val professorCoursesTextView: TextView = itemView.findViewById(R.id.tv_prof_courses)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_professor, parent, false)
        return ProfessorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = professorList[position]
        holder.professorNameTextView.text = "${professor.lastname}, ${professor.firstname}"
        holder.professorDepartmentTextView.text = professor.department
        holder.professorCoursesTextView.text = professor.course.joinToString(", ")

    }

    override fun getItemCount() = professorList.size
}