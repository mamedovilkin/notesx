package io.github.mamedovilkin.notesx.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.mamedovilkin.notesx.database.NoteDatabase
import io.github.mamedovilkin.notesx.repository.NoteRepository
import io.github.mamedovilkin.notesx.viewmodel.NoteViewModelFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return NoteDatabase.invoke(context)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDatabase: NoteDatabase): NoteRepository {
        return NoteRepository(noteDatabase)
    }

    @Provides
    @Singleton
    fun provideNoteViewModelFactory(application: Application, noteRepository: NoteRepository): NoteViewModelFactory {
        return NoteViewModelFactory(application, noteRepository)
    }
}