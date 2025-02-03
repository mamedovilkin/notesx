package io.github.mamedovilkin.notesx.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.notesx.R
import io.github.mamedovilkin.notesx.view.adapter.NoteRecyclerViewAdapter
import io.github.mamedovilkin.notesx.databinding.FragmentHomeBinding
import io.github.mamedovilkin.notesx.model.Note
import io.github.mamedovilkin.notesx.view.activity.MainActivity
import io.github.mamedovilkin.notesx.viewmodel.NoteViewModel
import io.github.mamedovilkin.notesx.viewmodel.NoteViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {

    private var binding: FragmentHomeBinding? = null
    @Inject lateinit var noteViewModelFactory: NoteViewModelFactory
    private val noteViewModel: NoteViewModel by viewModels { noteViewModelFactory }
    private lateinit var noteRecyclerViewAdapter: NoteRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)

        noteRecyclerViewAdapter = NoteRecyclerViewAdapter()

        binding?.notesRecyclerView?.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteRecyclerViewAdapter
        }

        fetchNotes()

        binding?.newNoteFab?.setOnClickListener {
            it.findNavController().navigate(R.id.action_home_fragment_to_new_note_fragment)
        }
    }

    private fun fetchNotes() {
        activity?.let {
            noteViewModel.getAllNotes().observe(
                viewLifecycleOwner
            ) { notes ->
                noteRecyclerViewAdapter.differ.submitList(notes)
                updateUI(notes)
            }
        }
    }

    private fun updateUI(notes: List<Note>?) {
        if (!notes.isNullOrEmpty()) {
            binding?.noNotesTextView?.visibility = View.GONE
            binding?.notesRecyclerView?.visibility = View.VISIBLE
        } else {
            binding?.noNotesTextView?.visibility = View.VISIBLE
            binding?.notesRecyclerView?.visibility = View.GONE
        }
    }

    private fun searchNote(query: String?) {
        val searchQuery = "%$query"
        noteViewModel.searchNoteBy(searchQuery).observe(this) { notes ->
            noteRecyclerViewAdapter.differ.submitList(notes)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        inflater.inflate(R.menu.home_menu, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.isSubmitButtonEnabled = false
        searchView.setMaxWidth(Integer.MAX_VALUE)
        searchView.queryHint = getString(R.string.search)
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchNote(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty()) {
            searchNote(newText)
        } else {
            fetchNotes()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}