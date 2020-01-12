//Modul type config
void setupModulType(){
  
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
