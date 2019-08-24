/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Bytevi
 */
public class CollectionUtilities {
    public static <T> List<T> copy(List<T> data)
    {
        return new ArrayList<>(data);
    }
    
    public static <T> Set<T> copy(Set<T> data)
    {
        return new HashSet<>(data);
    }
    
    public static <T> List<T> copyAsImmutable(List<T> data)
    {
        return Collections.unmodifiableList(copy(data));
    }
    
    public static <T> Set<T> copyAsImmutable(Set<T> data)
    {
        return Collections.unmodifiableSet(copy(data));
    }
    
    public static <T> List<T> copyAsReversed(List<T> data)
    {
        List<T> dataCopy = CollectionUtilities.copy(data);
        Collections.reverse(dataCopy);
        return dataCopy;
    }
}
