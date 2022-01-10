package com.judahben149.note.repository

import androidx.lifecycle.LiveData
import com.judahben149.note.data.NoteDao
import com.judahben149.note.model.Note

class NoteRepository(private val noteDao: NoteDao) {

    val readAllNotes: LiveData<List<Note>> = noteDao.readAllNotes()
    val readAllDeletedNotes: LiveData<List<Note>> = noteDao.readAllDeletedNotes()



    suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun sendAllNotesToTrash(time_deleted: Long) {
        noteDao.sendAllNotesToTrash(time_deleted)
    }

    //methods for deleted notes
    suspend fun deleteAllDeletedNotes() {
        noteDao.deleteAllDeletedNotes()
    }

    suspend fun restoreNotesFromTrash() {
        noteDao.restoreNotesFromTrash()
    }

}