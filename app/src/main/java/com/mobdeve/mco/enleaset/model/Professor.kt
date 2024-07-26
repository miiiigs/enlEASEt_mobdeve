package com.mobdeve.mco.enleaset.model

data class Professor (
    val lastname: String="",
    val firstname: String = "",
    val department: String = "",
    val course: List<String> = listOf()
)