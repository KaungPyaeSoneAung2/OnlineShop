package com.example.myapplication.onlineshopapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.activities.AddProductActivity
import com.example.myapplication.onlineshopapp.adapter.UserProductsAdapter
import com.example.myapplication.onlineshopapp.databinding.FragmentProductsBinding
import com.example.myapplication.onlineshopapp.firestore.FireStore
import com.example.myapplication.onlineshopapp.model.Product

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.noProductsFound
        homeViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = "This is home Fragment"
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id= item.itemId
        when(id){
            R.id.addProductMenu -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()

        getProductListFromFireStore()
    }

    private fun getProductListFromFireStore() {
        // Show the progress dialog.

        // Call the function of Firestore class.
        FireStore().getProductsList(this@ProductsFragment)
    }

    /**
     * A function to get the successful product list from cloud firestore.
     *
     * @param productsList Will receive the product list from cloud firestore.
     */
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {


        if (productsList.size > 0) {
            binding.myProductItems.visibility = View.VISIBLE
            binding.noProductsFound.visibility = View.GONE

            binding.myProductItems.layoutManager = LinearLayoutManager(activity)
            binding.myProductItems.setHasFixedSize(true)

            val adapterProducts =
                UserProductsAdapter(requireActivity(), productsList, this@ProductsFragment)
            binding.myProductItems.adapter = adapterProducts
        } else {
            binding.myProductItems.visibility = View.GONE
            binding.noProductsFound.visibility = View.VISIBLE
        }
    }

    /**
     * A function that will call the delete function of FirestoreClass that will delete the product added by the user.
     *
     * @param productID To specify which product need to be deleted.
     */
    fun deleteProduct(productID: String) {

        showAlertDialogToDeleteProduct(productID)
    }

    /**
     * A function to notify the success result of product deleted from cloud firestore.
     */
    fun productDeleteSuccess() {


        Toast.makeText(
            requireActivity(),
            "Deletion successfully",
            Toast.LENGTH_SHORT
        ).show()

        // Get the latest products list from cloud firestore.
        getProductListFromFireStore()
    }

    /**
     * A function to show the alert dialog for the confirmation of delete product from cloud firestore.
     */
    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle("Delete")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->


            // Call the function of Firestore class.
            FireStore().deleteProduct(this@ProductsFragment, productID)

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    // END
}