#include <Servo.h>
#include <SoftwareSerial.h>
char val;
int v;
static const int outputs[] = {4, 5, 6, 7, 8, 9, 10, 11};
static const uint8_t analog_pins[] = {A0, A1, A2, A3, A4, A5, A6, A7};
String states = "";


SoftwareSerial mySerial(2, 3); // RX, TX

void setup()
{
  // Open serial communications and wait for port to open:
  // set the data rate for the SoftwareSerial port
  mySerial.begin(9600);
  mySerial.println("Hello, world?");
  for (int i = 0; i <= sizeof(outputs); i++) {
    pinMode(outputs[i], OUTPUT);
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
    val = mySerial.read();
    mySerial.println(val - 65);
    open(val);
  } else {
    val = -1;
  }
}

void open(char val){
  digitalWrite(outputs[val-65], HIGH);
  delay(500);
  digitalWrite(outputs[val-65], LOW);
}
