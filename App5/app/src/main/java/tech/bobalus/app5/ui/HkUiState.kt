package tech.bobalus.app5.ui

data class HkUiState(val enteredText: String,
                     val discoveredDevices : MutableList<String>,
                     val len: Int = discoveredDevices.size)