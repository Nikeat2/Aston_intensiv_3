package com.example.astonhomework3

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ContactsAdapter(
    private var contactsList: MutableList<Contacts>,
    private var itemTouchHelper: ItemTouchHelper?
) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    private lateinit var mListener: OnContactClickListener
    private var selectionMode = false

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContactName: TextView = view.findViewById(R.id.tvName)
        val tvContactSurname: TextView = view.findViewById(R.id.tvSurname)
        val tvContactPhoneNumber: TextView = view.findViewById(R.id.tvPhoneNymber)

        init {
            itemView.setOnLongClickListener {
                itemTouchHelper?.startDrag(this)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactsList[position]
        holder.tvContactSurname.text = contact.surname
        holder.tvContactName.text = contact.name
        holder.tvContactPhoneNumber.text = contact.phoneNumber

        holder.itemView.setBackgroundColor(
            if (contact.isChecked) {
                Color.RED
            } else {
                Color.TRANSPARENT
            }
        )

        holder.itemView.setOnClickListener {
            if (selectionMode) {
                val newList = mutableListOf<Contacts>()
                newList.addAll(contactsList)
                val isChecked = newList[position].isChecked
                val newContact = newList[position].copy(isChecked = !isChecked)
                newList.removeAt(position)
                newList.add(position, newContact)
                updateContacts(newList.toList())
            } else mListener.onContactClick(position)
        }
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    fun updateContacts(newContacts: List<Contacts>) {
        val diffCallback = DiffUtilContacts(contactsList, newContacts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        contactsList.clear()
        contactsList.addAll(newContacts)
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearSelections() {
        val newList: MutableList<Contacts> = mutableListOf()
        newList.addAll(contactsList)
        for ((index, contact) in contactsList.withIndex()) {
            if (contact.isChecked) {
                val newContact = contact.copy(isChecked = false)
                newList.removeAt(index)
                newList.add(index, newContact)
            }
        }
        updateContacts(newList.toList())
    }

    fun setSelectionMode(isEnabled: Boolean) {
        selectionMode = isEnabled
        if (!selectionMode) {
            clearSelections()
        }
    }

    fun setOnContactClickListener(listener: OnContactClickListener) {
        mListener = listener
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val contact = contactsList.removeAt(fromPosition)
        contactsList.add(toPosition, contact)
        notifyItemMoved(fromPosition, toPosition)
        updateContacts(contactsList.toList())
    }

    fun setItemTouchHelper(itemTouchHelper: ItemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper
    }

    interface OnContactClickListener {
        fun onContactClick(position: Int)
    }
}