package com.example.postrequestpractice

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.android.synthetic.main.login_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewAdapter(val detailsInfo: ArrayList<Details.UserDetails>,val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = detailsInfo[position]

        holder.itemView.apply {
            tvName.text = data.name
            tvLocation.text = data.location
            tvID.text = data.id.toString()
            ivUpdate.setOnClickListener {
                customAlertDialog(data.id!!,data.location!!,data.name!!)
            }
            ivDelete.setOnClickListener {
                delete(data.id.toString().toInt())
            }
        }


    }

    override fun getItemCount() = detailsInfo.size


    fun update(id: Int, location: String, name: String) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.updateUserDetails(id, Details.UserDetails(location, name, id))?.enqueue(
            object : Callback<Details.UserDetails> {
                override fun onResponse(
                    call: Call<Details.UserDetails>,
                    response: Response<Details.UserDetails>
                ) {
                    Toast.makeText(context,"Updated successfully!",Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Details.UserDetails>, t: Throwable) {
                    Toast.makeText(context,"Updated failed!",Toast.LENGTH_SHORT).show()
                }
            })


    }
    fun delete(id: Int){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.deleteUserDetails(id)?.enqueue(   object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(context,"Deleted successfully!",Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
               call.cancel()
            }
        })
    }
    fun customAlertDialog(id:Int,location:String,name:String){
        //Inflate the dialog with custom view
        val DialogView = LayoutInflater.from(context).inflate(R.layout.login_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(context)
            .setView(DialogView)
            .setTitle("Login Form")
        //show dialog
        val  mAlertDialog = mBuilder.show()
        DialogView.etID.setText(id.toString())
        DialogView.etName.setText(name)
        DialogView.etLocation.setText(location)
        DialogView.btSave.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            DialogView.etID.setTextColor(Color.GRAY)
            DialogView.etName.setTextColor(Color.GRAY)
            DialogView.etLocation.setTextColor(Color.GRAY)
            //get text from EditTexts of custom layout
            val name = DialogView.etName.text.toString()
            val location = DialogView.etLocation.text.toString()
            if(name.isNotEmpty()&&location.isNotEmpty()){
                update(id,location,name)
            }else{
                DialogView.etID.setTextColor(Color.RED)
                DialogView.etName.setTextColor(Color.RED)
                DialogView.etLocation.setTextColor(Color.RED)
            }
        }
        //cancel button click of custom layout
        DialogView.btCancel.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }
}


