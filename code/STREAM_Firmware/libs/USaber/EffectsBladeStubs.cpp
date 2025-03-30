/*
 * EffectsBladeStubs.cpp
 *
 *  Created on: Jan 8, 2019
 *      Author: Jake
 */

#include "blade/EffectsBladeStubs.h"

//Implement do-nothing stubs for interface functions
void EffectsBladeStubs::SetPowerUpEffect(int aEffectIdx)
{
	//Do nothing
	(void)aEffectIdx; //Prevents compiler warning
}

void EffectsBladeStubs::SetPowerDownEffect(int aEffectIdx)
{
	//Do nothing
	(void)aEffectIdx; //Prevents compiler warning
}

void EffectsBladeStubs::SetEffectColor(uint8_t aCh1, uint8_t aCh2, uint8_t aCh3)
{
	//Do nothing, but prevent compiler warnings
	(void)aCh1;
	(void)aCh2;
	(void)aCh3;
}

void EffectsBladeStubs::SetEffectAnimationPeriod(unsigned long anFrameTime)
{
	//Do nothing
	(void)anFrameTime;
}

void EffectsBladeStubs::ApplyBlasterEffect(int aEffect, unsigned int aEffectTime)
{
	//Do nothing
	(void)aEffect;
	(void)aEffectTime;
}

void EffectsBladeStubs::SetBladeLength(int aLength)
{
	//Do nothing
	(void)aLength;
}
