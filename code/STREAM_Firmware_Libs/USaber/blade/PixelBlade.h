/*
 * PixelBlade.h
 *
 *  Created on: Nov 29, 2018
 *      Author: JakeSoft
 */

#ifndef PIXELBLADE_H_
#define PIXELBLADE_H_

#include <Arduino.h>
#include "IEffectsBlade.h"
#include "IBladeManager.h"
#include "../support/NRPixelStrip/NRPixelStrip.h"

#define MAX_EFFECT_PIXELS 32
#define MIN_FRAME_TIME 10UL

struct tPixel
{
	uint8_t mCh1;
	uint8_t mCh2;
	uint8_t mCh3;
};

class PixelBlade : public IEffectsBlade, public IBladeManager
{
public:
	/**
	 * Constructor.
	 * Args:
	 *   aBladeLenth - Size of frame buffer array
	 *   apPixelStrip - (optional) Pixel strip object to use to communicate with the blade
	 *   aPowerPin1 - (optional) Set the power pin 1 used to cut power when blade is off
	 *   aPowerPin2 - (optional) Set the power pin 2 used to cut power when blade is off
	 *   aPowerPin3 - (optional) Set the power pin 3 used to cut power when blade is off
	 */
	PixelBlade(int aBladeLenth, NRPixelStrip* apPixelStrip, int aPowerPin1 = -1, int aPowerPin2 = -1, int aPowerPin3 = -1);

	//Destructor
	virtual ~PixelBlade();

//Implement the IBladeManger interface
	virtual void Init();
	virtual bool PowerUp(int aRampTime);
	virtual bool PowerDown(int aRampTime);
	virtual void ApplyFlicker(int aType);
	virtual void SetChannel(unsigned char aLevel, int aChannel);
	virtual void PerformIO();
	virtual void Off();
	virtual void On();
	virtual BladeMetadata GetFeatures();

//Implement the IEffectsBlade interface
	virtual void SetPowerUpEffect(int aEffectIdx);
	virtual void SetPowerDownEffect(int aEffectIdx);
	virtual void SetEffectColor(uint8_t aCh1, uint8_t aCh2, uint8_t aCh3);
	virtual void SetEffectAnimationPeriod(unsigned long anFrameTime);
	void ApplyBlasterEffect(int aEffect, unsigned int aEffectTime);
	void SetBladeLength(int aLength);

protected:

	/**
	 * Applies the no-flicker effect
	 * (just fills base to blade tip with main color)
	 */
	void NoFlicker();

	/**
	 * Applies random flicker while keeping color balance.
	 *  Args:
	 *   aLowerBound - Lowest number as a percentage of set point (100 = full on, 0 = full off)
	 *   aUpperBound - Highest number as a percentage of set point (100 = full on, 0 = full off)
	 *   aUpdatePeroid - How often (in milliseconds) to apply a new random power level
	 */
	void RandomFlicker(int aLowerBound, int aUpperBound, unsigned long aUpdatePeriod);

	/**
	 * Applies a sine-wave flicker pattern while keeping color balance.
	 * Args:
	 *	 aAmplitude - Amplitude to modulate as a percentage of maximum brightness
	 *   aUpdatePeroid - How often (in milliseconds) to apply a new random power level
	 */
	void SineFlicker(float aAmplitude, unsigned long aUpdatePerioid);

	/**
	 * Applies a FU2-style pulse effect.
	 * Args:
	 *  aUpdatePeroid - How fast to move the pulses (frame rate in milliseconds)
	 *  aPatternSize - Size in pixels before pattern repeats
	 */
	void PulseFlicker(unsigned long aUpdatePeriod, int aPatternSize);

	/**
	 * Companion function to PulseFlicker(). Sends the frame from a PulseFlicker.
	 * Args:
	 *   aPatternSize - Size in pixels before pattern repeats
	 */
	void SendPulseFlickerFrame(int aPatternSize);

	void SendPixel(uint8_t aRed, uint8_t aGreen, uint8_t aBlue);

	void SendPixels(uint8_t aRed, uint8_t aGreen, uint8_t aBlue, int aNumPixels);

	void ShowFrame();

	/**
	 * Enables or disables power pins to save energy.
	 */
	void SetPowerEnable(bool aEnable);

	//Size of frame buffer (max length of blade)
	int mBladeLength;

	//Index of current tip
	volatile int mTipIndex;

	//Target power levels for main blade color (channels 0, 1, and 2)
	tPixel mMainColor;

	//Target power level for blade effect color (channels 3, 4, and 5)
	tPixel mEffectColor;

	//Interface to the pixel strip
	NRPixelStrip* mpPixelStrip;

	//Last time a flicker frame was sent
	unsigned long mLastFlickerUpdate;

	//Used by Sine style flicker effect
	float mSineFlickerStage;

	//Last flicker effect used
	int mLastFlickerEffect;

	//New flicker effect (used to init effect pixels if necessary)
	bool mIsNewEffect;

	int mFlickerStage;

	//Have some pi
	const float pi = 3.14;

	//What effect to apply during power up
	int mPowerupEffect;

	//What effect to apply during power down
	int mPowerdownEffect;

	//Animation frame rate for effects (higher numbers slow it down)
	unsigned long mEffectFramePeriod;

	//Pins to cut power to the blade
	int maPowerPins[3];
};

#endif /* PIXELBLADE_H_ */
