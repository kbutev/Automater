/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

/**
 *
 * @author Bytevi
 */
public class Errors {
    public static void throwUnknownError(String description) throws Exception
    {
        throw new Exception(description);
    }
    
    public static void throwInvalidArgument(String description)
    {
        throw new IllegalArgumentException(description);
    }
    
    public static void throwIndexOutOfBounds(String description)
    {
        throw new IndexOutOfBoundsException(description);
    }
    
    public static void throwIllegalMathOperation(String description)
    {
        throw new ArithmeticException(description);
    }
    
    public static void throwInternalLogicError(String description) throws RuntimeException
    {
        throw new RuntimeException(description);
    }
    
    public static void throwCannotStartTwice(String description) throws RuntimeException
    {
        throw new RuntimeException(description);
    }
    
    public static void throwSerializationFailed(String description) throws RuntimeException
    {
        throw new RuntimeException(description);
    }
    
    public static void throwClassNotFound(String description) throws ClassNotFoundException
    {
        throw new ClassNotFoundException(description);
    }
}
