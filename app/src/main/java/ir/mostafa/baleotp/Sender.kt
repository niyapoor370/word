package ir.mostafa.baleotp

import android.content.SharedPreferences
import java.net.HttpURLConnection
import java.net.URL

object Sender {

    fun push(prefs: SharedPreferences, code: String) {
        val ip = prefs.getString("ip", "") ?: ""
        val port = prefs.getString("port", "8720") ?: "8720"
        val token = prefs.getString("token", "") ?: ""
        if (ip.isEmpty()) return

        Thread {
            try {
                val url = URL("http://$ip:$port/push")
                (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("X-Token", token)
                    connectTimeout = 8000
                    readTimeout = 8000
                    outputStream.use { it.write("""{"code":"$code"}""".toByteArray(Charsets.UTF_8)) }
                    inputStream.use { it.readBytes() }
                    disconnect()
                }
            } catch (_: Exception) {
                // شبکه در دسترس نبود؛ نادیده
            }
        }.start()
    }
}
