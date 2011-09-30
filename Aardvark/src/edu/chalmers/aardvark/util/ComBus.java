package edu.chalmers.aardvark.util;

import java.util.ArrayList;
import java.util.List;

public class ComBus {
	private static List<EventListener> listeners = new ArrayList<EventListener>();
	
	public static void subscribe(EventListener listener) {
		listeners.add(listener);
	}
	
	public static void unsubscribe(EventListener listener) {
		listeners.remove(listener);
	}
	
	public static void notifyListeners(String stateChange, Object object) {
		for (EventListener l : listeners) {
			l.notifyEvent(stateChange, object);
		}
	}
}
