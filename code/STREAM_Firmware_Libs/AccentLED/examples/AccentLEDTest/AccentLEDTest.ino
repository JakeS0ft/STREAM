#include "AccentLED.h"

#include "Arduino.h"

IAccentLED* gpAccent1;
IAccentLED* gpAccent2;
IAccentLED* gpAccent3;

unsigned long gStartTime;

void PerformAccentIO(unsigned long aTime)
{
	gStartTime = millis();

	while(millis() - gStartTime < aTime)
	{
		gpAccent1->PerformIO();
		gpAccent2->PerformIO();
		gpAccent3->PerformIO();
	}

}

void SetAccentStyle(teAccentStyle aeStyle)
{
	gpAccent1->SetStyle(aeStyle);
    gpAccent2->SetStyle(aeStyle);
	gpAccent3->SetStyle(aeStyle);
}

//The setup function is called once at startup of the sketch
void setup()
{
	Serial.begin(9600);
}

// The loop function is called in an endless loop
void loop()
{
	gpAccent1 = new PwmAccentLED(11);
	gpAccent2 = new AccentLEDStub();
	gpAccent3 = new DigitalAccentLED(6);

	gpAccent1->Init();
	gpAccent2->Init();
	gpAccent3->Init();

	Serial.println("Accent style eeAccentEvenPulse");
	SetAccentStyle(teAccentStyle::eeAccentEvenPulse);
	PerformAccentIO(10000);

	Serial.println("Accent style eeAccentIdleBlink");
	SetAccentStyle(teAccentStyle::eeAccentIdleBlink);
	PerformAccentIO(10000);

	Serial.println("Accent style eeTwoStagePulse");
	SetAccentStyle(teAccentStyle::eeAccentTwoStagePulse);
	PerformAccentIO(10000);

	Serial.println("Accent style eeOff");
	SetAccentStyle(teAccentStyle::eeAccentOff);
	PerformAccentIO(10000);

	Serial.println("Accent style eeOn");
	SetAccentStyle(teAccentStyle::eeAccentOn);
	PerformAccentIO(10000);

	delete gpAccent1;
	delete gpAccent2;
	delete gpAccent3;

	delay (1000);

}
