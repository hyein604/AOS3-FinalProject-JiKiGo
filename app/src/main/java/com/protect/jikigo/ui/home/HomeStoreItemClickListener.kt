package com.protect.jikigo.ui.home

import com.protect.jikigo.data.model.Store

interface HomeStoreItemClickListener {
    fun onClickStore(store: Store)
}