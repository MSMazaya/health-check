#include "spo2_algorithm.h"
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <Arduino.h>
#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <MAX30105.h>

#define SERVICE_UUID "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define SPO2_CHARACTERISTIC_UUID "beb5483e-36e1-4688-b7f5-ea07361b26a8"
#define PULSE_CHARACTERISTIC_UUID "60467bc8-f5e3-4fca-8f86-ce2d6183d7c2"

MAX30105 particleSensor;

#define MAX_BRIGHTNESS 255

#if defined(AVR_ATmega328P) || defined(AVR_ATmega168)
// Arduino Uno doesn't have enough SRAM to store 100 samples of IR led data and
// red led data in 32-bit format To solve this problem, 16-bit MSB of the
// sampled data will be truncated. Samples become 16-bit data.
uint16_t irBuffer[100];  // infrared LED sensor data
uint16_t redBuffer[100]; // red LED sensor data
#else
uint32_t irBuffer[100];  // infrared LED sensor data
uint32_t redBuffer[100]; // red LED sensor data
#endif

int32_t bufferLength; // data length
int32_t spo2;         // SPO2 value
int8_t validSPO2;     // indicator to show if the SPO2 calculation is valid
int32_t heartRate;    // heart rate value
int8_t
    validHeartRate; // indicator to show if the heart rate calculation is valid
int p;
byte pulseLED = 11; // Must be on PWM pin
byte readLED = 13;  // Blinks with each data read

const int lebar = 128;                              // OLED
const int tinggi = 64;                              // OLED
const int reset = 4;                                // OLED
Adafruit_SSD1306 oled(lebar, tinggi, &Wire, reset); // OLED

void setup() {
  Serial.begin(
      115200); // initialize serial communication at 115200 bits per second:

  pinMode(pulseLED, OUTPUT);
  pinMode(readLED, OUTPUT);
  pinMode(23, OUTPUT);
  pinMode(18, OUTPUT);
  oled.begin(SSD1306_SWITCHCAPVCC, 0x3C); // OLED
  oled.clearDisplay();                    // OLED

  // Initialize sensor
  if (!particleSensor.begin(
          Wire, I2C_SPEED_FAST)) // Use default I2C port, 400kHz speed
  {
    Serial.println(F("MAX30105 was not found. Please check wiring/power."));
    while (1)
      ;
  }

  Serial.println(F("Attach sensor to finger with rubber band. Press any key to "
                   "start conversion"));
  // while (Serial.available() == 0) ; //wait until user presses a key
  //  Serial.read();

  byte ledBrightness = 60; // Options: 0=Off to 255=50mA
  byte sampleAverage = 4;  // Options: 1, 2, 4, 8, 16, 32
  byte ledMode = 2; // Options: 1 = Red only, 2 = Red + IR, 3 = Red + IR + Green
  byte sampleRate = 100; // Options: 50, 100, 200, 400, 800, 1000, 1600, 3200
  int pulseWidth = 411;  // Options: 69, 118, 215, 411
  int adcRange = 4096;   // Options: 2048, 4096, 8192, 16384

  particleSensor.setup(ledBrightness, sampleAverage, ledMode, sampleRate,
                       pulseWidth,
                       adcRange); // Configure sensor with these settings
}

void loop() {
  bufferLength =
      100; // buffer length of 100 stores 4 seconds of samples running at 25sps

  // read the first 100 samples, and determine the signal range
  for (byte i = 0; i < bufferLength; i++) {
    while (particleSensor.available() == false) // do we have new data?
      particleSensor.check();                   // Check the sensor for new data

    redBuffer[i] = particleSensor.getRed();
    irBuffer[i] = particleSensor.getIR();
    particleSensor
        .nextSample(); // We're finished with this sample so move to next sample
    Serial.print(F("red="));
    Serial.print(redBuffer[i], DEC);
    Serial.print(F(", ir="));
    Serial.println(irBuffer[i], DEC);
  }

  // calculate heart rate and SpO2 after first 100 samples (first 4 seconds of
  // samples)
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

  maxim_heart_rate_and_oxygen_saturation(irBuffer, bufferLength, redBuffer,
                                         &spo2, &validSPO2, &heartRate,
                                         &validHeartRate);

  // Continuously taking samples from MAX30102.  Heart rate and SpO2 are
  // calculated every 1 second
  while (1) {
    // dumping the first 25 sets of samples in the memory and shift the last 75
    // sets of samples to the top
    for (byte i = 25; i < 100; i++) {
      redBuffer[i - 25] = redBuffer[i];
      irBuffer[i - 25] = irBuffer[i];
    }
    Serial.println("Reading...");

    // take 25 sets of samples before calculating the heart rate.
    for (byte i = 75; i < 100; i++) {
      while (particleSensor.available() == false) // do we have new data?
        particleSensor.check(); // Check the sensor for new data

      digitalWrite(
          readLED,
          !digitalRead(readLED)); // Blink onboard LED with every data read

      redBuffer[i] = particleSensor.getRed();
      irBuffer[i] = particleSensor.getIR();
      particleSensor.nextSample(); // We're finished with this sample so move to
                                   // next sample

      // send samples and calculation result to terminal program through UART
      Serial.print(p);
      Serial.print(F("red="));
      Serial.print(redBuffer[i], DEC);
      Serial.print(F(", ir="));
      Serial.print(irBuffer[i], DEC);

      Serial.print(F(", HR="));
      Serial.print(heartRate, DEC);

      Serial.print(F(", HRvalid="));
      Serial.print(validHeartRate, DEC);

      Serial.print(F(", SPO2="));
      Serial.print(spo2, DEC);

      Serial.print(F(", SPO2Valid="));
      Serial.println(validSPO2, DEC);
      // p += 1 ;
      // long irValue =  particleSensor.getIR();

      if (particleSensor.getIR() <
          7000) { // If no finger is detected it inform the user and put the
                  // average BPM to 0 or it will be stored for the next measure
        oled.clearDisplay();
        oled.setTextSize(1);
        oled.setTextColor(WHITE);
        oled.setCursor(30, 5);
        oled.println("Please Place ");
        oled.setCursor(30, 15);
        oled.println("your finger ");
        oled.display();
        digitalWrite(23, LOW);
        digitalWrite(17, LOW);
        digitalWrite(18, LOW);
        digitalWrite(19, LOW);
      };

      if (particleSensor.getIR() > 7000 and validHeartRate == 1) {
        spo2 = redBuffer[i] / 2557;     // persamaan utk kalibrasi spo2
        heartRate = irBuffer[i] / 2381; // persamaan utk kalibrasi heeartrate
        spo2Characteristic->setValue(String(spo2).c_str());
        pulseCharacteristics->setValue(String(heartRate).c_str());
        String pulseValue = pulseCharacteristics->getValue().c_str();
        String spo2Value = spo2Characteristic->getValue().c_str();
        Serial.println(pulseValue);
        Serial.println(spo2Value);
        oled.clearDisplay();
        oled.setTextSize(1.99);
        oled.setTextColor(WHITE);
        oled.setCursor(3, 10);
        oled.print("SPO2: ");
        oled.print(spo2);
        oled.println("%");
        oled.setCursor(3, 30);
        oled.print("HR : ");
        oled.print(heartRate);
        oled.println(" BPM");
        oled.display();
        oled.clearDisplay();
        if (spo2 > 94 and spo2 < 102) {
          digitalWrite(23, HIGH);
          digitalWrite(18, LOW);
        };
        if (spo2 > 30 and spo2 < 93) {

          digitalWrite(18, HIGH);
          digitalWrite(23, LOW);
        };

        // 23 hijau, heartrate
        // 19 merah, heartrate
        // 18 hijau, spo2
        // 17 merah, spo2
      };
    }

    // After gathering 25 new samples recalculate HR and SP02
    maxim_heart_rate_and_oxygen_saturation(irBuffer, bufferLength, redBuffer,
                                           &spo2, &validSPO2, &heartRate,
                                           &validHeartRate);
  }
}
