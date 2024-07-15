package com.example.notesapp.domain.repositories

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.notesapp.Application
import com.example.notesapp.data.API.ApiInterface
import com.example.notesapp.data.API.RetrofitClass
import com.example.notesapp.domain.model.Notes
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class NotesRepository {

    private val realm = Application.realm

    suspend fun saveNotes(id: Int, content: String, isCompleted: Boolean, userId: Int) {
        realm.write {
            val note = Notes().apply {
                _id = id
                todo = content
                completed = isCompleted
                _userId = userId
            }
            Log.d("notesfromserver", "saveNotes: saved notes")
            copyToRealm(note, UpdatePolicy.ALL)
        }
    }

    val allTodo = realm
        .query<Notes>()
        .asFlow()
        .map { result ->
            result.list.toList()
        }

    private fun checkForAlreadyData() : Boolean{
        val isData= realm.query<Notes>().find()
        return isData.isEmpty()
    }

    suspend fun fetchNotesFromServer() {
        if (checkForAlreadyData()) {
            val notesApiResult = RetrofitClass.getInstance().create(ApiInterface::class.java)

            try {
                val result = notesApiResult.getNotes()
                if (!result.isJsonNull) {
                    Log.d("notesfromserver", "fetchNotesFromServer: -> " + result.asJsonObject)
                    if (result.isJsonObject) {
                        val todos = result.getAsJsonArray("todos")
                        if (todos.isJsonArray) {
                            for (i in 0 until todos.size() - 1) {
                                val todoJson = todos[i].asJsonObject
                                if (todoJson.isJsonObject) {
                                    saveNotes(
                                        todoJson.get("id").asInt,
                                        todoJson.get("todo").asString,
                                        todoJson.get("completed").asBoolean,
                                        todoJson.get("userId").asInt
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                throw e
            }
        }
    }

}