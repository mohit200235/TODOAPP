package com.example.notesapp.domain.model

data class UpdateTodoRequest(
    val todo: String,
    val completed: Boolean
)
