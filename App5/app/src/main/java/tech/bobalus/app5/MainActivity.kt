package tech.bobalus.app5

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import tech.bobalus.app5.ui.screens.MainScreen
import tech.bobalus.app5.ui.theme.App5Theme

class MainActivity : ComponentActivity() {

    private val sdk: HkSdk = HkSdk
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(arrayOf("android.permission.CHANGE_WIFI_MULTICAST_STATE"), 1)
        requestPermissions(arrayOf("android.permission.INTERNET"), 1)
        requestPermissions(arrayOf("android.permission.ACCESS_NETWORK_STATE"), 1)

        val wifi = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        val multicastLock = wifi.createMulticastLock("multicastLock")
        multicastLock.setReferenceCounted(true)
        multicastLock.acquire()

        HkSdk.configure("app5", filesDir.absolutePath)
        HkSdk.start()

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
