package com.example.wavesoffood.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wavesoffood.databinding.ItemBuyAgainBinding

class BuyAgainAdapter(private val buyAgainFoodName:MutableList<String>,
                      private val buyAgainPrice:MutableList<String>,
                      private val buyAgainImage:MutableList<String>,
                      private var requireContext: Context) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(buyAgainFoodName[position],buyAgainPrice[position],buyAgainImage[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = ItemBuyAgainBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int = buyAgainFoodName.size
    inner class BuyAgainViewHolder(private val binding: ItemBuyAgainBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(buyName: String, buyPrice: String, buyImage: String) {
            binding.txtBuyNameAgain.text = buyName
            binding.txtBuyPriceAgain.text = buyPrice
            val uriString = buyImage
            val uri = Uri.parse(uriString)
            Glide.with(requireContext).load(uri).into(binding.imgBuyAgain)
        }

    }

}