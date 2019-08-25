/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.utilities.CollectionUtilities;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Bytevi
 */
public class ActionSystemKeyModifiers {
    public final Set<ActionSystemKeyModifierValue> modifiers;
    
    public ActionSystemKeyModifiers()
    {
        this.modifiers = new HashSet<>();
    }
    
    public ActionSystemKeyModifiers(Set<ActionSystemKeyModifierValue> modifiers)
    {
        this.modifiers = CollectionUtilities.copyAsImmutable(modifiers);
    }
    
    public static ActionSystemKeyModifiers none()
    {
        return new ActionSystemKeyModifiers();
    }
    
    public static ActionSystemKeyModifiers createModifierValues(Set<ActionSystemKeyModifierValue> modifiers)
    {
        return new ActionSystemKeyModifiers(modifiers);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ActionSystemKeyModifiers)
        {
            ActionSystemKeyModifiers other = (ActionSystemKeyModifiers)o;
            
            return modifiers.equals(other.modifiers);
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.modifiers);
        return hash;
    }
    
    public ActionSystemKeyModifiers combine(ActionSystemKeyModifiers modifiers)
    {
        if (modifiers.isNone())
        {
            return this;
        }
        
        HashSet<ActionSystemKeyModifierValue> values = new HashSet<>();
        
        for (ActionSystemKeyModifierValue value : modifiers.modifiers)
        {
            values.add(value);
        }
        
        return ActionSystemKeyModifiers.createModifierValues(values);
    }
    
    public ActionSystemKeyModifiers removeModifier(ActionSystemKeyModifierValue value)
    {
        HashSet<ActionSystemKeyModifierValue> values = new HashSet<>(this.modifiers);
        
        values.remove(value);
        
        return ActionSystemKeyModifiers.createModifierValues(values);
    }
    
    public boolean isNone()
    {
        return this.modifiers.isEmpty();
    }
}
