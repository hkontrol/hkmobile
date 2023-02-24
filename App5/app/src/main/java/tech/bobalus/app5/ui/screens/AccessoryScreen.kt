package tech.bobalus.app5.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import tech.bobalus.app5.Accessory
import tech.bobalus.app5.ui.cards.ServiceCard

@Composable
fun AccessoryScreen(accessory: Accessory) {
    val lazyListState: LazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState) {
        items(accessory.services) { service ->
            ServiceCard(accessory = accessory, service = service)
        }
    }
}