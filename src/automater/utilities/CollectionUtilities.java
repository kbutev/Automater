/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Bytevi
 */
public class CollectionUtilities {
    public static <T> List<T> copy(List<T> data)
    {
        return new ArrayList<>(data);
    }
    
    public static <T> List<T> copyAsImmutable(List<T> data)
    {
        return Collections.unmodifiableList(new ArrayList<>(data));
    }
    
    public static <T> List<T> copyAsReversed(List<T> data)
    {
        List<T> dataCopy = CollectionUtilities.copy(data);
        Collections.reverse(dataCopy);
        return dataCopy;
    }
}
