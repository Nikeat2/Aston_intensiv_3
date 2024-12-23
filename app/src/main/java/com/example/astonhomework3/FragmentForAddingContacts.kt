package com.example.astonhomework3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf

class FragmentForAddingContacts : Fragment() {
    private lateinit var etNewContactName: EditText
    private lateinit var etNewContactSurname: EditText
    private lateinit var etNewContactPhoneNumber: EditText
    private lateinit var btnToAdd: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_for_adding_contacts, container, false)
        etNewContactName = view.findViewById(R.id.etNewContactName)
        etNewContactSurname = view.findViewById(R.id.etNewContactSurname)
        etNewContactPhoneNumber = view.findViewById(R.id.etNewContactPhoneNumber)
        btnToAdd = view.findViewById(R.id.btnAddInFragment)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnToAdd.setOnClickListener {
            sendNewContact()
        }
    }

    private fun sendNewContact() {
        val newContactName = etNewContactName.text.toString()
        val newContactSurname = etNewContactSurname.text.toString()
        val newContactPhoneNumber = etNewContactPhoneNumber.text.toString()
        val newContactInfo = bundleOf(
            "name" to newContactName,
            "surname" to newContactSurname,
            "phoneNumber" to newContactPhoneNumber
        )
        val newContact = Bundle().apply {
            putBundle(REQUEST_KEY_NEW_CONTACT, newContactInfo)
        }
        parentFragmentManager.setFragmentResult(REQUEST_KEY_NEW_CONTACT, newContact)
        parentFragmentManager.popBackStack()
    }

    companion object {
        @JvmStatic
        fun newInstance(): FragmentForAddingContacts {
            return FragmentForAddingContacts()
        }
    }
}