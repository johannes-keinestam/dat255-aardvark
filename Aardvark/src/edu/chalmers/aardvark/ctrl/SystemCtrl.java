package edu.chalmers.aardvark.ctrl;

public class SystemCtrl {
	private static SystemCtrl instance;
	
	public static SystemCtrl getCtrl() {
		if (instance == null) {
			instance = new SystemCtrl();
		}
		return instance;
	}
	
	public void performStartUpDuty() {
		if (isFirstRun()) {
			performSetup();
		}
		// TODO Load ID and pass from IO
		// TODO Create LocalUser
		// TODO Create Server connection
		// TODO load contacts, create Contacts
		// TODO check contact availability
	}
	
	public void performShutDownDuty() {
		// TODO remove users from roster
		// TODO disconnect from server
	}
	
	private void performSetup() {
		// TODO get ID for Aardvark ID, save
		// TODO generate password, save
		// TODO generate encryption keys, save
	}
	
	private boolean isFirstRun() {
		//TODO check if first time. Variable set?
		return true;
	}
}
