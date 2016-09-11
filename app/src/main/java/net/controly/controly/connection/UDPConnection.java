package net.controly.controly.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Map;

/**
 * This class represents a UDP connection.
 */
public class UDPConnection extends BasicConnection {

    private DatagramChannel mDatagramChannel;

    /**
     * Initialize the connection according to the details given.
     *
     * @param connectionDetails The details to set the connection according to.
     */
    public UDPConnection(Map<String, String> connectionDetails) {
        super(connectionDetails);
    }

    @Override
    void start() {
        try {
            int port = Integer.parseInt(connectionDetails.get("port"));

            mDatagramChannel = DatagramChannel.open();
            mDatagramChannel.socket().bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void finish() {
        mDatagramChannel.socket().close();
    }

    @Override
    void stop() {

    }
}
