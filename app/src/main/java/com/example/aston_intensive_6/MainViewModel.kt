package com.example.aston_intensive_6

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aston_intensive_6.models.ContactItem
import com.example.aston_intensive_6.extentions.getContactList

class MainViewModel : ViewModel() {

    private val mutableContactList = MutableLiveData<ArrayList<ContactItem>>()
    private val mutableSelectedContactItem = MutableLiveData<ContactItem?>()

    init {
        mutableContactList.value = getContactList()
    }

    val contactList: LiveData<ArrayList<ContactItem>> get() = mutableContactList

    val selectedContactItem: LiveData<ContactItem?> get() = mutableSelectedContactItem

    fun selectedContactItem(contactItem: ContactItem?) {
        mutableSelectedContactItem.value = contactItem
    }

    fun getContactItemById(id: Int): ContactItem {
        val list = getList()
        return list[id]
    }

    fun deleteContactItem(item: ContactItem) {
        val list = getList()
        list.remove(item)

        for ((index, contactItem) in list.withIndex()) {
            contactItem.id = index
        }

        mutableContactList.value = list
    }

    fun amendContactList(id: Int, item: ContactItem) {
        val list = getList()
        list.removeAt(id)
        list.add(id, item)
        mutableContactList.value = list
    }

    private fun getList(): ArrayList<ContactItem> {
        return mutableContactList.value!!
    }
}