package com.example.myapplication.onlineshopapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.GlideLoader
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.activities.ProductDetailsActivity
import com.example.myapplication.onlineshopapp.model.Posts
import com.example.myapplication.onlineshopapp.ui.home.ProductsFragment

open class UserProductsAdapter(
    private val context: Context,
    private var list: ArrayList<Posts>,
    private val fragment: ProductsFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(model, holder.itemView.findViewById(R.id.item_image))

            holder.itemView.findViewById<TextView>(R.id.item_name).text = model.title
            holder.itemView.findViewById<TextView>(R.id.item_price).text = "$${model.price}"

            holder.itemView.findViewById<ImageButton>(R.id.delete_product).setOnClickListener {

                fragment.deleteProduct(model.postID)
            }

            holder.itemView.setOnClickListener {
                // Launch Product details screen.
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.postID)
                context.startActivity(intent)
            }
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}