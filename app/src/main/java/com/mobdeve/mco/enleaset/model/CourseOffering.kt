package com.mobdeve.mco.enleaset.model

import com.google.firebase.firestore.DocumentReference

data class CourseOffering(
    val courseRef: DocumentReference? = null,
    val professorRef: DocumentReference? = null,
    val course: Course? = null,
    val professor: Professor? = null,
    val slots: Int = 0,
    val slotsTaken: Int = 0,
    val availability: String = ""
)
