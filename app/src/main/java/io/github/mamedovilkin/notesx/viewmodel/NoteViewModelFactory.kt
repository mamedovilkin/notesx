package io.github.mamedovilkin.notesx.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.mamedovilkin.notesx.repository.NoteRepository
import javax.inject.Inject

class NoteViewModelFactory @Inject constructor(
    private val application: Application,
    private val noteRepository: NoteRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(application, noteRepository) as T
    }
}