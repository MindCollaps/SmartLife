//Modul type config
#include <Adafruit_NeoPixel.h>

//Modul config
//ESP.getChipId(); last thre chars from mac address
#include <ESP8266mDNS.h>
#include <ESP8266WiFi.h>
//#include <ESP8266WebServer.h>
#include <EEPROM.h>
#include <FS.h>
#include <time.h>
#include "ESPAsyncTCP.h"
#include "ESPAsyncWebServer.h"

String modulName = "test_modul";
String modulVersion = "1.0";

//default config values
bool standAlone = true;
bool configMode = false;
int shutdownsInRow = 0;
AsyncWebServer server(80);

//wlan config
String wlanSsid = "";
String wlanPw = "";

//Http request definition:
#define httpGetConfig "/getconfig"

#define httpSetup "/setup"
#define httpSetupVSsid "wifissid"
#define httpSetupVPw "wifipw"
//?reboot=t / f
#define httpSetupReboot "reboot"
#define httpSetupSafe "safe"

//File System
#define locationConfigJson "/config.json"

Adafruit_NeoPixel strip(60, 14, NEO_GBR + NEO_KHZ800);

void colorWipe(uint32_t color, int wait) {
  for (int i = 0; i < strip.numPixels(); i++) {
    strip.setPixelColor(i, color);
    strip.show();
    delay(wait);
  }
}

void theaterChase(uint32_t color, int wait) {
  for (int a = 0; a < 10; a++) {
    for (int b = 0; b < 3; b++) {
      strip.clear();
      for (int c = b; c < strip.numPixels(); c += 3) {
        strip.setPixelColor(c, color);
      }
      strip.show();
      delay(wait);
    }
  }
}

void rainbow(int wait) {
  for (long firstPixelHue = 0; firstPixelHue < 3 * 65536; firstPixelHue += 256) {
    for (int i = 0; i < strip.numPixels(); i++) { // For each pixel in strip...
      int pixelHue = firstPixelHue + (i * 65536L / strip.numPixels());
      strip.setPixelColor(i, strip.gamma32(strip.ColorHSV(pixelHue)));
    }
    strip.show();
    delay(wait);
  }
}

void theaterChaseRainbow(int wait) {
  int firstPixelHue = 0;
  for (int a = 0; a < 30; a++) {
    for (int b = 0; b < 3; b++) {
      strip.clear();
      for (int c = b; c < strip.numPixels(); c += 3) {
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

bool runEffect = true;

//Modul type config
void setupModul() {
  strip.begin();

  server.on("/effect", HTTP_GET, [](AsyncWebServerRequest * request) {
    request->send(200, "text/plain", "recived");
    int r = 0;
    int g = 0;
    int b = 0;
    int s = 20;
    if (request->hasParam("stop")) {
      runEffect = false;
      return;
    }

    if (request->hasParam("r")) {
      r = request->getParam("r")->value().toInt();
    }

    if (request->hasParam("g")) {
      g = request->getParam("g")->value().toInt();
    }

    if (request->hasParam("b")) {
      b = request->getParam("b")->value().toInt();
    }

    if (request->hasParam("s")) {
      s = request->getParam("s")->value().toInt();
    }

    uint32_t color = strip.Color(r, g, b);
    if (request->hasParam("effect")) {
      String argument = request->getParam("effect")->value();


      if (argument == "tchase") {
        while (runEffect) {
          theaterChaseRainbow(s);
        }
      } else if (argument == "rchase") {
        while (runEffect) {
          theaterChase(color, s);
        }
      } else if (argument == "rainbow") {
        while (runEffect) {
          rainbow(s);
        }
      } else if (argument == "colorwipe") {
        while (runEffect) {
          colorWipe(color, s);
        }
      }
    }
  });
}

//ab address 102
void eepromWrites() {

}

//ab address 102
void eepromReads() {

}

void setup() {
  Serial.begin(9600);

  Serial.println();
  Serial.print("Hello, ");
  Serial.print(modulName);
  Serial.print(" Version: ");
  Serial.print(modulVersion);
  Serial.println(" started!");
  readMemory();
  if (shutdownsInRow >= 4) {
    configMode = true;
  }

  delay(100);

  if (standAlone) {
    setupWifiConnection();
  } else {
    Serial.println("A non standalone system isn't working yet! Stand alone goes back to true!");
    standAlone = true;
    reboot(true);
  }

  if (!configMode) {
    setupModul();
  } else {
    setupWiFiAp();
  }

  setupDns();
  setupServer();

  Serial.println("Setting up SPIFFS");
  SPIFFS.begin();

  Serial.println();
  Serial.println("Esp running on: ");
  Serial.print(WiFi.localIP());
  Serial.println();
  Serial.println();
}

void loop() {
  //server.handleClient();
  MDNS.update();
}

void setupServer() {
  Serial.println("Starting server...");
  //not found
  server.onNotFound([](AsyncWebServerRequest * request) {
    request->send(404, "text/plain", "Link wurde nicht gefunden!");
    Serial.println("Server recived 404");
  });

  //Homepage
  server.on("/", [](AsyncWebServerRequest * request) {
    request->send(200, "text/plain", "Hellu");
    Serial.println("Server recived /");
  });

  server.on(httpGetConfig, [](AsyncWebServerRequest * request) {
    Serial.println("Server recived /getconfig");
    Serial.println("Server recived /%httpGetConfig");
  });

  server.on(httpSetup, HTTP_GET, [](AsyncWebServerRequest * request) {
    Serial.println("Server recived /setup");
    request->send(200, "text/plain", "recived");
    if (request->hasParam(httpSetupVSsid)) {
      wlanSsid = request->getParam(httpSetupVSsid)->value();
      Serial.print("Set Wifi ssid to ");
      Serial.println(wlanSsid);
    }

    if (request->hasParam(httpSetupVPw)) {
      wlanPw = request->getParam(httpSetupVPw)->value();
      Serial.print("Set Wifi pw to ");
      Serial.println(wlanPw);
    }

    if (request->hasParam(httpSetupSafe)) {
      writeMemory();
    }

    if (request->hasParam(httpSetupReboot)) {
      request->send(200, "text/plain", "rebooting");
      reboot(false);
    }
  });
server.begin();

Serial.println("Server online!");
}

//void sendDataFromSpiff(String path, AsyncWebServerRequest *request) {
//  //  Serial.println("Sending data from spiff");
//  //  File dataFile = SPIFFS.open(path, "r");
//  //  if (!dataFile) {
//  //    Serial.println("Config data is missing!!!!!!");
//  //  }
//  //  server.sendHeader("Content-Disposition", "attachment; filename=" + path);
//  //  server.streamFile(dataFile, "application/octet-stream");
//  //  dataFile.close();
//  //  Serial.println("Sended!");
//}

void setupDns() {
  if (MDNS.begin(modulName)) {
    Serial.println("DNS gestartet!");
  } else {
    Serial.println("DNS error!!!");
    return;
  }
  MDNS.addService("http", "tcp", 80);
}

void setupWifiConnection() {
  Serial.println("Setting up WiFi connection...");
  Serial.println(wlanSsid);
  Serial.println(wlanPw);
  if (wlanSsid == "" || wlanPw == "") {
    configMode = true;
    return;
  }
  WiFi.mode(WIFI_STA);
  WiFi.begin(wlanSsid, wlanPw);
  Serial.print("Connect to WiFi ...");
  int tried = 0;
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
    tried++;
    if (tried >= 100) {
      configMode = true;
      Serial.println("Wifi connection failed, going in config mode!");
      return;
    }
  }
  Serial.println();
  Serial.println("WiFi connected!");
}

void setupWiFiAp() {
  Serial.println("Setup Acces point!");
  WiFi.softAP(modulName);
  Serial.println("Acces point active!");
}

//WLAN SSID: address 0 - 34
//WLAN PW: address 35 - 100
//config stand alone: address 101
void readMemory() {
  EEPROM.begin(4096);
  Serial.println("Reading EEPROM...");
  Serial.print(" Reading wlanssid: ");
  wlanSsid = readStringFromEeprom(0, 34);
  Serial.print(wlanSsid);
  Serial.print(" ...Reading wlanpw: ");
  wlanPw = readStringFromEeprom(35, 100);
  Serial.print(wlanPw);
  standAlone = (bool) EEPROM.read(101);

  eepromReads();
  EEPROM.end();
  Serial.println();
}

void writeMemory() {
  EEPROM.begin(4096);
  Serial.println("Writing EEPROM");
  if (wlanSsid != "") {
    writeStringToEeprom(0, wlanSsid);
  }
  if (wlanPw != "") {
    writeStringToEeprom(35, wlanPw);
  }
  EEPROM.put(101, (byte) standAlone);
  eepromWrites();
  if (EEPROM.commit()) {
    Serial.println("EEPROM successfully committed!");
  } else {
    Serial.println("ERROR! EEPROM commit failed");
  }
  EEPROM.end();
}

void writeStringToEeprom(int startAdr, String writeString) {
  writeString = writeString + char('\0');
  int charLength = writeString.length();
  for (int i = 0; i < charLength; ++i) {
    EEPROM.put(startAdr + i, writeString[i]);
  }
}


String readStringFromEeprom(int startAdr, int maxLength) {
  String s;
  char c;
  for (int i = 0; i < maxLength; ++i) {
    EEPROM.get(startAdr + i, c);
    if (c == '\0') break;
    s += c;
  }
  return s;
}

void reboot(boolean safe) {
  Serial.println("Rebooting...");
  if (safe) {
    writeMemory();
    delay(100);
  }
  ESP.restart();
}
