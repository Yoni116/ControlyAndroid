package net.controly.controly.model;

import net.controly.controly.view.KeyView;

/**
 * This class represents the key layout.
 */
public class KeysLayout {
    private KeyView[] keys;
    private String screenSize;

    public KeysLayout() {
    }

    public KeyView[] getKeys() {
        return keys;
    }

    public void setKeys(KeyView[] keys) {
        this.keys = keys;
    }

    /**
     * @return The screen size of the keyboard's maker.
     * The first value in the array is the width, and the second is the height.
     */
    public int[] getScreenSize() {
        String[] sizes = screenSize.split("x");
        return new int[]{Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1])};
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }
}
