package com.example.postrequestpractice

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var btAddNewUser: Button
    lateinit var rvMain: RecyclerView
    val detailsInfo = arrayListOf<Details.UserDetails>()
    var name: String = ""
    var location: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btAddNewUser = findViewById(R.id.btAddNewUser)
        rvMain = findViewById(R.id.rvMain)
        //RecyclerView
        rvMain.adapter = RecyclerViewAdapter(detailsInfo)
        rvMain.layoutManager = LinearLayoutManager(applicationContext)
        btAddNewUser.setOnClickListener {
            val intent = Intent(this, AddDetailsActivity::class.java)
            startActivity(intent)
        }

        getDetails()
    }


    fun getDetails() {
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getDetails()?.enqueue(object : Callback<List<Details.UserDetails>> {
                override fun onResponse(
                    call: Call<List<Details.UserDetails>>,
                    response: Response<List<Details.UserDetails>>
                ) {
                    progressDialog.dismiss()
                    Log.d("TAG", response.code().toString() + "")
                    for(User in response.body()!!){
                        val name = User.name
                        val location = User.location
                        //Log.d("TAG", "name: "+name + "")
                        detailsInfo.add(Details.UserDetails(location,name))
                    }
                    rvMain.adapter!!.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<List<Details.UserDetails>>, t: Throwable) {
                   // Toast.makeText(applicationContext, ""+t.message, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss()
                    call.cancel()
                }
            })
        }



    }
}




