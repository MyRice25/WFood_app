package com.example.wavesoffood.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wavesoffood.databinding.ActivityDetailsBinding
import com.example.wavesoffood.model.CartItems
import com.example.wavesoffood.model.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailsBinding
    private var foodName : String? = null
    private var foodImage : String? = null
    private var foodDescriptions : String? = null
    private var foodIngredients : String? = null
    private var foodPrice : String? = null

    private lateinit var menuItem: MenuItem

    private lateinit var auth :FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase auth
        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemName")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")
        foodDescriptions = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")


        with(binding){
            txtDetailsName.text = foodName
            txtDescription.text = foodDescriptions
            txtIngredient.text = foodIngredients
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(imgDetails)
        }

        /*val foodNameDetails = intent.getStringExtra("MenuItemName")
        val foodImageDetails = intent.getIntExtra("MenuItemImage",0)
        binding.txtDetailsName.text = foodNameDetails
        binding.imgDetails.setImageResource(foodImageDetails)*/
        // back
        binding.icBack.setOnClickListener {
            finish()
        }
        binding.btnAddToCartDetails.setOnClickListener {
            addItemToCart()
        }

    }
    //khong phai,nay t thay m add o 1 app khac mà

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""
        // Create a cartItems object
        val cartItem = CartItems(
            foodName.toString(),
            foodPrice.toString(),
            foodDescriptions.toString(),
            foodImage.toString(),
            1,
            foodIngredients.toString()
            )
        // save data to cart item to firebase
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this,"Items added into cart successfully",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Items not added",Toast.LENGTH_SHORT).show()
        }
    }
}