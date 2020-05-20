package com.example.shoppingapp.app

class Endpoints{

    companion object{

        private const val URL_CATEGORY = "category/"
        private const val URL_SUB_CATEGORY = "subcategory/"
        private const val URL_PRODUCT = "products/"

        fun getCategory(): String{
            return Config.BASE_URL + URL_CATEGORY
        }

        fun getSubCategory(catId : String?): String{
            return Config.BASE_URL + URL_SUB_CATEGORY + catId
        }


        fun getProduct(subId : String?) : String{
            return Config.BASE_URL + URL_PRODUCT + subId
        }



    }
}
