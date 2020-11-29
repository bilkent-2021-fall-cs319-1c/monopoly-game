package monopoly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import monopoly.lobby.*;

/**
 * Model for server, lobby and user management
 *
 * @author Alper Sarı, Javid Baghirov
 * @version Nov 29, 2020
 */

@Getter
@Setter
public class Model {
    private List<Lobby> lobbies;
    private List<User> users;
    private GameServer server;

    
    private Model() {
        lobbies = Collections.synchronizedList(new ArrayList<Lobby>());
        users = Collections.synchronizedList(new ArrayList<User>());
    }

    public void addLobby(Lobby lobby) {
        if (lobby != null) {
        	lobbies.add(lobby);
        }
    }
    
    public void closeLobby(Lobby lobby) {
        if (lobby != null) {
            for (int i = 0; i < lobbies.size(); i++) {
                if (lobbies.get(i).equals(lobby)) {
                    lobbies.remove(i);
                    break;
                }
            }
        }
    }
    
    /**
     * A user is added to the list
     * 
     * @param username the specified name
     * @param id the connection id of the user
     */
    public void userLogin(String username, int id) {
        boolean exists = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals( username) && users.get(i).getConnectionId() ==  id) {
                exists = true;
                break;
            }
        }

        if (!exists) {
        	users.add(new User(username, id));
        }
    }
    
    /**
     * A user is removed from the list
     * 
     * @param user the user to be logged out
     */
    public void userLogout(User user) {
        if (user != null) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).equals(user)) {
                    users.remove(i);
                    break;
                }
            }
        }
    }
    
    /**
     * Finds a user by using id
     * 
     * @param connectionID the specified id to be searched with
     * 
     * @return User if found one, null if not
     */
    public User getByID( int connectionID) {
    	boolean exists = false;
    	int index = 0;
    	
    	//Check if there exists a user with the specified id
    	for (int i = 0; i < users.size(); i++) {
    		if( users.get(i).getConnectionId() == connectionID) {
    			exists = true;
    			index = i;
    			break;
    		}
    	}
    	
    	if( exists) {
    		return users.get( index);
    	}
    	else return null;
    }
    
    /**
     * Finds a lobby by using id
     * 
     * @param id the specified id to be searched with
     * 
     * @return Lobby if found one, null if not
     */
    public Lobby getByID( long id){
    	boolean exists = false;
    	int index = 0;
    	
    	//Check if there exists a lobby with the specified id
    	for (int i = 0; i < lobbies.size(); i++) {
    		if( lobbies.get(i).getId() == id) {
    			exists = true;
    			index = i;
    			break;
    		}
    	}
    	
    	if( exists) {
    		return lobbies.get( index);
    	}
    	else return null;
    }

}
