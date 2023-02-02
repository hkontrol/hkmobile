package tech.bobalus.app5.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import tech.bobalus.app5.HkSdk

class HkViewModel: ViewModel() {
    protected val scope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow(HkUiState("", ArrayList()))
    val uiState: StateFlow<HkUiState> = _uiState.asStateFlow()
    init {
        println("HkViewModel creating")
        HkSdk.lostEvents.onEach {
            println("on each?")
            setText("lost-$it")
        }

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
            HkSdk.lostEvents
                .onEach {
                    removeDevice(it)
                    _uiState.emit(_uiState.value)
                }
                .catch { e -> println(e.message) }
                .collect()
        }

        println("HkViewModel created")
    }
    fun setText(text: String) {
        _uiState.value = HkUiState(text, _uiState.value.discoveredDevices)
        _uiState.value = _uiState.value
    }
    fun addDevice(name: String) {
        _uiState.value.discoveredDevices.add(name)
        //_uiState.tryEmit(_uiState.value.copy())
        _uiState.value = HkUiState(_uiState.value.enteredText, _uiState.value.discoveredDevices)
    }
    fun removeDevice(name: String) {
        val l =_uiState.value.discoveredDevices
        while (l.remove(name)) {
            continue
        }
        _uiState.value = HkUiState(_uiState.value.enteredText, l)
    }
    fun reset() {
        val l = _uiState.value.discoveredDevices
        l.clear()
        _uiState.value = HkUiState("", l)
    }
}
