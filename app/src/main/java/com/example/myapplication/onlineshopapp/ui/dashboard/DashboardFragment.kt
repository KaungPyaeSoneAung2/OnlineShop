package com.example.myapplication.onlineshopapp.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.GlideLoader
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.activities.DashBoardActivity
import com.example.myapplication.onlineshopapp.activities.SettingsActivity
import com.example.myapplication.onlineshopapp.adapter.DashboardItemsListAdapter
import com.example.myapplication.onlineshopapp.databinding.FragmentDashboardBinding
import com.example.myapplication.onlineshopapp.firestore.FireStore
import com.example.myapplication.onlineshopapp.model.Posts
import com.example.myapplication.onlineshopapp.model.User
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private var dashBoardActivity:DashBoardActivity = DashBoardActivity()

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
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dashboardViewModel.text.observe(viewLifecycleOwner) {
        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id= item.itemId
        when(id){
            R.id.action_setting -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
//    private fun getUserDetails(){
//        FireStore().getUserDetails(dashBoardActivity)
//    }
//    fun UserDetailsSuccess(user: User){
//        //    GlideLoader(dashBoardActivity).loadUserPicture(Uri.parse(user.image),binding.imageUserDashboard)
//        binding.NameDashboard.text = "${user.firstName} ${user.lastName }"
//        binding.NameDashboard.setOnClickListener {
//            Toast.makeText(DashBoardActivity(), "Hello", Toast.LENGTH_SHORT).show() }
//        binding.emailDashboard.text= user.email
//        binding.phNumberDashboard.text=user.mobile.toString()
//        binding.addressDashboard.text= user.address
//    }

    override fun onResume() {
        super.onResume()

        getDashboardItemsList()
    }

    /**
     * A function to get the dashboard items list from cloud firestore.
     */
    private fun getDashboardItemsList() {

        FireStore().getDashboardItemsList(this@DashboardFragment)
    }

    /**
     * A function to get the success result of the dashboard items from cloud firestore.
     *
     * @param dashboardItemsList
     */
    fun successDashboardItemsList(dashboardItemsList: ArrayList<Posts>) {


        if (dashboardItemsList.size > 0) {

            binding.dashboardItems.visibility = View.VISIBLE
            binding.noItemText.visibility = View.GONE

            binding.dashboardItems.layoutManager = GridLayoutManager(activity, 2)
            binding.dashboardItems.setHasFixedSize(true)

            val adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)
            binding.dashboardItems.adapter = adapter

            //TODO Step 6: Define the onclick listener here that is defined in the adapter class and handle the click on an item in the base class.
            // Earlier we have done is a different way of creating the function and calling it from the adapter class based on the instance of the class.

            // START
            adapter.setOnClickListener(object :
                DashboardItemsListAdapter.OnClickListener {
                override fun onClick(position: Int, product: Product) {

                    // TODO Step 7: Launch the product details screen from the dashboard.
                    // START
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                    startActivity(intent)
                    // END
                }
            })
            // END
        } else {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }
    }

}