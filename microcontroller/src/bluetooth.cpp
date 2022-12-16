#include "bluetooth.hpp"
#include <Arduino.h>
#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>

BLEServer *createBluetoothServer() {
  BLEDevice::init("Heart Check Device");
  BLEServer *pServer = BLEDevice::createServer();
  BLEService *pService = pServer->createService(SERVICE_UUID);

  BLECharacteristic *spo2Characteristic = pService->createCharacteristic(
      SPO2_CHARACTERISTIC_UUID,
      BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_WRITE);

  BLECharacteristic *pulseCharacteristics = pService->createCharacteristic(
      PULSE_CHARACTERISTIC_UUID,
      BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_WRITE);

  // initial value
  spo2Characteristic->setValue("0");
  pulseCharacteristics->setValue("0");

  pService->start();
  BLEAdvertising *pAdvertising = pServer->getAdvertising();

  pAdvertising->addServiceUUID(SERVICE_UUID);
  pAdvertising->setScanResponse(true);
  pAdvertising->setMinPreferred(0x06);

  pAdvertising->setMinPreferred(0x12);
  pAdvertising->start();

  return pServer;
}

String getCharacteristicValue(BLEServer *server, const char *uuid) {
  BLEService *service = server->getServiceByUUID(SERVICE_UUID);
  BLECharacteristic *characteristic = service->getCharacteristic(uuid);

  return characteristic->getValue().c_str();
}

void changeCharacteristicValue(BLEServer *server, const char *uuid,
                               const char *value) {
  BLEService *service = server->getServiceByUUID(SERVICE_UUID);
  BLECharacteristic *characteristic = service->getCharacteristic(uuid);

  characteristic->setValue(value);
}
