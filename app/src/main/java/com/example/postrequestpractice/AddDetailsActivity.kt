package com.example.postrequestpractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDetailsActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etLocation: EditText
    lateinit var btSave: ImageView
    lateinit var btView: ImageView
    var name: String = ""
    var location: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_details)
        etName = findViewById(R.id.etName)
        etLocation = findViewById(R.id.etLocation)
        btSave = findViewById(R.id.btSave)
        btView = findViewById(R.id.btView)
        btSave.setOnClickListener {
            name = etName.text.toString()
            location = etLocation.text.toString()
            if (name.isNotEmpty() && location.isNotEmpty()) {
                addDetails()
                Toast.makeText(applicationContext, "Save success", Toast.LENGTH_SHORT).show()
                etName.setText("")
                etLocation.setText("")
            } else {
                Toast.makeText(this, "Enter a name and location", Toast.LENGTH_SHORT).show()
            }
        }
        btView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun addDetails() {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.addUserDetails(Details.UserDetails( location,name))
                .enqueue(object : Callback<List<Details.UserDetails>> {
                    override fun onResponse(
                        call: Call<List<Details.UserDetails>>,
                        response: Response<List<Details.UserDetails>>
                    ) {

                    }

                    override fun onFailure(call: Call<List<Details.UserDetails>>, t: Throwable){
                        call.cancel()
                    }
                })

        }
    }
}