/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.TextValue;
import automater.utilities.CollectionUtilities;
import automater.utilities.DateUtilities;
import automater.utilities.Description;
import automater.work.BaseAction;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A collection of system actions such as user input and system operations.
 * 
 * @author Bytevi
 */
public class Macro implements Serializable, Description {
    public final String name;
    public final List<BaseAction> actions;
    public final List<Description> actionDescriptions;
    private String description = "";
    public final Date dateCreated;
    private int numberOfTimesPlayed = 0;
    private Date lastDatePlayed;
    public final Dimension screenSize;
    
    public Macro(String name, List<BaseAction> actions, Date dateCreated, Dimension screenSize)
    {
        this(name, actions, dateCreated, dateCreated, screenSize);
    }
    
    public Macro(String name, List<BaseAction> actions, Date dateCreated, Date lastDatePlayed, Dimension screenSize)
    {
        this.name = name;
        this.actions = CollectionUtilities.copyAsImmutable(actions);
        
        ArrayList<Description> descriptions = new ArrayList<>();
        descriptions.addAll(actions);
        this.actionDescriptions = CollectionUtilities.copyAsImmutable(descriptions);
        
        this.dateCreated = dateCreated;
        this.lastDatePlayed = lastDatePlayed;
        
        this.screenSize = screenSize;
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
        return actions.size();
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
}
