package com.judahben149.note.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.judahben149.note.LongPressed
import com.judahben149.note.databinding.NoteItemBinding
import com.judahben149.note.fragments.notes.NoteListFragmentDirections
import com.judahben149.note.model.Note
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class NoteListAdapter(val context: Context, private val longPressed: LongPressed): RecyclerView.Adapter<NoteListAdapter.NoteListRecyclerViewViewHolder>() {

        var noteList = emptyList<Note>()


        inner class NoteListRecyclerViewViewHolder(private val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root) {
            fun bindItem(position: Int) {
                val currentNote = noteList[position]

                val prettyTime: String = PrettyTime().format(Date(currentNote.timeUpdated))

                binding.tvNoteTitle.text = currentNote.noteTitle
                binding.tvNoteDescription.text = currentNote.noteBody
                binding.tvNoteDate.text = prettyTime
                if (currentNote.favoriteStatus == true) {
                    binding.favoriteIcon.visibility = View.VISIBLE
                } else binding.favoriteIcon.visibility = View.INVISIBLE

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

            holder.itemView.setOnLongClickListener {
                longPressed.popUpMenu(it, position)
                return@setOnLongClickListener true
            }
        }

        override fun getItemCount() = noteList.size


        fun setData(note: List<Note>) {
            this.noteList = note
            notifyDataSetChanged()
        }

    fun getItemPosition(position: Int): Note {
        return noteList[position]
    }

    fun returnItemId(position: Int): Int {
        val selectedNote = noteList[position]
        return selectedNote.id
    }
}