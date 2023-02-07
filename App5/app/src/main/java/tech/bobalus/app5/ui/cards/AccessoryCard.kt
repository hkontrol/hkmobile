package tech.bobalus.app5.ui.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun AccessoryCard(accessory: Accessory) {
    var service = HkSdk.findPrimaryService(accessory) ?: return
    when (service.type) {
        Hkmobile.SType_LightBulb -> LightbulbCardPrimary(accessory, service)
        Hkmobile.SType_Switch -> SwitchCardPrimary(accessory, service)
        else -> {
            val name = HkSdk.getAccessoryName(accessory) ?: ""
            var services = ""
            val it = accessory.services.iterator()
            while(it.hasNext()) {
                val ss = it.next()
                services += " * ${Hkmobile.serviceFriendly(ss.type)}\n"
            }
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
                    Text(text = services)
                }
            }
        }

    }
}