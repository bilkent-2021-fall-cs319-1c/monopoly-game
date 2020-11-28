package monopoly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import monopoly.lobby.*;

/**
 * Model for server and lobby management
 *
 * @author Alper Sarı
 * @version Nov 22, 2020
 */

public class Model {
    private static Model instance = null;
    private List<Lobby> lobbies;
    private List<User> users;
    private GameServer server;

    private Model() {
        lobbies = Collections.synchronizedList(new ArrayList<Lobby>());
        users = Collections.synchronizedList(new ArrayList<User>());
    }

    public static Model getInstance()
    {
        if (instance == null)
            instance = new Model();

        return instance;
    }

    public void addLobby(Lobby lobby)
    {
        if (lobby != null)
            lobbies.add(lobby);
    }

    public void closeLobby(Lobby lobby)
    {
        if (lobby != null)
            for (int i = 0; i < lobbies.size(); i++)
            {
                if (lobbies.get(i).equals(lobby))
                {
                    lobbies.remove(i);
                    break;
                }
            }
    }

    public void userLogin(String username, String ip)
    {
        boolean exists = false;
        for (User user : users) {
            if (user.getUserID().equals(ip) || user.getUsername().equals(username)) {
                exists = true;
                break;
            }
        }

        if (!exists)
            users.add(new User(username, ip));
    }

    public void userLogout(User user)
    {
        if (user != null)
            for (int i = 0; i < users.size(); i++)
            {
                if (users.get(i).equals(user))
                {
                    users.remove(i);
                    break;
                }
            }
    }

}
