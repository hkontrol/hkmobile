# hkmobile

`hkontroller` for gomobile bind.

## idea

The super idea: to develop HomeKit app for Android.
Using golang library this can be done in two ways:

  1. Entirely in golang: [native, gioui app.](https://github.com/hkontrol/hkapp). The cons is lack of integration with Android UI(how to implement widget for desktop, for example?). The pro is that it's crossplatform - we can use it in Linux as well.
  2. Native Android app with kotlin/java-golang coupling.

This repo is about second way.

Because gomobile [has limited support](https://pkg.go.dev/golang.org/x/mobile/cmd/gobind#hdr-Type_restrictions) for exported types we can wrap `hkontroller` into some kind json api: it is possible to operate with strings and byte slices.

To react on events like device discovery/lost, characteristic update we can define interface `MobileReceiver` and implement that interface on Java/Kotlin side:

```text
type MobileReceiver interface {
  OnDiscovery(string)
  OnLost(string)
  OnPaired(string)
  OnUnpaired(string)
  OnVerified(string)
  OnClose(string)
  OnCharacteristic(string)
}
```

## build

build `aar` archive:

```text
go get golang.org/x/mobile/bind
gomobile bind -target=android ./
cp ./hkmobile.aar ./App5/app/libs
```