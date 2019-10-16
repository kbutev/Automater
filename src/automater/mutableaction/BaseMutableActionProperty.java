/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mutableaction;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * A mutable action property.
 *
 * @author Byti
 */
public interface BaseMutableActionProperty {
    public boolean isValid();
    public @Nullable String getInvalidError();
    
    public @NotNull String getValue();
    public void setValue(@NotNull String value);
    
    public @NotNull String getName();
    public void setName(@NotNull String name);
}
