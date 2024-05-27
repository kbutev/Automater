/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.SwingUtilities;

/**
 * Contains utility methods for performing callbacks on the UI thread (swing thread).
 * Used this to make changes to the UI, since making changes from background threads
 * is not allowed.
 * 
 * @author Bytevi
 */
public class LooperSwing {
    private static LooperSwing singleton;
    
    private LooperSwing()
    {
        
    }

    synchronized public static @NotNull LooperSwing getShared()
    {
        if (singleton == null)
        {
            singleton = new LooperSwing();
        }

        return singleton;
    }
    
    public void performCallback(@NotNull final SimpleCallback callback)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                callback.perform();
            }
        });
    }
    
    public <T> void performCallback(@NotNull final Callback<T> callback, @Nullable final T parameter)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                callback.perform(parameter);
            }
        });
    }
}
