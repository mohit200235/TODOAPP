package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.data.Network.NetworkUtils
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.domain.adapter.NotesAdapter
import com.example.notesapp.domain.model.Notes
import com.example.notesapp.presentation.viewmodels.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    var notesList: List<Notes> = emptyList()
    lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intiallize viewmodel here
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        if (notesList.isEmpty() || NetworkUtils.isOnline(this@MainActivity)) {
            viewModel.fetchNotesFromServer()
        } else if (!NetworkUtils.isOnline(this@MainActivity)) {
            Toast.makeText(this@MainActivity, "Networks not available!", Toast.LENGTH_SHORT).show()
        } else {
            //should works on persistence data
        }

        //setup adapter
        notesAdapter = NotesAdapter(this, notesList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }

        lifecycleScope.launch {
            viewModel.allTodos.collectLatest { todo ->
                notesList = todo
                notesAdapter.refreshNotes(notesList)
            }
        }

        binding.refresh.setOnClickListener {
            if (NetworkUtils.isOnline(this@MainActivity)) {
                viewModel.fetchNotesFromServer()
            }
        }

        binding.addTodo.setOnClickListener {
            val intent = Intent(this, AddNewTodoActivity::class.java)
            startActivity(intent)
        }
    }
}