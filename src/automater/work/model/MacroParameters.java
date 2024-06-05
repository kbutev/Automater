/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.model;

import automater.TextValue;
import java.io.Serializable;

/**
 * Holds macro preference values that can modify the execution of a macro.
 *
 * @author Bytevi
 */
public class MacroParameters implements Serializable {

    public final double playSpeed;
    public final int repeatTimes;
    public final boolean repeatForever;

    public MacroParameters() {
        this.playSpeed = 1;
        this.repeatTimes = 0;
        this.repeatForever = false;
    }

    public MacroParameters(double playSpeed, int repeatTimes, boolean repeatForever) {
        this.playSpeed = playSpeed;
        this.repeatTimes = !repeatForever ? repeatTimes : 0;
        this.repeatForever = repeatForever;
    }

    @Override
    public String toString() {
        if (isStandart()) {
            return TextValue.getText(TextValue.MacroParameters_Default);
        }

        String playSpeedText = TextValue.getText(TextValue.MacroParameters_Playspeed, String.valueOf(playSpeed));

        String repeatTimesText = String.valueOf(repeatTimes);
        String repeatNeverText = TextValue.getText(TextValue.MacroParameters_RepeatNever);
        String repeatText = repeatTimes > 0 ? TextValue.getText(TextValue.MacroParameters_Repeat, repeatTimesText) : repeatNeverText;

        if (repeatForever) {
            repeatText = TextValue.getText(TextValue.MacroParameters_RepeatForever);
        }

        return playSpeedText + ", " + repeatText;
    }

    public boolean isStandart() {
        return playSpeed == 1 && repeatTimes == 0 && !repeatForever;
    }
}
