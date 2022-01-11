package com.judahben149.note.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.judahben149.note.databinding.NoteItemBinding
import com.judahben149.note.fragments.Notes.NoteListFragmentDirections
import com.judahben149.note.model.Note
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class NoteListAdapter(): RecyclerView.Adapter<NoteListAdapter.NoteListRecyclerViewViewHolder>() {

        var noteList = emptyList<Note>()


        inner class NoteListRecyclerViewViewHolder(private val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root) {
            fun bindItem(position: Int) {
                val currentNote = noteList[position]

                val prettyTime: String = PrettyTime().format(Date(currentNote.timeUpdated))

                binding.tvNoteTitle.text = currentNote.noteTitle
                binding.tvNoteDescription.text = currentNote.noteBody
                binding.tvNoteDate.text = prettyTime

                binding.noteItem.setOnClickListener {
                    val action = NoteListFragmentDirections.actionNoteListFragmentToNoteDetailsFragment(currentNote)
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListRecyclerViewViewHolder {
            val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return NoteListRecyclerViewViewHolder(binding)
        }

        override fun onBindViewHolder(holder: NoteListRecyclerViewViewHolder, position: Int) {
            holder.bindItem(position)
        }

        override fun getItemCount() = noteList.size


        fun setData(note: List<Note>) {
            this.noteList = note
            notifyDataSetChanged()
        }
}