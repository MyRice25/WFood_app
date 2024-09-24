package com.example.wavesoffood.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wavesoffood.Adapter.MenuAdapter
import com.example.wavesoffood.databinding.FragmentMenuBottomSheetBinding
import com.example.wavesoffood.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentMenuBottomSheetBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container,false)
        binding.btnBack.setOnClickListener {
            dismiss()
        }
       /* val menuFoodName = listOf("Buger","Sandwich","momo","item","hul","mmmm","ooao")
        val menuItemPrice = listOf("$4","$7","$8","$10","$4","$7","$8")
        val menuImage = listOf(
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3
        )
        val adapter = MenuAdapter(ArrayList(menuFoodName),ArrayList(menuItemPrice),ArrayList(menuImage),requireContext())*/

        retrieveMenuItems()


        return binding.root
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                Log.d("ITEMS","onDataChange: Data received")
                // once data receive , set to adapter
                setAdapter()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun setAdapter() {
        if (menuItems.isNotEmpty()){
            val adapter = MenuAdapter(menuItems,requireContext())
            binding.rycMenu.layoutManager = LinearLayoutManager(requireContext())
            binding.rycMenu.adapter = adapter
            Log.d("ITEMS","setAdapter: data set")
        }else{
            Log.d("ITEMS","setAdapter: data NOT set")
        }
    }

    companion object {

    }

}
