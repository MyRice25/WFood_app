package com.example.wavesoffood.Adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.wavesoffood.databinding.ItemCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class CartAdapter(private val context: Context,
                  private val cartItems:MutableList<String>,
                  private val cartItemPrices:MutableList<String>,
                  private var cartDescriptions: MutableList<String>,
                  private var cartImages:MutableList<String>,
                  private var cartIngredient: MutableList<String>,
                  private val cartQuantity: MutableList<Int>): RecyclerView.Adapter<CartAdapter.CartViewholder>() {

    //        firebase
    private val auth = FirebaseAuth.getInstance()
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItemNumber){1}
        cartItemsReference = database.reference.child("user").child(userId).child("CartItems")
    }
    companion object{
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference
    }

    /*private val itemQuantities = IntArray(cartItems.size){1}*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewholder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewholder(binding)
    }


    override fun onBindViewHolder(holder: CartViewholder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size  // return = cartItems.size (ghi tat)
    // get update quantities
    fun getUpdatedItemsQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }

    inner class CartViewholder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                txtCartName.text = cartItems[position]
                txtCartPrice.text = cartItemPrices[position]
                /*imgCart.setImageResource(cartImage[position])*/
                // load image
                val uriString = cartImages[position]
//                Log.d("image","food Url: $uriString")
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri)/*.listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide","onLoadFailed: Image loading failed")
                        return false
                    }
                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide","onLoadFailed: Image loading Success")
                        return false
                    }
                })*/.into(imgCart) // ktra lỗi load ảnh (trong cmt)
                // plus and minus and quality
                txtQuantity.text = quantity.toString()
                // plus,minus,trash
                btnMinus.setOnClickListener {
                    deceaseQuantity(position)
                }
                btnPlus.setOnClickListener {
                    increaseQuantity(position)
                }
                imgBtnTrash.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION){
                        deleteItem(itemPosition)
                    }
                }
            }
        }
        private fun increaseQuantity(position: Int){
            if (itemQuantities[position] < 10){
                itemQuantities[position]++
                cartQuantity[position] = itemQuantities[position] // total
                binding.txtQuantity.text = itemQuantities[position].toString()
            }
        }
        private fun deceaseQuantity(position: Int){
            if (itemQuantities[position] > 1){
                itemQuantities[position]--
                cartQuantity[position] = itemQuantities[position] // total
                binding.txtQuantity.text = itemQuantities[position].toString()
            }
        }
        private fun deleteItem(position: Int){
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve){uniqueKey ->
                if (uniqueKey != null){
                    removeItem(position,uniqueKey)
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if (uniqueKey != null){
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartImages.removeAt(position)
                    cartDescriptions.removeAt(position)
                    cartQuantity.removeAt(position)
                    cartItemPrices.removeAt(position)
                    cartIngredient.removeAt(position)
                    Toast.makeText(context, "Items Deleted", Toast.LENGTH_SHORT).show()
                    // update itemQuantities
                    itemQuantities = itemQuantities.filterIndexed { index, i -> index!= position }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete:(String?) -> Unit) {
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    //loop for snapshot children
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve){
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}