package tech.kapacitor.android.notes.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Body(
    val version: String
)

@Serializable
data class GetVersionEndpointResponse(
    override val message: String,
    override val body: Body
) : ApiResponse<Body>