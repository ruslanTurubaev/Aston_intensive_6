package com.example.aston_intensive_6.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aston_intensive_6.MainViewModel
import com.example.aston_intensive_6.models.ContactItem
import com.example.aston_intensive_6.databinding.FragmentContactDetailsBinding
import com.example.aston_intensive_6.extentions.ID
import com.example.aston_intensive_6.extentions.isPhone

class ContactDetailsFragment : Fragment() {

    private val mainViewModel by activityViewModels<MainViewModel>()

    private var _binding: FragmentContactDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactItem: ContactItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val id = requireArguments().getInt(ID)
        contactItem = mainViewModel.getContactItemById(id)

        _binding = FragmentContactDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.selectedContactItem(null)

        setFields(contactItem)

        binding.buttonSave.setOnClickListener { saveData(view.context, contactItem.id) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setFields(contactItem: ContactItem) {
        binding.textViewFirstName.setText(contactItem.firstName)
        binding.textViewSecondName.setText(contactItem.secondName)
        binding.textViewTelNumber.setText(contactItem.telNumber)

        if (contactItem.picture != null) {
            binding.imageViewContactImage.setImageBitmap(contactItem.picture)
        }
    }

    private fun saveData(device: Context, itemID: Int) {
        val firstName = binding.textViewFirstName.text.toString()
        val secondName = binding.textViewSecondName.text.toString()
        val telNumber = binding.textViewTelNumber.text.toString()

        val contactImage = try {
            binding.imageViewContactImage.drawable.toBitmap()
        } catch (exception: Exception) {
            null
        }

        contactItem = ContactItem(
            id = itemID,
            firstName = firstName,
            secondName = secondName,
            telNumber = telNumber,
            picture = contactImage
        )

        mainViewModel.amendContactList(itemID, contactItem)

        if (device.isPhone()) {
            requireActivity().onBackPressed()
        }
    }
}