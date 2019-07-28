/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

/**
 *
 * @author Bytevi
 */
public class Archiver 
{
    public static String serializeObject(Serializable object)
    {
        if (object == null)
        {
            return null;
        }
        
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(object);
            so.flush();
            
            String result = new String(Base64.getEncoder().encode(bo.toByteArray()));
            
            bo.close();
            so.close();
            
            return result;
        }
        catch (Exception e)
        {
            
        }
        
        return null;
    }
    
    public static String deserializeString(String data)
    {
        return deserializeObject(String.class, data);
    }
    
    public static ArrayList<String> deserializeStrings(String data)
    {
        return deserializeObject(String.class, data);
    }
    
    public static Integer deserializeInteger(String data)
    {
        return deserializeObject(Integer.class, data);
    }
    
    public static ArrayList<Integer> deserializeIntegers(String data)
    {
        return deserializeObject(Integer.class, data);
    }
    
    public static Double deserializeDouble(String data)
    {
        return deserializeObject(Double.class, data);
    }
    
    public static ArrayList<Double> deserializeDoubles(String data)
    {
        return deserializeObject(Double.class, data);
    }
    
    public static Date deserializeDate(String data)
    {
        return deserializeObject(Date.class, data);
    }
    
    public static <T> T deserializeObject(Class type, String data)
    {
        if (data == null || data.isEmpty())
        {
            return null;
        }

        try {
            byte b[] = Base64.getDecoder().decode(data.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            
            Object result = si.readObject();
            
            bi.close();
            si.close();
            
            if (!type.isInstance(result))
            {
                Class c = result.getClass();
                throw new Exception("Cannot deserialize array, element is not proper type - " + c.getCanonicalName());
            }
            
            return (T)result;
        }
        catch (Exception e)
        {
            
        }
        
        return null;
    }
    
    public static <T> ArrayList<T> deserializeArray(Class elementType, String data)
    {
        if (data == null || data.isEmpty())
        {
            return null;
        }

        try {
            byte b[] = Base64.getDecoder().decode(data.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            
            Object result = si.readObject();
            
            bi.close();
            si.close();
            
            if (!(result instanceof ArrayList))
            {
                throw new Exception("Cannot deserialize array, object is not array");
            }
            
            ArrayList<Object> array = (ArrayList<Object>)result;
            
            if (!array.isEmpty())
            {
                Object first = array.get(0);
                Class c = elementType;
                
                if (!c.isInstance(first))
                {
                    throw new Exception("Cannot deserialize array, element is not proper type - " + c.getCanonicalName());
                }
            }
            
            return (ArrayList<T>)result;
        }
        catch (Exception e)
        {
            Logger.utilityError(null, e.toString());
        }
        
        return null;
    }
}
