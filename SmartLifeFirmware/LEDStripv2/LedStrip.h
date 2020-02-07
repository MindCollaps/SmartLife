 #include "FastLED.h"
 #include "LedEffect.h"
class LedStrip {
  private:
  bool ledUpdate = false;
  void setupStripServer(){
    Serial.println(String("Setup " + stripName + " setting up variables"));
    server->on(String("/" + stripName), std::bind(&LedStrip::handleRoot, this));
    server->on(String("/" + stripName + "/fillsolid"), HTTP_GET, std::bind(&LedStrip::handleFillSolid, this));

    server->on(String("/" + stripName + "/effect"), HTTP_GET, std::bind(&LedStrip::handleEffects, this));
  }

  void handleRoot(){
    server->send(200, "text/plain", "This is a device");
  }

  void handleFillSolid(){
    CRGB strip[numLeds];
    server->send(200, "text/plain", "recived");
    getBrightnessFromServer();
    getRGBFromServer();
    runningEffect.setActive(false);
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
  CRGB strip[numLeds];
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
  getBrightnessFromServer();

  //execute effect
  if (effect == "rainbow") {
    Serial.println("called effects: rainbow");
    updateEffect(rainbow);
  }
}

void updateEffect(LedEffect effect){
  runningEffect.setActive(false);
  runningEffect = effect;
  runningEffect.setEffectSpeed(ledSpeed);
  runningEffect.setBright(255);
  runningEffect.setActive(true);
}
  public:
  ESP8266WebServer* server;
  //ledStrip
  int numLeds;
  String stripName;
  int stripId;

  //ledConfigs
  CRGB color;
  int brightness = 155;

  //ledEffectConfigs
  int ledSpeed = 50;
  LedEffect rainbow;
  LedEffect runningEffect;

  LedStrip(int numLeds, int stripId, ESP8266WebServer* server, String stripName){
    CRGB strip[numLeds];
    this->server = server;
    this->numLeds = numLeds;
    this->stripId = stripId;
    this->stripName = stripName;
    this->color = (40, 0, 255);
    setupEffects();
    setupStripServer();
    fill_solid(strip, numLeds, color);
    FastLED[stripId].showLeds();
  }

  void setupEffects(){
    CRGB strip[numLeds];
    rainbow.setNumLeds(numLeds);
    rainbow.onAction([](int numLeds, uint8_t gHue, int stripId, int brightness){
      CRGB strip[numLeds];

      CHSV hsv;
    hsv.hue = gHue;
    hsv.val = 255;
    hsv.sat = 240;
       for( int i = 0; i < numLeds; i++) {
        strip[i] = hsv;
        hsv.hue += 5;
    }
      FastLED[stripId].showLeds();
    });
  }

  void setColor(CRGB color){
    CRGB strip[numLeds];
    fill_solid(strip, numLeds, color);
    this->color = color;
  }

  void setColor(int r, int g, int b){
    CRGB strip[numLeds];
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

  void update(){
    runningEffect.update();
  }
};
