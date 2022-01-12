package com.judahben149.note.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.judahben149.note.model.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table WHERE deleted_status=0 ORDER BY id ASC")
    fun readAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("UPDATE note_table SET deleted_status = 1,time_deleted= :time_deleted WHERE deleted_status = 0 ")
    suspend fun sendAllNotesToTrash(time_deleted: Long)

    @Query("SELECT * FROM note_table WHERE deleted_status = 0 AND (note_title LIKE :searchQuery OR note_body LIKE :searchQuery)")
    fun searchDatabase(searchQuery: String): LiveData<List<Note>>

    //methods for favorite notes
    @Query("SELECT * FROM note_table WHERE favorite_status = 1 AND deleted_status = 0")
    fun readAllFavoriteNotes(): LiveData<List<Note>>

    //methods for deleted notes
    @Query("SELECT * FROM note_table WHERE deleted_status = 1")
    fun readAllDeletedNotes(): LiveData<List<Note>>

    @Query("UPDATE note_table SET deleted_status = 0 WHERE deleted_status = 1")
    suspend fun restoreNotesFromTrash()

    @Query("DELETE FROM note_table WHERE deleted_status = 1")
    suspend fun deleteAllDeletedNotes()

}