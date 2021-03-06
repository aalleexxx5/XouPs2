package net.ximias.effect;

import javafx.scene.paint.Color;
import net.ximias.fileParser.JsonSerializable;
import net.ximias.peripheral.PeripheralEffectProducer;
import net.ximias.peripheral.keyboard.KeyEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EffectProducer implements JsonSerializable {
	protected String name;
	private final ArrayList<PeripheralEffectProducer> peripheralEffects = new ArrayList<>(6);
	
	public abstract Effect build();
	
	public abstract void setColor(Color color);
	
	public String getName() {
		return name;
	}
	
	public void attachPeripheralEffect(PeripheralEffectProducer effectProducer) {
		peripheralEffects.add(effectProducer);
	}
	
	/**
	 * Used to obtain a list of all peripheralEffects assignable from a superclass.
	 * Will return an empty list, if none are found.
	 *
	 * @param superclass the class assignable from peripharalEffect. Fx. {@link KeyEffect}
	 * @param <T>        The type if the class.
	 * @return All peripheralEffectProducers subclassed to the provided argument, cast as the argument type.
	 */
	public <T extends PeripheralEffectProducer> List<T> getAllPeripheralEffectProducersBySuperclass(Class<T> superclass) {
		return peripheralEffects.stream().filter(it -> superclass.isAssignableFrom(it.getClass())).map(superclass::cast).collect(Collectors.toList());
	}
}
