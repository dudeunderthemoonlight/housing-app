package com.example.houseapp.ui.adapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun View.bindVisibility(value: Boolean?) {
    visibility = if (value == true) View.VISIBLE else View.GONE
}