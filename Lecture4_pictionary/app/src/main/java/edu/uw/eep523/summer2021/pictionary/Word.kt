package edu.uw.eep523.summer2021.pictionary

import androidx.annotation.StringRes

data class Word(@StringRes val wordResId: Int, val hintResId: Int)