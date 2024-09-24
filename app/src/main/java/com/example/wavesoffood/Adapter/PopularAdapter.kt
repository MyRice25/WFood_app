package com.example.wavesoffood.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wavesoffood.Activity.DetailsActivity
import com.example.wavesoffood.databinding.ItemPopularBinding

class PopularAdapter(private val items:List<String>,
                     private val price:List<String>,
                     private val image:List<Int>,
                     private val requireContext : Context ):RecyclerView.Adapter<PopularAdapter.PopulerViewHoder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopulerViewHoder {
        return PopulerViewHoder(ItemPopularBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopulerViewHoder, position: Int) {
        val item = items[position]
        val images = image[position]
        val price = price[position]
        holder.bind(item,images,price)

        holder.itemView.setOnClickListener {
            // set onclicklister to open details
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemName", item)
            intent.putExtra("MenuItemImage", images)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class PopulerViewHoder (private val binding:ItemPopularBinding) : RecyclerView.ViewHolder(binding.root){
        private val imagesView = binding.imgPopular
        fun bind(item: String, images: Int, price: String) {
            binding.txtFoodNamePopular.text = item
            binding.txtPricepopular.text = price
            imagesView.setImageResource(images)
        }

    }
}