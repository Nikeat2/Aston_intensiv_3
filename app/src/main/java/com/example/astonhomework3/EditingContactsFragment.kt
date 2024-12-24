package com.example.astonhomework3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class EditingContactsFragment : Fragment() {
    private var name: String? = null
    private var surname: String? = null
    private var phoneNumber: String? = null
    private lateinit var etNameEdited: EditText
    private lateinit var etSurnameEdited: EditText
    private lateinit var etPhoneNumberEdited: EditText
    private lateinit var changedName: String
    private lateinit var changedSurname: String
    private lateinit var changedPhoneNumber: String
    private lateinit var btnSaveChanges: Button
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name")
            surname = it.getString("surname")
            phoneNumber = it.getString("phoneNumber")
            position = it.getInt("position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editing_contacts, container, false)
        etNameEdited = view.findViewById(R.id.etEditName)
        etSurnameEdited = view.findViewById(R.id.etEditSurname)
        etPhoneNumberEdited = view.findViewById(R.id.etEditPhoneNumber)
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etNameEdited.setText(name)
        etSurnameEdited.setText(surname)
        etPhoneNumberEdited.setText(phoneNumber)
        btnSaveChanges.setOnClickListener {
            sendChangedContact()
        }
    }

    private fun sendChangedContact() {
        changedName = etNameEdited.text.toString()
        changedSurname = etSurnameEdited.text.toString()
        changedPhoneNumber = etPhoneNumberEdited.text.toString()
        val changedContactInfo = bundleOf(
            "changedName" to changedName,
            "changedSurname" to changedSurname,
            "changedPhoneNumber" to changedPhoneNumber,
            "position" to position
        )

        val changedContact = Bundle().apply {
            putBundle(REQUEST_KEY_CHANGED_CONTACT, changedContactInfo)
        }
        parentFragmentManager.setFragmentResult(REQUEST_KEY_CHANGED_CONTACT, changedContact)
        parentFragmentManager.popBackStack()
    }

    companion object {
        @JvmStatic
        fun newInstance(contact: Contacts) =
            EditingContactsFragment().apply {
                arguments = Bundle().apply {
                    putString("name", contact.name)
                    putString("surname", contact.surname)
                    putString("phoneNumber", contact.phoneNumber)
                    putInt("position", contact.id - 1)
                }
            }
    }
}