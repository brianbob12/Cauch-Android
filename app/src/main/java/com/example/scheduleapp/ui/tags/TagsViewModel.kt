package com.example.scheduleapp.ui.tags

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TagsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Tags!"
    }
    val text: LiveData<String> = _text
}