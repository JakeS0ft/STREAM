/*
 * IEffectsBlade.h
 *
 *  Created on: Dec 16, 2018
 *      Author: Jake
 */

#ifndef IEFFECTSBLADE_H_
#define IEFFECTSBLADE_H_

#include <Arduino.h>

//Describes properties specific to advanced effects blades
//struct tEffectsBladeProperties
//{
//	int PowerupEffects;
//	int PowerdownEffects;
//	int BlasterEffects;
//};

/**
 * This class is an extension to the IBladeManager interface. It adds extra
 * functions to control special effect parameters in greater detail than
 * the standard blade API provides.
 */
class IEffectsBlade
{
public:
	/**
	 * Sets the effect shown during a powerup event.
	 * Args:
	 *   aEffectIdx = Effect to apply
	 */
	virtual void SetPowerUpEffect(int aEffectIdx) = 0;

	/**
	 * Sets the effect shown during a powerdown event.
	 * Args:
	 *   aEffectIdx = Effect to apply
	 */
	virtual void SetPowerDownEffect(int aEffectIdx) = 0;

	/**
	 * Sets the color parameter used for a speical effect.
	 * Note: May not be applicable to all effects. In that case, this call should be
	 * implemented as a do-nothing function.
	 */
	virtual void SetEffectColor(uint8_t aCh1, uint8_t Ch2, uint8_t Ch3) = 0;

	/**
	 * Sets the update period for a special effect.
	 * Note: May not be applicable to all effects. In that case, this should be
	 * Implemented as a do-nothing function.
	 */
	virtual void SetEffectAnimationPeriod(unsigned long anFrameTime) = 0;

	/**
	 * Apply localized blaster effect.
	 * Args:
	 *   aEffect - Effect to apply
	 *   aTime - How long to apply the effect
	 */
	virtual void ApplyBlasterEffect(int aEffect, unsigned int aEffectTime) = 0;

	/**
	 * Set blade length for variable length blades.
	 * Note: Some blade types my impliment this as a do-nothing function.
	 * Args:
	 *   aLength - Length of blade
	 */
	virtual void SetBladeLength(int aLength) = 0;

	/**
	 * Fetch the advanced effects properties.
	 * Child classes should create and return a tEffectsBladeProperties object
	 * to describe the blade's abilites.
	 */
	//virtual tEffectsBladeProperties GetEffectsBladeProperties() = 0;

};



#endif /* IEFFECTSBLADE_H_ */
