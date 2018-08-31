package net.ximias.peripheral.Hue.Hue.hueEffects;

import com.philips.lighting.hue.sdk.wrapper.entertainment.Area;
import com.philips.lighting.hue.sdk.wrapper.entertainment.TweenType;
import com.philips.lighting.hue.sdk.wrapper.entertainment.animation.TweenAnimation;
import com.philips.lighting.hue.sdk.wrapper.entertainment.effect.AreaEffect;
import com.philips.lighting.hue.sdk.wrapper.entertainment.effect.Effect;
import javafx.scene.paint.Color;


public class FrontCenterEffect implements AreaEffectProducer {
	private final Color startColor;
	private final Color endColor;
	private final int duration;
	private double opacityMultiplier = 1;
	
	public FrontCenterEffect(Color startColor, Color endColor, int duration) {
		this.startColor = startColor;
		this.endColor = endColor;
		this.duration = duration;
	}
	
	
	@Override
	public Effect getEffect() {
		AreaEffect effect = new AreaEffect();
		TweenAnimation r = new TweenAnimation(startColor.getRed(), endColor.getRed(), duration, TweenType.Linear);
		TweenAnimation g = new TweenAnimation(startColor.getGreen(), endColor.getGreen(), duration, TweenType.Linear);
		TweenAnimation b = new TweenAnimation(startColor.getBlue(), endColor.getBlue(), duration, TweenType.Linear);
		TweenAnimation a = new TweenAnimation(startColor.getOpacity() * opacityMultiplier, endColor.getOpacity() * opacityMultiplier, duration, TweenType.Linear);
		
		effect.setColorAnimation(r, g, b);
		effect.setOpacityAnimation(a);
		effect.setArea(Area.Predefine.Front);
		
		return effect;
	}
	
	@Override
	public void setOpacity(double opacity) {
		opacityMultiplier = opacity;
	}
	
	@Override
	public int getDuration() {
		return duration;
	}
}
