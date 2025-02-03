package io.github.mamedovilkin.notesx.repository

import io.github.mamedovilkin.notesx.model.Note
import io.github.mamedovilkin.notesx.database.NoteDatabase
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDatabase: NoteDatabase) {

    suspend fun insertNote(note: Note) = noteDatabase.getNoteDAO().insertNote(note)

    suspend fun updateNote(note: Note) = noteDatabase.getNoteDAO().updateNote(note)

    suspend fun deleteNote(note: Note) = noteDatabase.getNoteDAO().deleteNote(note)

    fun getAllNotes() = noteDatabase.getNoteDAO().getAllNotes()

    fun searchNoteBy(query: String?) = noteDatabase.getNoteDAO().searchNoteBy(query)
}