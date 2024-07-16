package com.example.notesapp.domain.repositories

import android.util.Log
import com.example.notesapp.Application
import com.example.notesapp.data.API.ApiInterface
import com.example.notesapp.data.API.RetrofitClass
import com.example.notesapp.domain.model.Notes
import com.example.notesapp.domain.model.UpdateTodoRequest
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UpdateRepository {

    private val realm = Application.realm


    suspend fun findNoteById(noteId: Int): Flow<Notes?> = flow {
        val result = realm.query<Notes>("_id == $0", noteId).find().first()
        emit(result)
    }

    suspend fun updateTodo(noteId: Int, newTodo: String, isCompleted: Boolean) {
        realm.write {
            val note = query<Notes>("_id == $0", noteId).first().find()
            if (note != null) {
                findLatest(note)?.apply {
                    this.todo = newTodo
                    this.completed = isCompleted
                }
            }
        }
    }

    fun updateTodoInServer(id: Int, todo: String, completed: Boolean): Boolean {
        var getSuccessfulResponse: Boolean = false
        val updateResult = RetrofitClass.getInstance().create(ApiInterface::class.java)

        try {
            val call = updateResult.updateTodo(id, UpdateTodoRequest(todo, completed))
            call.enqueue(object : Callback<Notes> {
                override fun onResponse(call: Call<Notes>, response: Response<Notes>) {
                    if (response.isSuccessful) {
                        getSuccessfulResponse = true
                    } else {
                        getSuccessfulResponse = false
                    }
                }

                override fun onFailure(call: Call<Notes>, t: Throwable) {
                    getSuccessfulResponse = false
                }
            })
        } catch (e: IOException) {
            throw e
        }
        return getSuccessfulResponse
    }

    suspend fun deleteTodo(noteId: Int) {
        realm.write {
            val frogToDelete: Notes = query<Notes>("_id == $0", noteId).find().first()
            delete(frogToDelete)
        }
    }

    fun deleteTodoInServer(id: Int, callback: (Boolean) -> Unit) {
        val deleteResult = RetrofitClass.getInstance().create(ApiInterface::class.java)

        val call = deleteResult.deleteTodo(id)
        call.enqueue(object : Callback<Notes> {
            override fun onResponse(call: Call<Notes>, response: Response<Notes>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Notes>, t: Throwable) {
                callback(false)
            }
        })
    }

}