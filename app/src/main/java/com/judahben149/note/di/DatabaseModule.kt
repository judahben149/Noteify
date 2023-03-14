package com.judahben149.note.di

import android.content.Context
import androidx.room.Room
import com.judahben149.note.NoteDatabase
import com.judahben149.note.note.data.NoteDao
import com.judahben149.note.todo.data.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun providesTodoDao(database: NoteDatabase) : TodoDao {
        return database.todoDao()
    }
}