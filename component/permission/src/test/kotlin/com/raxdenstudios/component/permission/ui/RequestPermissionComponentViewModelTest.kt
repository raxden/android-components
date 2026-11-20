package com.raxdenstudios.component.permission.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.core.Answer
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import com.raxdenstudios.commons.permissions.model.Permission
import com.raxdenstudios.component.permission.R
import com.raxdenstudios.component.permission.ui.mapper.RequestPermissionComponentMapper
import com.raxdenstudios.component.permission.ui.model.RequestPermissionModel
import com.raxdenstudios.platform.device.domain.model.PermissionResult
import com.raxdenstudios.platform.device.domain.usecase.RequestPermissionUseCase
import com.raxdenstudios.platform.ui.EventDelegateImpl
import com.raxdenstudios.platform.ui.model.StringResource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
internal class RequestPermissionComponentViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher = testDispatcher)

    private val mapper: RequestPermissionComponentMapper = mockk()
    private val requestPermissionUseCase: RequestPermissionUseCase = mockk()

    private val model = RequestPermissionModel(
        reason = StringResource.ResourceId(R.string.component_permission_generic_title),
        reasonDescription = StringResource.ResourceId(R.string.component_permission_generic_description),
        acceptLabel = StringResource.ResourceId(R.string.component_permission_generic_accept),
        deniedLabel = StringResource.ResourceId(R.string.component_permission_generic_denied),
    )

    private lateinit var viewModel: RequestPermissionComponentViewModel

    @Before
    fun setUp() {
        viewModel = RequestPermissionComponentViewModel(
            requestPermissionComponentMapper = mapper,
            requestPermissionUseCase = requestPermissionUseCase,
            stateDelegate = RequestPermissionComponentStateDelegateImpl(),
            eventDelegate = EventDelegateImpl(),
        )
    }

    @Test
    fun `Given rationale result When permission is requested Then uiState model is set`() = runTest {
        val permission = Permission.Camera
        coEvery { requestPermissionUseCase(any()) } returns
            Answer.Success(PermissionResult.Rationale(permission))
        every { mapper.toModel(permission) } returns model

        viewModel.onAction(RequestPermissionComponentAction.OnPermissionRequested(permission))
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.model).isSameInstanceAs(model)
        verify { mapper.toModel(permission) }
    }

    @Test
    fun `Given dialog shown When rationale dismissed Then uiState model is cleared`() = runTest {
        val permission = Permission.Camera
        coEvery { requestPermissionUseCase(any()) } returns
            Answer.Success(PermissionResult.Rationale(permission))
        every { mapper.toModel(permission) } returns model
        viewModel.onAction(RequestPermissionComponentAction.OnPermissionRequested(permission))
        advanceUntilIdle()

        viewModel.onAction(RequestPermissionComponentAction.OnRationaleDismissed)

        assertThat(viewModel.uiState.value.model).isNull()
    }

    @Test
    fun `Given granted result When permission is requested Then emits Granted`() = runTest {
        val permission = Permission.Camera
        coEvery { requestPermissionUseCase(any()) } returns
            Answer.Success(PermissionResult.Granted(permission))

        viewModel.viewEvent.test {
            viewModel.onAction(RequestPermissionComponentAction.OnPermissionRequested(permission))
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(RequestPermissionComponentUIEvent.Granted(permission))
        }
    }
}
