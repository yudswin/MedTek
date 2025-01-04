package com.medtek.main.core.presentation.music.service

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.medtek.main.R

sealed class Track(
    @RawRes val id: Int,
    @DrawableRes val image: Int
) {
    object track_one : Track(
        id = R.raw.one,
        image = R.drawable.one
    )

    object track_two : Track(
        id = R.raw.two,
        image = R.drawable.two
    )

    object track_three : Track(
        id = R.raw.three,
        image = R.drawable.three
    )

    object track_four : Track(
        id = R.raw.four,
        image = R.drawable.four
    )

    object track_five : Track(
        id = R.raw.five,
        image = R.drawable.five
    )
}