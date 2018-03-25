#include <Keypad.h>

const byte numRows= 4;

const byte numCols= 4;

char keymap[numRows][numCols]= { {'0', '1', '2', '3'},

{'4', '5', '6', '7'},

{'8', '9', 'A', 'B'},

{'C', 'D', 'E', 'F'} };

byte rowPins[numRows] = {9,8,7,6}; //Rows 0 to 3

byte colPins[numCols]= {5,4,3,2}; //Columns 0 to 3

//initializes an instance of the Keypad class

Keypad myKeypad= Keypad(makeKeymap(keymap), rowPins, colPins, numRows, numCols);

int xPin = A1, yPin = A0, clickPin = 10;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

void loop() {

  char keypressed = myKeypad.getKey();

  if (keypressed != NO_KEY) {
    Serial.print("k:");
    Serial.println(keypressed);
  } else if (myKeypad.getState() == RELEASED) {
    Serial.println("r:0");
  }
  Serial.print("x:");
  Serial.println(analogRead(xPin));
  Serial.print("y:");  
  Serial.println(analogRead(yPin));
 
}
