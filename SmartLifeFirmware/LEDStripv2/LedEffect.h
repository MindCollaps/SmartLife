class LedEffect {
  public:
    typedef void (*voidFnPtr)(int numLeds, uint8_t gHue, int stripId, int brightnes, CRGB color, LedEffect* effect);
    voidFnPtr  CallBackFnPtr;
    int numLeds = 0;
    uint8_t gHue = 0; // rotating number
    bool isActive = false;
    int stripId = 0;
    int brightness = 155;
    int effectSpeed = 500;
    CRGB color = CRGB(0, 0, 255);

    void action() {
      CallBackFnPtr(numLeds, gHue, stripId, brightness, color, this);
    }

    void onAction(voidFnPtr fnPtr) {
      CallBackFnPtr = fnPtr;
    }

    void setNumLeds(int num) {
      this->numLeds = num;
    }

    uint8_t getGhue() {
      return gHue;
    }

    void setActive(bool state) {
      isActive = state;
    }

    bool getIsActive() {
      return isActive;
    }

    void setBright(int i) {
      brightness = i;
    }

    void setEffectSpeed(int i) {
      effectSpeed = i;
    }

    void setStripId(int i) {
      stripId = i;
    }

    void setColor(const struct CRGB& c) {
      color = c;
    }

    void resetHue(){
      gHue = 0;
    }

    void setHue(uint8_t i){
      gHue = i;
    }

    long lastRefreshTime = 0;

    void update() {
      if (millis() - lastRefreshTime >= effectSpeed) {
        gHue++;
        if (isActive)
          action();
        lastRefreshTime += effectSpeed;
      }
    }
};
