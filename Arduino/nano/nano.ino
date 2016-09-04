#include <Servo.h>
#include <SoftwareSerial.h>
char val;
int v;
static const int movement[] = {4, 5, 6, 7};
static const int servo1 = 8;
static const int servo2 = 9;
static const int servo3 = 10;


static const uint8_t analog_pins[] = {A0, A1, A2, A3, A4, A5, A6, A7};
String states = "";


SoftwareSerial mySerial(2, 3); // RX, TX

void setup()
{
  // Open serial communications and wait for port to open:
  // set the data rate for the SoftwareSerial port
  mySerial.begin(9600);
  mySerial.println("Hello, world?");
  for (int i = 0; i <= sizeof(movement); i++) {
    pinMode(movement[i], OUTPUT);
  }
}

void loop() // run over and over
{
  delay(500);
  states = "";
  for (int i = 0; i <= sizeof(analog_pins); i++) {
    states += digitalRead(analog_pins[i]);
  }
  mySerial.println(states + "#");

  if (mySerial.available()) {
    

    move(val);
  } else {
    val = -1;
  }
}

int readCommand(SoftwareSerial mySerial){
  char val;
  boolean goon = true;
  while(mySerial.available() && goon){
    val = mySerial.read();
    
  }

  return 0;
}


void move(char val) {
  switch (val) {
    case 'a':    // your hand is on the sensor
      digitalWrite(movement[0], HIGH);
      digitalWrite(movement[2], HIGH);

      break;
    case 'c':    // your hand is close to the sensor
      digitalWrite(movement[0], HIGH);
      digitalWrite(movement[3], HIGH);

      break;
    case 'b':    // your hand is a few inches from the sensor
      digitalWrite(movement[1], HIGH);
      digitalWrite(movement[3], HIGH);

      break;
    case 'd':    // your hand is nowhere near the sensor

      digitalWrite(movement[1], HIGH);
      digitalWrite(movement[2], HIGH);
      break;
  }
}


