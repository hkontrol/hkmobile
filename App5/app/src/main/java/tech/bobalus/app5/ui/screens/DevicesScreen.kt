package tech.bobalus.app5.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.bobalus.app5.Device
import tech.bobalus.app5.HkSdk
import tech.bobalus.app5.model.DevicesViewModel

@Composable
fun DevicesScreen() {
    val context = LocalContext.current
    val selectedDevice = remember { mutableStateOf(Device()) }
    val enteredPin = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        DiscoveredDevices(onDeviceSelect = {
            println("device selectd $it")
            selectedDevice.value = it
        })

        if (selectedDevice.value.name != "" && !selectedDevice.value.paired) {
            AlertDialog(
                title = {
                    Text("Pair <${selectedDevice.value}>")
                },
                text = {
                    OutlinedTextField(value = enteredPin.value,
                        onValueChange = {
                            enteredPin.value = it
                        },)
                },
                onDismissRequest = { selectedDevice.value = Device() },
                confirmButton = {

                    TextButton(onClick = {
                        selectedDevice.value = Device()
                        enteredPin.value = ""
                    } ) {
                        Text("Cancel")
                    }

                    TextButton(onClick = {
                        var result = HkSdk.controller?.pairSetup(selectedDevice.value.name, enteredPin.value)
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                        result = HkSdk.controller?.pairVerify(selectedDevice.value.name)
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()

                        selectedDevice.value = Device()
                        enteredPin.value = ""
                    } ) {
                        Text("Pair")
                    }

                },
                dismissButton = {}
            )
        } else if (selectedDevice.value.name != "" && selectedDevice.value.paired) {
            AlertDialog(
                title = {
                    Text("Unpair <${selectedDevice.value}>")
                },
                text = {
                    Text(text = "really unpair?",)
                },
                onDismissRequest = { selectedDevice.value = Device() },
                confirmButton = {

                    TextButton(onClick = {
                        selectedDevice.value = Device()
                    } ) {
                        Text("Cancel")
                    }

                    TextButton(onClick = {
                        val result = HkSdk.controller?.unpair(selectedDevice.value.name)
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()

                        selectedDevice.value = Device()
                        enteredPin.value = ""
                    } ) {
                        Text("UnpairPair")
                    }

                },
                dismissButton = {}
            )
        }
    }
}

@Composable
fun DiscoveredDevices(modifier: Modifier = Modifier,
                      devicesViewModel: DevicesViewModel = viewModel(),
                      onDeviceSelect: (Device)-> Unit) {

    val uiState = devicesViewModel.uiState.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()

    println("am I drawin?")
    LazyColumn(state = lazyListState) {
        items(uiState.value.discoveredDevices) { t ->
            OutlinedButton(onClick = {
                onDeviceSelect(t)
            }) {
                Column() {
                    Text(text = t.name)
                    Text(text = "dnssd: ${t.dnsServiceName}")
                    Text(text = "paired: ${t.paired}")
                    Text(text = "verified: ${t.verified}")
                }
            }
        }
    }
}