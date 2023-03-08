package tech.bobalus.app5

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContentProviderCompat.requireContext
import tech.bobalus.app5.ui.screens.MainScreen
import tech.bobalus.app5.ui.theme.App5Theme


class MainActivity : ComponentActivity() {

    private val sdk: HkSdk = HkSdk
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wifiManager = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        val multicastLock = wifiManager.createMulticastLock("multicastLock")
        multicastLock.setReferenceCounted(true)
        multicastLock.acquire()

        HkSdk.configure("app5", filesDir.absolutePath)
        HkSdk.start()

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        // https://stackoverflow.com/a/66572922
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                HkSdk.start()
                Toast.makeText(applicationContext, "Wifi is on!", Toast.LENGTH_SHORT).show()
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                HkSdk.stop()
                Toast.makeText(applicationContext, "Wifi is lost!", Toast.LENGTH_SHORT).show()
            }
        }
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)

        setContent {
            App5Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
