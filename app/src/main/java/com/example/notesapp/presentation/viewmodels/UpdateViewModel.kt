package com.example.notesapp.presentation.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.domain.model.Notes
import com.example.notesapp.domain.repositories.UpdateRepository
import kotlinx.coroutines.flow.Flow

class UpdateViewModel : ViewModel() {

    private val updateRepository = UpdateRepository()


    suspend fun getTodoById(notesId: Int): Flow<Notes?> {
        return updateRepository.findNoteById(notesId)
    }

    suspend fun updateTodo(notesId: Int, newTodo: String, isCompleted: Boolean) {
        return updateRepository.updateTodo(notesId, newTodo, isCompleted)
    }

    fun updateTodoToServer(notesId: Int, todo: String, completed: Boolean): Boolean {
        return updateRepository.updateTodoInServer(notesId, todo, completed)
    }

    suspend fun deleteTodo(notesId: Int) {
        return updateRepository.deleteTodo(notesId)
    }

    fun deleteTodoInServer(noteId: Int): Boolean {
        var isSuccess:Boolean= false
        updateRepository.deleteTodoInServer(noteId) { success ->
            isSuccess = success
        }
        return isSuccess
    }
}