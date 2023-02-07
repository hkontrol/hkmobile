package tech.bobalus.app5.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tech.bobalus.app5.Accessory
import tech.bobalus.app5.Device
import tech.bobalus.app5.HkSdk

// dunno why but on characteristic event
// accessory uiState is not updated.
// so counter helps
object Counter {
    var count: Int = 0
}

data class HomeUiState(
    val accessories: MutableList<Accessory>,
    val len: Int = accessories.size
)

class HomeViewModel : ViewModel() {
    protected val scope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow(HomeUiState(ArrayList()))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // register listeners
        scope.launch {
            HkSdk.characteristicEvents
                .onEach { ev ->
                    println("char event: $ev")

                    val accs = _uiState.value.accessories
                    accs.forEachIndexed { aix, accessory ->
                        if (accessory.id == ev.aid) {
                            accessory.services.forEachIndexed { six, service ->
                                service.characteristics.forEachIndexed { cix, characteristic ->
                                    if (characteristic.iid == ev.iid) {
                                        accs[aix].services[six].characteristics[cix].value =
                                            ev.value

                                        Counter.count += 1; // <- somehow ui state is not updated without this
                                        if (Counter.count == 100500) {
                                            Counter.count = 0
                                        }
                                        _uiState.value = HomeUiState(
                                            accs.toMutableList(),
                                            Counter.count
                                        )
                                        _uiState.emit(_uiState.value)
                                    }
                                }
                            }
                        }
                    }
                }
                .catch { e -> println(e.message) }
                .collect()
        }

        scope.launch {
            HkSdk.verifiedEvents
                .onEach {
                    addDeviceAccessories(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }
        scope.launch {
            HkSdk.closedEvents
                .onEach {
                    removeDeviceAccessories(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }
        scope.launch {
            HkSdk.lostEvents
                .onEach {
                    removeDeviceAccessories(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }

        refresh()
    }

    fun refresh() {
        val devices = HkSdk.getAllDevices().iterator()
        while (devices.hasNext()) {
            val dd = devices.next()
            if (dd.verified) {
                HkSdk.getAccessoriesReq(dd)
                addDeviceAccessories(dd)
            }
        }
    }

    private fun addDeviceAccessories(device: Device) {
        removeDeviceAccessories(device)  // in case of duplicates
        val accs = HkSdk.listAccessories(device)


        _uiState.value.accessories.addAll(accs)
        // without toMutableList ui is not updated
        _uiState.value = HomeUiState(_uiState.value.accessories.toMutableList())
    }

    private fun removeDeviceAccessories(device: Device) {
        val l = _uiState.value.accessories
        val each = l.iterator()
        while (each.hasNext()) {
            if (each.next().device == device.name) {
                each.remove()
            }
        }
        _uiState.value = HomeUiState(l.toMutableList())
    }

}
