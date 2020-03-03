/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.Nullable;
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
    private static final Archiver _archiver = new Archiver();
    
    private Archiver()
    {
        
    }
    
    public static @Nullable String serializeObject(Serializable object)
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
            Logger.utilityError(_archiver, "Cannot serialize object " + object.toString() + ", " + e.toString());
        }
        
        return null;
    }
    
    public static @Nullable String deserializeString(String data) throws Exception
    {
        return deserializeObject(String.class, data);
    }
    
    public static @Nullable ArrayList<String> deserializeStrings(String data) throws Exception
    {
        return deserializeObject(String.class, data);
    }
    
    public static @Nullable Integer deserializeInteger(String data) throws Exception
    {
        return deserializeObject(Integer.class, data);
    }
    
    public static @Nullable ArrayList<Integer> deserializeIntegers(String data) throws Exception
    {
        return deserializeObject(Integer.class, data);
    }
    
    public static @Nullable Double deserializeDouble(String data) throws Exception
    {
        return deserializeObject(Double.class, data);
    }
    
    public static @Nullable ArrayList<Double> deserializeDoubles(String data) throws Exception
    {
        return deserializeObject(Double.class, data);
    }
    
    public static @Nullable Date deserializeDate(String data) throws Exception
    {
        return deserializeObject(Date.class, data);
    }
    
    public static <T> @Nullable T deserializeObject(Class type, String data) throws Exception
    {
        if (data == null || data.isEmpty())
        {
            throw new Exception("Cannot deserialize empty data");
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
            Logger.utilityError(_archiver, e.toString());
        }
        
        return null;
    }
    
    public static <T> @Nullable ArrayList<T> deserializeArray(Class elementType, String data) throws Exception
    {
        if (data == null || data.isEmpty())
        {
            throw new Exception("Cannot deserialize empty data");
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
            Logger.utilityError(_archiver, e.toString());
        }
        
        return null;
    }
}
