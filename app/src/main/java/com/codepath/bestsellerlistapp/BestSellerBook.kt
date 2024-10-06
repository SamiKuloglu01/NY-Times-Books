package com.codepath.bestsellerlistapp

import com.google.gson.annotations.SerializedName

import com.google.gson.Gson
import org.json.JSONArray

/**
 * The Model for storing a single book from the NY Times API.
 */
class BestSellerBook {

    @SerializedName("rank")
    var rank: Int = 0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("author")
    var author: String? = null

    @SerializedName("book_image")
    var bookImageUrl: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("amazon_product_url")
    var amazonUrl: String? = null

    companion object {
        /**
         * Factory method to convert a JSON array into a list of BestSellerBook objects
         */
        fun fromJson(jsonArray: JSONArray): List<BestSellerBook> {
            val books = mutableListOf<BestSellerBook>()
            val gson = Gson()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val book = gson.fromJson(jsonObject.toString(), BestSellerBook::class.java)
                books.add(book)
            }

            return books
        }
    }
}
