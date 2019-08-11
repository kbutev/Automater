/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.TextValue;
import automater.recorder.RecorderResult;
import automater.utilities.DateUtilities;
import automater.utilities.Description;
import java.io.Serializable;
import java.util.Date;

/**
 * A collection of user input operations.
 * 
 * @author Bytevi
 */
public class Macro implements Serializable, Description {
    public final String name;
    private String description = "";
    public final Date dateCreated;
    private int numberOfTimesPlayed = 0;
    private Date lastDatePlayed;
    public final RecorderResult r;
    
    public Macro(String name, RecorderResult recorderResult)
    {
        this.name = name;
        this.dateCreated = new Date();
        this.lastDatePlayed = new Date();
        this.r = recorderResult;
        
        parseRecorderResult(recorderResult);
    }
    
    // # Description

    @Override
    public String getStandart() {
        return name;
    }

    @Override
    public String getVerbose() {
        String stringNumOfActions = TextValue.getText(TextValue.Open_NumActions, String.valueOf(getNumberOfActions()));
        String stringCreatedOn = TextValue.getText(TextValue.Open_CreatedOn, DateUtilities.asStandartDate(dateCreated));
        String stringLastPlayedOn = TextValue.getText(TextValue.Open_LastPlayedOn, DateUtilities.asStandartDate(lastDatePlayed));
        String stringNumTimesPlayed = TextValue.getText(TextValue.Open_NumTimesPlayed, String.valueOf(numberOfTimesPlayed));
        String stringHasNeverBeenPlayed = TextValue.getText(TextValue.Open_HasNeverBeenPlayed);
        
        String string = "<html>";
        
        if (description.length() > 0)
        {
            string += description;
            string += "<br>";
        }
        
        string += stringNumOfActions + "<br>";
        
        string += stringCreatedOn + "<br>";
        
        if (dateCreated.before(lastDatePlayed))
        {
            string += stringLastPlayedOn + "; " + stringNumTimesPlayed;
        }
        else
        {
            string += stringHasNeverBeenPlayed;
        }
        
        return string;
    }

    @Override
    public String getStandartTooltip() {
        return "Recorded on " + this.dateCreated.toString();
    }

    @Override
    public String getVerboseTooltip() {
        return "Recorded on " + this.dateCreated.toString() + ", last played on " + this.lastDatePlayed.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDebug() {
        return getVerbose();
    }
    
    // # Public
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description != null ? description : "";
    }
    
    public int getNumberOfActions()
    {
        return this.r.userInputs.size();
    }
    
    public int getNumberOfTimesPlayed()
    {
        return this.numberOfTimesPlayed;
    }
    
    public void incrementNumberOfTimesPlayed()
    {
        this.numberOfTimesPlayed++;
    }
    
    public Date getLastTimePlayedDate()
    {
        return this.lastDatePlayed;
    }
    
    public void setLastTimePlayedDate(Date date)
    {
        if (date != null)
        {
            this.lastDatePlayed = date;
        }
    }
    
    // # Private
    
    private void parseRecorderResult(RecorderResult recorderResult)
    {
        
    }
}
