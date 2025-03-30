/*
 * EffectsBladeStubs.h
 *
 *  Created on: Jan 8, 2019
 *      Author: Jake
 */

#ifndef _EFFECTSBLADESTUBS_H_
#define _EFFECTSBLADESTUBS_H_

#include "IEffectsBlade.h"

/**
 * Class used to stub out the IEffectsBlade API methods.
 */
class EffectsBladeStubs : public IEffectsBlade
{
public:
	EffectsBladeStubs()=default;

	virtual ~EffectsBladeStubs() = default;

	//Implement do-nothing stubs for interface functions
	virtual void SetPowerUpEffect(int aEffectIdx);
	virtual void SetPowerDownEffect(int aEffectIdx);
	virtual void SetEffectColor(uint8_t aCh1, uint8_t Ch2, uint8_t Ch3);
	virtual void SetEffectAnimationPeriod(unsigned long anFrameTime);
	virtual void ApplyBlasterEffect(int aEffect, unsigned int aEffectTime);
	virtual void SetBladeLength(int aLength);
};



#endif /* _EFFECTSBLADESTUBS_H_ */
