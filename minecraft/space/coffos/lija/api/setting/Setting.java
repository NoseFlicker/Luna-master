package space.coffos.lija.api.setting;

import space.coffos.lija.api.element.Element;

import java.util.ArrayList;

public class Setting {

    private boolean locked, doubleLocked;
    private double doubleVal, min, max;
    private ArrayList<String> options;
    private boolean onlyInt = false;
    private boolean booleanValue;
    private String stringValue;
    private Element parent;
    private String name;
    private Mode mode;

    private enum Mode {
        COMBO, CHECK, SLIDER
    }

    public Setting(String name, Element parent, String stringVal, ArrayList<String> options, Boolean locked) {
        this.name = name;
        this.parent = parent;
        this.stringValue = stringVal;
        this.options = options;
        this.locked = locked;
        mode = Mode.COMBO;
    }

    public Setting(String name, Element parent, boolean booleanVal) {
        this.name = name;
        this.parent = parent;
        this.booleanValue = booleanVal;
        mode = Mode.CHECK;
    }

    public Setting(String name, Element parent, double doubleVal, double min, double max, boolean onlyInt, Boolean locked) {
        this.name = name;
        this.parent = parent;
        this.doubleVal = doubleVal;
        this.min = min;
        this.max = max;
        this.onlyInt = onlyInt;
        this.doubleLocked = locked;
        mode = Mode.SLIDER;
    }

    public String getName() {
        return name;
    }

    public Element getParentMod() {
        return parent;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public int currentIndex() {
        return options.indexOf(stringValue);
    }

    public String getValString() {
        return stringValue.substring(0, 1).toUpperCase() + stringValue.substring(1);
    }

    public String getValStringForSaving() {
        return stringValue;
    }

    public boolean getValBoolean() {
        return booleanValue;
    }

    public void setValString(String in) {
        stringValue = in;
    }

    public void setValBoolean(boolean in) {
        booleanValue = in;
    }

    public void setValDouble(double in) {
        doubleVal = in;
    }

    public double getValDouble() {
        if (onlyInt) doubleVal = (int) doubleVal;
        return doubleVal;
    }

    public void setLockedMode(boolean lockedVal) {
        locked = lockedVal;
    }

    public boolean isLockedDouble() {
        return doubleLocked;
    }

    public void setLockedDouble(boolean lockedVal) {
        doubleLocked = lockedVal;
    }

    public boolean isLockedMode() {
        return locked;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public boolean isCombo() {
        return mode.equals(Mode.COMBO);
    }

    public boolean isCheck() {
        return mode.equals(Mode.CHECK);
    }

    public boolean isSlider() {
        return mode.equals(Mode.SLIDER);
    }

    public boolean onlyInt() {
        return onlyInt;
    }
}