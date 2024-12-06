package server.websocket;

import java.io.IOException;
import org.eclipse.jetty.websocket.api.Session;
public class Connection {
    private Session session;
    private String username;
    private String authToken;

    public Connection(Session session, String username, String authToken) {
        this.session = session;
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}