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
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.bobalus.app5.model.HomeViewModel

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        HomeView()
    }
}

@Composable
fun HomeView(homeViewModel: HomeViewModel = viewModel()) {

    val uiState = homeViewModel.uiState.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState) {
        items(uiState.value.accessories) {
            t ->
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(text = t.toString())
            }
        }
    }
}