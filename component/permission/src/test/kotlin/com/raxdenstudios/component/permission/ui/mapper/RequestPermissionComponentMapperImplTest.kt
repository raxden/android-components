package com.raxdenstudios.component.permission.ui.mapper

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.permissions.model.Permission
import com.raxdenstudios.component.permission.R
import com.raxdenstudios.component.permission.ui.model.RequestPermissionModel
import com.raxdenstudios.platform.ui.model.StringResource
import org.junit.Test

internal class RequestPermissionComponentMapperImplTest {

    private val mapper = RequestPermissionComponentMapperImpl()

    @Test
    fun `Given AccessFineLocation When toModel Then returns location model`() {
        val result = mapper.toModel(Permission.AccessFineLocation)

        result.assertLocation()
    }

    @Test
    fun `Given AccessCoarseLocation When toModel Then returns location model`() {
        val result = mapper.toModel(Permission.AccessCoarseLocation)

        result.assertLocation()
    }

    @Test
    fun `Given PostNotifications When toModel Then returns notifications model`() {
        val result = mapper.toModel(Permission.PostNotifications)

        assertModel(
            result,
            R.string.component_permission_notifications_title,
            R.string.component_permission_notifications_description,
            R.string.component_permission_notifications_accept,
            R.string.component_permission_notifications_denied,
        )
    }

    @Test
    fun `Given any other permission When toModel Then returns generic model and does not throw`() {
        val permissions = listOf(
            Permission.CallPhone,
            Permission.Camera,
            Permission.Other("custom.permission.NAME"),
            Permission.ReadContacts,
            Permission.ReadExternalStorage,
            Permission.RecordAudio,
            Permission.WriteContacts,
            Permission.WriteExternalStorage,
        )

        permissions.forEach { permission ->
            val result = mapper.toModel(permission)

            assertModel(
                result,
                R.string.component_permission_generic_title,
                R.string.component_permission_generic_description,
                R.string.component_permission_generic_accept,
                R.string.component_permission_generic_denied,
            )
        }
    }

    private fun RequestPermissionModel.assertLocation() = assertModel(
        this,
        R.string.component_permission_location_title,
        R.string.component_permission_location_description,
        R.string.component_permission_location_accept,
        R.string.component_permission_location_denied,
    )

    private fun assertModel(
        model: RequestPermissionModel,
        title: Int,
        description: Int,
        accept: Int,
        denied: Int,
    ) {
        assertThat(model.reason).isEqualTo(StringResource.ResourceId(title))
        assertThat(model.reasonDescription).isEqualTo(StringResource.ResourceId(description))
        assertThat(model.acceptLabel).isEqualTo(StringResource.ResourceId(accept))
        assertThat(model.deniedLabel).isEqualTo(StringResource.ResourceId(denied))
    }
}
