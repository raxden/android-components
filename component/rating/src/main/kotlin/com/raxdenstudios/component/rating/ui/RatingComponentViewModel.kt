package com.raxdenstudios.component.rating.ui

import androidx.lifecycle.ViewModel
import com.raxdenstudios.commons.core.ext.onSuccess
import com.raxdenstudios.component.rating.domain.ShouldShowRatingDialogUseCase
import com.raxdenstudios.component.rating.domain.UpdateRatingStateUseCase
import com.raxdenstudios.platform.ui.ActionDelegate
import com.raxdenstudios.platform.ui.EventDelegate
import com.raxdenstudios.platform.ui.safeLaunch
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

typealias RatingComponentEventDelegate = EventDelegate<RatingComponentUIEvent>
typealias RatingComponentActionDelegate = ActionDelegate<RatingComponentAction>

class RatingComponentViewModel(
    private val shouldShowRatingDialogUseCase: ShouldShowRatingDialogUseCase,
    private val updateRatingStateUseCase: UpdateRatingStateUseCase,
    private val stateDelegate: RatingComponentStateDelegate,
    private val eventDelegate: RatingComponentEventDelegate,
) : ViewModel(),
    RatingComponentStateDelegate by stateDelegate,
    RatingComponentEventDelegate by eventDelegate,
    RatingComponentActionDelegate {

    init {
        checkRatingDialog()
    }

    override fun onAction(action: RatingComponentAction) {
        when (action) {
            is RatingComponentAction.RateApp -> rateApp(action.value)
            is RatingComponentAction.DismissRating -> dismissRating()
        }
    }

    private fun checkRatingDialog() = safeLaunch {
        shouldShowRatingDialogUseCase()
            .onSuccess { shouldShow ->
                if (shouldShow) {
                    showRating()
                }
            }
    }

    private fun rateApp(value: Int) = safeLaunch {
        updateRating(value)
        delay(500.milliseconds)

        updateRatingStateUseCase.markAsRated()
            .onSuccess {
                hideRating()
                if (value == FIVE_STARS) {
                    emitEvent(RatingComponentUIEvent.OpenPlayStore)
                }
            }
    }

    private fun dismissRating() = safeLaunch {
        updateRatingStateUseCase.markAsDismissed()
            .onSuccess { hideRating() }
    }

    companion object {
        const val FIVE_STARS = 5
    }
}
