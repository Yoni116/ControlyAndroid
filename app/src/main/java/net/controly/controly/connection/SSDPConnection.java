package net.controly.controly.connection;

import java.util.Map;

/**
 * This class represents a SSDP connection.
 */
public class SSDPConnection extends BasicConnection {

    /**
     * Initialize the connection according to the details given.
     *
     * @param connectionDetails The details to set the connection according to.
     */
    public SSDPConnection(Map<String, String> connectionDetails) {
        super(connectionDetails);
    }

    @Override
    void start() {

    }

    @Override
    void finish() {

    }

    @Override
    void stop() {

    }
}
