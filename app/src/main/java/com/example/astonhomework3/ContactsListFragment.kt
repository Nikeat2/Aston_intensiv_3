package com.example.astonhomework3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactsListFragment : Fragment() {
    private val fragmentForAddingContacts = FragmentForAddingContacts.newInstance()
    private lateinit var btnForAdding: Button
    private lateinit var rvContacts: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ContactsAdapter
    private lateinit var contactsList: MutableList<Contacts>
    private lateinit var fragmentEditing: EditingContactsFragment
    private lateinit var btnDelete: Button
    private lateinit var btnCancel: Button
    private lateinit var btnForSelecting: ImageButton
    private var isSelectionMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_contacts_list, container, false)
        btnForAdding = view.findViewById(R.id.btnAdd)
        rvContacts = view.findViewById(R.id.rvContacts)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnDelete = view.findViewById(R.id.btnDelete)
        layoutManager = LinearLayoutManager(requireContext())
        adapter = ContactsAdapter(getContacts(), null)
        rvContacts.adapter = adapter
        rvContacts.layoutManager = layoutManager

        val contactTouchHelper = ItemTouchHelper(ContactTouchHelper(adapter))
        adapter.setItemTouchHelper(contactTouchHelper)
        contactTouchHelper.attachToRecyclerView(rvContacts)
        btnForSelecting = view.findViewById(R.id.btnSelect)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListenerChangedContact()
        setFragmentResultListenerNewContact()
        btnForAdding.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main, fragmentForAddingContacts)
                .addToBackStack(BACKSTACK_TAG).commit()
        }

        adapter.setOnContactClickListener(object : ContactsAdapter.OnContactClickListener {
            override fun onContactClick(position: Int) {
                if (!isSelectionMode) {
                    val contact = contactsList[position]
                    getToEditingFragment(contact)
                }
            }
        })

        btnForSelecting.setOnClickListener {
            isSelectionMode = true
            adapter.setSelectionMode(true)
            btnForSelecting.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
            btnCancel.visibility = View.VISIBLE
        }

        btnCancel.setOnClickListener {
            isSelectionMode = false
            adapter.clearSelections()
            btnCancel.visibility = View.GONE
            btnDelete.visibility = View.GONE
            btnForSelecting.visibility = View.VISIBLE
            adapter.setSelectionMode(isSelectionMode)
        }

        btnDelete.setOnClickListener {
            val newList: MutableList<Contacts> = mutableListOf()
            newList.addAll(contactsList)
            for (contact in contactsList) {
                if (contact.isChecked) {
                    val i = newList.indexOf(contact)
                    newList.removeAt(i)
                }
            }
            btnCancel.visibility = View.GONE
            btnDelete.visibility = View.GONE
            btnForSelecting.visibility = View.VISIBLE
            isSelectionMode = false
            adapter.setSelectionMode(false)
            adapter.updateContacts(newList.toList())
            adapter.notifyDataSetChanged()
        }
    }

    private fun setFragmentResultListenerNewContact() {
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY_NEW_CONTACT, viewLifecycleOwner
        ) { _, bundle ->
            val newContactInfo = bundle.getBundle(REQUEST_KEY_NEW_CONTACT)
            val newContactName = newContactInfo?.getString("name")
            val newContactSurname = newContactInfo?.getString("surname")
            val newContactPhoneNumber = newContactInfo?.getString("phoneNumber")
            val newContactId = contactsList.last().id + 1
            val newContact = Contacts(
                newContactId,
                newContactName.toString(),
                newContactSurname.toString(),
                newContactPhoneNumber.toString()
            )
            contactsList.add(newContact)
            adapter.updateContacts(contactsList.toList())
        }
    }

    private fun setFragmentResultListenerChangedContact() {
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY_CHANGED_CONTACT, viewLifecycleOwner
        ) { _, bundle ->
            Log.d("fragmentResult", "onContactClick: ")
            val changedContactInfo = bundle.getBundle(REQUEST_KEY_CHANGED_CONTACT)
            val changedContactName = changedContactInfo?.getString("changedName")
            val changedContactSurname = changedContactInfo?.getString("changedSurname")
            val changedContactPhoneNumber = changedContactInfo?.getString("changedPhoneNumber")
            val position = changedContactInfo?.getInt("position")!!.toInt()

            val newList = mutableListOf<Contacts>()
            newList.addAll(contactsList)

            if (position in contactsList.indices) {
                val contactCopy = contactsList[position].copy(
                    name = changedContactName.toString(),
                    surname = changedContactSurname.toString(),
                    phoneNumber = changedContactPhoneNumber.toString()
                )
                newList.removeAt(position)
                newList.add(position, contactCopy)
            }
            adapter.updateContacts(newList.toList())
        }
    }

    private fun getContacts(): MutableList<Contacts> {
        contactsList = mutableListOf()
        contactsList.addAll(MockContacts().mockContacts)
        return contactsList
    }

    private fun getToEditingFragment(contact: Contacts) {
        fragmentEditing = EditingContactsFragment.newInstance(
            contact
        )
        parentFragmentManager.beginTransaction().replace(R.id.main, fragmentEditing)
            .addToBackStack(BACKSTACK_TAG).commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(): ContactsListFragment {
            return ContactsListFragment()
        }
    }
}

const val BACKSTACK_TAG = "BACKSTACK_TAG"
const val REQUEST_KEY_NEW_CONTACT = "REQUEST_KEY_NEW_CONTACT"
const val REQUEST_KEY_CHANGED_CONTACT = "REQUEST_KEY_CHANGED_CONTACT"