package com.example.postrequestpractice

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SearchEvent
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var rvMain: RecyclerView
    val detailsInfo = arrayListOf<UserDetailsItem>()
    val searchArray = arrayListOf<UserDetailsItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMain = findViewById(R.id.rvMain)
        //RecyclerView
        //  rvMain.adapter = RecyclerViewAdapter(detailsInfo,this)
        rvMain.adapter = RecyclerViewAdapter(searchArray, this)
        rvMain.layoutManager = LinearLayoutManager(applicationContext)
        getDetails()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu?.findItem(R.id.search_action)

        if (menuItem != null) {
            val searchItem = menuItem.actionView as SearchView
            searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        searchArray.clear()
                        val search = newText!!.toLowerCase(Locale.getDefault())
                        detailsInfo.forEach {
                            if (it.location?.toLowerCase(Locale.getDefault()).toString()
                                    .contains(search)
                            ) {
                                searchArray.add(it)
                            }
                        }
                        rvMain.adapter!!.notifyDataSetChanged()
                    } else {
                        searchArray.clear()
                        searchArray.addAll(detailsInfo)
                        rvMain.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.getItemId()==R.id.addNewUser){
            val intent = Intent(this, AddDetailsActivity::class.java)
            startActivity(intent)
        }
        if(item.getItemId()==R.id.refresh){
                rvMain.adapter!!.notifyDataSetChanged()
                this.recreate()

            }
        return super.onOptionsItemSelected(item)
    }

    fun getDetails() {
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getUserDetails()?.enqueue(object : Callback<UserDetails> {
                override fun onResponse(
                    call: Call<UserDetails>,
                    response: Response<UserDetails>
                ) {
                    progressDialog.dismiss()
                    Log.d("TAG", response.code().toString() + "")
                    for (User in response.body()!!) {
                        val name = User.name
                        val location = User.location
                        val id = User.pk
                        detailsInfo.add(UserDetailsItem(location, name, id))
                    }
                    searchArray.addAll(detailsInfo)
                    rvMain.adapter!!.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                    // Toast.makeText(applicationContext, ""+t.message, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss()
                    call.cancel()
                }
            })
        }


    }

}




