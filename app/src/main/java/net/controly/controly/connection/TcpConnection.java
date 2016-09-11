package net.controly.controly.connection;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

/**
 * This class represents a TCP connection.
 */
public class TCPConnection extends BasicConnection {

    private Socket socket;

    /**
     * Initialize the connection according to the details given.
     *
     * @param connectionDetails The details to set the connection according to.
     */
    public TCPConnection(Map<String, String> connectionDetails) {
        super(connectionDetails);
    }

    private void setupTcpConnectionSocket() throws IOException {
        String ipv4 = connectionDetails.get("ip");
        int port = Integer.parseInt(connectionDetails.get("port"));

        InetAddress ipAddress = Inet4Address.getByName(ipv4);

        socket = new Socket(ipAddress, port);
    }

    @Override
    void start() {
        try {
            setupTcpConnectionSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void finish() {
    }

    @Override
    void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
