package edu.chalmers.aardvark.test.unit.services;

import java.util.Collection;
import java.util.UUID;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.ToContainsFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.util.Log;

import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.ServerConnection;
import junit.framework.TestCase;

/**
 * Not really possible to test the services. However,
 * it is possible to test receiving and sending of messages
 * and receiving user status updates from server.
 *
 */
public class ServerCommunicationTest extends TestCase implements PacketListener, RosterListener {

	boolean receivedMessage = false;
	boolean receivedStatus = false;
	String user;
	String sender;
	String packetID;
	
	protected void setUp() throws Exception {
		super.setUp();
		ServerConnection.getConnection();
		user = "TestUser";
		String password = "test";
		
		LocalUser.createUser(user, password);
		ServerHandlerCtrl.getInstance().logInWithAlias(UUID.randomUUID().toString());
		PacketFilter filter = new ToContainsFilter(LocalUser.getLocalUser()
				.getAardvarkID());
		ServerConnection.getConnection().addPacketListener(this, filter);
		
		Roster roster = ServerConnection.getConnection().getRoster();
		roster.addRosterListener(this);
	}

	protected void tearDown() throws Exception {
		ServerConnection.restart();
	}

	public void testSendReceivePackage() {
		String userSuffix = "@"+ServerConnection.getConnection().getServiceName();
		sender = "TestSender "+Math.random()+userSuffix;
		
		Message messagePacket = new Message(user+userSuffix);
		messagePacket.setFrom(sender);
		messagePacket.setType(Message.Type.chat);
		messagePacket.setBody("Hello");
		packetID = messagePacket.getPacketID();
		
		ServerConnection.getConnection().sendPacket(messagePacket);
		
		int counter = 0;
		while (!receivedMessage) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			counter++;
			if (counter >= 10) {
				break;
			}
		}
		assertTrue(receivedMessage);
	}
	
	public void testReceiveStatus() {
		int counter = 0;
		while (!receivedStatus) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			counter++;
			if (counter >= 10) {
				break;
			}
		}
		assertTrue(receivedStatus);
	}
	
	@Override
	public void processPacket(Packet packet) {
		if (packet.getPacketID().equals(packetID)) {
			receivedMessage = true;
		}
		
	}

	@Override
	public void entriesAdded(Collection<String> arg0) {
		receivedStatus = true;
	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
		receivedStatus = true;
	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		receivedStatus = true;
	}

	@Override
	public void presenceChanged(Presence arg0) {
		receivedStatus = true;
	}

}
