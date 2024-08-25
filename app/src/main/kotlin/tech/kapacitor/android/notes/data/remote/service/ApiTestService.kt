package tech.kapacitor.android.notes.data.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.path
import kotlinx.coroutines.runBlocking
import tech.kapacitor.android.notes.data.remote.client.HttpRoutes
import tech.kapacitor.android.notes.data.remote.client.KtorClient
import tech.kapacitor.android.notes.data.remote.error.APIErrorReason
import tech.kapacitor.android.notes.data.remote.model.ApiResult
import tech.kapacitor.android.notes.data.remote.model.GetVersionEndpointResponse
import tech.kapacitor.android.notes.data.remote.model.TestEndpointResponse
import javax.inject.Inject

class ApiTestService
@Inject
constructor(
    ktorClient: KtorClient,
) {
    private var client: HttpClient

    suspend fun test(
        serverUrl: String,
        serverPort: Int,
    ): ApiResult<TestEndpointResponse> {
        try {
            val body: TestEndpointResponse =
                client
                    .get {
                        url {
                            host = serverUrl
                            port = serverPort
                            path(HttpRoutes.TEST)
                        }
                    }.body<TestEndpointResponse>()

            return ApiResult.Success(data = body)
        } catch (e: RedirectResponseException) {
            return ApiResult.Error(message = e.message, reason = APIErrorReason.SERVER)
        } catch (e: ClientRequestException) {
            return ApiResult.Error(message = e.message, reason = APIErrorReason.USER)
        } catch (e: ServerResponseException) {
            return ApiResult.Error(message = e.message, reason = APIErrorReason.SERVER)
        } catch (e: Exception) {
            return ApiResult.Fatal(throwable = e)
        }
    }

    suspend fun getVersion(
        serverPassword: String
    ): ApiResult<GetVersionEndpointResponse> {
        try {
            val body: GetVersionEndpointResponse =
                client
                    .get {
                        url {
                            path(HttpRoutes.GET_VERSION)
                        }

                        header("X-Server-Password", serverPassword)
                    }.body<GetVersionEndpointResponse>()

            return ApiResult.Success(data = body)
        } catch (e: RedirectResponseException) {
            return ApiResult.Error(message = e.message, reason = APIErrorReason.SERVER)
        } catch (e: ClientRequestException) {
            return ApiResult.Error(message = e.message, reason = APIErrorReason.USER)
        } catch (e: ServerResponseException) {
            return ApiResult.Error(message = e.message, reason = APIErrorReason.SERVER)
        } catch (e: Exception) {
            return ApiResult.Fatal(throwable = e)
        }
    }

    init {
        runBlocking {
            client = ktorClient.getClient()
        }
    }
}