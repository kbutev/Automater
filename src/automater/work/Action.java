/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.TextValue;
import automater.input.InputDescriptions;
import automater.input.InputDoNothing;
import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.model.ActionSystemKey;
import automater.work.model.ActionSystemKeyModifierValue;
import automater.work.model.ActionSystemKeyModifiers;
import automater.work.model.BaseActionContext;
import automater.work.parser.ActionKeyTranslator;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import automater.input.InputKeyClick;
import automater.input.InputMouseMotion;
import automater.input.InputMouseMove;
import automater.input.InputKey;
import automater.input.InputKeyValue;
import automater.input.InputMouse;
import automater.input.InputMouseWheel;
import automater.input.InputScreenshot;
import automater.input.InputSystemCommand;
import automater.utilities.DeviceNotifications;
import automater.utilities.FileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.imageio.ImageIO;

/**
 * Simulates user actions such as keyboard and mouse clicks.
 * 
 * Can also perform more complex system actions, like execute system commands
 * and take screen screenshots.
 *
 * @author Bytevi
 */
public class Action extends BaseAction {
    public static @NotNull Action createDoNothing(long timestamp)
    {
        return new ActionDoNothing(timestamp);
    }
    
    public static @NotNull Action createWait(long timestamp, long milliseconds)
    {
        return new ActionWait(timestamp, milliseconds);
    }
    
    public static @NotNull Action createKeyClick(long timestamp, @NotNull InputKeyClick keyClick, @Nullable Description description) throws Exception
    {
        boolean isMouseClick = false;
        InputKeyValue key = keyClick.getKeyValue().value;
        
        if (key == InputKeyValue._MOUSE_LEFT_CLICK || 
                key == InputKeyValue._MOUSE_RIGHT_CLICK || 
                key == InputKeyValue._MOUSE_MIDDLE_CLICK)
        {
            isMouseClick = true;
        }
        
        if (isMouseClick)
        {
            if (keyClick.isPress())
            {
                return new ActionMouseKeyPress(timestamp, keyClick, description);
            }
            
            return new ActionMouseKeyRelease(timestamp, keyClick, description);
        }
        
        if (keyClick.isPress())
        {
            return new ActionKeyPress(timestamp, keyClick, description);
        }
        
        return new ActionKeyRelease(timestamp, keyClick, description);
    }
    
    public static @NotNull Action createMouseMovement(long timestamp, int x, int y, @Nullable Description description) throws Exception
    {
        return new ActionMouseMove(timestamp, x, y, description);
    }
    
    public static @NotNull Action createMouseMovement(long timestamp, @NotNull List<InputMouseMove> mouseMovements, @Nullable Description description) throws Exception
    {
        int maxNumberOfSubmovements = ActionSettingsManager.getDefault().getMaxNumberOfSubmovements();
        
        return new ActionMouseMovement(timestamp, mouseMovements, maxNumberOfSubmovements, description);
    }
    
    public static @NotNull Action createMouseWheel(long timestamp, int scrollValue, @Nullable Description description) throws Exception
    {
        return new ActionMouseWheel(timestamp, scrollValue, description);
    }
    
    public static @NotNull Action createSystemCommand(long timestamp, @NotNull String value, boolean reportsErrors, @Nullable Description description) throws Exception
    {
        return new ActionSystemCommand(timestamp, value, reportsErrors, description);
    }
    
    public static @NotNull Action createScreenshot(long timestamp, @NotNull String path, @Nullable Description description) throws Exception
    {
        return new ActionScreenshot(timestamp, path, description);
    }
    
    @Override
    public boolean isComplex()
    {
        return getWaitTime() > 0;
    }
    
    @Override
    public long getWaitTime()
    {
        return 0;
    }
    
    @Override
    public long getPerformTime()
    {
        Errors.throwNotImplemented("Action getPerformTime method has not been implemented!");
        return 0;
    }
    
    @Override
    public long getPerformDuration()
    {
        return 0;
    }
    
    @Override
    public long getPerformEndTime()
    {
        return getPerformTime();
    }
    
    @Override
    public void perform(@NotNull BaseActionContext context)
    {
        Errors.throwNotImplemented("Action perform method has not been implemented!");
    }

    @Override
    public @Nullable String getStandart() {
        return toString();
    }

    @Override
    public @Nullable String getVerbose() {
        return getStandart();
    }

    @Override
    public @Nullable String getStandartTooltip() {
        return getStandart();
    }

    @Override
    public @Nullable String getVerboseTooltip() {
        return getStandart();
    }

    @Override
    public @Nullable String getName() {
        return getStandart();
    }

    @Override
    public @Nullable String getDebug() {
        return getVerbose();
    }
}

class ActionDoNothing extends Action implements InputDoNothing {
    long time;
    @NotNull Description description;
    
    ActionDoNothing(long time)
    {
        this.time = time;
        this.description = InputDescriptions.getDoNothingDescription(time);
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        
    }
    
    @Override
    public @Nullable String getStandart() {
        return description.getStandart();
    }
    
    @Override
    public @Nullable String getVerbose() {
        return description.getVerbose();
    }
    
    @Override
    public long getTimestamp() {
        return time;
    }

    @Override
    public long getDuration() {
        return 0;
    }
}

class ActionWait extends Action implements InputDoNothing {
    long time;
    long wait;
    @NotNull Description description;
    
    ActionWait(long time, long wait)
    {
        this.time = time;
        this.wait = wait;
        this.description = InputDescriptions.getWaitDescription(time, wait);
    }
    
    @Override
    public long getWaitTime()
    {
        return wait;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(@NotNull BaseActionContext context)
    {
        double waitTime = wait;
        waitTime /= context.getTimer().getTimeScale();
        
        long actualWaitTime = (long)waitTime;
        
        try {
            Thread.sleep(actualWaitTime);
        } catch (Exception e) {
            
        }
    }
    
    @Override
    public @Nullable String getStandart() {
        return description.getStandart();
    }
    
    @Override
    public @Nullable String getVerbose() {
        return description.getVerbose();
    }
    
    @Override
    public long getTimestamp() {
        return time;
    }
    
    @Override
    public long getDuration() {
        return wait;
    }
}

class ActionKeyPress extends Action implements InputKeyClick {
    long time;
    @NotNull InputKey inputKey;
    @NotNull ActionSystemKey key;
    @NotNull ActionSystemKeyModifiers modifiers;
    
    @Nullable String standartDescription;
    @Nullable String verboseDescription;
    
    ActionKeyPress(long time, @NotNull InputKeyClick keyClick, @Nullable Description description) throws Exception
    {
        this.time = time;
        this.inputKey = keyClick.getKeyValue();
        this.key = ActionKeyTranslator.translateKeystroke(keyClick.getKeyValue());
        this.modifiers = ActionKeyTranslator.translateModifiers(keyClick.getKeyValue());
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(@NotNull BaseActionContext context)
    {
        Robot robot = context.getRobot();
        
        // First, simulate press modifier keys
        for (ActionSystemKeyModifierValue value : modifiers.modifiers)
        {
            if (!context.isModifierPressed(value))
            {
                robot.keyPress(value.getValue());
            }
        }
        
        // Second, simulate press keystroke
        if (!context.isKeyPressed(key))
        {
            if (key.isKeyboardKey())
            {
                robot.keyPress(key.getValue());
            }
            
            if (key.isMouseKey())
            {
                robot.mousePress(key.getValue());
            }
        }
        
        // Finally, alert context
        context.onPressKey(key, modifiers);
    }
    
    @Override
    public @Nullable String getStandart() {
        return standartDescription;
    }
    
    @Override
    public @Nullable String getVerbose() {
        return verboseDescription;
    }

    @Override
    public @NotNull InputKey getKeyValue() {
        return inputKey;
    }

    @Override
    public boolean isPress() {
        return true;
    }

    @Override
    public long getTimestamp() {
        return time;
    }
}

class ActionKeyRelease extends Action implements InputKeyClick {
    long time;
    @NotNull InputKey inputKey;
    @NotNull ActionSystemKey key;
    @NotNull ActionSystemKeyModifiers modifiers;
    
    @Nullable String standartDescription;
    @Nullable String verboseDescription;
    
    ActionKeyRelease(long time, @NotNull InputKeyClick keyClick, @Nullable Description description) throws Exception
    {
        this.time = time;
        this.inputKey = keyClick.getKeyValue();
        this.key = ActionKeyTranslator.translateKeystroke(keyClick.getKeyValue());
        this.modifiers = ActionKeyTranslator.translateModifiers(keyClick.getKeyValue());
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(@NotNull BaseActionContext context)
    {
        Robot robot = context.getRobot();
        
        // First, simulate release modifier keys
        for (ActionSystemKeyModifierValue value : modifiers.modifiers)
        {
            if (context.isModifierPressed(value))
            {
                robot.keyRelease(value.getValue());
            }
        }
        
        // Second, simulate release keystroke
        if (context.isKeyPressed(key))
        {
            if (key.isKeyboardKey())
            {
                robot.keyRelease(key.getValue());
            }
            
            if (key.isMouseKey())
            {
                robot.mouseRelease(key.getValue());
            }
        }
        
        // Finally, alert context
        context.onReleaseKey(key, modifiers);
    }
    
    @Override
    public @Nullable String getStandart() {
        return standartDescription;
    }
    
    @Override
    public @Nullable String getVerbose() {
        return verboseDescription;
    }

    @Override
    public @NotNull InputKey getKeyValue() {
        return inputKey;
    }

    @Override
    public boolean isPress() {
        return false;
    }

    @Override
    public long getTimestamp() {
        return time;
    }
}

class ActionMouseKeyPress extends ActionKeyPress implements InputMouse {
    ActionMouseKeyPress(long time, @NotNull InputKeyClick keyClick, @Nullable Description description) throws Exception
    {
        super(time, keyClick, description);
    }
}

class ActionMouseKeyRelease extends ActionKeyRelease implements InputMouse {
    ActionMouseKeyRelease(long time, @NotNull InputKeyClick keyClick, @Nullable Description description) throws Exception
    {
        super(time, keyClick, description);
    }
}

class ActionMouseMove extends Action implements InputMouseMove {
    final long time;
    final int x;
    final int y;
    
    @Nullable String standartDescription;
    @Nullable String verboseDescription;
    
    ActionMouseMove(long time, int x, int y, @Nullable Description description)
    {
        this.time = time;
        this.x = x > 0 ? x : 0;
        this.y = y > 0 ? y : 0;
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public boolean isComplex()
    {
        return false;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(@NotNull BaseActionContext context)
    {
        float screenScaleX = context.getCurrentScreenSize().width;
        screenScaleX /= context.getRecordedScreenSize().width;
        float screenScaleY = context.getCurrentScreenSize().height;
        screenScaleY /= context.getRecordedScreenSize().height;
        
        int resultX = (int)(x * screenScaleX);
        int resultY = (int)(y * screenScaleY);
        
        context.getRobot().mouseMove(resultX, resultY);
    }
    
    @Override
    public @Nullable String getStandart() {
        return standartDescription;
    }
    
    @Override
    public @Nullable String getVerbose() {
        return verboseDescription;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public long getTimestamp() {
        return time;
    }
}

class ActionMouseMovement extends Action implements InputMouseMotion {
    final long time;
    final long duration;
    @NotNull final List<InputMouseMove> movements;
    @NotNull final List<BaseAction> actions;
    final int maxNumberOfSubmovements;
    
    @Nullable String standartDescription;
    @Nullable String verboseDescription;
    
    ActionMouseMovement(long time, @NotNull List<InputMouseMove> movements, int maxNumberOfSubmovements, @Nullable Description description)
    {
        this.time = time;
        this.movements = movements;
        this.actions = parseMouseMoveActions(CollectionUtilities.copyAsImmutable(movements));
        long lastPerformTime = this.actions.get(this.actions.size()-1).getPerformTime();
        this.duration = lastPerformTime - time;
        this.maxNumberOfSubmovements = maxNumberOfSubmovements;
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public boolean isComplex()
    {
        return true;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public long getPerformDuration()
    {
        return duration;
    }
    
    @Override
    public long getPerformEndTime()
    {
        return time + duration;
    }
    
    @Override
    public void perform(@NotNull BaseActionContext context)
    {
        Logger.messageEvent(this, "Performing mouse motion action with " + String.valueOf(actions.size()) + " movements...");
        
        BaseExecutorTimer timer = context.getTimer();
        
        for (int e = 0; e < actions.size(); )
        {
            BaseAction action = actions.get(e);
            
            if (timer.canPerformNextAction(action))
            {
                action.perform(context);
                e++;
            }
            else
            {
                try {
                    wait(2);
                } catch (Exception exc) {
                    
                }
            }
        }
        
        Logger.messageEvent(this, "Finished mouse motion action");
    }
    
    @Override
    public @Nullable String getStandart() {
        return standartDescription;
    }
    
    @Override
    public @Nullable String getVerbose() {
        return verboseDescription;
    }
    
    @Override
    public int numberOfMovements() {
        return movements.size();
    }
    
    @Override
    public @NotNull List<InputMouseMove> getMoves() {
        return CollectionUtilities.copyAsImmutable(movements);
    }

    @Override
    public @NotNull InputMouseMove getFirstMove() {
        return movements.get(0);
    }

    @Override
    public @NotNull InputMouseMove getLastMove() {
        return movements.get(movements.size()-1);
    }

    @Override
    public @NotNull InputMouseMove getMoveAt(int index) {
        return movements.get(index);
    }

    @Override
    public long getTimestamp() {
        return time;
    }
    
    private @NotNull ArrayList<BaseAction> parseMouseMoveActions(@NotNull List<InputMouseMove> movements)
    {
        final int size = movements.size();
        
        ArrayList<BaseAction> result = new ArrayList<>();
        BaseAction previousAction = null;
        
        for (int e = 0; e < size; e++)
        {
            InputMouseMove current = movements.get(e);
            InputMouseMove next = null;
            
            if (e + 1 < size)
            {
                next = movements.get(e+1);
            }
            
            ArrayList<BaseAction> subActions = parseMouseMoveAction(current, next);
            result.addAll(subActions);
        }
        
        return result;
    }
    
    private @NotNull ArrayList<BaseAction> parseMouseMoveAction(@NotNull InputMouseMove current, @Nullable InputMouseMove next)
    {
        ArrayList<BaseAction> result = new ArrayList<>();
        
        final long currentStartTime = current.getTimestamp();
        final int currentX = current.getX();
        final int currentY = current.getY();
        
        // When next is null, create only one subaction
        if (next == null)
        {
            result.add(new ActionMouseMove(currentStartTime, currentX, currentY, null));
            return result;
        }
        
        final long nextStartTime = next.getTimestamp();
        final int nextX = next.getX();
        final int nextY = next.getY();
        
        // Break down the move action in multiple actions, so we can achieve smooth movement
        final long timeDiff = diff(currentStartTime, nextStartTime);
        final int xDiff = diff(currentX, nextX);
        final int yDiff = diff(currentY, nextY);
        
        result.add(new ActionMouseMove(currentStartTime, currentX, currentY, null));
        
        for (int e = maxNumberOfSubmovements; e > 0; e--)
        {
            final long startTime = currentStartTime + (timeDiff / maxNumberOfSubmovements);
            final int x = currentX + (xDiff / maxNumberOfSubmovements);
            final int y = currentY + (yDiff / maxNumberOfSubmovements);
            actions.add(new ActionMouseMove(startTime, x, y, null));
        }
        
        return result;
    }
    
    private int diff(int a, int b)
    {
        int result = a - b;
        
        if (result < 0)
        {
            result *= -1;
        }
        
        return result;
    }
    
    private long diff(long a, long b)
    {
        long result = a - b;
        
        if (result < 0)
        {
            result *= -1;
        }
        
        return result;
    }
}

class ActionMouseWheel extends Action implements InputMouseWheel {
    final long time;
    final int scrollValue;
    
    @Nullable String standartDescription;
    @Nullable String verboseDescription;
    
    ActionMouseWheel(long time, int scrollValue, @Nullable Description description)
    {
        this.time = time;
        this.scrollValue = scrollValue;
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public boolean isComplex()
    {
        return false;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(@NotNull BaseActionContext context)
    {
        int amount = convertScrollWheelValueToRobotWheelValue(scrollValue);
        context.getRobot().mouseWheel(amount);
    }
    
    @Override
    public @Nullable String getStandart() {
        return standartDescription;
    }
    
    @Override
    public @Nullable String getVerbose() {
        return verboseDescription;
    }

    @Override
    public int getScrollValue() {
        return scrollValue;
    }
    
    @Override
    public long getTimestamp() {
        return time;
    }
    
    private int convertScrollWheelValueToRobotWheelValue(int value)
    {
        return (value / -2);
    }
}

class ActionSystemCommand extends Action implements InputSystemCommand {
    final long time;
    
    @NotNull final String value;
    final boolean reportsErrors;
    
    @Nullable String standartDescription;
    @Nullable String verboseDescription;
    
    ActionSystemCommand(long time, @NotNull String value, boolean reportsErrors, @Nullable Description description)
    {
        this.time = time;
        this.value = value;
        this.reportsErrors = reportsErrors;
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public boolean isComplex()
    {
        return false;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(@NotNull BaseActionContext context)
    {
        try {
            Runtime.getRuntime().exec(value);
        } catch (Exception e) {
            Logger.warning(this, "Failed to perform command '" + value + "' because " + e.getMessage());
            
            if (reportsErrors)
            {
                String title = TextValue.getText(TextValue.Commands_NotificationErrorTitle, value);
                String message = TextValue.getText(TextValue.Commands_NotificationErrorMessage, e.getMessage());
                
                DeviceNotifications.getShared().displayGlobalNotification(title, message);
            }
        }
    }
    
    @Override
    public @Nullable String getStandart() {
        return standartDescription;
    }
    
    @Override
    public @Nullable String getVerbose() {
        return verboseDescription;
    }
    
    @Override
    public @NotNull String getValue() {
        return value;
    }
    
    @Override
    public boolean reportsErrors() {
        return reportsErrors;
    }
}

class ActionScreenshot extends Action implements InputScreenshot {
    static final String yearPlaceholder = "%y";
    static final String monthPlaceholder = "%mt";
    static final String dayPlaceholder = "%d";
    static final String hourPlaceholder = "%h";
    static final String minPlaceholder = "%min";
    static final String secPlaceholder = "%s";
    static final String msPlaceholder = "%ms";
    
    @NotNull static final String timestampPlaceholder = "%t";
    
    final long time;
    
    @NotNull final String screenshotPath;
    
    @Nullable String standartDescription;
    @Nullable String verboseDescription;
    
    ActionScreenshot(long time, @NotNull String path, @Nullable Description description)
    {
        this.time = time;
        this.screenshotPath = path;
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public boolean isComplex()
    {
        return false;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(@NotNull BaseActionContext context)
    {
        Dimension screen = context.getCurrentScreenSize();
        Rectangle fullScreenArea = new Rectangle(0, 0, screen.width, screen.height);
        String filePath = evaluatePath(screenshotPath, context);
        String pathWithoutFileName = FileSystem.createFilePathWithoutTheFileName(filePath);
        
        try {
            createFolderForScreenshot(pathWithoutFileName);
            BufferedImage result = context.getRobot().createScreenCapture(fullScreenArea);
            File outputFile = new File(filePath);
            ImageIO.write(result, "jpg", outputFile);
        } catch (Exception e) {
            Logger.warning(this, "Failed to capture screenshot: " + e.toString());
        }
    }
    
    @Override
    public @Nullable String getStandart() {
        return standartDescription;
    }
    
    @Override
    public @Nullable String getVerbose() {
        return verboseDescription;
    }
    
    @Override
    public @NotNull String getPath() {
        return screenshotPath;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public @NotNull Rectangle getArea() {
        return new Rectangle();
    }
    
    private String evaluatePath(@NotNull String path, @NotNull BaseActionContext context) {
        // Make sure that the path is OK
        path = FileSystem.createFilePathEndingWithExtension(path, ".jpg");
        
        Date now = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(now);
        
        // Evaluate aguments
        if (path.contains(yearPlaceholder))
        {
            path = path.replaceAll(yearPlaceholder, String.valueOf(calendar.get(Calendar.YEAR)));
        }
        
        if (path.contains(monthPlaceholder))
        {
            path = path.replaceAll(monthPlaceholder, String.valueOf(calendar.get(Calendar.MONTH) + 1));
        }
        
        if (path.contains(dayPlaceholder))
        {
            path = path.replaceAll(dayPlaceholder, String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }
        
        if (path.contains(hourPlaceholder))
        {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            path = path.replaceAll(hourPlaceholder, String.format("%02d", hour));
        }
        
        if (path.contains(minPlaceholder))
        {
            int min = calendar.get(Calendar.MINUTE);
            path = path.replaceAll(minPlaceholder, String.format("%02d", min));
        }
        
        if (path.contains(secPlaceholder))
        {
            int sec = calendar.get(Calendar.SECOND);
            path = path.replaceAll(secPlaceholder, String.format("%02d", sec));
        }
        
        if (path.contains(msPlaceholder))
        {
            int sec = calendar.get(Calendar.MILLISECOND);
            path = path.replaceAll(msPlaceholder, String.format("%03d", sec));
        }
        
        if (path.contains(timestampPlaceholder))
        {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int sec = calendar.get(Calendar.SECOND);
            
            String timestamp = String.format("%02d", hour);
            timestamp += "-" + String.format("%02d", min);
            timestamp += "-" + String.format("%02d", sec);
            
            path = path.replaceAll(timestampPlaceholder, timestamp);
        }
        
        return path;
    }
    
    private void createFolderForScreenshot(@NotNull String folderPath) throws Exception
    {
        File directory = new File(folderPath);
        
        directory.mkdirs();
    }
}
