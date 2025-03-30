/*
 * PixelBlade.cpp
 *
 *  Created on: Nov 29, 2018
 *      Author: JakeSoft
 */

#include <Arduino.h>
#include "blade/PixelBlade.h"

PixelBlade::PixelBlade(int aBladeLength, NRPixelStrip* apPixelStrip, int aPowerPin1, int aPowerPin2, int aPowerPin3) :
	mBladeLength(aBladeLength),
	mTipIndex(0),
	mLastFlickerUpdate(0)
{
	if(nullptr == apPixelStrip)
	{
		mpPixelStrip = new NRPixelStrip();
	}
	else
	{
		mpPixelStrip = apPixelStrip;
	}

	mLastFlickerUpdate = 0;
	mSineFlickerStage = pi;
	mLastFlickerEffect = 0;
	mIsNewEffect = true;
	mPowerupEffect = 0;
	mPowerdownEffect = 0;
	mFlickerStage = 0;
	mEffectFramePeriod = 25;
	maPowerPins[0] = aPowerPin1;
	maPowerPins[1] = aPowerPin2;
	maPowerPins[2] = aPowerPin3;
}

PixelBlade::~PixelBlade()
{
	//Do nothing
}

void PixelBlade::Init()
{
	mpPixelStrip->Init();

	//Set up the power pins if defined (negative values are ignored)
	for(int lnIdx = 0; lnIdx < 3; lnIdx++)
	{
		if(maPowerPins[lnIdx] > 0)
		{
			pinMode(maPowerPins[lnIdx], OUTPUT);
		}
	}

	SetPowerEnable(false); //Turn off power so we don't waste energy
}

bool PixelBlade::PowerUp(int aRampTime)
{
	SetPowerEnable(true);

	float lnPixelsPerMs = (float)mBladeLength / (float)aRampTime;

	unsigned long lnStartTime = millis();
	while(mTipIndex < mBladeLength-1)
	{
		ApplyFlicker(mPowerupEffect);

		unsigned long lTimeDelta = millis() - lnStartTime; //How much total time has passed
		float lTipFloat = (float)lTimeDelta * lnPixelsPerMs; //Where the tip should be at this time
		mTipIndex = (int)(lTipFloat + 0.5);
		if(mTipIndex > mBladeLength-1) //Compensate for overshoot
		{
			mTipIndex = mBladeLength-1;
		}
	}

	return true;
}

bool PixelBlade::PowerDown(int aRampTime)
{
	float lnPixelsPerMs = (float)mBladeLength / (float)aRampTime;

	unsigned long lnStartTime = millis();
	while(mTipIndex > 0)
	{
		ApplyFlicker(mPowerdownEffect);

		//Figure out how much total time has passed
		unsigned long lTimeDelta = millis() - lnStartTime;
		//Figure out where the tip should be at this time
		float lTipFloat = ((float)mBladeLength-1.0) - ( (float)lTimeDelta * lnPixelsPerMs);
		mTipIndex = (int)(lTipFloat + 0.5);
		if(mTipIndex < 0) //Compensate for overshoot
		{
			mTipIndex = 0;
		}
	}

	Off();

	return true;
}

void PixelBlade::ApplyFlicker(int aType)
{
	//Don't do anything if tip is at zero (blade is off)
	if(0 == mTipIndex)
	{
		return;
	}

	//Check if flicker type changed
	if(mLastFlickerEffect != aType)
	{
		mIsNewEffect = true;
		mLastFlickerEffect = aType;
	}
	else
	{
		mIsNewEffect = false;
	}

	//Select flicker style
	switch(aType)
	{
	case 0:
		NoFlicker();
		break;
	case 1: //Random flicker
		RandomFlicker(80, 100, 20);
		break;
	case 2:
		RandomFlicker(60, 100, 20);
		break;
	case 3:
		RandomFlicker(50, 100, 50);
		break;
	case 4:
		SineFlicker(0.6, 1);
		break;
	case 5:
		SineFlicker(0.6, 10);
		break;
	case 6:
		SineFlicker(0.5, 25);
		break;
	case 7:
		PulseFlicker(40, 10);
		break;
	case 8:
		PulseFlicker(20, 10);
		break;
	case 9:
		PulseFlicker(10, 10);
		break;
	default:
		//Do nothing
		break;
	}
}

void PixelBlade::SetChannel(unsigned char aLevel, int aChannel)
{
	switch(aChannel)
	{
	case 0:
		mMainColor.mCh1 = aLevel;
		break;
	case 1:
		mMainColor.mCh2 = aLevel;
		break;
	case 2:
		mMainColor.mCh3 = aLevel;
		break;
	case 3:
		mEffectColor.mCh1 = aLevel;
		break;
	case 4:
		mEffectColor.mCh2 = aLevel;
		break;
	case 5:
		mEffectColor.mCh3 = aLevel;
		break;
	default:
		//Do nothing
		break;
	}
}

void PixelBlade::PerformIO()
{
	//Send the frame to the blade pixel-by-pixel
	ApplyFlicker(mLastFlickerEffect);
}

void PixelBlade::Off()
{
	mTipIndex = 0;
	SendPixels(0, 0, 0, mBladeLength);
	ShowFrame();

	SetPowerEnable(false);
}

void PixelBlade::On()
{
	SetPowerEnable(true);

	mTipIndex = mBladeLength-1;

	for(int lIdx=0; lIdx < mBladeLength; lIdx++)
	{
		SendPixel(mMainColor.mCh1,
				  mMainColor.mCh2,
				  mMainColor.mCh3);
	}

	ShowFrame();
}

BladeMetadata PixelBlade::GetFeatures()
{
	BladeMetadata lData;

	lData.Channels = 3;
	lData.Flickers = 10;

	return lData;
}

void PixelBlade::NoFlicker()
{
	//Send lighted pixels
	SendPixels(mMainColor.mCh1,
			mMainColor.mCh2,
			mMainColor.mCh3,
			mTipIndex+1);
	//Send off pixels to finish out the frame
	SendPixels(0,0,0, (mBladeLength - (mTipIndex + 1)) );
	ShowFrame();
}

void PixelBlade::RandomFlicker(int aLowerBound, int aUpperBound, unsigned long aUpdatePeriod)
{

	//Compensate for values too high
	if(aLowerBound > 100)
	{
		aLowerBound = 100;
	}
	if(aUpperBound > 100)
	{
		aUpperBound = 100;
	}

	//--- Update frame parameters if it is time ---
	if(millis() - mLastFlickerUpdate > aUpdatePeriod || mIsNewEffect)
	{
		//Capture update time
		mLastFlickerUpdate = millis();

		//Generate a random number between lower and upper bounds
		int lRandomModifier = random(aLowerBound, aUpperBound);
		//Convert the integer value to a floating point number
		float lRandomMultiplier = ((float)lRandomModifier)/100;

		//Calculate randomized values while maintaining color channel ratios
		float lTempPowerLevel1 = (float)mMainColor.mCh1 * lRandomMultiplier;
		float lTempPowerLevel2 = (float)mMainColor.mCh2 * lRandomMultiplier;
		float lTempPowerLevel3 = (float)mMainColor.mCh3 * lRandomMultiplier;

		//Store randomized values
		mEffectColor.mCh1 = (uint8_t)lTempPowerLevel1;
		mEffectColor.mCh2 = (uint8_t)lTempPowerLevel2;
		mEffectColor.mCh3 = (uint8_t)lTempPowerLevel3;
	}

	//--- Send the frame ---
	//Do I/O to make flicker take effect
	int lPxlIndex = 0;
	for(lPxlIndex = 0; lPxlIndex <= mTipIndex; lPxlIndex++)
	{

		SendPixel(mEffectColor.mCh1,
				mEffectColor.mCh2,
				mEffectColor.mCh3);
	}
	//Send blank pixels beyond the tip to finish out the frame
	SendPixels(0, 0, 0, mBladeLength - lPxlIndex+1);

	ShowFrame();

}

void  PixelBlade::SineFlicker(float aAmplitude, unsigned long aUpdatePeroid)
{
	//Compensate for values too high
	if(aAmplitude > 100)
	{
		aAmplitude = 100;
	}

	//--- Update frame parameters if it is time ---
	if(millis() - mLastFlickerUpdate > aUpdatePeroid)
	{
		mLastFlickerUpdate = millis();
		mSineFlickerStage += 0.05;
		if(mSineFlickerStage > 2 * pi)
		{
			mSineFlickerStage = pi;
		}
	}

	//--- Now calculate and send the frame ---
	float lModifier = sin(mSineFlickerStage) * (1.0 - aAmplitude);

	//Calculate new power levels
	float lTempPowerLevel1 = (float)mMainColor.mCh1 + (lModifier * (float)mMainColor.mCh1);
	float lTempPowerLevel2 = (float)mMainColor.mCh2 + (lModifier * (float)mMainColor.mCh2);
	float lTempPowerLevel3 = (float)mMainColor.mCh3 + (lModifier * (float)mMainColor.mCh3);

	//Compensate for overshoot
	if(lTempPowerLevel1 > 255)
	{
		lTempPowerLevel1 = 255;
	}
	if(lTempPowerLevel2 > 255)
	{
		lTempPowerLevel2 = 255;
	}
	if(lTempPowerLevel3 > 255)
	{
		lTempPowerLevel3 = 255;
	}

	//Do I/O to make flicker take effect
	int lnPxl = 0;
	for(lnPxl = 0; lnPxl <= mTipIndex; lnPxl++)
	{
		SendPixel((uint8_t)lTempPowerLevel1,
				(uint8_t)lTempPowerLevel2,
				(uint8_t)lTempPowerLevel3);
	}
	//Blank out pixels that are beyond the tip
	SendPixels(0, 0, 0, mBladeLength - lnPxl);
	ShowFrame();
}

void PixelBlade::PulseFlicker(unsigned long aUpdatePeriod, int aPatternSize)
{

	if(millis() - mLastFlickerUpdate > aUpdatePeriod)
	{
		mLastFlickerUpdate = millis();

		mFlickerStage--;
		if(mFlickerStage <= 0)
		{
			mFlickerStage = aPatternSize;
		}
	}

	SendPulseFlickerFrame(aPatternSize);
}

void PixelBlade::SendPulseFlickerFrame(int aPatternSize)
{
	int lPatternSize = aPatternSize;

	int lPulsePixelPosition = mFlickerStage;
	int lNormPixelsBefore = lPatternSize - lPulsePixelPosition;
	int lNormPixelsAfter = lPatternSize - lNormPixelsBefore - 1; //Pixels already sent minus the pulse pixel

	//Repeat pattern until blade tip is reached
	int lnPxlIdx = 0;
	while(lnPxlIdx <= mTipIndex)
	{
		//Send pixels before pulse
		for(int lnNormPxl = 0; lnNormPxl < lNormPixelsBefore && lnPxlIdx <= mTipIndex; lnNormPxl++)
		{
			SendPixel(mMainColor.mCh1,
								    mMainColor.mCh2,
								    mMainColor.mCh3);
			lnPxlIdx++;
		}

		//Send pulse pixel
		if(lnPxlIdx <= mTipIndex)
		{
			SendPixel(mEffectColor.mCh1,
			                        mEffectColor.mCh2,
									mEffectColor.mCh3);
			lnPxlIdx++;
		}

		//Send pixels after pulse
		for(int lnNormPxl = 0; lnNormPxl < lNormPixelsAfter && lnPxlIdx <= mTipIndex; lnNormPxl++)
		{
			SendPixel(mMainColor.mCh1,
								    mMainColor.mCh2,
								    mMainColor.mCh3);
			lnPxlIdx++;
		}
	}
	//Send blank pixels until end of blade is reached to finish out the frame
	SendPixels(0, 0, 0, mBladeLength - lnPxlIdx+1);

	//Latch the frame
	ShowFrame();
}

void PixelBlade::SetPowerUpEffect(int aEffectIdx)
{
	mPowerupEffect = aEffectIdx;
}

void PixelBlade::SetPowerDownEffect(int aEffectIdx)
{
	mPowerdownEffect = aEffectIdx;
}

void PixelBlade::SetEffectColor(uint8_t aCh1, uint8_t aCh2, uint8_t aCh3)
{
	mEffectColor.mCh1 = aCh1;
	mEffectColor.mCh2 = aCh2;
	mEffectColor.mCh3 = aCh3;
}

void PixelBlade::SetEffectAnimationPeriod(unsigned long anFrameTime)
{
	mEffectFramePeriod = anFrameTime;
}

void PixelBlade::ApplyBlasterEffect(int aEffect, unsigned int aEffectTime)
{
	int lBlastCenterIdx = random(0+3, mTipIndex-3);
	int lBlastSize = random(2, 5);

	int lIdx = 0;
	while(lIdx < mTipIndex)
	{
		if(lIdx == lBlastCenterIdx)
		{
			SendPixels(mEffectColor.mCh1, mEffectColor.mCh2, mEffectColor.mCh3, lBlastSize);
			lIdx += lBlastSize;
		}
		else
		{
			SendPixel(mMainColor.mCh1, mMainColor.mCh2, mMainColor.mCh3);
			lIdx++;
		}
	}

	ShowFrame();
	delay(aEffectTime); //Show the frame for this amount of time
}

//tEffectsBladeProperties PixelBlade::GetEffectsBladeProperties()
//{
//	tEffectsBladeProperties lProps;
//
//	lProps.PowerdownEffects = GetFeatures().Flickers;
//	lProps.PowerupEffects = GetFeatures().Flickers;
//	lProps.BlasterEffects = 1;
//
//	return lProps;
//}

void PixelBlade::SendPixel(uint8_t aRed, uint8_t aGreen, uint8_t aBlue)
{
	cli();//Turn off interrupts
	mpPixelStrip->SendPixel(aRed, aGreen, aBlue);
	sei(); //Turn on interrupts
}

void PixelBlade::SendPixels(uint8_t aRed, uint8_t aGreen, uint8_t aBlue, int aNumPixels)
{
	for(int lPxlIdx = 0; lPxlIdx < aNumPixels; lPxlIdx++)
	{
		SendPixel(aRed, aGreen, aBlue);
	}
}

void PixelBlade::ShowFrame()
{
	mpPixelStrip->Show(); //Latch the frame
}

void  PixelBlade::SetPowerEnable(bool aEnable)
{
	for(int lnIdx = 0; lnIdx < 3; lnIdx++)
	{
		if(maPowerPins[lnIdx] > 0)
		{
			if(aEnable)
			{
				//Turn ON power pins
				digitalWrite(maPowerPins[lnIdx], HIGH);
			}
			else
			{
				//Turn OFF power pins
				digitalWrite(maPowerPins[lnIdx], LOW);
			}
		}
	}
}

void PixelBlade::SetBladeLength(int aLength)
{
	mBladeLength = aLength;
}
