class LedEffect {
  public:
  typedef void (*voidFnPtr)(CRGB strip);
  voidFnPtr  CallBackFnPtr;
  
  LedEffect();
  CRGB strip;

  void action(){
    CallBackFnPtr(strip);
  }

  void onAction(voidFnPtr fnPtr){
    CallBackFnPtr = fnPtr;
  }

  void setStrip(CRGB strip){
    this->strip = strip;
  }
};
