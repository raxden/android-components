package com.raxdenstudios.component.rating.ui

import com.raxdenstudios.platform.ui.StateDelegate
import com.raxdenstudios.platform.ui.StateDelegateImpl

interface RatingComponentStateDelegate : StateDelegate<RatingComponentUIState> {
    fun showRating()
    fun hideRating()
    fun updateRating(rating: Int)
}

class RatingComponentStateDelegateImpl : StateDelegateImpl<RatingComponentUIState>(),
    RatingComponentStateDelegate {

    override val initialUIState: RatingComponentUIState
        get() = RatingComponentUIState()

    override fun updateRating(rating: Int) {
        updateState { value ->
            value.copy(selectedRating = rating)
        }
    }

    override fun showRating() {
        updateState { value ->
            value.copy(isVisible = true)
        }
    }

    override fun hideRating() {
        updateState { value ->
            value.copy(isVisible = false)
        }
    }
}
