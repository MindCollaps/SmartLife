 #include "FastLED.h"
class LedStrip {
  private:
  void setupStripServer(){
    Serial.println(String("Setup " + stripName + " setting up variables"));
    server->on(String("/" + stripName), std::bind(&LedStrip::handleRoot, this));
    server->on(String("/" + stripName + "/fillsolid"), HTTP_GET, std::bind(&LedStrip::handleFillSolid, this));

    server->on(String("/" + stripName + "/effect"), HTTP_GET, std::bind(&LedStrip::handleEffects, this));
  }

  void handleRoot(){
    server->send(200, "text/plain", String("led strip: " + stripName));
  }

  void handleFillSolid(){
    server->send(200, "text/plain", "recived");
    getBrightnessFromServer();
    getRGBFromServer();
    fill_solid(strip, numLeds, color);
    FastLED[stripId].showLeds(brightness);
  }

  void getBrightnessFromServer() {
  if (server->hasArg("brightness")) {
    brightness = server->arg("brightness").toInt();
    if (brightness > 255)
      brightness = 255;
    else if (brightness < 0)
      brightness = 0;
  }
}

void getRGBFromServer() {
  int r = 0;
  int g = 0;
  int b = 0;
  bool setColor = false;
  if (server->hasArg("r")) {
    r = server->arg("r").toInt();
    setColor = true;
  }

  if (server->hasArg("g")) {
    g = server->arg("g").toInt();
    setColor = true;
  }

  if (server->hasArg("b")) {
    b = server->arg("b").toInt();
    setColor = true;
  }
  if (setColor) {
    CRGB c(r, g, b);
    this->color = c;
  }
}

void handleEffects() {
  server->send(200, "text/plain", "recived");
  Serial.println("called effects");
  //getting parmams
  String effect = "";
  if (server->hasArg("effect")) {
    effect = server->arg("effect");
  }
  if (server->hasArg("speed")) {
   ledSpeed = server->arg("speed").toInt();
  }

  //execute effect
  if (effect == "rainbow") {
    Serial.println("called effects: rainbow");
    fill_rainbow(strip, numLeds, gHue, 5);
    FastLED[stripId].showLeds(brightness);
  }
}

void loop(){
  EVERY_N_MILLISECONDS(ledSpeed) {
    gHue++;
  }
} 
  public:
  ESP8266WebServer* server;
  //ledStrip
  CRGB* strip;
  int numLeds;
  String stripName;
  int stripId;

  //ledConfigs
  CRGB color;
  int brightness = 155;

  //ledEffectConfigs
  uint8_t gHue = 0; // rotating "base color"
  int ledSpeed = 40;

  LedStrip(int numLeds, int stripId, ESP8266WebServer* server, CRGB* strip, String stripName){
    this->server = server;
    this->numLeds = numLeds;
    this->stripId = stripId;
    this->strip = strip;
    this->stripName = stripName;
    color = (40, 0, 255);
    fill_solid(strip, numLeds, color);
    FastLED[stripId].showLeds(brightness);
    setupStripServer();
  }

  void setColor(CRGB color){
    fill_solid(strip, numLeds, color);
    this->color = color;
  }

  void setColor(int r, int g, int b){
    if (r > 255)
      r = 255;
    else if (r < 0)
      r = 0;

      if (g > 255)
      g = 255;
    else if (g < 0)
      g = 0;

      if (b > 255)
      b = 255;
    else if (b < 0)
      b = 0;
      
    CRGB color(r, g, b);
    fill_solid(strip, numLeds, color);
    this->color = color;
  }
};
