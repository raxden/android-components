package com.raxdenstudios.component.permission.ui.model

import androidx.compose.runtime.Immutable
import com.raxdenstudios.commons.android.compose.model.StringResource

@Immutable
class RequestPermissionModel(
    val reason: StringResource,
    val reasonDescription: StringResource,
    val acceptLabel: StringResource,
    val deniedLabel: StringResource,
)
