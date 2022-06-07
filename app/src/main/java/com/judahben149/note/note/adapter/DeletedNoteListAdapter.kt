package com.judahben149.note.note.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.judahben149.note.databinding.NoteItemBinding
import com.judahben149.note.note.fragment.deletedNote.DeletedNoteListFragmentDirections
import com.judahben149.note.note.model.Note
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class DeletedNoteListAdapter() : RecyclerView.Adapter<DeletedNoteListAdapter.DeletedNoteListViewHolder>() {

    var deletedNotes = emptyList<Note>()

    inner class DeletedNoteListViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(position: Int) {
            val currentNote = deletedNotes[position]
            val timeDeleted: String = PrettyTime().format(Date(currentNote.timeDeleted))

            binding.tvNoteTitle.text = currentNote.noteTitle
            binding.tvNoteDescription.text = currentNote.noteBody
            binding.tvNoteDate.text = "Deleted: " + timeDeleted

            binding.noteItem.setOnClickListener {
                val action = DeletedNoteListFragmentDirections.actionDeletedNoteListFragmentToDeletedNoteDetailsFragment(currentNote)
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeletedNoteListViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeletedNoteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeletedNoteListViewHolder, position: Int) {
        holder.bindItem(position)
    }

    override fun getItemCount() = deletedNotes.size

    fun setData(note: List<Note>) {
        this.deletedNotes = note
        notifyDataSetChanged()
    }
}