package com.myshoppal.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.onlineshopapp.GlideLoader
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.model.Posts

/**
 * A adapter class for dashboard items list.
 */
open class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Posts>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // TODO Step 2: Create a global variable for OnClickListener interface.
    // START
    // A global variable for OnClickListener interface.
    private var onClickListener: OnClickListener? = null
    // END

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.dashboard_items_layout,
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
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.findViewById(R.id.iv_dashboard_item_image)
            )
            holder.itemView.findViewById<TextView>(R.id.dashboard_item_title).text = model.title
            holder.itemView.tv_dashboard_item_price.text = "$${model.price}"

            // TODO Step 5: Assign the on click event for item view and pass the required params in the on click function.
            // START
            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
            // END
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    // TODO Step 3: Create A function for OnClickListener where the Interface is the expected parameter and assigned to the global variable.
    // START
    /**
     * A function for OnClickListener where the Interface is the expected parameter and assigned to the global variable.
     *
     * @param onClickListener
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    // END

    // TODO Step 1: Create an interface for OnClickListener.
    /**
     * An interface for onclick items.
     */
    interface OnClickListener {

        // TODO Step 4: Define a function to get the required params when user clicks on the item view in the interface.
        // START
        fun onClick(position: Int, product: Product)
        // END
    }
    // END

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}