package com.example.myapplication.onlineshopapp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.activities.DashBoardActivity
import com.example.myapplication.onlineshopapp.activities.ProductDetailsActivity
import com.example.myapplication.onlineshopapp.activities.SettingsActivity
import com.example.myapplication.onlineshopapp.databinding.FragmentDashboardBinding
import com.example.myapplication.onlineshopapp.firestore.FireStore
import com.example.myapplication.onlineshopapp.model.Product
import com.myshoppal.ui.adapters.DashboardItemsListAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private var dashBoardActivity:DashBoardActivity = DashBoardActivity()

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
            ViewModelProvider(this)[DashboardViewModel::class.java]

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
            R.id.action_Search ->{
                Toast.makeText(activity, "Couldn't Finish it due to errors \n Gomenasai", Toast.LENGTH_SHORT).show()
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

    private fun getDashboardItemsList() {

        FireStore().getDashboardItemsList(this@DashboardFragment)
    }

    fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>) {


        if (dashboardItemsList.size > 0) {

            binding.dashboardItems.visibility = View.VISIBLE
            binding.noItemText.visibility = View.GONE

            binding.dashboardItems.layoutManager = GridLayoutManager(activity, 2)
            binding.dashboardItems.setHasFixedSize(true)

            val adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)
            binding.dashboardItems.adapter = adapter

            adapter.setOnClickListener(object :
                DashboardItemsListAdapter.OnClickListener {
                override fun onClick(position: Int, product: Product) {

                    Intent(context,ProductDetailsActivity::class.java).putExtra(Constants.EXTRA_PRODUCT_ID,product.product_id).also { startActivity(it) }

                }
            })
        } else {
            binding.dashboardItems.visibility = View.GONE
            binding.noItemText.visibility = View.VISIBLE
        }
    }

}