package tech.bobalus.app5.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.bobalus.app5.ui.HkViewModel

@Composable
fun DevicesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        DiscoveredDevices(onDeviceSelect = {
            println("device selectd $it")
        })
    }
}

@Composable
fun DiscoveredDevices(modifier: Modifier = Modifier,
                      hkViewModel: HkViewModel = viewModel(),
                      onDeviceSelect: (String)-> Unit) {

    val uiState = hkViewModel.uiState.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()

    println("am I drawin?")
    LazyColumn(state = lazyListState) {
        items(uiState.value.discoveredDevices) { t ->
            OutlinedButton(onClick = {
                onDeviceSelect(t)
            }) {
                Text(text = t)
            }
        }
    }
}