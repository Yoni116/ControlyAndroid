package net.controly.controly.model;

import net.controly.controly.view.KeyView;

/**
 * This class represents the key layout.
 */
public class KeysLayout {
    private final KeyView[] keys;
    private final String screenSize;

    public KeysLayout(KeyView[] keys, String screenSize) {
        this.keys = keys;
        this.screenSize = screenSize;
    }

    public KeyView[] getKeys() {
        return keys;
    }

    /**
     * @return The screen size of the keyboard's maker.
     * The first value in the array is the width, and the second is the height.
     */
    public int[] getScreenSize() {
        String[] sizes = screenSize.split("x");
        return new int[]{Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1])};
    }
}
