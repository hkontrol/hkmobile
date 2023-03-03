package tech.bobalus.app5.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.bobalus.app5.Accessory
import tech.bobalus.app5.model.HomeViewModel
import tech.bobalus.app5.ui.cards.ServiceCard

@Composable
fun AccessoryScreen(accessory: Accessory, homeViewModel: HomeViewModel = viewModel()) {

    val uiState = homeViewModel.uiState.collectAsState()
    val it  = uiState.value.accessories.iterator()
    var acc = accessory
    while(it.hasNext()) {
        val aa = it.next()
        if (aa.id == accessory.id && aa.device == accessory.device) {
            acc = aa
            break
        }
    }

    val lazyListState: LazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState) {
        items(acc.services) { service ->
            ServiceCard(accessory = acc, service = service)
        }
    }
}