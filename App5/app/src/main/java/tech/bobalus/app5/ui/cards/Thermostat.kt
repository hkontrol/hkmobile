package tech.bobalus.app5.ui.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hkmobile.Hkmobile
import tech.bobalus.app5.Accessory
import tech.bobalus.app5.Characteristic
import tech.bobalus.app5.HkSdk
import tech.bobalus.app5.Service
import kotlin.math.floor

fun cmode2str(cc: Characteristic): String {
    var mode = -1
    when (cc.value) {
        is Int -> mode = cc.value as Int
        is Float -> mode = floor(cc.value as Float).toInt()
        is Double -> mode = floor(cc.value as Double).toInt()
    }
    return when(mode) {
        0 -> "off"
        1 -> "heat"
        2 -> "cool"
        else -> cc.value.toString()
    }
}
fun tmode2str(cc: Characteristic): String {
    var mode = -1
    when (cc.value) {
        is Int -> mode = cc.value as Int
        is Float -> mode = floor(cc.value as Float).toInt()
        is Double -> mode = floor(cc.value as Double).toInt()
    }
    return when(mode) {
        0 -> "off"
        1 -> "heat"
        2 -> "cool"
        3 -> "auto"
        else -> cc.value.toString()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThermostatCardPrimary(
    accessory: Accessory,
    service: Service,
    onLongClick: ((Accessory) -> Unit)? = null,
) {
    val name = HkSdk.getAccessoryName(accessory) ?: ""
    val ctc = HkSdk.findCharacteristic(service, Hkmobile.CType_CurrentTemperature) ?: return
    val ttc = HkSdk.findCharacteristic(service, Hkmobile.CType_TargetTemperature) ?: return
    val chc = HkSdk.findCharacteristic(service, Hkmobile.CType_CurrentHeatingCoolingState) ?: return
    val thc = HkSdk.findCharacteristic(service, Hkmobile.CType_TargetHeatingCoolingState) ?: return


    val onClick : () -> Unit = {
        // TODO something
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
        Column(
            Modifier.padding(4.dp),
        ) {
            Text(text = name)
            Text(text = "")
            Text(text = "temp: ${ctc.value.toString()} >> ${ttc.value.toString()}")
            Text(text = "mode: ${cmode2str(chc)} >> ${tmode2str(thc)}")
        }
    }
}