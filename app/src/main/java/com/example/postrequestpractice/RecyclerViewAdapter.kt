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

class RecyclerViewAdapter(val detailsInfo: ArrayList<UserDetailsItem>,val context: Context) :
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
            tvID.text = data.pk.toString()
            ivUpdate.setOnClickListener {
                customAlertDialog(data.pk!!,data.location!!,data.name!!)
            }
            ivDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Deleting User Details")
                //set message for alert dialog
                builder.setMessage("Are you sure you want to delete this user?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Delete"){dialogInterface, which ->
                    delete(data.pk.toString().toInt())
                }
                //performing cancel action
                builder.setNeutralButton("Cancel"){dialogInterface , which ->
                    dialogInterface.cancel()
                }

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }


            }
        }

    override fun getItemCount() = detailsInfo.size


    fun update(id: Int, location: String, name: String) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.updateUserDetails(id, UserDetailsItem(location, name, id))?.enqueue(
            object : Callback<UserDetailsItem> {
                override fun onResponse(
                    call: Call<UserDetailsItem>,
                    response: Response<UserDetailsItem>
                ) {
                    Toast.makeText(context,"Updated successfully!",Toast.LENGTH_SHORT).show()
                    notifyDataSetChanged()
                }

                override fun onFailure(call: Call<UserDetailsItem>, t: Throwable) {
                    Toast.makeText(context,"Updated failed!",Toast.LENGTH_SHORT).show()
                }
            })


    }
    fun delete(id: Int){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.deleteUserDetails(id)?.enqueue(   object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(context,"Deleted successfully!",Toast.LENGTH_SHORT).show()
                notifyDataSetChanged()
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


