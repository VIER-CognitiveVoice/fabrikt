package examples.parameterNameClash.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import examples.parameterNameClash.models.SomeObject
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.jvm.Throws

@Suppress("unused")
class ExampleClient(
    private val objectMapper: ObjectMapper,
    private val baseUrl: String,
    private val client: OkHttpClient
) {
    /**
     *
     *
     * @param pathB
     * @param queryB
     */
    @Throws(ApiException::class)
    fun getExampleB(
        pathB: String,
        queryB: String,
        additionalHeaders: Map<String, String> = emptyMap()
    ): ApiResponse<Unit> {
        val httpUrl: HttpUrl = "$baseUrl/example/{b}"
            .pathParam("{b}" to pathB)
            .toHttpUrl()
            .newBuilder()
            .queryParam("b", queryB)
            .build()

        val headerBuilder = Headers.Builder()
        additionalHeaders.forEach { headerBuilder.header(it.key, it.value) }
        val httpHeaders: Headers = headerBuilder.build()

        val request: Request = Request.Builder()
            .url(httpUrl)
            .headers(httpHeaders)
            .get()
            .build()

        return request.execute(client, objectMapper, jacksonTypeRef())
    }

    /**
     *
     *
     * @param bodySomeObject example
     * @param querySomeObject
     */
    @Throws(ApiException::class)
    fun postExample(
        bodySomeObject: SomeObject,
        querySomeObject: String,
        additionalHeaders: Map<String, String> = emptyMap()
    ): ApiResponse<Unit> {
        val httpUrl: HttpUrl = "$baseUrl/example"
            .toHttpUrl()
            .newBuilder()
            .queryParam("someObject", querySomeObject)
            .build()

        val headerBuilder = Headers.Builder()
        additionalHeaders.forEach { headerBuilder.header(it.key, it.value) }
        val httpHeaders: Headers = headerBuilder.build()

        val request: Request = Request.Builder()
            .url(httpUrl)
            .headers(httpHeaders)
            .post(objectMapper.writeValueAsString(bodySomeObject).toRequestBody("application/json".toMediaType()))
            .build()

        return request.execute(client, objectMapper, jacksonTypeRef())
    }
}
