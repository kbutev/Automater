/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.model;

import automater.work.ExecutorTimer;
import org.jetbrains.annotations.NotNull;
import java.awt.Robot;
import java.util.HashSet;
import java.awt.Dimension;
import java.util.Set;

/**
 * Contains information about the current state of action execution.
 *
 * The cleanup() method should be called only once, by the execution process to
 * make sure that all keys are released at the end.
 *
 * Has one single, unique robot instance, used to simulate keystrokes.
 *
 *
 * When an action is simulating a user keyboard or mouse key, the action should
 * call one of these two methods: onPressKey() onReleaseKey()
 *
 * Keeps track of which keys are pressed and which are not.
 *
 * Context objects are not guaranteed to be thread safe.
 *
 * @author Bytevi
 */
public interface ActionContext {

    static ActionContext.Protocol build(@NotNull Robot robot, @NotNull ExecutorTimer.Protocol timer, @NotNull Dimension recordedScreenSize, @NotNull Dimension currentScreenSize) {
        return new Impl(robot, timer, recordedScreenSize, currentScreenSize);
    }

    interface Protocol {

        @NotNull Robot getRobot();
        @NotNull ExecutorTimer.Protocol getTimer();
        @NotNull Dimension getRecordedScreenSize();
        @NotNull Dimension getCurrentScreenSize();

        @NotNull ActionSystemKeyModifiers getPressedModifiers();
        @NotNull Set<ActionSystemKey> getPressedKeys();
        boolean isModifierPressed(@NotNull ActionSystemKeyModifierValue modifier);
        boolean isCtrlModifierPressed();
        boolean isAltModifierPressed();
        boolean isShiftModifierPressed();
        boolean isKeyPressed(@NotNull ActionSystemKey key);

        void onPressKey(@NotNull ActionSystemKey key, @NotNull ActionSystemKeyModifiers modifiers);
        void onReleaseKey(@NotNull ActionSystemKey key, @NotNull ActionSystemKeyModifiers modifiers);

        void cleanup();
    }

    /**
     * Holds values for a specific action execution context.
     *
     * Not thread safe.
     */
    class Impl implements Protocol {

        @NotNull private final Robot _robot;
        @NotNull private final ExecutorTimer.Protocol _timer;
        @NotNull private final Dimension _recordedScreenSize;
        @NotNull private final Dimension _currentScreenSize;

        @NotNull private ActionSystemKeyModifiers _modifiers = ActionSystemKeyModifiers.none();

        @NotNull private final HashSet<ActionSystemKey> _keysPressed = new HashSet<>();

        public Impl(@NotNull Robot robot, @NotNull ExecutorTimer.Protocol timer, @NotNull Dimension recordedScreenSize, @NotNull Dimension currentScreenSize) {
            this._robot = robot;
            this._timer = timer;
            this._recordedScreenSize = recordedScreenSize;
            this._currentScreenSize = currentScreenSize;
        }

        @Override
        public @NotNull Robot getRobot() {
            return _robot;
        }

        @Override
        public @NotNull ExecutorTimer.Protocol getTimer() {
            return _timer;
        }

        @Override
        public @NotNull Dimension getRecordedScreenSize() {
            return _recordedScreenSize;
        }

        @Override
        public @NotNull Dimension getCurrentScreenSize() {
            return _currentScreenSize;
        }

        @Override
        public @NotNull ActionSystemKeyModifiers getPressedModifiers() {
            return _modifiers;
        }

        @Override
        public @NotNull Set<ActionSystemKey> getPressedKeys() {
            return new HashSet(_keysPressed);
        }

        @Override
        public boolean isModifierPressed(@NotNull ActionSystemKeyModifierValue modifier) {
            return _modifiers.modifiers.contains(modifier);
        }

        @Override
        public boolean isCtrlModifierPressed() {
            return _modifiers.modifiers.contains(ActionSystemKeyModifierValue.CTRL);
        }

        @Override
        public boolean isAltModifierPressed() {
            return _modifiers.modifiers.contains(ActionSystemKeyModifierValue.ALT);
        }

        @Override
        public boolean isShiftModifierPressed() {
            return _modifiers.modifiers.contains(ActionSystemKeyModifierValue.SHIFT);
        }

        @Override
        public boolean isKeyPressed(@NotNull ActionSystemKey key) {
            return _keysPressed.contains(key);
        }

        @Override
        public void onPressKey(@NotNull ActionSystemKey key, @NotNull ActionSystemKeyModifiers modifiers) {
            _modifiers = _modifiers.combine(modifiers);

            _keysPressed.add(key);
        }

        @Override
        public void onReleaseKey(@NotNull ActionSystemKey key, @NotNull ActionSystemKeyModifiers modifiers) {
            for (ActionSystemKeyModifierValue value : modifiers.modifiers) {
                _modifiers = _modifiers.removeModifier(value);
            }

            _keysPressed.remove(key);
        }

        @Override
        public void cleanup() {
            for (ActionSystemKeyModifierValue value : _modifiers.modifiers) {
                try {
                    _robot.keyRelease(value.getValue());
                } catch (Exception e) {

                }
            }

            for (ActionSystemKey key : _keysPressed) {
                try {
                    _robot.keyRelease(key.getValue());
                } catch (Exception e) {

                }
            }
        }
    }
}
