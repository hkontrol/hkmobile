package tech.bobalus.app5

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hkmobile.CompatibleKontroller
import hkmobile.MobileReceiver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking


/**

 device is serialized into

 {
    "id": "homebridge\\ 702C",
    "name": "homebridge\\ 702C",
    "discovered": true,
    "paired": false,
    "verified": false,
    "pairing": {
        "id": "homebridge\\ 702C",
        "pubk": null
    },
    "dns_service_name": "homebridge\\ 702C._hap._tcp.local.",
    "txt": {
        "c#": "2",
        "ci": "2",
        "ff": "0",
        "id": "AA:BB:CC:DD:EE:FF",
        "md": "homebridge",
        "pv": "1.1",
        "s#": "1",
        "sf": "0",
        "sh": "Wmdh8g=="
    }
}

 */

data class Pairing(@Json(name = "name") val name: String?, @Json(name = "id") val id: String?, @Json(name = "pubk") val publicKey: String?)

data class Device(
    @Json(name = "name") val name: String,
    @Json(name = "discovered") val discovered: Boolean,
    @Json(name = "paired") val paired: Boolean,
    @Json(name = "verified") val verified: Boolean,
    @Json(name = "pairing") val pairing: Pairing?,
    @Json(name = "dns_service_name") val dnsServiceName: String,
    @Json(name = "txt") val txt: Map<String, String>?,
) {
    constructor() : this( "", false, false, false, null, "", null)
}

data class Response(
    @Json(name = "error") val error: String,
    @Json(name = "result") val result: Any?
)

object HkSdk : MobileReceiver {
    var controller: CompatibleKontroller? = null

    private val _discoverEvents = MutableSharedFlow<Device>()
    val discoverEvents = _discoverEvents.asSharedFlow() // read-only public view

    private val _lostEvents = MutableSharedFlow<Device>()
    val lostEvents = _lostEvents.asSharedFlow() // read-only public view

    private val _verifiedEvents = MutableSharedFlow<Device>()
    val verifiedEvents = _verifiedEvents.asSharedFlow() // read-only public view

    private val _pairedEvents = MutableSharedFlow<Device>()
    val pairedEvents = _pairedEvents.asSharedFlow() // read-only public view

    private val _unpairedEvents = MutableSharedFlow<Device>()
    val unpairedEvents = _unpairedEvents.asSharedFlow() // read-only public view

    private val _closedEvents = MutableSharedFlow<Device>()
    val closedEvents = _closedEvents.asSharedFlow() // read-only public view

    private val moshi: Moshi = Moshi.Builder()
                                  .add(KotlinJsonAdapterFactory())
                                    .build()

    init {
        println("HkSdk object created")
    }
    fun configure(name: String = "app5", configDir: String) {
        if (controller == null) {
            controller = hkmobile.Hkmobile.newCompatibleController(name, configDir, this)
            println("initialized controller")
        }
    }
    fun start() {
        controller?.startDiscovery()
        println("mdns discovery started")
    }
    override fun onCharacteristic(p0: String?) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onClose(p0: String?) {
        runBlocking {
            if (p0 != null) {
                try {
                    val jsonAdapter: JsonAdapter<Device> = moshi.adapter<Device>()
                    val device = jsonAdapter.fromJson(p0)
                    println("closed $device")
                    if (device != null) {
                        _closedEvents.emit(device)
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onDiscovery(p0: String?) {
        println("discovered $p0")
        runBlocking {
            if (p0 != null) {
                try {
                    val jsonAdapter: JsonAdapter<Device> = moshi.adapter()
                    val device = jsonAdapter.fromJson(p0)
                    if (device != null) {
                        _discoverEvents.emit(device)
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onLost(p0: String?) {
        runBlocking {
            if (p0 != null) {
                try {
                    val jsonAdapter: JsonAdapter<Device> = moshi.adapter()
                    val device = jsonAdapter.fromJson(p0)
                    println("lost $device")
                    if (device != null) {
                        _lostEvents.emit(device)
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onPaired(p0: String?) {
        runBlocking {
            if (p0 != null) {
                try {
                    val jsonAdapter: JsonAdapter<Device> = moshi.adapter()
                    val device = jsonAdapter.fromJson(p0)
                    println("paired $device")
                    if (device != null) {
                        _pairedEvents.emit(device)
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onUnpaired(p0: String?) {
        runBlocking {
            if (p0 != null) {
                try {
                    val jsonAdapter: JsonAdapter<Device> = moshi.adapter()
                    val device = jsonAdapter.fromJson(p0)
                    println("unpaired $device")
                    if (device != null) {
                        _unpairedEvents.emit(device)
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onVerified(p0: String?) {
        runBlocking {
            if (p0 != null) {
                try {
                    val jsonAdapter: JsonAdapter<Device> = moshi.adapter()
                    val device = jsonAdapter.fromJson(p0)
                    println("verified $device")
                    if (device != null) {
                        _verifiedEvents.emit(device)
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    // api funcs
    @OptIn(ExperimentalStdlibApi::class)
    fun getAllDevices() : List<Device> {
        val jjResponse = controller?.allDevices
        val jsonAdapter: JsonAdapter<Response> = moshi.adapter()
        val response = jsonAdapter.fromJson(jjResponse.toString())
        if (response?.error != "") {
            return emptyList()
        }
        // back to json
        val anyJsonAdapter: JsonAdapter<Any> = moshi.adapter()
        val jjResult = anyJsonAdapter.toJson(response.result)

        // then parse into list
        val devListJsonAdapter: JsonAdapter<List<Device>> = moshi.adapter()
        val devices = devListJsonAdapter.fromJson(jjResult)
        if (devices != null) {
            return devices
        }
        return emptyList()
    }
}