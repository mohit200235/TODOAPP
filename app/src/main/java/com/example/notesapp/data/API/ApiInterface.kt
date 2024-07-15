package com.example.notesapp.data.API

import com.example.notesapp.domain.model.Notes
import com.example.notesapp.domain.model.UpdateTodoRequest
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    @GET("todos")
    suspend fun getNotes():JsonObject

    @PUT("todos/{id}")
    fun updateTodo(
        @Path("id") id: Int,
        @Body updatedTodo: UpdateTodoRequest
    ): Call<Notes>

    @DELETE("todos/{id}")
    fun deleteTodo(
        @Path("id") id: Int
    ): Call<Notes>
}