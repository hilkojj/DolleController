

int xPin = A1, yPin = A0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

void loop() {

  Serial.print("x:");
  Serial.println(analogRead(xPin));
  Serial.print("y:");  
  Serial.println(analogRead(yPin));
 
}
