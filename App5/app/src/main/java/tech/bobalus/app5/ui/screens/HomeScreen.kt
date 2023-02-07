package tech.bobalus.app5.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.bobalus.app5.model.HomeViewModel
import tech.bobalus.app5.ui.cards.AccessoryCard


@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        HomeView()
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeView(homeViewModel: HomeViewModel = viewModel()) {

    val uiState = homeViewModel.uiState.collectAsState()
    val lazyGridState: LazyGridState = rememberLazyGridState()
    var refreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(refreshing, {
        refreshing = true
        homeViewModel.refresh()
        refreshing = false
    })

    Box(Modifier.pullRefresh(pullRefreshState)) {
        if (!refreshing) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                state = lazyGridState,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(uiState.value.accessories) { t ->
                    AccessoryCard(accessory = t)
                }
            }
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}