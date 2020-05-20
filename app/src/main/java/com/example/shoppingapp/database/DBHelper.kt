package com.example.shoppingapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.shoppingapp.models.Product

class DBHelper(var mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION_CODE) {

    var database: SQLiteDatabase = writableDatabase
    companion object {
        private const val DATABASE_NAME = "mydatabase"
        private const val TABLE_NAME = "product"
        private const val DATABASE_VERSION_CODE = 5
        private const val COLUMN_ID = "id"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_MRP = "mrp"
        private const val COLUMN_QUANTITY = "quantity"

    }

    override fun onCreate(database: SQLiteDatabase?) {
        val createTable =
            "create table $TABLE_NAME (" +
                    "$COLUMN_ID char(250), " +
                    "$COLUMN_IMAGE char(50), " +
                    "$COLUMN_NAME char(50), " +
                    "$COLUMN_PRICE DOUBLE, " + "$COLUMN_MRP DOUBLE, " +
                    "$COLUMN_QUANTITY INTEGER)"
        Log.i("data", createTable)
        database?.execSQL(createTable)
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var dropTable = "drop table $TABLE_NAME"
        database?.execSQL(dropTable)
        onCreate(database)
    }

    fun isIdExist(product : Product) : Boolean{

        var cursor =  database.rawQuery("select * from $TABLE_NAME where $COLUMN_ID=?", arrayOf(product._id))
        return cursor.count != 0

    }
    fun getQuantity(product : Product) : Int{
        var cursor =  database.rawQuery("select * from $TABLE_NAME where $COLUMN_ID=?", arrayOf(product._id))
        var qty = 0
        if(cursor != null && cursor.moveToFirst()){
            qty = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
        }
        return qty
    }

    fun createProduct(product : Product){

        var contentValues = ContentValues()
        contentValues.put(COLUMN_ID, product._id)
        contentValues.put(COLUMN_IMAGE, product.image)
        contentValues.put(COLUMN_NAME, product.productName)
        contentValues.put(COLUMN_PRICE, product.price)
        contentValues.put(COLUMN_MRP, product.mrp)
        contentValues.put(COLUMN_QUANTITY, 1)
        var x = database.insert(TABLE_NAME, null, contentValues)
        Log.e("data", x.toString())
    }

    fun readProduct(): ArrayList<Product>{
        var productList: ArrayList<Product> = ArrayList()
        var columns = arrayOf(
            COLUMN_ID,
            COLUMN_IMAGE,
            COLUMN_NAME,
            COLUMN_PRICE,
            COLUMN_MRP,
            COLUMN_QUANTITY
        )

        var cursor = database.query(TABLE_NAME, columns, null, null, null, null, null)
        if(cursor !=null && cursor.moveToFirst()){
            do{
                var id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                var image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                var productName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                var price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                var mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))
                var quantityOrdered = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))

                var product = Product(id, image, productName, "", price, mrp, quantityOrdered)
                productList.add(product)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }


    fun updateProduct(product: Product, isIncrease : Boolean){
        var whereClause = "$COLUMN_ID=?"
        var whereArgs = arrayOf(product._id)

        //get the curr quantity of this product
        var columns = arrayOf(
            COLUMN_ID,
            COLUMN_IMAGE,
            COLUMN_NAME,
            COLUMN_PRICE,
            COLUMN_MRP,
            COLUMN_QUANTITY
        )
        var cursor = database.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null)
        var currQty = 0
        if(cursor != null && cursor.moveToFirst()){
            currQty = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
        }

        //new entry to update the table
        var contentValues = ContentValues()
        contentValues.put(COLUMN_ID, product._id)
        contentValues.put(COLUMN_IMAGE, product.image)
        contentValues.put(COLUMN_NAME, product.productName)
        contentValues.put(COLUMN_PRICE, product.price)
        contentValues.put(COLUMN_MRP, product.mrp)

        if(isIncrease) currQty++
        else currQty--
        contentValues.put(COLUMN_QUANTITY, currQty)


        var x = database.update(TABLE_NAME, contentValues, whereClause, whereArgs)
        Log.i("x", x.toString())
    }

    fun deleteProduct(product: Product){

        var whereClause = "$COLUMN_ID=?"
        var whereArgs = arrayOf(product._id.toString())

        database.delete(TABLE_NAME, whereClause, whereArgs)
    }




}