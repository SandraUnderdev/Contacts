package com.example.contacts

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.Contact
import com.example.myproject.ContactAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var fab: FloatingActionButton

    private lateinit var nameEdt: EditText
    private lateinit var phoneEdt: EditText
    private lateinit var previewImage: ImageView
    private lateinit var btnChooseImage: Button
    private lateinit var btnAddContact: Button
    private lateinit var dialog: Dialog

    private lateinit var contactAdapter: ContactAdapter
   // val listOfUsers = mutableListOf<DataEntryContact>()

    private lateinit var repo: Repo
    private lateinit var viewModelFactory: ContactViewModelFactory
    private lateinit var viewModel: ContactViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
        setContentView(R.layout.activity_main)

        repo = Repo()
        viewModelFactory = ContactViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ContactViewModel::class.java)

        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
      //  contactAdapter = DataEntryAdapter(listOfUsers)
       // rv.adapter = contactAdapter
        fab = findViewById(R.id.fab)

        viewModel.listOfContactLiveData.observe(this) {
            contactAdapter = ContactAdapter(it)
            rv.adapter = contactAdapter
        }

        fab.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.data_entry_form)

        nameEdt = dialog.findViewById(R.id.fullName)
        phoneEdt = dialog.findViewById(R.id.number)
        previewImage = dialog.findViewById(R.id.img_preview)
        btnChooseImage = dialog.findViewById(R.id.choose_image)
        btnAddContact = dialog.findViewById(R.id.add_contact)

        btnChooseImage.setOnClickListener {
            //   Toast.makeText(this, "should open gallery", Toast.LENGTH_SHORT).show()
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 101)
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == AppCompatActivity.RESULT_OK) {
            previewImage.visibility = View.VISIBLE
            previewImage.setImageURI(data?.data)

            btnAddContact.setOnClickListener {
                val name = nameEdt.text.toString()
                val phoneNumber = phoneEdt.text.toString()
                val image = data?.data

                val contact = Contact(
                    textName = name,
                    textPhoneNumber = phoneNumber,
                    profileImage = image,
                )
         //       listOfUsers.add(contact)
              //  contactAdapter.notifyDataSetChanged()
                viewModel.addContact(contact)
                dialog.dismiss()
            }
            //
        }
    }


}