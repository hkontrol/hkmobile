package tech.bobalus.app5

import hkmobile.CompatibleKontroller
import hkmobile.MobileReceiver
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object HkSdk : MobileReceiver {
    var controller: CompatibleKontroller? = null

    private val _discoverEvents = MutableSharedFlow<String>()
    val discoverEvents = _discoverEvents.asSharedFlow() // read-only public view

    private val _lostEvents = MutableSharedFlow<String>()
    val lostEvents = _lostEvents.asSharedFlow() // read-only public view

    private val _verifiedEvents = MutableSharedFlow<String>()
    val verifiedEvents = _verifiedEvents.asSharedFlow() // read-only public view

    private val _pairedEvents = MutableSharedFlow<String>()
    val pairedEvents = _pairedEvents.asSharedFlow() // read-only public view

    private val _unpairedEvents = MutableSharedFlow<String>()
    val unpairedEvents = _unpairedEvents.asSharedFlow() // read-only public view

    private val _closedEvents = MutableSharedFlow<String>()
    val closedEvents = _closedEvents.asSharedFlow() // read-only public view


    init {
        println("HkSdk object created")
    }
    fun start(name: String = "app5", configDir: String) {
        if (controller == null) {
            controller = hkmobile.Hkmobile.newCompatibleController(name, configDir, this)
            controller?.startDiscovery()
            println("initialized controller")
        }
    }
    override fun onCharacteristic(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onClose(p0: String?) {
        runBlocking {
            if (p0 != null) {
                _closedEvents.emit(p0)
            }
        }
    }

    override fun onDiscovery(p0: String?) {
        println("discovered $p0")
        runBlocking {
            if (p0 != null) {
                _discoverEvents.emit(p0)
            }
        }
    }

    override fun onLost(p0: String?) {
        println("lost $p0")
        runBlocking {
            if (p0 != null) {
                _lostEvents.emit(p0)
            }
        }
    }

    override fun onPaired(p0: String?) {
        runBlocking {
            if (p0 != null) {
                _pairedEvents.emit(p0)
            }
        }
    }

    override fun onUnpaired(p0: String?) {
        runBlocking {
            if (p0 != null) {
                _unpairedEvents.emit(p0)
            }
        }
    }

    override fun onVerified(p0: String?) {
        runBlocking {
            if (p0 != null) {
                _verifiedEvents.emit(p0)
            }
        }
    }
}