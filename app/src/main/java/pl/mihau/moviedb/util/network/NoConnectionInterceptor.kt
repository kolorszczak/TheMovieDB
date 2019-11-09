package pl.mihau.moviedb.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import pl.mihau.moviedb.R
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NoConnectionInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isConnectionOn()) {
            throw NoConnectivityException(context.getString(R.string.error_no_connection))
        } else if(!isInternetAvailable()) {
            throw NoInternetException(context.getString(R.string.error_no_internet))
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun isConnectionOn(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)
            return connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            }
            return false
        }
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            sock.connect(socketAddress, timeoutMs)
            sock.close()

            true
        } catch (e: IOException) {
            false
        }
    }

    class NoConnectivityException(errorMsg: String) : IOException() {
        override val message = errorMsg
    }

    class NoInternetException(errorMsg: String) : IOException() {
        override val message = errorMsg
    }
}