package com.codepath.bestsellerlistapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.bestsellerlistapp.R

/**
 * [RecyclerView.Adapter] that can display a [BestSellerBook] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class BestSellerBooksRecyclerViewAdapter(
    private val books: List<BestSellerBook>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<BestSellerBooksRecyclerViewAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_best_seller_book, parent, false) // Updated layout file name
        return BookViewHolder(view)
    }

    /** This inner class lets us refer to all the different View elements. */
    inner class BookViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: BestSellerBook? = null
        val mRanking: TextView = mView.findViewById(R.id.ranking) // Reference to the ranking TextView
        val mBookTitle: TextView = mView.findViewById(R.id.book_title)
        val mBookAuthor: TextView = mView.findViewById(R.id.book_author)
        val mBookImage: ImageView = mView.findViewById(R.id.book_image) // For the book image
        val mBookDescription: TextView = mView.findViewById(R.id.book_description) // Reference to book description
        val mBuyButton: Button = mView.findViewById(R.id.buy_button) // Reference to Buy button

        override fun toString(): String {
            return "${mBookTitle.text} by '${mBookAuthor.text}'"
        }
    }

    /** This lets us "bind" each Views in the ViewHolder to its actual data! */
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.mItem = book
        holder.mBookTitle.text = book.title
        holder.mBookAuthor.text = book.author
        holder.mBookDescription.text = book.description // Set the description text
        holder.mRanking.text = (position + 1).toString() // Set the rank (position + 1)

        // Load the book image using Glide
        Glide.with(holder.mView.context)
            .load(book.bookImageUrl) // Assuming you have a bookImageUrl in your model
            .into(holder.mBookImage)

        holder.mView.setOnClickListener {
            holder.mItem?.let { book ->
                mListener?.onItemClick(book)
            }
        }

        // Handle the Buy button click
        holder.mBuyButton.setOnClickListener {
            book.amazonUrl?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                holder.mView.context.startActivity(intent)
            }
        }
    }

    /** Remember: RecyclerView adapters require a getItemCount() method. */
    override fun getItemCount(): Int {
        return books.size
    }
}
