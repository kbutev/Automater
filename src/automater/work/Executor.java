/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work;

import automater.utilities.Logger;
import automater.work.model.ExecutorState;
import automater.work.model.Macro;
import automater.work.model.MacroParameters;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.Robot;

/**
 * Executes macros. Can execute multiple macros simultaneously.
 * 
 * The ExecutorListener delegate methods are always called on the java AWT queue.
 *
 * @author Bytevi
 */
public interface Executor {
    
    interface Protocol {
        boolean isPerforming();
        ExecutorProcess.Protocol performMacro(@NotNull Macro macro, @NotNull Listener listener) throws Exception;
        ExecutorProcess.Protocol performMacro(@NotNull Macro macro, @NotNull MacroParameters parameters, @NotNull Listener listener) throws Exception;
        ExecutorProcess.Protocol performMacro(@NotNull Macro macro, @NotNull MacroParameters parameters, @NotNull Listener listener, @NotNull ExecutorTimer.Protocol timer) throws Exception;
        void stopAll();
    }
    
    /**
    * Forwards requests when the executor does work.
    */
    interface Listener {
        void onStart(int repeatTimes);
    
        void onActionExecute(@NotNull BaseAction action);
        void onActionUpdate(@NotNull BaseAction action);
        void onActionFinish(@NotNull BaseAction action);

        void onWait();
        void onCancel();
        void onRepeat(int numberOfTimesPlayed, int numberOfTimesToPlay);
        void onFinish();
    }
    
    class Impl implements Protocol {
        @NotNull private final Object _lock = new Object();

        @NotNull private final ExecutorState _state = new ExecutorState();

        @Nullable private Robot _robot;

        @Override
        public boolean isPerforming()
        {
            synchronized (_lock)
            {
                return !_state.isIdle();
            }
        }

        @Override
        public ExecutorProcess.Protocol performMacro(@NotNull Macro macro, @NotNull Listener listener) throws Exception
        {
            return performMacro(macro, new MacroParameters(), listener);
        }

        @Override
        public ExecutorProcess.Protocol performMacro(@NotNull Macro macro, @NotNull MacroParameters parameters, @NotNull Listener listener) throws Exception
        {
            return performMacro(macro, parameters, listener, new ExecutorTimer.Standard());
        }

        @Override
        public ExecutorProcess.Protocol performMacro(@NotNull Macro macro, @NotNull MacroParameters parameters, @NotNull Listener listener, @NotNull ExecutorTimer.Protocol timer) throws Exception
        {
            Logger.messageEvent(this, "Perform macro '" + macro.getName() + "' with parameters " + parameters.toString());

            synchronized (_lock)
            {
                if (_robot == null)
                {
                    initRobot();
                }

                ExecutorProcess.Protocol process = ExecutorProcess.create(_robot, macro.actions, timer);
                process.setListener(listener);

                _state.startProcess(process, macro, parameters);

                return process;
            }
        }

        @Override
        public void stopAll()
        {
            Logger.messageEvent("Executor", "Stop all processes");

            synchronized (_lock)
            {
                _state.stopAll();
            }
        }

        // # Private

        private void initRobot() throws Exception
        {
            if (_robot == null)
            {
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                _robot = new Robot(gd);
            }
        }
    }
}
