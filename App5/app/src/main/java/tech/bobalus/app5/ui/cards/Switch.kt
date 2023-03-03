package tech.bobalus.app5.ui.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    val cc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_On) ?: return

    var onState by remember { mutableStateOf(false) }

    when (cc.value) {
        is Boolean -> onState = cc.value as Boolean
        is Int -> onState = cc.value == 1
        is Long -> onState = cc.value == 1L
        is Double -> onState = cc.value == 1.0
        is Float -> onState = cc.value == 1.0F
    }

    val onClick: () -> Unit = {
        val newValue = !onState
        HkSdk.controller?.putCharacteristicReq(
            accessory.device,
            accessory.id, cc.iid, newValue.toString()
        )
        cc.value = newValue
        onState = newValue
    }
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = {
                    onClick()
                }, onLongClick = {
                    println("long click")
                    if (onLongClick != null) {
                        onLongClick(accessory)
                    }
                }
            )
    ) {
        Row(
            Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HkSdk.getAccessoryName(accessory)?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.ExtraBold
                )
                Switch(checked = onState, onCheckedChange = { onClick() })
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwitchCardService(
    accessory: Accessory,
    service: Service,
) {
    val onCc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_On) ?: return

    var onState by remember { mutableStateOf(false) }

    when (onCc.value) {
        is Boolean -> onState = onCc.value as Boolean
        is Int -> onState = onCc.value == 1
        is Long -> onState = onCc.value == 1L
        is Double -> onState = onCc.value == 1.0
        is Float -> onState = onCc.value == 1.0F
    }

    val onClick : () -> Unit = {
        val newValue = !onState
        HkSdk.putCharacteristicValue(accessory, Hkmobile.CType_On, newValue)
        onCc.value = newValue
        onState = newValue
    }
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp)
    ) {
        Column() {
            Row(
                Modifier.padding(4.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HkSdk.getAccessoryName(accessory)?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Switch(checked = onState, onCheckedChange = { onClick() })
            }
        }
    }
}