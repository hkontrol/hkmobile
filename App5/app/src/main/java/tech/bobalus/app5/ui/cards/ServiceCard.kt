package tech.bobalus.app5.ui.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hkmobile.Hkmobile
import tech.bobalus.app5.Accessory
import tech.bobalus.app5.HkSdk
import tech.bobalus.app5.Service

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceCard(accessory: Accessory, service: Service) {
    // TODO uiState
    when (service.type) {
        Hkmobile.SType_LightBulb -> LightbulbCardService(accessory, service)
        Hkmobile.SType_Switch -> SwitchCardService(accessory, service)
        Hkmobile.SType_Thermostat -> ThermostatCardService(accessory, service)
        else -> {
            val name = HkSdk.getServiceName(service) ?: ""
            Card(
                elevation = 4.dp,
                modifier = Modifier
                    .padding(4.dp)
            ) {
                Column(Modifier.padding(4.dp)) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}