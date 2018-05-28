package net.ximias.network;

import net.ximias.gui.StatusSeverity;
import net.ximias.gui.guiElements.StatusIndicator;
import net.ximias.persistence.ApplicationConstants;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Used for polling the census as a backup for when the streaming is down.
 */
public class Ps2BackupPollingService {
	private Timer pollTimer = new Timer(true);
	private String lastTimestamp = "";
	private Ps2EventStreamingConnection receiver;
	private Logger logger = Logger.getLogger(getClass().getName());
	private static final String STATUS_STRING = "Event streaming servers offline. Polling census for events. Expect slowness (potato mode activated).";
	public Ps2BackupPollingService(Ps2EventStreamingConnection receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * Starts polling the census for events. Should only bne used when streaming is down.
	 * Polls the most recent event at a rate defeined by ApplicationConstants.BACKUP_POLLING_RATE_IN_MS.
	 */
	public void start(){
		resetTimer();
		logger.warning("Starting backup polling service. Expect events to be received with high delay. Some will be missed entirely");
		pollTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				JSONObject payload = CensusConnection.poll("characters_event/?character_id="+CurrentPlayer.getInstance().getPlayerID()+"&c:limit=1");
				logger.info("Response from polling: "+payload);
				if (!payload.has("characters_event_list")) return;
				payload = payload.getJSONArray("characters_event_list").getJSONObject(0);
				if (payload.has("timestamp") && !payload.getString("timestamp").equals(lastTimestamp)){
					payload.put("event_name",payload.getString("event_type"));
					logger.warning("Event received through polling: "+payload.getString("event_name"));
					lastTimestamp = payload.getString("timestamp");
					receiver.delegatePayload(payload);
				}
			}
		},ApplicationConstants.BACKUP_POLLING_RATE_IN_MS, ApplicationConstants.BACKUP_POLLING_RATE_IN_MS);
		StatusIndicator.getInstance().addStatus(STATUS_STRING, StatusSeverity.INCONVIENIENCE);
	}
	
	/**
	 * Stops polling the census.
	 */
	public void stop(){
		logger.warning("Stopping polling");
		resetTimer();
		StatusIndicator.getInstance().removeStatus(STATUS_STRING);
	}
	
	private void resetTimer(){
		pollTimer.cancel();
		pollTimer = new Timer(true);
	}
}