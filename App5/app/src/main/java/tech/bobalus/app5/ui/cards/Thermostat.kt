package tech.bobalus.app5.ui.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hkmobile.Hkmobile
import tech.bobalus.app5.Accessory
import tech.bobalus.app5.Characteristic
import tech.bobalus.app5.HkSdk
import tech.bobalus.app5.Service
import kotlin.math.floor
import kotlin.math.roundToInt

const val modeOff = 0
const val modeHeat = 1
const val modeCool = 2
const val modeAuto = 3

fun cmode2str(cc: Characteristic): String {
    var mode = -1
    when (cc.value) {
        is Int -> mode = cc.value as Int
        is Float -> mode = floor(cc.value as Float).toInt()
        is Double -> mode = floor(cc.value as Double).toInt()
    }
    return when(mode) {
        modeOff -> "off"
        modeHeat -> "heat"
        modeCool -> "cool"
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
        modeOff -> "off"
        modeHeat -> "heat"
        modeCool -> "cool"
        modeAuto -> "auto"
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

    val name = HkSdk.getAccessoryName(accessory) ?: "Unknown thermostat"
    val ctc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_CurrentTemperature) ?: return
    val ttc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_TargetTemperature) ?: return
    val chc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_CurrentHeatingCoolingState) ?: return
    val thc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_TargetHeatingCoolingState) ?: return

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

@Composable
fun ThermostatCardService(
    accessory: Accessory,
    service: Service,
    ) {

    val name = HkSdk.getAccessoryName(accessory) ?: "Unknown thermostat"
    val ctc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_CurrentTemperature) ?: return
    val ttc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_TargetTemperature) ?: return
    val chc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_CurrentHeatingCoolingState) ?: return
    val thc = HkSdk.findCharacteristicInService(service, Hkmobile.CType_TargetHeatingCoolingState) ?: return


    println("currentHeatingCoolingState: $chc")
    println("targetHeatingCoolingState: $thc")

    var targetTemp = 10.0f // TODO: extract from characteristic
    try {
        targetTemp = ttc.value.toString().toFloat()
    } catch (e: Exception) {
        println(e.message)
    }
    var currentTemp = 10.0f
    try {
        currentTemp = ctc.value.toString().toFloat()
    } catch (e: Exception) {
        println(e.message)
    }

    Card(
        elevation = 2.dp,
        modifier = Modifier
                        .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Temperature slider
            var temperatureSliderValue by remember { mutableStateOf(targetTemp) }

            Slider(
                value = temperatureSliderValue,
                onValueChange = {
                    temperatureSliderValue = it
                },
                onValueChangeFinished = {
                    HkSdk.putCharacteristicValue(
                        accessory,
                        Hkmobile.CType_TargetTemperature,
                        (temperatureSliderValue*100).roundToInt()/100.0
                    )
                },
                valueRange = 10f..35f,
                steps = 25,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Temperature: ${currentTemp}°C >> ${targetTemp}°C",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            Text(
                text = "Mode: ${cmode2str(chc)} >> ${tmode2str(thc)}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            val onModeChange = fun(modeStr: String) {
                var mode = 0
                when (modeStr) {
                    "off" -> mode = modeOff
                    "heat" -> mode = modeHeat
                    "cool" -> mode = modeCool
                    "auto" -> mode = modeAuto
                    else -> return
                }
                HkSdk.putCharacteristicValue(
                    accessory,
                    Hkmobile.CType_TargetHeatingCoolingState,
                    mode
                )
            }
            var mode = tmode2str(thc)
            // Mode selector
            var modeSelectorValue by remember { mutableStateOf(mode) }
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (chc.validValues == null || chc.validValues?.contains(modeOff.toFloat()) == true) {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        RadioButton(
                            selected = modeSelectorValue == "off",
                            onClick = {
                                modeSelectorValue = "off"
                                onModeChange(modeSelectorValue)
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Gray),
                        )
                        Text(
                            text = "Off",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                if (chc.validValues == null || chc.validValues?.contains(modeHeat.toFloat()) == true) {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        RadioButton(
                            selected = modeSelectorValue == "heat",
                            onClick = {
                                modeSelectorValue = "heat"
                                onModeChange(modeSelectorValue)
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Red),
                        )
                        Text(
                            text = "Heat",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                if (chc.validValues == null || chc.validValues?.contains(modeCool.toFloat()) == true) {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        RadioButton(
                            selected = modeSelectorValue == "cool",
                            onClick = {
                                modeSelectorValue = "cool"
                                onModeChange(modeSelectorValue)
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Blue),
                        )
                        Text(
                            text = "Cool",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                        )

                    }
                }
                if (chc.validValues == null || chc.validValues?.contains(modeAuto.toFloat()) == true) {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        RadioButton(
                            selected = modeSelectorValue == "auto",
                            onClick = {
                                modeSelectorValue = "auto"
                                onModeChange(modeSelectorValue)
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Green),
                        )
                        Text(
                            text = "Auto",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
