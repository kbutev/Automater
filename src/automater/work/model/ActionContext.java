/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import java.awt.Robot;

/**
 *
 * @author Bytevi
 */
public class ActionContext implements BaseActionContext {
    private final Robot _robot;
    
    public ActionContext(Robot robot)
    {
        this._robot = robot;
    }

    @Override
    public Robot getRobot() {
        return _robot;
    }
}
