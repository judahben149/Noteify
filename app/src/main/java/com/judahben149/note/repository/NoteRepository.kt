package com.judahben149.note.repository

import androidx.lifecycle.LiveData
import com.judahben149.note.data.NoteDao
import com.judahben149.note.model.Note

class NoteRepository(private val noteDao: NoteDao) {

    val readAllNotes: LiveData<List<Note>> = noteDao.readAllNotes()
    val readAllFavoriteNotes: LiveData<List<Note>> = noteDao.readAllFavoriteNotes()
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

    suspend fun sendSingleNoteToTrash(time_deleted: Long, id: Int) {
        noteDao.sendSingleNoteToTrash(time_deleted, id)
    }

    suspend fun sendAllNotesToTrash(time_deleted: Long) {
        noteDao.sendAllNotesToTrash(time_deleted)
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Note>> {
        return noteDao.searchDatabase(searchQuery)
    }

    //methods for favorite notes
    suspend fun addSingleNoteToFavorites(id: Int) {
        noteDao.addSingleNoteToFavorites(id)
    }

    suspend fun removeAllNotesFromFavorites() {
        noteDao.removeAllNotesFromFavorites()
    }

    suspend fun sendAllFavoriteNotesToTrash(time_deleted: Long) {
        noteDao.sendAllFavoriteNotesToTrash(time_deleted)
    }

    //methods for deleted notes
    suspend fun deleteAllDeletedNotes() {
        noteDao.deleteAllDeletedNotes()
    }

    suspend fun restoreNotesFromTrash() {
        noteDao.restoreNotesFromTrash()
    }
}