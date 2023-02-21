package hkmobile

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/hkontrol/hkontroller"
	"github.com/olebedev/emitter"
	"path"
	"time"
)

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
	StartDiscovery() string
	StopDiscovery() string

	GetAllDevices() string
	GetPairedDevices() string
	GetVerifiedDevices() string

	PairSetup(deviceName string, pin string) string
	PairVerify(deviceName string) string
	Unpair(deviceName string) string

	PairSetupAndVerify(deviceName string, pin string) string

	GetDeviceInfo(deviceName string) string
	ListAccessories(deviceName string) string
	GetAccessoryInfo(deviceName string, aid int) string
	FindService(deviceName string, aid int, stype string) string
	FindCharacteristic(deviceName string, aid int, stype string, ctype string) string

	GetAccessoriesReq(deviceName string) string
	GetCharacteristicReq(deviceName string, aid int, iid int) string
	PutCharacteristicReq(deviceName string, aid int, iid int, value string) string

	SubscribeToCharacteristic(deviceName string, aid int, iid int) string
	UnsubscribeFromCharacteristic(deviceName string, aid int, iid int) string

	// SubscribeToAllEvents supposed to be used instead of other subscribe methods.
	// There is no unsubscribe method because subscriptions should not persist across session.
	// So, channels should close automatically on device lost/unpaired event.
	SubscribeToAllEvents(deviceName string) string
}

type Result struct {
	Error  string      `json:"error"`
	Result interface{} `json:"result"`
}

func responseError(err string) string {
	jj, _ := json.Marshal(
		Result{
			Error:  err,
			Result: "",
		})
	return string(jj)
}
func responseResult(res interface{}) string {
	jj, err := json.Marshal(
		Result{
			Error:  "",
			Result: res,
		})
	if err != nil {
		return responseError(
			fmt.Sprintf("error marshaling result json: %v", err),
		)
	}
	return string(jj)
}

type hkWrapper struct {
	controller      *hkontroller.Controller
	receiver        MobileReceiver
	cancelDiscovery context.CancelFunc
}

func (k *hkWrapper) StartDiscovery() string {
	ctx, cancel := context.WithCancel(context.Background())
	k.cancelDiscovery = cancel
	disco, lost := k.controller.StartDiscoveryWithContext(ctx)

	verify := func(d *hkontroller.Device) {
		ctx := context.Background()
		err := d.PairSetupAndVerify(ctx, "pin doesn't matter if already paired", 5*time.Second)
		if err != nil {
			fmt.Println("pair-verify err: ", err)
			return
		}
		fmt.Println("should be connected now")
	}

	go func() {
		for d := range disco {
			if d.IsPaired() {
				go verify(d)
			}

			// catch events and pass to receiver
			go func() {
				cc := convertDevice(d)
				jj, err := json.Marshal(cc)
				if err != nil {
					return
				}
				k.receiver.OnDiscovery(string(jj))
			}()

			go func(dd *hkontroller.Device) {
				for range dd.OnPaired() {
					cc := convertDevice(dd)
					jj, err := json.Marshal(cc)
					if err != nil {
						continue
					}
					k.receiver.OnPaired(string(jj))
				}
			}(d)
			go func(dd *hkontroller.Device) {
				for range dd.OnUnpaired() {
					cc := convertDevice(dd)
					jj, err := json.Marshal(cc)
					if err != nil {
						continue
					}
					k.receiver.OnUnpaired(string(jj))
				}
			}(d)
			go func(dd *hkontroller.Device) {
				for range dd.OnVerified() {
					cc := convertDevice(dd)
					jj, err := json.Marshal(cc)
					if err != nil {
						continue
					}
					k.receiver.OnVerified(string(jj))
				}
			}(d)
			go func(dd *hkontroller.Device) {
				for range dd.OnClose() {
					cc := convertDevice(dd)
					jj, err := json.Marshal(cc)
					if err != nil {
						continue
					}
					k.receiver.OnClose(string(jj))
				}
			}(d)
		}
	}()

	go func() {
		for d := range lost {
			cc := convertDevice(d)
			jj, err := json.Marshal(cc)
			if err != nil {
				return
			}
			k.receiver.OnLost(string(jj))
		}
	}()

	return responseResult("ok")
}

func (k *hkWrapper) StopDiscovery() string {
	if k.cancelDiscovery != nil {
		k.cancelDiscovery()
	}
	return responseResult("canceled")
}

type Device struct {
	Name           string              `json:"name"`
	Discovered     bool                `json:"discovered"`
	Paired         bool                `json:"paired"`
	Verified       bool                `json:"verified"`
	Pairing        hkontroller.Pairing `json:"pairing"`
	DnsServiceName string              `json:"dns_service_name"`
	Txt            map[string]string   `json:"txt"`
}

func convertDevice(d *hkontroller.Device) Device {
	return Device{
		Name:           d.Name,
		Discovered:     d.IsDiscovered(),
		Paired:         d.IsPaired(),
		Verified:       d.IsVerified(),
		Pairing:        d.GetPairingInfo(),
		Txt:            d.GetDnssdEntry().Text,
		DnsServiceName: d.GetDnssdEntry().ServiceInstanceName(),
	}
}

func convertDeviceList(devs []*hkontroller.Device) []Device {
	var res []Device
	for _, d := range devs {
		res = append(res, convertDevice(d))
	}
	return res
}

func (k *hkWrapper) GetAllDevices() string {
	devs := k.controller.GetAllDevices()
	res := convertDeviceList(devs)
	return responseResult(res)
}

func (k *hkWrapper) GetPairedDevices() string {
	devs := k.controller.GetPairedDevices()
	res := convertDeviceList(devs)
	return responseResult(res)
}

func (k *hkWrapper) GetVerifiedDevices() string {
	devs := k.controller.GetVerifiedDevices()
	res := convertDeviceList(devs)
	return responseResult(res)
}

func (k *hkWrapper) PairSetup(deviceName string, pin string) string {

	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}

	err := dd.PairSetup(pin)
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult("paired")
}

func (k *hkWrapper) PairVerify(deviceName string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}

	err := dd.PairVerify()
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult("verified")
}

func (k *hkWrapper) Unpair(deviceName string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}

	err := dd.Unpair()
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult("unpaired")
}

func (k *hkWrapper) PairSetupAndVerify(deviceName string, pin string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	if !dd.IsPaired() {
		err := dd.PairSetup(pin)
		if err != nil {
			return responseError(err.Error())
		}
	}
	if !dd.IsVerified() {
		err := dd.PairVerify()
		if err != nil {
			return responseError(err.Error())
		}
	}

	//err := dd.PairSetupAndVerify(context.Background(), pin, 5000*time.Millisecond)
	//if err != nil {
	//	return responseError(err.Error())
	//}

	return responseResult("verified")
}

func (k *hkWrapper) GetDeviceInfo(deviceName string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	return responseResult(convertDevice(dd))
}

func (k *hkWrapper) ListAccessories(deviceName string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	return responseResult(dd.Accessories())
}

func (k *hkWrapper) GetAccessoryInfo(deviceName string, aid int) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	for _, aa := range dd.Accessories() {
		if int(aa.Id) == aid {
			return responseResult(aa)
		}
	}
	return responseError("accessory not found")
}

func (k *hkWrapper) GetAccessoriesReq(deviceName string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	err := dd.GetAccessories()
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult(dd.Accessories())
}

func (k *hkWrapper) GetCharacteristicReq(deviceName string, aid int, iid int) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	characteristic, err := dd.GetCharacteristic(uint64(aid), uint64(iid))
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult(characteristic)
}

func (k *hkWrapper) PutCharacteristicReq(deviceName string, aid int, iid int, value string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	var vv interface{}
	err := json.Unmarshal([]byte(value), &vv)
	if err != nil {
		return responseError(err.Error())
	}
	err = dd.PutCharacteristic(uint64(aid), uint64(iid), vv)
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult(vv)
}

type characteristicEvent struct {
	Dev string      `json:"device"`
	Aid interface{} `json:"aid"`
	Iid interface{} `json:"iid"`
	Val interface{} `json:"value"`
}

func (k *hkWrapper) onEvent(deviceName string, e emitter.Event) {
	if len(e.Args) != 3 {
		return
	}

	ev := characteristicEvent{
		Dev: deviceName,
		Aid: e.Args[0],
		Iid: e.Args[1],
		Val: e.Args[2],
	}

	jj, err := json.Marshal(ev)
	if err != nil {
		fmt.Println("cannot marshal characteristic event: %s", err.Error())
		return
	}

	k.receiver.OnCharacteristic(string(jj))
}

func (k *hkWrapper) SubscribeToAllEvents(deviceName string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	events, err := dd.SubscribeToAllEvents()
	if err != nil {
		return responseError(fmt.Sprintf("cannot subscribe to device events: %s", fmt.Sprintf(err.Error())))
	}
	go func() {
		for e := range events {
			k.onEvent(deviceName, e)
		}
	}()
	return responseResult("success")
}

func (k *hkWrapper) SubscribeToCharacteristic(deviceName string, aid int, iid int) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	events, err := dd.SubscribeToEvents(uint64(aid), uint64(iid))
	if err != nil {
		err = fmt.Errorf("cannot subscribe to characteristic events: %w", err)
		return responseError(err.Error())
	}
	go func() {
		for e := range events {
			k.onEvent(deviceName, e)
		}
	}()
	return responseResult("success")
}
func (k *hkWrapper) UnsubscribeFromCharacteristic(deviceName string, aid int, iid int) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	err := dd.UnsubscribeFromEvents(uint64(aid), uint64(iid))
	if err != nil {
		err = fmt.Errorf("cannot unsubscribe from characteristic events: %w", err)
		return responseError(err.Error())
	}
	return responseResult("")
}

func (k *hkWrapper) FindService(deviceName string, aid int, stype string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	for _, aa := range dd.Accessories() {
		if int(aa.Id) != aid {
			continue
		}
		ss := aa.GetService(hkontroller.HapServiceType(stype))
		if ss == nil {
			return responseError("service not found")
		}
		return responseResult(ss)
	}
	return responseError("accessory not found")
}

func (k *hkWrapper) FindCharacteristic(deviceName string, aid int, stype string, ctype string) string {
	dd := k.controller.GetDevice(deviceName)
	if dd == nil {
		return responseError("device not found")
	}
	for _, aa := range dd.Accessories() {
		if int(aa.Id) != aid {
			continue
		}
		ss := aa.GetService(hkontroller.HapServiceType(stype))
		if ss == nil {
			return responseError("service not found")
		}
		cc := ss.GetCharacteristic(hkontroller.HapCharacteristicType(ctype))
		if cc == nil {
			return responseError("characteristic not found")
		}
		responseResult(cc)
	}
	return responseError("accessory not found")
}

// NewCompatibleController returns wrapper aroung hkontroller.Controller.
// NewCompatibleController(name, configDir, receiver)
func NewCompatibleController(name string, configDir string, receiver MobileReceiver) CompatibleKontroller {
	store := hkontroller.NewFsStore(path.Join(configDir, "hkontroller"))
	controller, err := hkontroller.NewController(store, name)
	if err != nil {
		panic(err)
	}

	err = controller.LoadPairings()
	if err != nil {
		panic(err)
	}

	return &hkWrapper{
		controller: controller,
		receiver:   receiver,
	}
}
