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

//        setContent {
//            val selectedDevice = remember { mutableStateOf("") }
//            val enteredPin = remember { mutableStateOf("") }
//
//            App5Theme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    DiscoveredDevices(
//                        hkViewModel = viewModel(),
//                        onDeviceSelect = {
//                            selectedDevice.value = it
//                        })
//                    if (selectedDevice.value != "") {
//                        AlertDialog(
//                            title = {
//                                Text("Pair <${selectedDevice.value}>")
//                            },
//                            text = {
//                                OutlinedTextField(value = enteredPin.value,
//                                    onValueChange = {
//                                        enteredPin.value = it
//                                    },)
//                            },
//                            onDismissRequest = { selectedDevice.value = "" },
//                            confirmButton = {
//                                TextButton(onClick = {
//                                    selectedDevice.value = ""
//                                    enteredPin.value = ""
//                                } ) {
//                                    Text("Cancel")
//                                }
//                                TextButton(onClick = {
////                                    val result = HkSdk.controller?.pairSetupAndVerify(selectedDevice.value, enteredPin.value)
//                                    var result = HkSdk.controller?.pairSetup(selectedDevice.value, enteredPin.value)
//                                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
//                                    result = HkSdk.controller?.pairVerify(selectedDevice.value)
//                                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
//
//                                    selectedDevice.value = ""
//                                    enteredPin.value = ""
//                                } ) {
//                                    Text("Pair")
//                                }
//                            },
//                            dismissButton = {}
//                        )
//                    }
//                }
//            }
//        }
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

//@Composable
//fun DiscoveredDevices(modifier: Modifier = Modifier,
//                      hkViewModel: HkViewModel = viewModel(),
//                      onDeviceSelect: (String)-> Unit) {
//
//    val uiState = hkViewModel.uiState.collectAsState()
//    val lazyListState: LazyListState = rememberLazyListState()
//
//    println("am I drawin?")
//    LazyColumn(state = lazyListState) {
//        items(uiState.value.discoveredDevices) { t ->
//            OutlinedButton(onClick = {
//                onDeviceSelect(t)
//            }) {
//                Text(text = t)
//            }
//        }
//    }
//}
