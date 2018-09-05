package net.ximias.datastructures.gui.data;

import javafx.scene.paint.Color;
import net.ximias.effect.EffectProducer;
import net.ximias.effect.EffectView;
import net.ximias.effect.producers.TimedEffectProducer;
import net.ximias.effect.views.scenes.PlayStateScene;
import net.ximias.network.Ps2EventStreamingConnection;
import net.ximias.psEvent.condition.EventCondition;
import net.ximias.psEvent.handler.Ps2EventHandler;

import java.util.HashMap;
import net.ximias.logging.Logger;


public class EffectData {
	private final HashMap<String, EffectProducer> effects = new HashMap<>();
	private final HashMap<String, UnlinkedEvent> availableEvents = new HashMap<>();
	private final HashMap<String, String> linkedEventNames = new HashMap<>();
	private final HashMap<String, Ps2EventHandler> linkedEvents = new HashMap<>();
	private final HashMap<String, EventCondition> conditions = new HashMap<>();
	private final Logger logger = Logger.getLogger(getClass().getName());
	private final EffectView view;
	
	private final Ps2EventStreamingConnection connection = new Ps2EventStreamingConnection();
	private final PlayStateScene scene;
	
	public EffectData(EffectView view) {
		this.view = view;
		scene = new PlayStateScene(view, connection);
		connection.hasDisconnectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue){
				view.addEffect(new TimedEffectProducer(Color.BLACK, 200).build());
				scene.updateBackground();
			}
		});
	}
	
	public void linkEffectWithEvent(String eventName, String effectName){
		if (!effects.containsKey(effectName) || !availableEvents.containsKey(eventName)) {
			logger.application().warning("Effect could not be linked. One or more of the names does not exist");
			return;
		}
		if (linkedEvents.containsKey(eventName)){
			logger.application().warning("Effect was already linked. Removing old link..");
			connection.removeEvent(linkedEvents.get(eventName));
		}
		linkedEvents.put(eventName,availableEvents.get(eventName).linkWithEffect(effects.get(effectName),view, connection));
		linkedEventNames.put(eventName, effectName);
	}
	
	public void addAvailableEvent(String name, UnlinkedEvent unlinkedEvent){
	    availableEvents.put(name, unlinkedEvent);
	}
	
	public void removeAvailableEvent(String name){
	    availableEvents.remove(name);
	}
	
	public void addEffect(String name, EffectProducer producer){
	    effects.put(name, producer);
	}
	
	public void removeEffect(String name){
	    effects.remove(name);
	}
	
	public void addCondition(String name, EventCondition condition){
	    conditions.put(name, condition);
	}
	
	public void removeCondition(String name){
	    conditions.remove(name);
	}
	
	public HashMap<String, EventCondition> getConditions() {
		return conditions;
	}
	
	public EventCondition getConditionByName(String name){
		return conditions.get(name);
	}
	
	public HashMap<String, String> getLinkedEventNames() {
		return linkedEventNames;
	}
	
	public void intensityChanged(double brightness, double transparency) {
		scene.intensityChanged(brightness, transparency);
	}
	
	public void updateBackground() {
		scene.updateBackground();
	}
	
	public void playerIDUpdated() {
		connection.resubscribeAllEvents();
	}
}