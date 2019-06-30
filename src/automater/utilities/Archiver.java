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
import java.util.Base64;

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
    
    public static Object deserializeObject(String data)
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
            
            return result;
        }
        catch (Exception e)
        {
            
        }
        
        return null;
    }
}
