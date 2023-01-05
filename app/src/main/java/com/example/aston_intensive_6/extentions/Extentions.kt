package com.example.aston_intensive_6.extentions

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.View
import com.example.aston_intensive_6.models.ContactItem

const val ID = "ID"

fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun <T : View?> Activity.find(idRes: Int) = unsafeLazy<T?> { findViewById(idRes) }

fun View.dip2px(value: Int): Int = (resources.displayMetrics.density * value).toInt()

fun View?.isExist(): Boolean = this != null

fun Context.isPhone(): Boolean =
    this.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK < Configuration.SCREENLAYOUT_SIZE_LARGE

fun getContactList(): ArrayList<ContactItem> {
    val contactList = ArrayList<ContactItem>()
    for (i in 1..100) {
        val id = i - 1
        val firstName = "First Name $i"
        val secondName = "Second Name $i"
        val telNumber = (Math.random() * 899999 + 100000).toInt().toString()
        contactList.add(
            ContactItem(
                id = id,
                firstName = firstName,
                secondName = secondName,
                telNumber = telNumber,
                picture = null
            )
        )
    }
    return contactList
}
