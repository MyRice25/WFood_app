package com.example.wavesoffood.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wavesoffood.Activity.DetailsActivity
import com.example.wavesoffood.databinding.ItemMenuBinding
import com.example.wavesoffood.model.MenuItem

class MenuAdapter(private val menuItems: List<MenuItem>,
                  private val requireContext: Context) : RecyclerView.Adapter<MenuAdapter.MenuViewholder>() {

    /*private val itemClickListener : OnClickListener ?= null*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewholder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewholder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewholder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewholder(private val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    /*itemClickListener?.onItemClick(position)*/
                    openDetailsActivity(position)
                }
                // set onclicklister to open details
                /*val intent = Intent(requireContext, DetailsActivity::class.java)
                intent.putExtra("MenuItemName", menuItems.get(position))
                intent.putExtra("MenuItemImage", menuImage.get(position))
                requireContext.startActivity(intent)*/
            }
        }
        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]
            // a intent to open details and pass data
            val intent = Intent(requireContext,DetailsActivity::class.java).apply {
                putExtra("MenuItemName",menuItem.foodName)
                putExtra("MenuItemImage",menuItem.foodImage)
                putExtra("MenuItemDescription",menuItem.foodDescription)
                putExtra("MenuItemIngredients",menuItem.foodIngredients)
                // cái màn hình nào m add item mới đâu ?

                putExtra("MenuItemPrice",menuItem.foodPrice)
            }
            // start the details activity
            requireContext.startActivity(intent)
        }
        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                txtFoodNameMenu.text = menuItem.foodName
                txtPricemenu.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(imgMenu)
            }
        }
    }
}




