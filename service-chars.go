package hkmobile

const (
	SType_HapProtocolInfo               string = "A2"
	SType_AccessoryInfo                 string = "3E"
	SType_AirPurifier                   string = "BB"
	SType_AirQualitySensor              string = "8D"
	SType_AudioStreamManagement         string = "127"
	SType_BatteryService                string = "96"
	SType_CameraRTPStreamManagement     string = "110"
	SType_CarbonDioxideSensor           string = "97"
	SType_CarbonMonoxideSensor          string = "7F"
	SType_ContactSensor                 string = "80"
	SType_DataStreamTransportManagement string = "129"
	SType_Door                          string = "81"
	SType_Doorbell                      string = "121"
	SType_Fan                           string = "B7"
	SType_Faucet                        string = "D7"
	SType_FilterMaintenance             string = "BA"
	SType_GarageDoorOpener              string = "41"
	SType_HeaterCooler                  string = "BC"
	SType_HumidifierDehumidifier        string = "BD"
	SType_HumiditySensor                string = "82"
	SType_IrrigationSystem              string = "CF"
	SType_LeakSensor                    string = "83"
	SType_LightBulb                     string = "43"
	SType_LightSensor                   string = "84"
	SType_LockManagement                string = "44"
	SType_LockMechanism                 string = "45"
	SType_Microphone                    string = "112"
	SType_MotionSensor                  string = "85"
	SType_OccupancySensor               string = "86"
	SType_Outlet                        string = "47"
	SType_SecuritySystem                string = "7E"
	SType_ServiceLabel                  string = "CC"
	SType_Siri                          string = "133"
	SType_Slat                          string = "B9"
	SType_SmokeSensor                   string = "87"
	SType_Speaker                       string = "113"
	SType_StatelessProgrammableSwitch   string = "89"
	SType_Switch                        string = "49"
	SType_TargetControl                 string = "125"
	SType_TargetControlManagement       string = "122"
	SType_TemperatureSensor             string = "8A"
	SType_Thermostat                    string = "4A"
	SType_Valve                         string = "D0"
	SType_Window                        string = "8B"
	SType_WindowCovering                string = "8C"
)

const (
	CType_Identify                                  string = "14"
	CType_Manufacturer                              string = "20"
	CType_Model                                     string = "21"
	CType_Name                                      string = "23"
	CType_SerialNumber                              string = "30"
	CType_Version                                   string = "37"
	CType_FirmwareRevision                          string = "52"
	CType_HardwareRevision                          string = "53"
	CType_On                                        string = "25"
	CType_Brightness                                string = "8"
	CType_AccessoryFlags                            string = "A6"
	CType_Active                                    string = "B0"
	CType_ActiveIdentifier                          string = "E7"
	CType_AdministratorOnlyAccess                   string = "1"
	CType_AudioFeedback                             string = "5"
	CType_AirParticulateSize                        string = "65"
	CType_AirQuality                                string = "95"
	CType_BatteryLevel                              string = "68"
	CType_ButtonEvent                               string = "126"
	CType_CarbonMonoxideLevel                       string = "90"
	CType_CarbonMonoxidePeakLevel                   string = "91"
	CType_CarbonMonoxideDetected                    string = "69"
	CType_CarbonDioxideLevel                        string = "93"
	CType_CarbonDioxidePeakLevel                    string = "94"
	CType_CarbonDioxideDetected                     string = "92"
	CType_ChargingState                             string = "8F"
	CType_CoolingThresholdTemperature               string = "D"
	CType_ColorTemperature                          string = "CE"
	CType_ContactSensorState                        string = "6A"
	CType_CurrentAmbientLightLevel                  string = "6B"
	CType_CurrentHorizontalTiltAngle                string = "6C"
	CType_CurrentAirPurifierState                   string = "A9"
	CType_CurrentSlatState                          string = "AA"
	CType_CurrentPosition                           string = "6D"
	CType_CurrentVerticalTiltAngle                  string = "6E"
	CType_CurrentHumidifierDehumidifierState        string = "B3"
	CType_CurrentDoorState                          string = "E"
	CType_CurrentFanState                           string = "AF"
	CType_CurrentHeatingCoolingState                string = "F"
	CType_CurrentHeaterCoolerState                  string = "B1"
	CType_CurrentRelativeHumidity                   string = "10"
	CType_CurrentTemperature                        string = "11"
	CType_CurrentTiltAngle                          string = "C1"
	CType_DigitalZoom                               string = "11D"
	CType_FilterLifeLevel                           string = "AB"
	CType_FilterChangeIndication                    string = "AC"
	CType_HeatingThresholdTemperature               string = "12"
	CType_HoldPosition                              string = "6F"
	CType_Hue                                       string = "13"
	CType_ImageRotation                             string = "11E"
	CType_ImageMirroring                            string = "11F"
	CType_InUse                                     string = "D2"
	CType_IsConfigured                              string = "D6"
	CType_LeakDetected                              string = "70"
	CType_LockControlPoint                          string = "19"
	CType_LockCurrentState                          string = "1D"
	CType_LockLastKnownAction                       string = "1C"
	CType_LockManagementAutoSecurityTimeout         string = "1A"
	CType_LockPhysicalControls                      string = "A7"
	CType_LockTargetState                           string = "1E"
	CType_Logs                                      string = "1F"
	CType_MotionDetected                            string = "22"
	CType_Mute                                      string = "11A"
	CType_NightVision                               string = "11B"
	CType_NitrogenDioxideDensity                    string = "C4"
	CType_ObstructionDetected                       string = "24"
	CType_PM25Density                               string = "C6"
	CType_OccupancyDetected                         string = "71"
	CType_OpticalZoom                               string = "11C"
	CType_OutletInUse                               string = "26"
	CType_OzoneDensity                              string = "C3"
	CType_PM10Density                               string = "C7"
	CType_PositionState                             string = "72"
	CType_ProgramMode                               string = "D1"
	CType_ProgrammableSwitchEvent                   string = "73"
	CType_RelativeHumidityDehumidifierThreshold     string = "C9"
	CType_RelativeHumidityHumidifierThreshold       string = "CA"
	CType_RemainingDuration                         string = "D4"
	CType_ResetFilterIndication                     string = "AD"
	CType_RotationDirection                         string = "28"
	CType_RotationSpeed                             string = "29"
	CType_Saturation                                string = "2F"
	CType_SecuritySystemAlarmType                   string = "BE"
	CType_SecuritySystemCurrentState                string = "66"
	CType_SecuritySystemTargetState                 string = "67"
	CType_SelectedAudioStreamConfiguration          string = "128"
	CType_ServiceLabelIndex                         string = "CB"
	CType_ServiceLabelNamespace                     string = "CD"
	CType_SetupDataStreamTransport                  string = "131"
	CType_SelectedRTPStreamConfiguration            string = "117"
	CType_SetupEndpoints                            string = "118"
	CType_SiriInputType                             string = "132"
	CType_SlatType                                  string = "C0"
	CType_SmokeDetected                             string = "76"
	CType_StatusActive                              string = "75"
	CType_StatusFault                               string = "77"
	CType_StatusJammed                              string = "78"
	CType_StatusLowBattery                          string = "79"
	CType_StatusTampered                            string = "7A"
	CType_StreamingStatus                           string = "120"
	CType_SupportedAudioStreamConfiguration         string = "115"
	CType_SupportedDataStreamTransportConfiguration string = "130"
	CType_SupportedRTPConfiguration                 string = "116"
	CType_SupportedVideoStreamConfiguration         string = "114"
	CType_SulphurDioxideDensity                     string = "C5"
	CType_SwingMode                                 string = "B6"
	CType_TargetAirPurifierState                    string = "A8"
	CType_TargetFanState                            string = "BF"
	CType_TargetTiltAngle                           string = "C2"
	CType_TargetHeaterCoolerState                   string = "B2"
	CType_SetDuration                               string = "D3"
	CType_TargetControlSupportedConfiguration       string = "123"
	CType_TargetControlList                         string = "124"
	CType_TargetHorizontalTiltAngle                 string = "7B"
	CType_TargetHumidifierDehumidifierState         string = "B4"
	CType_TargetPosition                            string = "7C"
	CType_TargetDoorState                           string = "32"
	CType_TargetHeatingCoolingState                 string = "33"
	CType_TargetRelativeHumidity                    string = "34"
	CType_TargetTemperature                         string = "35"
	CType_TemperatureDisplayUnits                   string = "36"
	CType_TargetVerticalTiltAngle                   string = "7D"
	CType_ValveType                                 string = "D5"
	CType_VOCDensity                                string = "C8"
	CType_Volume                                    string = "119"
	CType_WaterLevel                                string = "B5"
)

func ServiceFriendly(st string) string {
	switch st {
	case SType_AccessoryInfo:
		return "AccessoryInfo"
	case SType_HapProtocolInfo:
		return "HapProtocolInfo"
	case SType_LightBulb:
		return "LightBulb"
	case SType_AirPurifier:
		return "AirPurifier"
	case SType_AirQualitySensor:
		return "AirQualitySensor"
	case SType_AudioStreamManagement:
		return "AudioStreamManagement"
	case SType_BatteryService:
		return "BatteryService"
	case SType_CameraRTPStreamManagement:
		return "CameraRTPStreamManagement"
	case SType_CarbonDioxideSensor:
		return "CarbonDioxideSensor"
	case SType_CarbonMonoxideSensor:
		return "CarbonMonoxideSensor"
	case SType_ContactSensor:
		return "ContactSensor"
	case SType_DataStreamTransportManagement:
		return "DataStreamTransportManagement"
	case SType_Door:
		return "Door"
	case SType_Doorbell:
		return "Doorbell"
	case SType_Fan:
		return "Fan"
	case SType_Faucet:
		return "Faucet"
	case SType_FilterMaintenance:
		return "FilterMaintenance"
	case SType_GarageDoorOpener:
		return "GarageDoorOpener"
	case SType_HeaterCooler:
		return "HeaterCooler"
	case SType_HumidifierDehumidifier:
		return "HumidifierDehumidifier"
	case SType_HumiditySensor:
		return "HumiditySensor"
	case SType_IrrigationSystem:
		return "IrrigationSystem"
	case SType_LeakSensor:
		return "LeakSensor"
	case SType_LightSensor:
		return "LightSensor"
	case SType_LockManagement:
		return "LockManagement"
	case SType_LockMechanism:
		return "LockMechanism"
	case SType_Microphone:
		return "Microphone"
	case SType_MotionSensor:
		return "MotionSensor"
	case SType_OccupancySensor:
		return "OccupanceSensor"
	case SType_Outlet:
		return "Outlet"
	case SType_SecuritySystem:
		return "SecuritySystem"
	case SType_ServiceLabel:
		return "ServiceLabel"
	case SType_Siri:
		return "Siri"
	case SType_Slat:
		return "Slat"
	case SType_SmokeSensor:
		return "SmokeSensor"
	case SType_Speaker:
		return "Speaker"
	case SType_StatelessProgrammableSwitch:
		return "StatelessProgrammableSwitch"
	case SType_Switch:
		return "Switch"
	case SType_TargetControl:
		return "TargetControl"
	case SType_TargetControlManagement:
		return "TargetControlManagement"
	case SType_TemperatureSensor:
		return "TemperatureSensor"
	case SType_Thermostat:
		return "Thermostat"
	case SType_Valve:
		return "Valve"
	case SType_Window:
		return "Window"
	case SType_WindowCovering:
		return "WindowCovering"
	}

	return st
}

func CharacteristicFriendly(ct string) string {
	switch ct {
	case CType_Identify:
		return "Identify"
	case CType_Manufacturer:
		return "Manufacturer"
	case CType_Model:
		return "Model"
	case CType_Name:
		return "Name"
	case CType_SerialNumber:
		return "SerialNumber"
	case CType_Version:
		return "Version"
	case CType_FirmwareRevision:
		return "FirmwareRevision"
	case CType_HardwareRevision:
		return "HardwareRevision"
	case CType_On:
		return "On"
	case CType_Brightness:
		return "Brightness"
	case CType_AccessoryFlags:
		return "AccessoryFlags"
	case CType_Active:
		return "Active"
	case CType_ActiveIdentifier:
		return "ActiveIdentifier"
	case CType_AdministratorOnlyAccess:
		return "AdministratorOnlyAccess"
	case CType_AudioFeedback:
		return "AudioFeedback"
	case CType_AirParticulateSize:
		return "AirParticulateSize"
	case CType_AirQuality:
		return "AirQuality"
	case CType_BatteryLevel:
		return "BatteryLevel"
	case CType_ButtonEvent:
		return "ButtonEvent "
	case CType_CarbonMonoxideLevel:
		return "CarbonMonoxideLevel"
	case CType_CarbonMonoxidePeakLevel:
		return "CarbonMonoxidePeakLevel"
	case CType_CarbonMonoxideDetected:
		return "CarbonMonoxideDetected"
	case CType_CarbonDioxideLevel:
		return "CarbonDioxideLevel"
	case CType_CarbonDioxidePeakLevel:
		return "CarbonDioxidePeakLevel"
	case CType_CarbonDioxideDetected:
		return "CarbonDioxideDetected"
	case CType_ChargingState:
		return "ChargingState"
	case CType_CoolingThresholdTemperature:
		return "CoolingThresholdTemperature"
	case CType_ColorTemperature:
		return "ColorTemperature"
	case CType_ContactSensorState:
		return "ContactSensorState"
	case CType_CurrentAmbientLightLevel:
		return "CurrentAmbientLightLevel"
	case CType_CurrentHorizontalTiltAngle:
		return "CurrentHorizontalTiltAngle"
	case CType_CurrentAirPurifierState:
		return "CurrentAirPurifierState"
	case CType_CurrentSlatState:
		return "CurrentSlatState"
	case CType_CurrentPosition:
		return "CurrentPosition"
	case CType_CurrentVerticalTiltAngle:
		return "CurrentVerticalTiltAngle"
	case CType_CurrentHumidifierDehumidifierState:
		return "CurrentHumidifierDehumidifierState"
	case CType_CurrentDoorState:
		return "CurrentDoorState"
	case CType_CurrentFanState:
		return "CurrentFanState"
	case CType_CurrentHeatingCoolingState:
		return "CurrentHeatingCoolingState"
	case CType_CurrentHeaterCoolerState:
		return "CurrentHeaterCoolerState"
	case CType_CurrentRelativeHumidity:
		return "CurrentRelativeHumidity"
	case CType_CurrentTemperature:
		return "CurrentTemperature"
	case CType_CurrentTiltAngle:
		return "CurrentTiltAngle"
	case CType_DigitalZoom:
		return "DigitalZoom "
	case CType_FilterLifeLevel:
		return "FilterLifeLevel"
	case CType_FilterChangeIndication:
		return "FilterChangeIndication"
	case CType_HeatingThresholdTemperature:
		return "HeatingThresholdTemperature"
	case CType_HoldPosition:
		return "HoldPosition"
	case CType_Hue:
		return "Hue"
	case CType_ImageRotation:
		return "ImageRotation "
	case CType_ImageMirroring:
		return "ImageMirroring"
	case CType_InUse:
		return "InUse"
	case CType_IsConfigured:
		return "IsConfigured"
	case CType_LeakDetected:
		return "LeakDetected"
	case CType_LockControlPoint:
		return "LockControlPoint"
	case CType_LockCurrentState:
		return "LockCurrentState"
	case CType_LockLastKnownAction:
		return "LockLastKnownAction"
	case CType_LockManagementAutoSecurityTimeout:
		return "LockManagementAutoSecurityTimeout"
	case CType_LockPhysicalControls:
		return "LockPhysicalControls"
	case CType_LockTargetState:
		return "LockTargetState"
	case CType_Logs:
		return "Logs"
	case CType_MotionDetected:
		return "MotionDetected"
	case CType_Mute:
		return "Mute"
	case CType_NightVision:
		return "NightVision "
	case CType_NitrogenDioxideDensity:
		return "NitrogenDioxideDensity"
	case CType_ObstructionDetected:
		return "ObstructionDetected"
	case CType_PM25Density:
		return "PM25Density"
	case CType_OccupancyDetected:
		return "OccupancyDetected"
	case CType_OpticalZoom:
		return "OpticalZoom "
	case CType_OutletInUse:
		return "OutletInUse"
	case CType_OzoneDensity:
		return "OzoneDensity"
	case CType_PM10Density:
		return "PM10Density"
	case CType_PositionState:
		return "PositionState"
	case CType_ProgramMode:
		return "ProgramMode"
	case CType_ProgrammableSwitchEvent:
		return "ProgrammableSwitchEvent"
	case CType_RelativeHumidityDehumidifierThreshold:
		return "RelativeHumidityDehumidifierThreshold"
	case CType_RelativeHumidityHumidifierThreshold:
		return "RelativeHumidityHumidifierThreshold"
	case CType_RemainingDuration:
		return "RemainingDuration"
	case CType_ResetFilterIndication:
		return "ResetFilterIndication"
	case CType_RotationDirection:
		return "RotationDirection"
	case CType_RotationSpeed:
		return "RotationSpeed"
	case CType_Saturation:
		return "Saturation"
	case CType_SecuritySystemAlarmType:
		return "SecuritySystemAlarmType"
	case CType_SecuritySystemCurrentState:
		return "SecuritySystemCurrentState"
	case CType_SecuritySystemTargetState:
		return "SecuritySystemTargetState"
	case CType_SelectedAudioStreamConfiguration:
		return "SelectedAudioStreamConfiguration"
	case CType_ServiceLabelIndex:
		return "ServiceLabelIndex"
	case CType_ServiceLabelNamespace:
		return "ServiceLabelNamespace"
	case CType_SetupDataStreamTransport:
		return "SetupDataStreamTransport"
	case CType_SelectedRTPStreamConfiguration:
		return "SelectedRTPStreamConfiguration"
	case CType_SetupEndpoints:
		return "SetupEndpoints"
	case CType_SiriInputType:
		return "SiriInputType "
	case CType_SlatType:
		return "SlatType"
	case CType_SmokeDetected:
		return "SmokeDetected"
	case CType_StatusActive:
		return "StatusActive"
	case CType_StatusFault:
		return "StatusFault"
	case CType_StatusJammed:
		return "StatusJammed"
	case CType_StatusLowBattery:
		return "StatusLowBattery"
	case CType_StatusTampered:
		return "StatusTampered"
	case CType_StreamingStatus:
		return "StreamingStatus "
	case CType_SupportedAudioStreamConfiguration:
		return "SupportedAudioStreamConfiguration "
	case CType_SupportedDataStreamTransportConfiguration:
		return "SupportedDataStreamTransportConfiguration "
	case CType_SupportedRTPConfiguration:
		return "SupportedRTPConfiguration "
	case CType_SupportedVideoStreamConfiguration:
		return "SupportedVideoStreamConfiguration "
	case CType_SulphurDioxideDensity:
		return "SulphurDioxideDensity"
	case CType_SwingMode:
		return "SwingMode"
	case CType_TargetAirPurifierState:
		return "TargetAirPurifierState"
	case CType_TargetFanState:
		return "TargetFanState"
	case CType_TargetTiltAngle:
		return "TargetTiltAngle"
	case CType_TargetHeaterCoolerState:
		return "TargetHeaterCoolerState"
	case CType_SetDuration:
		return "SetDuration"
	case CType_TargetControlSupportedConfiguration:
		return "TargetControlSupportedConfiguration "
	case CType_TargetControlList:
		return "TargetControlList "
	case CType_TargetHorizontalTiltAngle:
		return "TargetHorizontalTiltAngle"
	case CType_TargetHumidifierDehumidifierState:
		return "TargetHumidifierDehumidifierState"
	case CType_TargetPosition:
		return "TargetPosition"
	case CType_TargetDoorState:
		return "TargetDoorState"
	case CType_TargetHeatingCoolingState:
		return "TargetHeatingCoolingState"
	case CType_TargetRelativeHumidity:
		return "TargetRelativeHumidity"
	case CType_TargetTemperature:
		return "TargetTemperature"
	case CType_TemperatureDisplayUnits:
		return "TemperatureDisplayUnits"
	case CType_TargetVerticalTiltAngle:
		return "TargetVerticalTiltAngle"
	case CType_ValveType:
		return "ValveType"
	case CType_VOCDensity:
		return "VOCDensity"
	case CType_Volume:
		return "Volume"
	case CType_WaterLevel:
		return "WaterLevel"
	}
	return ct
}
