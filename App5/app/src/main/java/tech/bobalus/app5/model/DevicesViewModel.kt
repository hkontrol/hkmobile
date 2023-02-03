package tech.bobalus.app5.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tech.bobalus.app5.Device
import tech.bobalus.app5.HkSdk

data class DevicesUiState(
                     val discoveredDevices : MutableList<Device>,
                     val len: Int = discoveredDevices.size)

class DevicesViewModel: ViewModel() {
    protected val scope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow(DevicesUiState(ArrayList()))
    val uiState: StateFlow<DevicesUiState> = _uiState.asStateFlow()

    init {

        // register listeners

        scope.launch {
            HkSdk.discoverEvents
                .onEach {
                    addDevice(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }
        scope.launch {
            HkSdk.pairedEvents
                .onEach {
                    addDevice(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }
        scope.launch {
            HkSdk.verifiedEvents
                .onEach {
                    addDevice(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }
        scope.launch {
            HkSdk.closedEvents
                .onEach {
                    addDevice(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }
        scope.launch {
            HkSdk.unpairedEvents
                .onEach {
                    addDevice(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }
        scope.launch {
            HkSdk.lostEvents
                .onEach {
                    removeDevice(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }

        println("HkViewModel created")

        var all = HkSdk.getAllDevices()

        val allIt = all.iterator()
        val l = _uiState.value.discoveredDevices
        while (allIt.hasNext()) {
            l.add(allIt.next())
        }

        _uiState.value = DevicesUiState(l)
    }

    fun addDevice(device: Device) {
        removeDevice(device) // in case of duplicates
        _uiState.value.discoveredDevices.add(device)
        // without toMutableList ui is not updated
        _uiState.value = DevicesUiState(_uiState.value.discoveredDevices.toMutableList())
    }

    fun removeDevice(device: Device) {
        val l =_uiState.value.discoveredDevices
        val each  = l.iterator()
        while (each.hasNext()) {
            if (each.next().name == device.name) {
                each.remove()
            }
        }
        _uiState.value = DevicesUiState(l.toMutableList())
    }

}
