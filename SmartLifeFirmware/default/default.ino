//Modul type config
void setupModul() {

}

//ab address 102
void eepromWrites() {

}

//ab address 102
void eepromReads() {

}

//Modul config
//ESP.getChipId(); last thre chars from mac address
#include <ESP8266mDNS.h>
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>
#include <FS.h>

String modulName = "SL" + ESP.getChipId();
String modulVersion = "1.0";

//default config values
bool standAlone = true;
bool configMode = false;
int shutdownsInRow = 0;
ESP8266WebServer server(80);

//wlan config
String wlanSsid = "";
String wlanPw = "";

//Http request definition:
#define httpGetConfig "/getconfig"

#define httpSetup "/setup/"
#define httpSetupVSsid "wifissid"
#define httpSetupVPw "wifipw"

//File System
#define locationConfigJson "config.json"

void setup() {
  Serial.begin(115200);

  Serial.print(modulName);
  Serial.print(" Version: ");
  Serial.print(modulVersion);
  Serial.println(" started!");

  EEPROM.begin(4096);
  readMemory();
  SPIFFS.begin();
  delay(1000);
  if (shutdownsInRow >= 4) {
    configMode = true;
  }

  if (standAlone) {
    setupWifiConnection();
  } else {
    Serial.println("A non standalone system isn't working yet! Stand alone goes back to true!");
    standAlone = true;
    Serial.println("rebooting!!!");
    writeMemory();
    delay(1000);
    ESP.restart();
  }

  if (!configMode) {
    setupModul();
  } else {
    setupWiFiAp();
  }

  setupServer();
  setupDns();
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
    server.send(200, "text/plain", "Hellu");
  });

  server.on(httpGetConfig, []() {
    sendDataFromSpiff(locationConfigJson);
  });

  server.on(httpSetup, HTTP_GET, handleSetup);

  server.begin();
}

void handleSetup() {
  if (server.hasArg(httpSetupVSsid)) {
    wlanSsid = server.arg(httpSetupVSsid);
    Serial.print("Set Wifi ssid to ");
    Serial.println(wlanSsid);
  }

  if (server.hasArg(httpSetupVPw)) {
    wlanPw = server.arg(httpSetupVPw);
    Serial.print("Set Wifi pw to ");
    Serial.println(wlanPw);
  }
}

void sendDataFromSpiff(String path) {
  Serial.println("Sending data from spiff");
  File dataFile = SPIFFS.open(path.c_str(), "r");
  if (server.streamFile(dataFile, "application/octet-stream") != dataFile.size()) {
  }

  dataFile.close();
  Serial.println("Sended!");
}

void setupDns() {
  if (MDNS.begin(modulName)) {
    Serial.print("DNS gestartet!");
  }
}

void setupWifiConnection() {
  if (wlanSsid == "" || wlanPw == "") {
    configMode = true;
    return;
  }
  WiFi.begin(wlanSsid, wlanPw);
  Serial.println("Connect to WiFi ...");
  int tried = 0;
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
    tried++;
    if (tried >= 200) {
      configMode = true;
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
  Serial.println("Reading EEPROM");
  wlanSsid = readStringFromEeprom(0, 34);
  wlanPw = readStringFromEeprom(35, 100);
  standAlone = (bool) EEPROM.read(101);

  eepromReads();
}

void writeMemory() {
  Serial.println("Writing EEPROM");
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

  eepromWrites();
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
