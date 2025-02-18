package io.github.mamedovilkin.notesx.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.notesx.R
import io.github.mamedovilkin.notesx.databinding.FragmentEditNoteBinding
import io.github.mamedovilkin.notesx.model.Note
import io.github.mamedovilkin.notesx.view.activity.MainActivity
import io.github.mamedovilkin.notesx.viewmodel.NoteViewModel
import io.github.mamedovilkin.notesx.viewmodel.NoteViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class EditNoteFragment : Fragment(R.layout.fragment_edit_note) {

    private var binding: FragmentEditNoteBinding? = null
    @Inject lateinit var noteViewModelFactory: NoteViewModelFactory
    private val noteViewModel: NoteViewModel by viewModels { noteViewModelFactory }
    private val args: EditNoteFragmentArgs by navArgs()
    private lateinit var note: Note
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.note = args.note!!
        this.view = view
        (activity as MainActivity).supportActionBar?.title = getString(R.string.edit_note)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding?.titleEditText?.setText(note.title)
        binding?.contentEditText?.setText(note.content)
        binding?.saveNoteFab?.setOnClickListener {
            saveNote(it)
        }
    }

    private fun saveNote(view: View) {
        val title = binding?.titleEditText?.text.toString().trim()
        val content = binding?.contentEditText?.text.toString().trim()

        if (content.isNotEmpty()) {
            val note = Note(note.id, title, content, note.color)
            noteViewModel.updateNote(note)
            Toast.makeText(view.context,
                getString(R.string.note_has_been_edited_successfully), Toast.LENGTH_LONG).show()
            view.findNavController().navigate(R.id.action_edit_note_fragment_to_home_fragment)
        } else {
            Toast.makeText(view.context, getString(R.string.fields_cannot_be_empty), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(view.context).apply {
            setTitle(getString(R.string.delete_note))
            setMessage(getString(R.string.delete_message, note.title))
            setPositiveButton(getString(R.string.delete)) { _,_ ->
                noteViewModel.deleteNote(note)
                Toast.makeText(view.context, getString(R.string.note_has_been_deleted_successfully), Toast.LENGTH_LONG).show()
                view.findNavController().navigate(R.id.action_edit_note_fragment_to_home_fragment)
            }
            setNeutralButton(getString(R.string.cancel), null)
        }.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.edit_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_note -> {
                deleteNote()
            }
            android.R.id.home -> {
                view.findNavController().navigate(R.id.action_edit_note_fragment_to_home_fragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}