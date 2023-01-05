package com.example.aston_intensive_6.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aston_intensive_6.MainViewModel
import com.example.aston_intensive_6.R
import com.example.aston_intensive_6.models.ContactItem
import com.example.aston_intensive_6.databinding.AlertDialogDeleteBinding
import com.example.aston_intensive_6.databinding.FragmentContactListBinding
import com.example.aston_intensive_6.extentions.dip2px
import com.example.aston_intensive_6.recycler.ContactListAdapter
import com.example.aston_intensive_6.recycler.CustomItemDecoration

class ContactItemListFragment : Fragment(R.layout.fragment_contact_list) {

    private val mainViewModel by activityViewModels<MainViewModel>()

    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ContactListAdapter
    private val contactList = ArrayList<ContactItem>()
    private var query: String = ""

    private val onClick: (ContactItem) -> Unit = {
        mainViewModel.selectedContactItem(it)
    }

    private val onLongClick: (ContactItem) -> Boolean = {
        showAlertDialog(it)
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView(view = view)
        setUpSearchView()

        mainViewModel.contactList.observe(viewLifecycleOwner) { list ->
            contactList.clear()
            contactList.addAll(list)
            filter(query)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null

        for (job in adapter.jobs) {
            if (!job.isCompleted) {
                job.cancel()
            }
        }
    }

    private fun setUpRecyclerView(view: View) {
        adapter = ContactListAdapter(
            context = requireContext(),
            onClick = onClick,
            onLongClick = onLongClick
        )

        binding.recyclerViewContactList.adapter = adapter
        binding.recyclerViewContactList.layoutManager = LinearLayoutManager(requireContext())

        val customItemDecoration = CustomItemDecoration(
            left = view.dip2px(10),
            top = view.dip2px(20),
            right = view.dip2px(10),
            bottom = view.dip2px(20),
            divider = ResourcesCompat.getDrawable(resources, R.drawable.divider, null)!!
        )
        binding.recyclerViewContactList.addItemDecoration(customItemDecoration)
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText)
                }
                return false
            }
        })
    }

    private fun showAlertDialog(contactItem: ContactItem) {
        val alertDialog = AlertDialog.Builder(requireContext())
        val binding = AlertDialogDeleteBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogWindow = binding.root

        val dialogQuestion = contactItem.firstName + "?"
        binding.textViewAlertDialog.text = dialogQuestion

        alertDialog.setView(dialogWindow)

        alertDialog.setNegativeButton(resources.getString(R.string.cancel)) { dialog, i -> dialog.dismiss() }

        alertDialog.setPositiveButton(resources.getString(R.string.delete)) { dialog, i ->
            mainViewModel.deleteContactItem(contactItem)
            dialog.dismiss()
        }

        alertDialog.show()
    }

    private fun filter(query: String) {
        this.query = query
        val filteredList: ArrayList<ContactItem> = ArrayList()

        for (item in contactList) {
            if (item.firstName.lowercase().contains(query.lowercase()) ||
                item.secondName.lowercase().contains(query.lowercase())
            ) {
                filteredList.add(item)
            }
        }

        adapter.submit(filteredList)
    }
}