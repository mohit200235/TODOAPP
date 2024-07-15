package com.example.notesapp.domain.adapter

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.AddNewTodoActivity
import com.example.notesapp.R
import com.example.notesapp.domain.model.Notes

class NotesAdapter(private val context: Context, private var notesList: List<Notes>) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val isCompleted: CheckBox = itemView.findViewById(R.id.isCompleted)
        val updateNote: ImageView = itemView.findViewById(R.id.edit_Note)
        val deleteNote: ImageView = itemView.findViewById(R.id.deleteNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notes_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = notesList[position]
        holder.title.text = todo.todo
        holder.isCompleted.isChecked = todo.completed

        holder.updateNote.setOnClickListener {
            val intent = Intent(context, AddNewTodoActivity::class.java)
            intent.putExtra("notes_id",todo._id)
            Log.d("checkids", "onBindViewHolder: ${todo._id} + ${todo._userId}")
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteNote.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setMessage("Are you sure ou wan't to delete this item ?")
            dialog.setTitle("Alert!")
            dialog.setPositiveButton("delete") { _, _ ->
//                notesList[position].id?.let { it -> databaseHelper.deleteDataBase(it) }
//                refreshNotes(databaseHelper.showNotes())
                Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
    }

    override fun getItemCount() = notesList.size

    fun refreshNotes(newNote: List<Notes>) {
        notesList = newNote
        notifyDataSetChanged()
    }
}