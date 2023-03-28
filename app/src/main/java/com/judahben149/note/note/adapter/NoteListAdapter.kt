package com.judahben149.note.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.judahben149.note.util.LongPressed
import com.judahben149.note.databinding.NoteItemBinding
import com.judahben149.note.note.fragment.noteList.NoteListFragmentDirections
import com.judahben149.note.note.model.Note
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class NoteListAdapter(val context: Context, private val longPressed: LongPressed, val onItemClicked: (noteId: Int) -> Unit): ListAdapter<Note, NoteListAdapter.NoteListRecyclerViewViewHolder>(NoteDiffUtil()) {

        var noteList = emptyList<Note>()


        inner class NoteListRecyclerViewViewHolder(private val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root) {
            fun bindItem(noteItem: Note) {

                val prettyTime: String = PrettyTime().format(Date(noteItem.timeUpdated))

                binding.tvNoteTitle.text = noteItem.noteTitle
                binding.tvNoteDescription.text = noteItem.noteBody
                binding.tvNoteDate.text = prettyTime
                if (noteItem.favoriteStatus) {
                    binding.favoriteIcon.visibility = View.VISIBLE
                } else binding.favoriteIcon.visibility = View.INVISIBLE

                binding.noteItem.setOnClickListener { onItemClicked(noteItem.id) }
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListRecyclerViewViewHolder {
            val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return NoteListRecyclerViewViewHolder(binding)
        }

        override fun onBindViewHolder(holder: NoteListRecyclerViewViewHolder, position: Int) {
            holder.bindItem(getItem(position))

            holder.itemView.setOnLongClickListener {
                longPressed.popUpMenu(it, position)
                return@setOnLongClickListener true
            }
        }


    fun returnItemId(position: Int): Int {
        val selectedNote = noteList[position]
        return selectedNote.id
    }

    class NoteDiffUtil: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }
}