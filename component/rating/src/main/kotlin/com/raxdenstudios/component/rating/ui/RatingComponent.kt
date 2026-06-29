package com.raxdenstudios.component.rating.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.component.rating.R
import com.raxdenstudios.platform.core.util.IntentFactory
import com.raxdenstudios.platform.core.util.IntentType
import com.raxdenstudios.platform.ui.Component
import com.raxdenstudios.platform.ui.ScreenState
import com.raxdenstudios.platform.ui.composable.BottomSheet
import com.raxdenstudios.platform.ui.composable.Label
import com.raxdenstudios.platform.ui.composable.Spacer
import com.raxdenstudios.platform.ui.model.StringResource
import com.raxdenstudios.platform.ui.theme.Spacing.xLarge
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RatingComponent(
    modifier: Modifier = Modifier,
    screenState: ScreenState,
    viewModel: RatingComponentViewModel,
) : Component<RatingComponentUIState, RatingComponentUIEvent>(
    modifier = modifier,
    screenState = screenState,
    viewModel = viewModel
), KoinComponent {

    private val intentFactory: IntentFactory by inject()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(uiState: RatingComponentUIState) {
        if (uiState.isVisible) {
            BottomSheet.Simple(
                primaryButton = StringResource.ResourceId(R.string.rating_dialog_later) to {
                    onAction(RatingComponentAction.DismissRating)
                },
                onDismissRequest = { onAction(RatingComponentAction.DismissRating) },
            ) {
                Label.TitleLarge(
                    StringResource.ResourceId(R.string.rating_dialog_title).toText()
                )
                Spacer.Vertical.Small()
                Label.BodyMedium(
                    StringResource.ResourceId(R.string.rating_dialog_message).toText()
                )
                Spacer.Vertical.Custom(xLarge)
                StarRatingRow(
                    rating = uiState.selectedRating,
                    onRatingSelected = { onAction(RatingComponentAction.RateApp(it)) },
                )
                Spacer.Vertical.Custom(xLarge)
            }
        }
    }

    override suspend fun handleUIEvent(uiEvent: RatingComponentUIEvent) {
        when (uiEvent) {
            is RatingComponentUIEvent.OpenPlayStore -> {
                val intent = intentFactory.build(IntentType.PlayStore)
                navigator.navigate(intent)
            }
        }
    }
}

@Composable
private fun StarRatingRow(
    rating: Int,
    modifier: Modifier = Modifier,
    onRatingSelected: (Int) -> Unit,
) {
    val starColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        for (index in 1..5) {
            IconButton(onClick = { onRatingSelected(index) }) {
                Icon(
                    imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = null,
                    tint = starColor,
                    modifier = Modifier.size(48.dp),
                )
            }
        }
    }
}

