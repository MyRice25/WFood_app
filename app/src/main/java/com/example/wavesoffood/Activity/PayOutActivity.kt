package com.example.wavesoffood.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.wavesoffood.Fragment.CongratsBottomSheet
import com.example.wavesoffood.R
import com.example.wavesoffood.databinding.ActivityPayOutBinding
import com.example.wavesoffood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {

    lateinit var binding : ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemIngredient: ArrayList<String>
    private lateinit var foodItemQuantities: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        // set user data
        setUserData()
        // get user details form Firebase
        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient") as ArrayList<String>
        foodItemQuantities = intent.getIntegerArrayListExtra("FoodItemQuantities") as ArrayList<Int>
        // total
        totalAmount = calculateTotalAmount().toString() + "$"
        binding.edtTotalAmount.isEnabled = false
        binding.edtTotalAmount.setText(totalAmount)

        binding.btnPlaceMyOder.setOnClickListener {
            // get data
            name = binding.edtName.text.toString().trim()
            address = binding.edtAddress.text.toString().trim()
            phone = binding.edtPhone.text.toString().trim()
            if (name.isBlank() && address.isBlank() && phone.isBlank()){
                Toast.makeText(this,"Please Enter All The Details", Toast.LENGTH_SHORT).show()
            }else{
                plaseOder()
            }
            /*val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager,"Test")*/
        }
        binding.btnBack.setOnClickListener { finish() }

    }

    private fun plaseOder() {
        userId = auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(userId,name,foodItemName,foodItemPrice,foodItemImage,foodItemQuantities,address,totalAmount,phone,time,itemPushKey,false,false)
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager,"Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to order", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails){
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for(i in 0 until foodItemPrice.size){
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if (lastChar == '$') {
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantity = foodItemQuantities[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null){
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val name = snapshot.child("name").getValue(String::class.java)?:""
                        val address = snapshot.child("address").getValue(String::class.java)?:""
                        val phone = snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            edtName.setText(name)
                            edtAddress.setText(address)
                            edtPhone.setText(phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}

