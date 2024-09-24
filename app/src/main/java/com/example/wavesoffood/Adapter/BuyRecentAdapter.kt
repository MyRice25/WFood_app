package com.example.wavesoffood.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wavesoffood.databinding.ItemBuyRecentBinding

class BuyRecentAdapter(private val buyRecentFoodName:ArrayList<String>, private val buyRecentPrice:ArrayList<String>,
                       private val buyRecentImage:ArrayList<Int>) : RecyclerView.Adapter<BuyRecentAdapter.BuyRecentViewHolder>() {


    override fun onBindViewHolder(holder: BuyRecentViewHolder, position: Int) {
        holder.bind(buyRecentFoodName[position],buyRecentPrice[position],buyRecentImage[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyRecentViewHolder {
        val binding = ItemBuyRecentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyRecentViewHolder(binding)
    }

    override fun getItemCount(): Int = buyRecentFoodName.size

    class BuyRecentViewHolder ( private val binding : ItemBuyRecentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(RecentName: String, RecentPrice: String, RecentImage: Int) {
            /*binding.txtBuyName.text = RecentName
            binding.txtBuyPrice.text = RecentPrice
            binding.imgBuy.setImageResource(RecentImage)*/
        }

    }
}