/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.storage;

import automater.TextValue;
import automater.utilities.Archiver;
import automater.utilities.Errors;
import automater.utilities.FileSystem;
import automater.utilities.StringFormatting;
import automater.work.model.Macro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bytevi
 */
public class MacroStorageFile {
    public static String FILE_NAME_EXTENSION = ".txt";
    public static final int MACRO_NAME_MIN_LENGTH = 3;
    public static final int MACRO_NAME_MAX_LENGTH = 18;
    
    private final Object _lock = new Object();
    
    private Macro _macro;
    
    public static MacroStorageFile createFromMacro(Macro macro)
    {
        return new MacroStorageFile(macro);
    }
    
    public static MacroStorageFile createFromFile(File file)
    {
        String data = MacroStorageFile.readFromFile(file);
        
        if (data == null)
        {
            return null;
        }
        
        Macro macro = MacroStorageFile.parseMacroFromData(data);
        
        if (macro == null)
        {
            return null;
        }
        
        return new MacroStorageFile(macro);
    }
    
    private MacroStorageFile(Macro macro)
    {
        _macro = macro;
    }
    
    // # Validators
    
    public static List<Character> getAllowedSpecialCharacters()
    {
        ArrayList<Character> chars = new ArrayList<>();
        chars.add(' ');
        chars.add('_');
        chars.add('-');
        chars.add('(');
        chars.add(')');
        chars.add('[');
        chars.add(']');
        chars.add('+');
        return chars;
    }
    
    public static Exception getMacroNameIsUnavailableError(String name)
    {
        if (name.isEmpty())
        {
            return new Exception(TextValue.getText(TextValue.Error_NameIsEmpty));
        }
        
        if (name.length() < MACRO_NAME_MIN_LENGTH)
        {
            return new Exception(TextValue.getText(TextValue.Error_NameIsTooShort,  String.valueOf(MACRO_NAME_MIN_LENGTH)));
        }
        
        if (name.length() > MACRO_NAME_MAX_LENGTH)
        {
            return new Exception(TextValue.getText(TextValue.Error_NameIsTooLong,  String.valueOf(MACRO_NAME_MAX_LENGTH)));
        }
        
        if (!isMacroCharsValid(name))
        {
            String allowedChars = getAllowedSpecialCharacters().toString();
            return new Exception(TextValue.getText(TextValue.Error_NameMustBeAlphaNumericWithSpecialSymbols, allowedChars));
        }
        
        return null;
    }
    
    public static boolean isMacroCharsValid(String name)
    {
        List<Character> allowedChars = getAllowedSpecialCharacters();
        
        for (int e = 0; e < name.length(); e++)
        {
            char c = name.charAt(e);
            
            if (StringFormatting.isStringAlphanumeric(String.valueOf(c)))
            {
                continue;
            }
            
            if (!allowedChars.contains(c))
            {
                return false;
            }
        }
        
        return true;
    }
    
    // # Properties
    
    public Macro getMacro()
    {
        synchronized (_lock)
        {
            return _macro;
        }
    }
    
    public String name()
    {
        return _macro.name;
    }
    
    public String fileName()
    {
        return name() + FILE_NAME_EXTENSION;
    }
    
    public String filePath()
    {
        String path = FileSystem.getLocalFilePath();
        return FileSystem.createFilePathWithBasePath(path, fileName());
    }
    
    // # Operations
    
    public void create() throws Exception
    {
        String data = Archiver.serializeObject(_macro);
        
        if (data == null)
        {
            Errors.throwSerializationFailed("Failed to serialize macro '" + _macro.name + "'");
        }
        
        File file = getFile();
        
        if (file.exists())
        {
            Errors.throwSerializationFailed("Failed to create macro '" + _macro.name + "', already exists");
        }
        
        // Update file
        synchronized (_lock)
        {
            writeToFile(getFile(), data);
        }
    }
    
    public void update(Macro macro) throws Exception
    {
        String data = Archiver.serializeObject(_macro);
        
        if (data == null)
        {
            Errors.throwSerializationFailed("Failed to serialize macro '" + _macro.name + "'");
        }
        
        // Update local variable
        synchronized (_lock)
        {
            _macro = macro;
        }
        
        // Update file
        synchronized (_lock)
        {
            writeToFile(getFile(), data);
        }
    }
    
    public void delete() throws Exception
    {
        File file = getFile();
        
        if (file != null)
        {
            file.delete();
        }
    }
    
    // # Parsing
    
    public static Macro parseMacroFromData(String data)
    {
        return Archiver.deserializeObject(Macro.class, data);
    }
    
    // # Private
    
    private File getFile() throws Exception
    {
        File file = new File(filePath());
        return file;
    }
    
    private static void writeToFile(File file, String data) throws Exception
    {
        PrintWriter writer = null;
        
        try {
            writer = new PrintWriter(file);
            String[] lines = data.split("\n");
            
            for (String line : lines)
            {
                writer.println(line);
            } 
            
            writer.println();
            writer.close();
        } catch (Exception e) {
            try {
                if (writer != null)
                {
                    writer.close();
                }
            } catch (Exception e2) {
                
            }
        }
    }
    
    private static String readFromFile(File file)
    {
        String data = "";
        
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(file));
            
            String line = reader.readLine();
            
            while (line != null)
            {
                data = data.concat(line);
                line = reader.readLine();
            }
            
            reader.readLine();
            reader.close();
        } catch (Exception e) {
            try {
                if (reader != null)
                {
                    reader.close();
                }
            } catch (Exception e2) {
            
            }
        }
        
        return data;
    }
}
