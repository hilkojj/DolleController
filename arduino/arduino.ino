
// left joystick
int xPinLeft = A1, yPinLeft = A0, clickPinLeft = 10;

int left = 13, up = 12, right = 11;

void setup() {
  Serial.begin(9600);
}

void loop() {

  Serial.print("I.LEFT_JOYSTICK=");
  Serial.print(1024 - analogRead(xPinLeft));
  Serial.print(",");
  Serial.println(analogRead(yPinLeft));

  Serial.print("I.LEFT=");
  Serial.println(digitalRead(left));

  Serial.print("I.UP=");
  Serial.println(digitalRead(up));

  Serial.print("I.RIGHT=");
  Serial.println(digitalRead(right));
 
}
