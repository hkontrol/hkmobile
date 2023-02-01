package hkmobile

type MobileReceiver interface {
	OnDiscovery(string)
	OnLost(string)
	OnPaired(string)
	OnUnpaired(string)
	OnVerified(string)
	OnClose(string)
	OnCharacteristic(string)
}

type CompatibleKontroller interface {
	Init(mobile MobileReceiver) string
	StartDiscovery() string
	StopDiscovery() string

	GetAllDevices() string
	GetPairedDevices() string
	GetVerifiedDevices() string

	PairSetup(deviceId string, pin string) string
	PairVerify(deviceId string) string
	Unpair(deviceId string) string

	PairSetupAndVerify(deviceId string, pin string) string

	GetDeviceInfo(deviceId string) string
	ListAccessories(deviceId string) string
	GetAccessoryInfo(deviceId string, aid int) string
	GetCharacteristicInfo(deviceId string, aid int, iid string) string

	GetAccessoriesReq(deviceId string) string
	GetCharacteristicReq(deviceId string, characteristic string) string
	PutCharacteristicReq(deviceId string, aid int, iid int, value string) string

	WatchCharacteristic(deviceId string, aid int, iid int) string
}
