#pragma once
#include <Arduino.h>
#include <BLEServer.h>

#define SERVICE_UUID "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define SPO2_CHARACTERISTIC_UUID "beb5483e-36e1-4688-b7f5-ea07361b26a8"
#define PULSE_CHARACTERISTIC_UUID "60467bc8-f5e3-4fca-8f86-ce2d6183d7c2"

BLEServer *createBluetoothServer();
String getCharacteristicValue(BLEServer *, const char *);
void changeCharacteristicValue(BLEServer *server, const char *uuid,
                               const char *value);
