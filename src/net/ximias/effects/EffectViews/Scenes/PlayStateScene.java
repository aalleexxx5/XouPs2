package net.ximias.effects.EffectViews.Scenes;

import javafx.scene.paint.Color;
import net.ximias.effects.EffectView;
import net.ximias.effects.impl.*;
import net.ximias.network.CurrentPlayer;
import net.ximias.network.Ps2EventStreamingConnection;
import net.ximias.psEvent.condition.*;
import net.ximias.psEvent.handler.GlobalHandler;
import net.ximias.psEvent.handler.Ps2EventType;
import net.ximias.psEvent.handler.SingleEventHandler;

import java.io.IOException;
import java.util.HashMap;

public class PlayStateScene implements EffectScene{
	EffectView view;
	Ps2EventStreamingConnection connection;
	
	private SingleCondition isPlayer = new SingleCondition(Condition.EQUALS,
			new EventData(CurrentPlayer.getInstance().getPlayerID(),ConditionDataSource.CONSTANT),
			new EventData("character_id", ConditionDataSource.EVENT)
	);
	public PlayStateScene(EffectView view) {
		this.view = view;
		try {
			magic();
		} catch (IOException e) {
			throw new Error("Error in setting up playstate");
		}
	}
	
	private void magic() throws IOException {
		connection = new Ps2EventStreamingConnection();
		background();
		death();
		kill();
		
		repair();
		experience();
		//First playtest
		
		/*multiKill();
		experience();
		headshot();
		//Play test
		
		reviving();
		healing();
		repairing();
		amsSpawn();
		vehicle();
		level();
		achievement();
		facility();
		// Play test
		
		alert();
		logging();
		killingXimias();
		//Alpha release
		
		*/
		
	}
	
	private void kill() {
		
		
		FadingEffectProducer killEffect = new FadingEffectProducer(Color.WHITE, 500);
		FadingEffectProducer teamKillEffect = new FadingEffectProducer(Color.HOTPINK,500);
		FadingEffectProducer VSKillEnd = new FadingEffectProducer(SceneConstants.VS, 300);
		FadingEffectProducer NCKillEnd = new FadingEffectProducer(SceneConstants.NC, 300);
		FadingEffectProducer TRKillEnd = new FadingEffectProducer(SceneConstants.TR, 300);
		TimedEffectProducer blank = new TimedEffectProducer(Color.TRANSPARENT, 100);
		
		MultiEffectProducer VSKill = new MultiEffectProducer(blank, VSKillEnd);
		MultiEffectProducer NCKill = new MultiEffectProducer(blank, NCKillEnd);
		MultiEffectProducer TRKill = new MultiEffectProducer(blank, TRKillEnd);
		
		
		SingleCondition isKill = new SingleCondition(Condition.EQUALS,
				new EventData(CurrentPlayer.getInstance().getPlayerID(),ConditionDataSource.CONSTANT),
				new EventData("attacker_character_id", ConditionDataSource.EVENT)
		);
		
		HashMap<String, String> eventPlayer = new HashMap<>(4);
		eventPlayer.put("character_id","character_id");
		
		SingleCondition isVS = new SingleCondition(Condition.EQUALS,
				new EventData(String.valueOf(SceneConstants.VS_ID),ConditionDataSource.CONSTANT),
				new CensusData("character", "faction_id", new HashMap<>(0),eventPlayer));
		
		SingleCondition isNC = new SingleCondition(Condition.EQUALS,
				new EventData(String.valueOf(SceneConstants.NC_ID),ConditionDataSource.CONSTANT),
				new CensusData("character", "faction_id", new HashMap<>(0),eventPlayer));
		
		SingleCondition isTR = new SingleCondition(Condition.EQUALS,
				new EventData(String.valueOf(SceneConstants.TR_ID),ConditionDataSource.CONSTANT),
				new CensusData("character", "faction_id", new HashMap<>(0),eventPlayer));
		
		SingleCondition isSame = new SingleCondition(Condition.EQUALS,
				new EventData("faction_id",ConditionDataSource.PLAYER),
				new CensusData("character", "faction_id", new HashMap<>(0),eventPlayer));
		
		SingleCondition isNotSame = new SingleCondition(Condition.NOT_EQUALS,
				new EventData("faction_id",ConditionDataSource.PLAYER),
				new CensusData("character", "faction_id", new HashMap<>(0),eventPlayer));
		
		AllCondition isTeamKill = new AllCondition(isKill, isSame);
		AllCondition isHonestKill = new AllCondition(isKill, isNotSame);
		AllCondition isVSKill = new AllCondition(isVS, isKill);
		AllCondition isNCKill = new AllCondition(isNC, isKill);
		AllCondition isTRKill = new AllCondition(isTR, isKill);
		
		SingleEventHandler teamKill = new SingleEventHandler(view , teamKillEffect, isTeamKill, Ps2EventType.PLAYER, "Death", "teamKill");
		SingleEventHandler honestKill = new SingleEventHandler(view,killEffect, isHonestKill, Ps2EventType.PLAYER,"Death","honestKill");
		SingleEventHandler factionVSKill = new SingleEventHandler(view, VSKill, isVSKill, Ps2EventType.PLAYER, "Death", "VSKill");
		SingleEventHandler factionNCKill = new SingleEventHandler(view, NCKill, isNCKill, Ps2EventType.PLAYER, "Death", "NCKill");
		SingleEventHandler factionTRKill = new SingleEventHandler(view, TRKill, isTRKill, Ps2EventType.PLAYER, "Death", "TRKill");
		
		teamKill.register(connection);
		honestKill.register(connection);
		factionVSKill.register(connection);
		factionNCKill.register(connection);
		factionTRKill.register(connection);
	}
	
	private void experience(){
		Color expColor = bias(Color.DARKCYAN, 0.05);
		BlendingEffectProducer fadein = new BlendingEffectProducer(Color.TRANSPARENT, expColor, 100);
		FadingEffectProducer fadeout = new FadingEffectProducer(expColor, 200);
		MultiEffectProducer exp = new MultiEffectProducer(fadein, fadeout);
		
		SingleEventHandler expEvent = new SingleEventHandler(view, exp, isPlayer, Ps2EventType.PLAYER, "GainExperience", "experience");
		expEvent.register(connection);
	}
	
	private void repair(){
		Color repair = bias(new Color(0.8,1,1,1),0.2);
		FadingEffectProducer repairing = new FadingEffectProducer(repair, 800);
		HashMap<String, String> experienceid = new HashMap<>(4);
		experienceid.put("experience_id", "experience_id");
		
		SingleCondition containsRepair = new SingleCondition(Condition.CONTAINS,
				new CensusData("experience","description", new HashMap<String, String>(0), experienceid),
				new EventData("repair", ConditionDataSource.CONSTANT));
		
		AllCondition isRepair = new AllCondition(isPlayer, containsRepair);
		SingleEventHandler repairEvent = new SingleEventHandler(view, repairing, isRepair, Ps2EventType.PLAYER, "GainExperience", "repair");
		repairEvent.register(connection);
	}
	
	private void death() {
		
		BlendingEffectProducer VSDeathFade = new BlendingEffectProducer(SceneConstants.VS,Color.BLACK,1000);
		BlendingEffectProducer NCDeathFade = new BlendingEffectProducer(SceneConstants.NC,Color.BLACK,1000);
		BlendingEffectProducer TRDeathFade = new BlendingEffectProducer(SceneConstants.TR,Color.BLACK,1000);
		FadingEffectProducer fadeout = new FadingEffectProducer(Color.BLACK, 500);
		TimedEffectProducer black = new TimedEffectProducer(Color.BLACK, 1000);
		
		MultiEffectProducer VSDeath = new MultiEffectProducer(
				VSDeathFade,
				black,
				fadeout
		);
		MultiEffectProducer NCDeath = new MultiEffectProducer(
				NCDeathFade,
				black,
				fadeout
		);
		MultiEffectProducer TRDeath = new MultiEffectProducer(
				TRDeathFade,
				black,
				fadeout
		);
		
		HashMap<String, String> eventPlayer = new HashMap<>(4);
		eventPlayer.put("character_id","attacker_character_id");
		
		
		
		SingleCondition isVS = new SingleCondition(Condition.EQUALS, new CensusData("character","faction_id", new HashMap<>(0),eventPlayer),new EventData(String.valueOf(SceneConstants.VS_ID),ConditionDataSource.CONSTANT));
		SingleCondition isNC = new SingleCondition(Condition.EQUALS, new CensusData("character","faction_id", new HashMap<>(0),eventPlayer),new EventData(String.valueOf(SceneConstants.NC_ID),ConditionDataSource.CONSTANT));
		SingleCondition isTR = new SingleCondition(Condition.EQUALS, new CensusData("character","faction_id", new HashMap<>(0),eventPlayer),new EventData(String.valueOf(SceneConstants.TR_ID),ConditionDataSource.CONSTANT));
		
		AllCondition isVSDeath = new AllCondition(isVS, isPlayer);
		AllCondition isNCDeath = new AllCondition(isNC, isPlayer);
		AllCondition isTRDeath = new AllCondition(isTR, isPlayer);
		
		SingleEventHandler VSDeathhandler = new SingleEventHandler(view, VSDeath, isVSDeath, Ps2EventType.PLAYER, "Death", "vsDeath");
		SingleEventHandler NCDeathhandler = new SingleEventHandler(view, NCDeath, isNCDeath, Ps2EventType.PLAYER, "Death", "ncDeath");
		SingleEventHandler TRDeathhandler = new SingleEventHandler(view, TRDeath, isTRDeath, Ps2EventType.PLAYER, "Death", "trDeath");
		
		VSDeathhandler.register(connection);
		NCDeathhandler.register(connection);
		TRDeathhandler.register(connection);
	}
	
	private void background() {
		EventEffectProducer esamir = new EventEffectProducer(bias(SceneConstants.ESAMIR,0.05),"background");
		EventEffectProducer amerish = new EventEffectProducer(bias(SceneConstants.AMERISH, 0.05),"background");
		EventEffectProducer indar = new EventEffectProducer(bias(SceneConstants.INDAR,0.05),"background");
		EventEffectProducer hossin = new EventEffectProducer(bias(SceneConstants.HOSSIN,0.05),"background");
		EventEffectProducer other = new EventEffectProducer(bias(SceneConstants.OTHER,0.05),"background");
		
		SingleCondition isEsamir = new SingleCondition(Condition.EQUALS, new EventData("zone_id", ConditionDataSource.PLAYER),new EventData(String.valueOf(SceneConstants.ESAMIR_ID),ConditionDataSource.CONSTANT));
		SingleCondition isAmerish = new SingleCondition(Condition.EQUALS, new EventData("zone_id",ConditionDataSource.PLAYER),new EventData(String.valueOf(SceneConstants.AMERISH_ID),ConditionDataSource.CONSTANT));
		SingleCondition isIndar = new SingleCondition(Condition.EQUALS, new EventData("zone_id",ConditionDataSource.PLAYER),new EventData(String.valueOf(SceneConstants.INDAR_ID),ConditionDataSource.CONSTANT));
		SingleCondition isHossin = new SingleCondition(Condition.EQUALS, new EventData("zone_id",ConditionDataSource.PLAYER),new EventData(String.valueOf(SceneConstants.HOSSIN_ID),ConditionDataSource.CONSTANT));
		
		
		GlobalHandler esamirHandler = new GlobalHandler(isEsamir, esamir, view);
		GlobalHandler amerishHandler = new GlobalHandler(isAmerish, amerish, view);
		GlobalHandler indarHandler = new GlobalHandler(isIndar, indar, view);
		GlobalHandler hossinHandler = new GlobalHandler(isHossin, hossin, view);
		
		esamirHandler.register(connection);
		amerishHandler.register(connection);
		indarHandler.register(connection);
		hossinHandler.register(connection);
		
		view.addEffect(other.build());
	}
	private Color bias(Color color,double bias){
		return color.deriveColor(0,1,1,bias);
	}
}
