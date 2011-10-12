package edu.chalmers.aardvark.test.unit.model;

import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.model.User;

public class DummyUserFactory {
    public static User getRandomDummyUser(){
	return new User("Alias "+((Math.random()*100)+(Math.random()*10)),"AardvarkID "+((Math.random()*100)+(Math.random()*10))){
		@Override
		public boolean equals(Object o) {
			if (o instanceof User) {
				User other = (User)o;
				return this.getAardvarkID() == other.getAardvarkID();
			}
			return false;
		}
	};
    }
    
    public static Contact getRandomDummyContact() {
    	return new Contact("Nickname "+((Math.random()*100)+(Math.random()*10)), "AardvarkID "+((Math.random()*100)+(Math.random()*10)));
    }
}
