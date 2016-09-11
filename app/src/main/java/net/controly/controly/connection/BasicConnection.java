package net.controly.controly.connection;

import java.util.Map;

/**
 * This class represents a basic connection.
 */
public abstract class BasicConnection {

    protected final Map<String, String> connectionDetails;

    /**
     * Initialize the connection according to the details given.
     * @param connectionDetails The details to set the connection according to.
     */
    public BasicConnection(Map<String, String> connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    abstract void start();

    abstract void finish();

    abstract void stop();
}
