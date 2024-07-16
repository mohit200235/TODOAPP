package com.example.notesapp

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.data.Network.NetworkUtils
import com.example.notesapp.databinding.ActivityAddNewTodoBinding
import com.example.notesapp.presentation.viewmodels.UpdateViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddNewTodoActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddNewTodoBinding
    lateinit var viewModel: UpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //register viewmodel
        viewModel = ViewModelProvider(this)[UpdateViewModel::class.java]

        val getIntentNotesId = intent.extras

        val id = getIntentNotesId?.getInt("notes_id")
        if (id != null) {
            initialCondition(id)
        }

    }

    private fun initialCondition(id: Int) {
        lifecycleScope.launch {
            viewModel.getTodoById(id).collectLatest { note ->
                if (note != null) {
                    binding.desc.setText(note.todo)
                    binding.isCompleted.isChecked = note.completed
                }
            }

            binding.SaveTodo.visibility = View.GONE
            binding.updateTodo.visibility = View.VISIBLE
        }

        binding.updateTodo.setOnClickListener {
            updateTodo(id)
        }

        binding.deleteNote.setOnClickListener {
            deleteTodo(id)
        }
    }

    private fun updateTodo(id:Int) {
        val isCompleted1 = binding.isCompleted.isChecked
        val content = binding.desc.text.toString()

        lifecycleScope.launch {
            viewModel.updateTodo(id,content,isCompleted1)
            var isSuccess  = false
            if (NetworkUtils.isOnline(this@AddNewTodoActivity)) {
                isSuccess = viewModel.updateTodoToServer(id, content, isCompleted1)
            }
            if (!isSuccess){
                Toast.makeText(this@AddNewTodoActivity, "TODO updated successfully in local database", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@AddNewTodoActivity, "TODO updated successfully in server also.", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

//    private fun saveNewTodo() {
//        binding.SaveTodo.setOnClickListener {
//
//            val content = binding.desc.text.toString()
////            viewModel.saveTODO(title,content)
//            finish()
//            Toast.makeText(this, "TODO saved Successfully", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun deleteTodo(id:Int){
        lifecycleScope.launch {
            viewModel.deleteTodo(id)
            viewModel.deleteTodoInServer(id)
            finish()
        }
    }
}