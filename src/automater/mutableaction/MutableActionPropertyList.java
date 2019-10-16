/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mutableaction;

import com.sun.istack.internal.NotNull;
import java.util.List;

/**
 * List of strings mutable action property.
 *
 * @author Byti
 */
public interface MutableActionPropertyList extends BaseMutableActionProperty {
    public @NotNull List<String> getValues();
}
