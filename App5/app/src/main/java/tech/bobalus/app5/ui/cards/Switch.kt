package tech.bobalus.app5.ui.cards;

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hkmobile.Hkmobile
import tech.bobalus.app5.Accessory
import tech.bobalus.app5.HkSdk
import tech.bobalus.app5.Service

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwitchCardPrimary(
    accessory: Accessory,
    service: Service,
    onLongClick: ((Accessory) -> Unit)? = null,
) {
    val cc = HkSdk.findCharacteristic(service, Hkmobile.CType_On) ?: return
    val onValue = cc.value
        .toString()
        .toBoolean()
            || cc.value
        .toString()
        .equals("1")

    var onState by remember { mutableStateOf(onValue) }
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = {
                    val newValue = !onState
                    HkSdk.controller?.putCharacteristicReq(
                        accessory.device,
                        accessory.id, cc.iid, newValue.toString()
                    )
                    cc.value = newValue
                    onState = newValue
                }, onLongClick = {
                    println("long click")
                    if (onLongClick != null) {
                        onLongClick(accessory)
                    }
                }
            )
    ) {
        Column(Modifier.padding(4.dp)) {
            HkSdk.getAccessoryName(accessory)?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.ExtraBold
                )
                Switch(checked = onState, onCheckedChange = {})
            }
        }
    }
}