package tech.bobalus.app5.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hkmobile.Hkmobile
import hkmobile.Hkmobile.serviceFriendly
import tech.bobalus.app5.HkSdk
import tech.bobalus.app5.model.HomeViewModel
import kotlin.reflect.jvm.internal.impl.resolve.constants.BooleanValue


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

@OptIn(ExperimentalMaterialApi::class)
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
                    var name = ""
                    var services = ""
                    val servIt = t.services.iterator()
                    while (servIt.hasNext()) {
                        val srv = servIt.next()
                        services += " * ${serviceFriendly(srv.type)}\n"
                        if (srv.type == Hkmobile.SType_AccessoryInfo) {
                            val nameFound =
                                srv.characteristics.find { c -> c.type == Hkmobile.CType_Name }
                            if (nameFound != null) {
                                name = nameFound.value.toString()
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            println("accessory on click")

                            // TODO: it won't be here
                            t.services.forEach { ss ->
                                if (ss.type == Hkmobile.SType_LightBulb) {
                                    ss.characteristics.forEach{ cc->
                                        val value = cc.value.toString().toBoolean()
                                                     || cc.value.toString().equals("1")

                                        // TODO: HkSdk.putCharacteristic(...)
                                        HkSdk.controller?.putCharacteristicReq(t.device,
                                                            t.id, cc.iid, (!value).toString())
                                        cc.value = !value
                                    }
                                }

                            }
                        },
                    ) {
                        Column() {
                            Text(text = name, fontWeight = FontWeight.ExtraBold)
                            Text(text = services)
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}