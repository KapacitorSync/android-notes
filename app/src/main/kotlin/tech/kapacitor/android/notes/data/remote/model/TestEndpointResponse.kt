package tech.kapacitor.android.notes.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TestEndpointResponse(
    override val message: String,
    override val body: Boolean
) : ApiResponse<Boolean>