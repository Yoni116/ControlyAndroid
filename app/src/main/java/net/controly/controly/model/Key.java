package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Itai on 16-Sep-16.
 */
public abstract class Key implements BoxListItem, Serializable {

    @SerializedName("Command")
    protected final String command;

    public Key(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
