package com.mobdeve.mco.enleaset.model

import com.google.firebase.firestore.DocumentReference

data class CourseOffering(
    var courseRef: DocumentReference? = null,
    var professorRef: DocumentReference? = null,
    var course: Course? = null,
    var professor: Professor? = null,
    val slots: Int = 0,
    val slotsTaken: Int = 0,
    val availability: String = ""
)
