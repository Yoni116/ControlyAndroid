package net.controly.controly.model;

import android.graphics.Color;

/**
 * This class represents a keyboard button.
 */
public class Key {

    private String width;
    private String height;
    private String x;
    private String y;

    private String commandId;
    private String hexColor;
    private String iconName;
    private String image;
    private String name;
    private String view;

    public Key() {
    }

    public float getWidth() {
        return Float.parseFloat(width);
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public float getHeight() {
        return Float.parseFloat(height);
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public float getX() {
        return Float.parseFloat(x);
    }

    public void setX(String x) {
        this.x = x;
    }

    public float getY() {
        return Float.parseFloat(y);
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getHexColor() {
        return hexColor;
    }

    public int getColor() {
        return Color.parseColor("#" + getHexColor());
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    /**
     * @return Whether the key is a circle_key_button type.
     */
    public boolean isCircle() {
        return view.equals("circle");
    }
}
