package com.raxdenstudios.component.permission.ui.mapper

import android.annotation.SuppressLint
import com.raxdenstudios.commons.android.compose.model.StringResource
import com.raxdenstudios.commons.permissions.model.Permission
import com.raxdenstudios.component.permission.R
import com.raxdenstudios.component.permission.ui.model.RequestPermissionModel

interface RequestPermissionComponentMapper {

    fun toModel(
        permission: Permission,
    ): RequestPermissionModel
}

class RequestPermissionComponentMapperImpl : RequestPermissionComponentMapper {

    @SuppressLint("NewApi")
    override fun toModel(
        permission: Permission,
    ) = when (permission) {
        Permission.AccessCoarseLocation,
        Permission.AccessFineLocation,
        -> locationModel()

        Permission.PostNotifications -> notificationsModel()

        Permission.CallPhone,
        Permission.Camera,
        is Permission.Other,
        Permission.ReadContacts,
        Permission.ReadExternalStorage,
        Permission.RecordAudio,
        Permission.WriteContacts,
        Permission.WriteExternalStorage,
        -> genericModel()
    }

    private fun locationModel() = RequestPermissionModel(
        reason = StringResource.ResourceId(R.string.component_permission_location_title),
        reasonDescription = StringResource.ResourceId(R.string.component_permission_location_description),
        acceptLabel = StringResource.ResourceId(R.string.component_permission_location_accept),
        deniedLabel = StringResource.ResourceId(R.string.component_permission_location_denied),
    )

    private fun notificationsModel() = RequestPermissionModel(
        reason = StringResource.ResourceId(R.string.component_permission_notifications_title),
        reasonDescription = StringResource.ResourceId(R.string.component_permission_notifications_description),
        acceptLabel = StringResource.ResourceId(R.string.component_permission_notifications_accept),
        deniedLabel = StringResource.ResourceId(R.string.component_permission_notifications_denied),
    )

    private fun genericModel() = RequestPermissionModel(
        reason = StringResource.ResourceId(R.string.component_permission_generic_title),
        reasonDescription = StringResource.ResourceId(R.string.component_permission_generic_description),
        acceptLabel = StringResource.ResourceId(R.string.component_permission_generic_accept),
        deniedLabel = StringResource.ResourceId(R.string.component_permission_generic_denied),
    )
}
