package com.example.shoppingapp.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.activities.EditAddressActivity
import com.example.shoppingapp.activities.PaymentActivity
import com.example.shoppingapp.activities.SubCategoryActivity
import com.example.shoppingapp.app.Config
import com.example.shoppingapp.models.Address
import com.example.shoppingapp.models.Category
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.grid_layout_address.view.*
import kotlinx.android.synthetic.main.grid_layout_category.view.*

class RecyclerViewSelectAddressAdapter(var selectAddressActivity : Context) : RecyclerView.Adapter<RecyclerViewSelectAddressAdapter.MyViewHolder>(){

    private var addressList : ArrayList<Address> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view = LayoutInflater.from(selectAddressActivity).inflate(R.layout.grid_layout_address, parent, false)
        var viewHolder = MyViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {

        return addressList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(addressList.get(position))
    }

    fun setData(addressList : ArrayList<Address>){
        this.addressList = addressList
        notifyDataSetChanged()
    }

    fun deleteItem(holder: RecyclerViewSelectAddressAdapter.MyViewHolder, position: Int) {
        addressList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, addressList.size)
        holder.itemView.visibility = View.GONE
    }

    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view){

        fun bind(address: Address){

            view.text_view_address_type.text = address.addressType
            view.text_view_name.text = address.name
            view.text_view_address.text = address.streetName + " #" + address.houseNumber + ", " + address.city
            view.text_view_address_zipcode.text = address.zipCode

            view.setOnClickListener{
                selectAddressActivity.startActivity(Intent(selectAddressActivity,PaymentActivity::class.java))
            }

            view.text_view_edit.setOnClickListener{
                var intent = Intent(selectAddressActivity, EditAddressActivity::class.java)
                intent.putExtra(Address.KEY_ADDRESS, address)
                selectAddressActivity.startActivity(intent)

            }

            view.text_view_delete.setOnClickListener{

                //delete from database
                var databaseReference = FirebaseDatabase.getInstance().getReference(Config.FIREBASE_DATABASE_NAME)
                databaseReference.child(address?.key!!).setValue(null)

                //remove the address from recycler view
                var position = adapterPosition
                deleteItem(this, position)



            }



        }


    }



}