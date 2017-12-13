/*
 * MyDisplayMode.java
 *
 * Created on 31 August 2003, 00:03
 */
package jfreerails.client.common;

import java.awt.DisplayMode;


/**
 * Stores a DisplayMode and provides a customised implementation of toString that can be used in menus.
 * @author  Luke Lindsay
 */
public class MyDisplayMode {
    public final DisplayMode displayMode;

    public MyDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public String toString() {
        return displayMode.getWidth() + "x" + displayMode.getHeight() + " " +
        displayMode.getBitDepth() + " bit " + displayMode.getRefreshRate() +
        "Hz";
    }

    public int hashCode() {
        return displayMode.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof MyDisplayMode) {
            MyDisplayMode test = (MyDisplayMode)o;

            return test.displayMode.equals(this.displayMode);
        } else {
            return false;
        }
    }
}