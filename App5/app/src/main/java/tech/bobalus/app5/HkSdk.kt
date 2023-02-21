package tech.bobalus.app5

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hkmobile.CompatibleKontroller
import hkmobile.Hkmobile
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

data class Pairing(
    @Json(name = "name") val name: String?,
    @Json(name = "id") val id: String?,
    @Json(name = "pubk") val publicKey: String?
)

data class Device(
    @Json(name = "name") val name: String,
    @Json(name = "discovered") val discovered: Boolean,
    @Json(name = "paired") val paired: Boolean,
    @Json(name = "verified") val verified: Boolean,
    @Json(name = "pairing") val pairing: Pairing?,
    @Json(name = "dns_service_name") val dnsServiceName: String,
    @Json(name = "txt") val txt: Map<String, String>?,
) {
    constructor() : this("", false, false, false, null, "", null)
}

data class Characteristic(
    @Json(name = "aid") val aid: Long,
    @Json(name = "iid") val iid: Long,
    @Json(name = "type") val type: String,
    @Json(name = "value") var value: Any?,
    @Json(name = "perms") val permissions: List<String>?,
    @Json(name = "format") val format: String?,
    @Json(name = "maxLen") val maxLem: Long?,
    // TODO another fields
)

data class Service(
    @Json(name = "iid") val iid: Long,
    @Json(name = "type") val type: String,
    @Json(name = "characteristics") val characteristics: List<Characteristic>,
    @Json(name = "primary") val primary: Boolean?,
    @Json(name = "hidden") val hidden: Boolean?,
)

data class Accessory(
    @Json(ignore = true) var device: String = "", // assigned in this module
    @Json(name = "aid") val id: Long,
    @Json(name = "services") val services: List<Service>
)

data class CharacteristicEvent(
    @Json(name = "device") val deviceName: String,
    @Json(name = "aid") val aid: Long,
    @Json(name = "iid") val iid: Long,
    @Json(name = "value") val value: Any
)


data class Response(
    @Json(name = "error") val error: String,
    @Json(name = "result") val result: Any?
)

object HkSdk : MobileReceiver {
    var running = false
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

    private val _characteristicEvents = MutableSharedFlow<CharacteristicEvent>()
    val characteristicEvents = _characteristicEvents.asSharedFlow()

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
        if (running) {
            return
        }
        controller?.startDiscovery()
        println("mdns discovery started")
        running = true
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCharacteristic(p0: String?) {
        runBlocking {
            if (p0 != null) {
                try {
                    val jsonAdapter: JsonAdapter<CharacteristicEvent> = moshi.adapter()
                    val event = jsonAdapter.fromJson(p0)
                    if (event != null) {
                        _characteristicEvents.emit(event)
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onClose(p0: String?) {
        runBlocking {
            if (p0 != null) {
                try {
                    val jsonAdapter: JsonAdapter<Device> = moshi.adapter()
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
                        getAccessoriesReq(device) // so they are saved by hkontroller core module
                        subscribeToEvents(device)
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
    fun getAllDevices(): List<Device> {
        val jjResponse: String? = controller?.allDevices
        val jsonAdapter: JsonAdapter<Response> = moshi.adapter()
        val response: Response? = jsonAdapter.fromJson(jjResponse.toString())
        if (response?.error != "") {
            return emptyList()
        }
        // back to json
        val anyJsonAdapter: JsonAdapter<Any> = moshi.adapter()
        val jjResult: String? = anyJsonAdapter.toJson(response.result)
        // then parse into list
        val devListJsonAdapter: JsonAdapter<List<Device>> = moshi.adapter()
        try {
            val devices: List<Device> = devListJsonAdapter.fromJson(jjResult.toString())!!
            return devices
        } catch (e: Exception) {
            println("exception getting device list: ${e.message}")
        }

        return emptyList()
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun listAccessories(device: Device): List<Accessory> {
        val jjResponse: String? = controller?.listAccessories(device.name)
        val jsonAdapter: JsonAdapter<Response> = moshi.adapter()
        val response: Response? = jsonAdapter.fromJson(jjResponse.toString())

        // back to json. is there a way to avoid it?
        val anyJsonAdapter: JsonAdapter<Any> = moshi.adapter()
        val jjResult: String? = anyJsonAdapter.toJson(response!!.result)

        val accListJsonAdapter: JsonAdapter<List<Accessory>> = moshi.adapter()
        var accessories: List<Accessory> = emptyList()

        try {
            accessories = accListJsonAdapter.fromJson(jjResult.toString())!!
        } catch (e: Exception) {
            println("exception parsing listAccessories result: ${e.message}")
        }

        accessories.forEach {
            it.device = device.name
        }

        return accessories
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getAccessoriesReq(device: Device): Response? {
        val jjResponse: String? = controller?.getAccessoriesReq(device.name)
        val jsonAdapter: JsonAdapter<Response> = moshi.adapter()
        val response: Response? = jsonAdapter.fromJson(jjResponse.toString())
        if (response?.error != "") {
            println("error getting accessories: ${response?.error}")
        }
        println("success for getting accessories")
        return response
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun subscribeToEvents(device: Device): Response? {
        val jjResponse: String? = controller?.subscribeToAllEvents(device.name)
        val jsonAdapter: JsonAdapter<Response> = moshi.adapter()
        val response: Response? = jsonAdapter.fromJson(jjResponse.toString())
        if (response?.error != "") {
            println("error getting accessories: ${response?.error}")
        }
        println("success for subscribing to events")
        return response
    }

    fun findService(accessory: Accessory, serviceType: String): Service? {
        val it = accessory.services.iterator()
        while (it.hasNext()) {
            val srv = it.next()
            if (srv.type == serviceType) {
                return srv
            }
        }
        return null
    }

    fun findCharacteristic(service: Service, characteristicsType: String): Characteristic? {
        val it = service.characteristics.iterator()
        while (it.hasNext()) {
            val chr = it.next()
            if (chr.type == characteristicsType) {
                return chr
            }
        }

        return null
    }

    fun getAccessoryName(accessory: Accessory): String? {
        val ss = findService(accessory, Hkmobile.SType_AccessoryInfo) ?: return null
        val cc = findCharacteristic(ss, Hkmobile.CType_Name) ?: return null
        return cc.value.toString()
    }

    fun findPrimaryService(accessory: Accessory): Service? {
        // first, try to find directly
        var it = accessory.services.iterator()
        while (it.hasNext()) {
            val ss = it.next()
            if (ss.primary == true) {
                return ss
            }
        }

        // if not service with primary field found
        // then just return first available after AccessoryInfo
        it = accessory.services.iterator()
        while (it.hasNext()) {
            val ss = it.next()
            if (ss.type == Hkmobile.SType_AccessoryInfo) {
                continue
            }
            return ss
        }

        return null
    }
}