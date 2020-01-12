//Modul type config
#include <Adafruit_NeoPixel.h>

Adafruit_NeoPixel strip(LED_COUNT, LED_PIN, NEO_RGB + NEO_KHZ800);
void setupModulType() {
  strip.begin();

  
}

void colorWipe(uint32_t color, int wait) {
  for (int i = 0; i < strip.numPixels(); i++) {
    strip.setPixelColor(i, color);
    strip.show();
    delay(wait);
  }
}

void theaterChase(uint32_t color, int wait) {
  for(int a=0; a<10; a++) {
    for(int b=0; b<3; b++) {
      strip.clear();
      for(int c=b; c<strip.numPixels(); c += 3) {
        strip.setPixelColor(c, color);
      }
      strip.show();
      delay(wait);
    }
  }
}

void rainbow(int wait) {
  for(long firstPixelHue = 0; firstPixelHue < 3*65536; firstPixelHue += 256) {
    for(int i=0; i<strip.numPixels(); i++) { // For each pixel in strip...
      int pixelHue = firstPixelHue + (i * 65536L / strip.numPixels());
      strip.setPixelColor(i, strip.gamma32(strip.ColorHSV(pixelHue)));
    }
    strip.show();
    delay(wait);
  }
}

void theaterChaseRainbow(int wait) {
  int firstPixelHue = 0;
  for(int a=0; a<30; a++) {
    for(int b=0; b<3; b++) {
      strip.clear();
      for(int c=b; c<strip.numPixels(); c += 3) {
        int      hue   = firstPixelHue + c * 65536L / strip.numPixels();
        uint32_t color = strip.gamma32(strip.ColorHSV(hue));
        strip.setPixelColor(c, color);
      }
      strip.show();
      delay(wait);
      firstPixelHue += 65536 / 90;
    }
  }
}

//Modul config
//ESP.getChipId(); last thre chars from mac address
#include <ESP8266mDNS.h>
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>

String modulName = "ESP-" + ESP.getChipId();
String modulVersion = "1.0";

//default config values
bool standAlone = false;
bool configMode = false;
int shutdownsInRow = 0;
ESP8266WebServer server(80);

//wlan config
String wlanSsid = "";
String wlanPw = "";

void setup() {
  Serial.begin(115200);

  Serial.print(modulName);
  Serial.print(" Version: ");
  Serial.print(modulVersion);
  Serial.println(" started!");

  EEPROM.begin(4096);
  readMemory();

  setupWifiConnection();
  setupServer();
  setupDns();
  setupModulType();
}

void loop() {
  server.handleClient();

}

void setupServer() {

  //not found
  server.onNotFound([]() {
    server.send(404, "text/plain", "Link wurde nicht gefunden!");
  });

  //Homepage
  server.on("/", []() {
    server.send(200, "text/plain", "ESP-Homepage");
  });

  server.begin();
}

void setupDns() {
  if (MDNS.begin(modulName)) {
    Serial.print("DNS gestartet!");
  }
}

void setupWifiConnection() {

}

byte makeBoolByte(bool b[7]) {
  byte by;
  for (int i = 0; i < 7; i++) {
    by.bitSet(b[i], i);
  }
  return by;
}


//WLAN SSID: address 0 - 34
//WLAN PW: address 35 - 100
//config stand alone: address 101
void readMemory() {
  wlanSsid = readStringFromEeprom(0, 34);
  wlanPw = readStringFromEeprom(35, 100);
  standAlone = (bool) EEPROM.read(101);
}

void writeMemory() {
  if (wlanSsid != "") {
    writeStringToEeprom(0, wlanSsid);
  }

  if (wlanPw != "") {
    writeStringToEeprom(35, wlanPw);
  }

  EEPROM.write(101, (byte) standAlone);

  if (EEPROM.commit()) {
    Serial.println("EEPROM successfully committed!");
  } else {
    Serial.println("ERROR! EEPROM commit failed");
  }
}

void writeStringToEeprom(int startAdr, String writeString) {
  int charLength = writeString.length();
  for (int i = 0; i < charLength; ++i) {
    EEPROM.write(startAdr + i, writeString[i]);
  }
}


String readStringFromEeprom(int startAdr, int maxLength) {
  String s;
  for (int i = 0; i < maxLength; ++i) {
    s += char(EEPROM.read(startAdr + i));
    if (s[i] = '\0') break; //break when end of sting is reached before maxLength
  }
  return s;
}
