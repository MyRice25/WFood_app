package com.example.wavesoffood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wavesoffood.Adapter.NotificationAdapter
import com.example.wavesoffood.R
import com.example.wavesoffood.databinding.FragmentNotificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NotificationFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(layoutInflater, container,false)
        val notifications = listOf("Your order has been Canceled Successfully","Order has been taken by the driver","Congrats Your Order Placed")
        val notificationImages = listOf(R.drawable.sademoji, R.drawable.car_02, R.drawable.accept2)
        val adapter = NotificationAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages))
        binding.rycNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.rycNotification.adapter = adapter
        return binding.root
    }

    companion object {

    }
}