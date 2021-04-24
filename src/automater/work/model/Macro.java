/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.model;

import automater.TextValue;
import automater.utilities.CollectionUtilities;
import automater.utilities.DateUtilities;
import automater.utilities.Description;
import automater.work.BaseAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    @NotNull public final String name;
    @NotNull public final List<BaseAction> actions;
    @NotNull public final List<Description> actionDescriptions;
    @NotNull private String description = "";
    @NotNull public final Date dateCreated;
    private int numberOfTimesPlayed = 0;
    @NotNull private Date lastDatePlayed;
    @NotNull public final Dimension screenSize;
    
    public Macro(@NotNull String name, @NotNull List<BaseAction> actions, @NotNull Date dateCreated, @NotNull Dimension screenSize)
    {
        this(name, actions, dateCreated, dateCreated, screenSize);
    }
    
    public Macro(@NotNull String name, @NotNull List<BaseAction> actions, @NotNull Date dateCreated, @NotNull Date lastDatePlayed, @NotNull Dimension screenSize)
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
    public @Nullable String getStandart() {
        return name;
    }

    @Override
    public @Nullable String getVerbose() {
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
    public @Nullable String getStandartTooltip() {
        return "Recorded on " + this.dateCreated.toString();
    }

    @Override
    public @Nullable String getVerboseTooltip() {
        return "Recorded on " + this.dateCreated.toString() + ", last played on " + this.lastDatePlayed.toString();
    }

    @Override
    public @Nullable String getName() {
        return name;
    }

    @Override
    public @Nullable String getDebug() {
        return getVerbose();
    }
    
    // # Public
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(@Nullable String description)
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
    
    public void setNumberOfTimesPlayed(int value)
    {
        this.numberOfTimesPlayed = value;
    }
    
    public void incrementNumberOfTimesPlayed()
    {
        this.numberOfTimesPlayed++;
    }
    
    public @NotNull Date getLastTimePlayedDate()
    {
        return this.lastDatePlayed;
    }
    
    public void setLastTimePlayedDate(@NotNull Date date)
    {
        this.lastDatePlayed = date;
    }
}
