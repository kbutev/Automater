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
    public static Exception throwUnknownError(String description)
    {
        return new Exception(description);
    }
    
    public static Exception throwInvalidArgument(String description)
    {
        return new IllegalArgumentException(description);
    }
    
    public static Exception throwIndexOutOfBounds(String description)
    {
        return new IndexOutOfBoundsException(description);
    }
    
    public static Exception throwIllegalMathOperation(String description)
    {
        return new ArithmeticException(description);
    }
    
    public static Exception throwInternalLogicError(String description)
    {
        return new Exception(description);
    }
    
    public static Exception throwCannotStartTwice(String description)
    {
        return new Exception(description);
    }
    
    public static Exception throwSerializationFailed(String description)
    {
        return new Exception(description);
    }
    
    public static Exception throwClassNotFound(String description)
    {
        return new ClassNotFoundException(description);
    }
}
