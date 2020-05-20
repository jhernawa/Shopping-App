package com.example.shoppingapp.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.shoppingapp.fragments.ProductFragment
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.models.SubCategory
import kotlinx.android.synthetic.main.toolbar_layout.*

class SubCategoryAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){

    private var subCategoryList : ArrayList<SubCategory>? = ArrayList<SubCategory>()
    private var productFragmentList : ArrayList<ProductFragment>? = ArrayList<ProductFragment>()

    override fun getItem(position: Int): Fragment {
        return productFragmentList!!.get(position)

    }

    override fun getCount(): Int {

        return productFragmentList!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return subCategoryList!!.get(position).subName
    }

    fun setSubCategory(subCategoryList : ArrayList<SubCategory>){
        this.subCategoryList = subCategoryList
    }

    fun addFragment(subCategoryId : Int){

        productFragmentList?.add(ProductFragment.newInstance(subCategoryId))

        Log.i("sizeFragment", productFragmentList?.size.toString())
    }




}