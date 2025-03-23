package ru.bear.weatherjusttogether.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException

//  для логирования запросов к API
class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // Логирование запроса
        val request: Request = chain.request()
        var requestBodyString: String? = null

        if (request.body != null) {
            // Читаем тело запроса в строку
            val buffer = Buffer()
            request.body!!.writeTo(buffer)
            requestBodyString = buffer.readUtf8()
        }

        Log.d("API_REQUEST", "Request URL: " + request.url)
        Log.d("API_REQUEST", "Request Method: " + request.method)
        Log.d("API_REQUEST", "Request Body: $requestBodyString")

        // Выполнение запроса
        var response: Response = chain.proceed(request)

        // Логирование ответа
        var responseBodyString: String? = null
        if (response.body != null) {
            // Читаем тело ответа в строку
            val responseCopy = response.body!!.string()

            // Возвращаем новый Response, который можно использовать дальше в Retrofit
            response = response.newBuilder()
                .body(ResponseBody.create(response.body!!.contentType(), responseCopy))
                .build()

            responseBodyString = responseCopy
        }

        Log.d("API_RESPONSE", "Response Code: " + response.code)
        Log.d("API_RESPONSE", "Response Body: $responseBodyString")

        return response
    }
}