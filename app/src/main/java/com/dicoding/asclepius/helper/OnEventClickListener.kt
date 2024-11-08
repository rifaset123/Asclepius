package com.dicoding.asclepius.helper

import com.dicoding.asclepius.data.response.ArticlesItem


interface OnEventClickListener {
    fun onEventClick(event: ArticlesItem)
}