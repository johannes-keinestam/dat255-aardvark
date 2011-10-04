package edu.chalmers.aardvark.util;

import org.jivesoftware.smack.packet.Packet;

import android.util.Log;

public class MessagePacket extends Packet {

    public MessagePacket(String from, String to, String msg) {
	this.setFrom(from);
	this.setTo(to);
	this.setProperty("message", msg);
	Log.i("INFO", this.toString() + " STARTED");
    }

    @Override
    public String toXML() {
	return "<aardvarkmessage>" + "	<sender>" + this.getFrom() + "</sender>"
		+ " 	<recipient>" + this.getTo() + "</recipient>"
		+ " 	<message>" + this.getProperty("message") + "</message>"
		+ "</aardvarkmessage>";
    }

}
