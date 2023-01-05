package com.example.aston_intensive_6.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.aston_intensive_6.R
import com.example.aston_intensive_6.models.ContactItem
import com.example.aston_intensive_6.databinding.ListItemBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

class ContactListAdapter(
    private val context: Context,
    private val onClick: (ContactItem) -> Unit,
    private val onLongClick: (ContactItem) -> Boolean
) : ListAdapter<ContactItem, ContactListAdapter.ContactViewHolder>(ContactDiffUtils) {

    val jobs = ArrayList<Job>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contactItem = getItem(position)

        if (contactItem.picture == null) {
            holder.binding.progressBar.visibility = View.VISIBLE

            val job = CoroutineScope(Dispatchers.IO).launch {

                try {
                    contactItem.picture = Picasso.get().load("https://picsum.photos/200").get()
                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, R.string.Toast, Toast.LENGTH_SHORT).show()
                    }
                }

                withContext(Dispatchers.Main) {
                    holder.binding.progressBar.visibility = View.GONE
                    holder.bind(contactItem = contactItem)
                }
            }

            jobs.add(job)
        } else {
            holder.bind(contactItem = contactItem)
        }

        holder.binding.root.setOnClickListener { onClick(contactItem) }
        holder.binding.root.setOnLongClickListener { onLongClick(contactItem) }
    }

    fun submit(filterList: ArrayList<ContactItem>) {
        submitList(filterList)
        notifyDataSetChanged()
    }

    class ContactViewHolder(val binding: ListItemBinding) : ViewHolder(binding.root) {

        fun bind(contactItem: ContactItem) {
            binding.textViewItemFirstName.text = contactItem.firstName
            binding.textViewItemSecondName.text = contactItem.secondName

            if (contactItem.picture != null) {
                binding.imageViewItemImage.setImageBitmap(contactItem.picture)
            }
        }
    }

    object ContactDiffUtils : DiffUtil.ItemCallback<ContactItem>() {
        override fun areItemsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
            return oldItem == newItem
        }
    }
}