package com.protect.jikigo.ui

import com.protect.jikigo.data.Coupon

interface HomeStoreItemClickListener {
    fun onClickStore(coupon: Coupon)
}