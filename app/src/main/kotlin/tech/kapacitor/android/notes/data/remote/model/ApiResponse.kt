package tech.kapacitor.android.notes.data.remote.model

interface ApiResponse<T> {
    val message: String
    val body: T
}