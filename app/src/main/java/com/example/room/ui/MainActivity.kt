package com.example.room.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.room.R
import com.example.room.database.Note
import com.example.room.database.NoteDao
import com.example.room.database.NoteRoomDatabase
import com.example.room.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    private fun getAllNotes() {
        mNotesDao.allNotes.observe(this) { notes ->
            val adapter: ArrayAdapter<Note> = ArrayAdapter<Note>(
                this,
                android.R.layout.simple_list_item_1, notes
            )
            binding.listView.adapter = adapter
        }
    }

    private fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    private fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    private fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }
    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
    private fun setEmptyField() {
        with(binding){
            edtTitle.setText("")
            edtDesc.setText("")
            edtDate.setText("")
        }
    }


}