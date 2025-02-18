package io.github.mamedovilkin.notesx.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.mamedovilkin.notesx.databinding.NoteRecyclerViewItemBinding
import io.github.mamedovilkin.notesx.model.Note
import io.github.mamedovilkin.notesx.view.fragment.HomeFragmentDirections

class NoteRecyclerViewAdapter : RecyclerView.Adapter<NoteRecyclerViewAdapter.NotesRecyclerViewViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.title == newItem.title
                    && oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesRecyclerViewViewHolder {
        return NotesRecyclerViewViewHolder(
            NoteRecyclerViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: NotesRecyclerViewViewHolder, position: Int) {
        val note = differ.currentList[position]

        holder.setNote(note)

        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(note)
            it.findNavController().navigate(direction)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class NotesRecyclerViewViewHolder(private val binding: NoteRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setNote(note: Note) {
            binding.titleTextText.visibility = if (note.title.isEmpty()) View.GONE else View.VISIBLE
            binding.note = note
            binding.colorView.setBackgroundColor(note.color)
        }
    }
}