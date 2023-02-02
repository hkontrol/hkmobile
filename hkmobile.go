package hkmobile

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/hkontrol/hkontroller"
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

	PairSetup(deviceId string, pin string) string
	PairVerify(deviceId string) string
	Unpair(deviceId string) string

	PairSetupAndVerify(deviceId string, pin string) string

	GetDeviceInfo(deviceId string) string
	ListAccessories(deviceId string) string
	GetAccessoryInfo(deviceId string, aid int) string

	GetAccessoriesReq(deviceId string) string
	GetCharacteristicReq(deviceId string, aid int, iid int) string
	PutCharacteristicReq(deviceId string, aid int, iid int, value string) string

	SubscribeToCharacteristic(deviceId string, aid int, iid int) string
	UnsubscribeFromCharacteristic(deviceId string, aid int, iid int) string
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
			fmt.Sprintf("error composing result json: %v", err),
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

	go func() {
		for d := range disco {
			fmt.Println("discovered", d.Id)
			k.receiver.OnDiscovery(d.Id)

			// catch events and pass to receiver
			go func(dd *hkontroller.Device) {
				for range dd.OnPaired() {
					k.receiver.OnPaired(dd.Id)
				}
			}(d)
			go func(dd *hkontroller.Device) {
				for range dd.OnUnpaired() {
					k.receiver.OnUnpaired(dd.Id)
				}
			}(d)
			go func(dd *hkontroller.Device) {
				for range dd.OnVerified() {
					k.receiver.OnVerified(dd.Id)
				}
			}(d)
			go func(dd *hkontroller.Device) {
				for range dd.OnClose() {
					k.receiver.OnClose(dd.Id)
				}
			}(d)
		}
	}()

	go func() {
		for d := range lost {
			fmt.Println("lost", d.Id)
			k.receiver.OnLost(d.Id)
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
	Id         string `json:"id"`
	Name       string `json:"name"`
	Discovered bool   `json:"discovered"`
	Paired     bool   `json:"paired"`
	Verified   bool   `json:"verified"`
}

func convertDevice(d *hkontroller.Device) Device {
	return Device{
		Id:         d.Id,
		Name:       d.Name,
		Discovered: d.IsDiscovered(),
		Paired:     d.IsPaired(),
		Verified:   d.IsVerified(),
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

func (k *hkWrapper) PairSetup(deviceId string, pin string) string {

	dd := k.controller.GetDevice(deviceId)
	if dd == nil {
		return responseError("device not found")
	}

	err := dd.PairSetup(pin)
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult("paired")
}

func (k *hkWrapper) PairVerify(deviceId string) string {
	dd := k.controller.GetDevice(deviceId)
	if dd == nil {
		return responseError("device not found")
	}

	err := dd.PairVerify()
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult("verified")
}

func (k *hkWrapper) Unpair(deviceId string) string {
	dd := k.controller.GetDevice(deviceId)
	if dd == nil {
		return responseError("device not found")
	}

	err := dd.Unpair()
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult("unpaired")
}

func (k *hkWrapper) PairSetupAndVerify(deviceId string, pin string) string {
	dd := k.controller.GetDevice(deviceId)
	if dd == nil {
		return responseError("device not found")
	}

	err := dd.PairSetupAndVerify(context.Background(), pin, 5000*time.Millisecond)
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult("verified")
}

func (k *hkWrapper) GetDeviceInfo(deviceId string) string {
	dd := k.controller.GetDevice(deviceId)
	if dd == nil {
		return responseError("device not found")
	}
	return responseResult(convertDevice(dd))
}

func (k *hkWrapper) ListAccessories(deviceId string) string {
	dd := k.controller.GetDevice(deviceId)
	if dd == nil {
		return responseError("device not found")
	}
	return responseResult(dd.Accessories())
}

func (k *hkWrapper) GetAccessoryInfo(deviceId string, aid int) string {
	dd := k.controller.GetDevice(deviceId)
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

func (k *hkWrapper) GetAccessoriesReq(deviceId string) string {
	dd := k.controller.GetDevice(deviceId)
	if dd == nil {
		return responseError("device not found")
	}
	err := dd.GetAccessories()
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult(dd.Accessories())
}

func (k *hkWrapper) GetCharacteristicReq(deviceId string, aid int, iid int) string {
	dd := k.controller.GetDevice(deviceId)
	if dd == nil {
		return responseError("device not found")
	}
	characteristic, err := dd.GetCharacteristic(uint64(aid), uint64(iid))
	if err != nil {
		return responseError(err.Error())
	}
	return responseResult(characteristic)
}

func (k *hkWrapper) PutCharacteristicReq(deviceId string, aid int, iid int, value string) string {
	dd := k.controller.GetDevice(deviceId)
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

func (k *hkWrapper) SubscribeToCharacteristic(deviceId string, aid int, iid int) string {
	//dd := k.controller.GetDevice(deviceId)
	//if dd == nil {
	//	return responseError("device not found")
	//}
	//events, err := dd.SubscribeToEvents(aid, iid)
	//if err != nil {
	//	return ""
	//}
	return responseError("implement me")
}
func (k *hkWrapper) UnsubscribeFromCharacteristic(deviceId string, aid int, iid int) string {
	//dd := k.controller.GetDevice(deviceId)
	//if dd == nil {
	//	return responseError("device not found")
	//}
	return responseError("implement me")
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
