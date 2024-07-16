package com.example.notesapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.domain.repositories.NotesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val notesRepository = NotesRepository()

    fun saveNotes(id: Int, content: String, isCompleted: Boolean, userId: Int) {
        viewModelScope.launch {
            notesRepository.saveNotes(id,content,isCompleted,userId)
        }
    }

    val allTodos = notesRepository.allTodo.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun fetchNotesFromServer(){

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            notesRepository.fetchNotesFromServer()
        }
    }
}