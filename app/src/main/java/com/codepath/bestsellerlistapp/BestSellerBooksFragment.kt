package com.codepath.bestsellerlistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler

import okhttp3.Headers
import org.json.JSONArray

// ------------------------------------
// API Key - NY Times Books API
// ------------------------------------
private const val API_KEY = "kGL5cx5iXejBt1BVr9AeHV6Pk9qUrnLQ"

/**
 * The class for the only fragment in the app, which contains the progress bar,
 * recyclerView, and performs the network calls to the NY Times API.
 */
class BestSellerBooksFragment : Fragment(), OnListFragmentInteractionListener {

    /**
     * Constructing the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_best_seller_books_list, container, false)

        val progressBar = view.findViewById<ContentLoadingProgressBar>(R.id.progress)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)

        // Set up the RecyclerView layout as a grid with 2 columns
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)

        // Call method to perform network request and update the adapter
        updateAdapter(progressBar, recyclerView)

        return view
    }

    /**
     * Updates the RecyclerView adapter with new data. This is where the networking magic happens!
     */
    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Create and set up AsyncHTTPClient
        val client = AsyncHttpClient()
        val url = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=$API_KEY"

        // Perform the HTTP request
        client.get(url, object : JsonHttpResponseHandler() {
            // On success
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JSON
            ) {
                // The wait for a response is over
                progressBar.hide()

                // Parse JSON into Models
                try {
                    val resultsJSON = json.jsonObject.getJSONObject("results").getJSONArray("books")
                    val models: List<BestSellerBook> = BestSellerBook.fromJson(resultsJSON)

                    // Set the adapter to the RecyclerView with the parsed data
                    recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)

                    // Log the successful response
                    Log.d("BestSellerBooksFragment", "Response successful")
                } catch (e: Exception) {
                    Log.e("BestSellerBooksFragment", "Failed to parse JSON: $e")
                }
            }

            // On failure
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                // The wait for a response is over
                progressBar.hide()

                // If the error is not null, log it
                t?.message?.let {
                    Log.e("BestSellerBooksFragment", "Error: $errorResponse")
                }
            }
        })
    }

    /**
     * What happens when a particular book is clicked.
     */
    override fun onItemClick(item: BestSellerBook) {
        Toast.makeText(context, "Selected book: " + item.title, Toast.LENGTH_LONG).show()
    }
}
