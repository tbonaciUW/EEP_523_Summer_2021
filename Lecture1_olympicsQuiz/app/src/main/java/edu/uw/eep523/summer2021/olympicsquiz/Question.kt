package edu.uw.eep523.summer2021.olympicsquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean)
