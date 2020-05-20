package com.example.shoppingapp.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.example.shoppingapp.R
import com.example.shoppingapp.activities.CartActivity
import com.example.shoppingapp.adapters.RecycleViewProductListAdapter
import com.example.shoppingapp.app.Endpoints
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.models.ProductResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_product.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val SUB_CATEGORY_ID = "param1"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProductFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var subCategoryId: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subCategoryId = it.getString(SUB_CATEGORY_ID)
        }

        Log.i("subcategoryId", subCategoryId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_product, container, false)

        Log.i("hello", "hello")
        //connect to recycleViewProductListAdapter to show product
        var recycleViewProductListAdapter : RecycleViewProductListAdapter = RecycleViewProductListAdapter(view.context)
        view.recycler_view_combined.layoutManager = LinearLayoutManager(activity)
        view.recycler_view_combined.adapter = recycleViewProductListAdapter

        //load the product data
        getProductData(view, recycleViewProductListAdapter)

        return view
    }

    private fun getProductData(view : View, recyclerViewProductListAdapter: RecycleViewProductListAdapter){

        var url = Endpoints.getProduct(subCategoryId)
        //var url = "https://apolis-grocery.herokuapp.com/api/products/${subCategoryId}"
        Log.i("url", url)

        var requestQueue = Volley.newRequestQueue(view.context)
        var stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener {
                //parse the json object before displaying it via adapter
                Log.i("dataProduct", it.toString())
                var gson = GsonBuilder().create()
                var ProductResponse = gson.fromJson(it.toString(), ProductResponse::class.java)
                var productList : ArrayList<Product> = ProductResponse.data

                //add product fragment to show from each subcategory
                recyclerViewProductListAdapter.setData(productList)

            },
            Response.ErrorListener {
                Log.e("data", it.message)
            })

        requestQueue.add(stringRequest)
}
// TODO: Rename method, update argument and hook method into UI event
fun onButtonPressed(uri: Uri) {
listener?.onFragmentInteraction(uri)
}

/*
override fun onAttach(context: Context) {
super.onAttach(context)
if (context is OnFragmentInteractionListener) {
    listener = context
} else {
    throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
}
}

override fun onDetach() {
super.onDetach()
listener = null
}
*/

/**
* This interface must be implemented by activities that contain this
* fragment to allow an interaction in this fragment to be communicated
* to the activity and potentially other fragments contained in that
* activity.
*
*
* See the Android Training lesson [Communicating with Other Fragments]
* (http://developer.android.com/training/basics/fragments/communicating.html)
* for more information.
*/
interface OnFragmentInteractionListener {
// TODO: Update argument type and name
fun onFragmentInteraction(uri: Uri)
}

companion object {
/**
 * Use this factory method to create a new instance of
 * this fragment using the provided parameters.
 *
 * @param param1 Parameter 1.
 * @param param2 Parameter 2.
 * @return A new instance of fragment ProductFragment.
 */
// TODO: Rename and change types and number of parameters
@JvmStatic
fun newInstance(subCategoryId : Int) =
    ProductFragment().apply {
        arguments = Bundle().apply {
            Log.i("fragment created", subCategoryId.toString())
            putString(SUB_CATEGORY_ID, subCategoryId.toString())

        }
    }


}
}
