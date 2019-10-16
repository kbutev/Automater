/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import com.sun.istack.internal.Nullable;

/**
 * Defines commonly used string methods used to parse various values.
 *
 * @author Bytevi
 */
public class StringFormatting {
    public static boolean isStringAlphabetic(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        return string.matches("[A-Za-z]+");
    }
    
    public static boolean isStringNumeric(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        return string.matches("[0-9]+");
    }
    
    public static boolean isStringAlphanumeric(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        return string.matches("[A-Za-z0-9]+");
    }
    
    public static boolean isStringANumber(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        try {  
            Double.parseDouble(string); 
            return true;
        } catch (NumberFormatException e){  
            return false;  
        }  
    }
    
    public static boolean isStringAnInt(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        try {  
            Integer.parseInt(string); 
            return true;
        } catch (NumberFormatException e){  
            return false;  
        }  
    }
    
    public static boolean isStringAPositiveInt(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        try {  
            return Integer.parseInt(string) > 0;
        } catch (NumberFormatException e){  
            return false;  
        }  
    }
    
    public static boolean isStringANonNegativeInt(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        try {  
            return Integer.parseInt(string) >= 0; 
        } catch (NumberFormatException e){  
            return false;  
        }  
    }
    
    public static boolean isStringADouble(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        try {  
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e){  
            return false;  
        }  
    }
    
    public static boolean isStringAPositiveDouble(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        try {  
            return Double.parseDouble(string) > 0; 
        } catch (NumberFormatException e){  
            return false;  
        }  
    }
    
    public static boolean isStringANonNegativeDouble(@Nullable String string)
    {
        if (string == null)
        {
            return false;
        }
        
        try {  
            return Double.parseDouble(string) >= 0; 
        } catch (NumberFormatException e){  
            return false;  
        }  
    }
}
