package io.github.mamedovilkin.notesx.view.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.notesx.R
import io.github.mamedovilkin.notesx.databinding.FragmentNewNoteBinding
import io.github.mamedovilkin.notesx.model.Note
import io.github.mamedovilkin.notesx.view.activity.MainActivity
import io.github.mamedovilkin.notesx.viewmodel.NoteViewModel
import io.github.mamedovilkin.notesx.viewmodel.NoteViewModelFactory
import java.util.Random
import javax.inject.Inject

@AndroidEntryPoint
class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private var binding: FragmentNewNoteBinding? = null
    @Inject lateinit var noteViewModelFactory: NoteViewModelFactory
    private val noteViewModel: NoteViewModel by viewModels { noteViewModelFactory }
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = getString(R.string.new_note)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        this.view = view

        binding?.saveNoteFab?.setOnClickListener {
            saveNote(it)
        }
    }

    private fun saveNote(view: View) {
        val title = binding?.titleEditText?.text.toString().trim()
        val content = binding?.contentEditText?.text.toString().trim()

        if (content.isNotEmpty()) {
            val random = Random()
            val note = Note(
                0,
                title,
                content,
                Color.argb(
                    255,
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)
                )
            )
            noteViewModel.insertNote(note)
            Toast.makeText(view.context,
                getString(R.string.note_has_been_saved_successfully), Toast.LENGTH_LONG).show()
            view.findNavController().navigate(R.id.action_new_note_fragment_to_home_fragment)
        } else {
            Toast.makeText(view.context, getString(R.string.fields_cannot_be_empty), Toast.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                view.findNavController().navigate(R.id.action_new_note_fragment_to_home_fragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}