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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var btAddNewUser: ImageView
    lateinit var btRefresh:ImageView
    lateinit var rvMain: RecyclerView
    val detailsInfo = arrayListOf<Details.UserDetails>()
    val searchArray= arrayListOf<Details.UserDetails>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btAddNewUser = findViewById(R.id.btAddNewUser)
        btRefresh=findViewById(R.id.btRefresh)
        rvMain = findViewById(R.id.rvMain)
        //RecyclerView
      //  rvMain.adapter = RecyclerViewAdapter(detailsInfo,this)
        rvMain.adapter = RecyclerViewAdapter(searchArray,this)
        rvMain.layoutManager = LinearLayoutManager(applicationContext)
        btAddNewUser.setOnClickListener {
            val intent = Intent(this, AddDetailsActivity::class.java)
            startActivity(intent)
        }
        btRefresh.setOnClickListener {
            rvMain.adapter!!.notifyDataSetChanged()
            this.recreate()
        }
        getDetails()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem=menu?.findItem(R.id.search_action)
        if(menuItem!=null){
            val searchItem=menuItem.actionView as SearchView
            searchItem.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                        searchArray.clear()
                        val search=newText!!.toLowerCase(Locale.getDefault())
                        detailsInfo.forEach {
                            if(it.location?.toLowerCase(Locale.getDefault()).toString().contains(search)){
                                searchArray.add(it)
                            }
                        }
                        rvMain.adapter!!.notifyDataSetChanged()
                    }else{
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

//        when(item.itemId){
//            R.id.search_action -> {
//                searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                    Log.d("TAG","Here")
//                        searchArray.clear()
//                        val searchText=newText!!.toLowerCase(Locale.getDefault())
//                        if(searchText.isNotEmpty()){
//                            for(x in detailsInfo){
//                                if(x.location?.toLowerCase(Locale.getDefault()).toString().contains(searchText)){
//                                    searchArray.add(x)
//                                }
//                            }
//                            rvMain.adapter!!.notifyDataSetChanged()
//                        }else{
//                            searchArray.clear()
//                            searchArray.addAll(detailsInfo)
//                            rvMain.adapter!!.notifyDataSetChanged()
//
//                        }
//                        return false
//                    }
//
//                })
//
//                return true
//            }
//        }
        return super.onOptionsItemSelected(item)
    }
    fun getDetails() {
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getUserDetails()?.enqueue(object : Callback<List<Details.UserDetails>> {
                override fun onResponse(
                    call: Call<List<Details.UserDetails>>,
                    response: Response<List<Details.UserDetails>>
                ) {
                    progressDialog.dismiss()
                    Log.d("TAG", response.code().toString() + "")
                    for(User in response.body()!!){
                        val name = User.name
                        val location = User.location
                        val id=User.id
                        detailsInfo.add(Details.UserDetails(location,name,id))
                    }
                    searchArray.addAll(detailsInfo)
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




