package net.ximias.peripheral.Hue.Hue;

import com.philips.lighting.hue.sdk.wrapper.entertainment.Callback;
import com.philips.lighting.hue.sdk.wrapper.entertainment.Entertainment;
import com.philips.lighting.hue.sdk.wrapper.entertainment.StartCallback;
import javafx.beans.property.SimpleBooleanProperty;
import net.ximias.peripheral.Hue.Hue.hueEffects.HueEffect;
import net.ximias.peripheral.Hue.Hue.hueEffects.GlobalConstantEffect;
import net.ximias.effect.Effect;
import net.ximias.effect.EffectAddListener;
import net.ximias.effect.views.EffectContainer;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import net.ximias.logging.Logger;


public class HueGameState implements EffectAddListener {
	private final Entertainment entertainment;
	private final HueEffectWrapper effectWrapper = new HueEffectWrapper();
	private final SimpleBooleanProperty running = new SimpleBooleanProperty(false);
	private final Logger logger = Logger.getLogger(getClass().getName());
	private final Timer cancellationTimer = new Timer("HueEffect cancellation timer.",true);
	private final EffectContainer effectContainer;
	
	public HueGameState(Entertainment entertainment, EffectContainer effectContainer) {
		this.entertainment = entertainment;
		this.effectContainer = effectContainer;
		startEntertainmentSystem();
	}
	
	private void startEntertainmentSystem() {
		entertainment.start(startStatus -> {
			if (startStatus != StartCallback.StartStatus.Success) return;
			entertainmentStarted();
		});
	}
	
	public void endGameState(){
		entertainment.stop(this::entertainmentStopped);
	}
	
	private void entertainmentStarted() {
		logger.effects().warning("Entertainment game state started");
		running.set(true);
		effectContainer.addEffectAddListener(this);
	}
	
	private void entertainmentStopped() {
		logger.effects().warning("Entertainment game state stopped");
		running.set(false);
		effectContainer.removeEffectListener(this);
	}
	
	@Override
	public void onEffectAdded(Effect effect) {
		logger.effects().info("Hue effect received: "+effect.getName());
		if (!running.get()) return;
		
		List<HueEffect> attachedEffects = effect.getProducer().getAllPeripheralEffectProducersBySuperclass(HueEffect.class);
		LinkedList<HueEffect> effects = new LinkedList<>();
		
		if (attachedEffects.isEmpty()) {
			effects.add(effectWrapper.getAsHueEffect(effect));
		}else{
			logger.effects().info(effect.getName() + " Had "+attachedEffects.size()+" peripheral effects.");
			effects.addAll(attachedEffects);
		}
		
		for (HueEffect hueEffect : effects) {
			if (hueEffect instanceof GlobalConstantEffect){
				hueEffect.setOpacity(1);
			}else{
				hueEffect.setOpacity(effectContainer.getEffectIntensity());
			}
			com.philips.lighting.hue.sdk.wrapper.entertainment.effect.Effect entertainmentEffect = hueEffect.getEffect();
			synchronized (entertainment){
				entertainment.lockMixer();
				entertainment.addEffect(entertainmentEffect);
				entertainment.unlockMixer();
			}
			entertainmentEffect.enable();
			if (hueEffect.getDuration()>0){
				cancellationTimer.schedule(cancellationTask(entertainmentEffect),hueEffect.getDuration());
			}
		}
	}
	
	private TimerTask cancellationTask(com.philips.lighting.hue.sdk.wrapper.entertainment.effect.Effect entertainmentEffect) {
		return new TimerTask() {
			@Override
			public void run() {
				entertainmentEffect.disable();
				entertainmentEffect.finish();
			}
		};
	}
	
	public boolean isRunning() {
		return running.get();
	}
	
	public SimpleBooleanProperty runningProperty() {
		return running;
	}
}
