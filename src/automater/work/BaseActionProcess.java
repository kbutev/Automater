/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work;

import automater.work.model.ActionContext;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the process of performing an action.
 *
 * This object as a state: active, as long as the action is performing, and
 * inactive if the action is not performing.
 *
 * The perform(ActionContext) will never block the main thread, even if the
 * action is complex.
 *
 * @author Bytevi
 */
public interface BaseActionProcess {

    public boolean isActive();
    public @NotNull BaseAction getAction();

    public void perform(@NotNull ActionContext.Protocol context) throws Exception;
}
