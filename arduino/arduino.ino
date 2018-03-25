#include <Keypad.h>

const byte numRows= 4;

const byte numCols= 4;

String keymap[numRows][numCols]= { {"0ghfghfg", "1", "2", "3"},

{"4", "5", "6", "7"},

{"8", "9", "A", "B"},

{"C", "D", "E", "F"} };

byte rowPins[numRows] = {9,8,7,6}; //Rows 0 to 3

byte colPins[numCols]= {5,4,3,2}; //Columns 0 to 3

//initializes an instance of the Keypad class

Keypad myKeypad= Keypad(makeKeymap(keymap), rowPins, colPins, numRows, numCols);

int xPin = A1, yPin = A0, clickPin = 10;

void setup() {
  Serial.begin(9600);
}

void loop() {

//  char keypressed = myKeypad.getKey();
//
//  if (keypressed != NO_KEY) {
//    Serial.print("k:");
//    Serial.println(keypressed);
//  } else if (myKeypad.getState() == RELEASED) {
//    Serial.println("r:0");
//  }
  Serial.print("I.X_LEFT=");
  Serial.println(analogRead(xPin));
  Serial.print("I.Y_LEFT=");  
  Serial.println(analogRead(yPin));
 
}
