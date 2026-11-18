package com.raxdenstudios.component.rating.data

import com.raxdenstudios.commons.core.Answer
import com.raxdenstudios.component.rating.domain.model.RatingData
import com.raxdenstudios.platform.core.domain.model.Failure
import kotlinx.coroutines.flow.Flow

interface RatingRepository {

    fun observeRatingData(): Flow<Answer<RatingData, Failure>>
    suspend fun updateRatingData(
        ratingData: RatingData,
    ): Answer<Unit, Failure>
    suspend fun getRatingData(): Answer<RatingData, Failure>
}

class RatingRepositoryImpl(
    private val ratingLocalDataSource: RatingLocalDataSource,
) : RatingRepository {

    override fun observeRatingData(): Flow<Answer<RatingData, Failure>> = ratingLocalDataSource.observeRatingData()

    override suspend fun getRatingData(): Answer<RatingData, Failure> = ratingLocalDataSource.getRatingData()

    override suspend fun updateRatingData(
        ratingData: RatingData,
    ): Answer<Unit, Failure> = ratingLocalDataSource.updateRatingData(ratingData)
}
