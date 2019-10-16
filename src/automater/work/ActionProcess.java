/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.utilities.Errors;
import automater.utilities.Looper;
import automater.utilities.SimpleCallback;
import automater.work.model.ActionContext;

/**
 * An action process, that wraps a single action, and is performed when requested.
 * 
 * This class has a state.
 * Simple actions are performed synchronously, and immediately.
 * Complex actions are performed in background threads, asynchronously.
 * 
 * @author Bytevi
 */
public class ActionProcess implements BaseActionProcess {
    private final Object _lock = new Object();
    
    private final BaseAction _action;
    private final boolean _isComplexAction;
    private ActionContext _context;
    
    public ActionProcess(BaseAction action)
    {
        _action = action;
        _isComplexAction = _action.isComplex();
    }
    
    // # BaseActionProcess
    
    @Override
    public boolean isActive()
    {
        synchronized (_lock)
        {
            return _context != null;
        }
    }
    
    @Override
    public BaseAction getAction()
    {
        return _action;
    }
    
    @Override
    public void perform(ActionContext context) throws Exception
    {
        if (!_isComplexAction)
        {
            Exception exc = null;
            
            synchronized (_lock)
            {
                _context = context;
            }
            
            try {
                perform();
            } catch (Exception e) {
                exc = e;
            }
            
            synchronized (_lock)
            {
                _context = null;
            }
            
            // Rethrow error here, action could not be performed
            if (exc != null)
            {
                throw exc;
            }
            
            return;
        }
        
        synchronized (_lock)
        {
            if (_context != null)
            {
                Errors.throwCannotStartTwice("Action process is already active");
                return;
            }
            
            _context = context;
            
            final ActionProcess process = this;
            
            Looper.getShared().performAsyncCallback(new SimpleCallback() {
                @Override
                public void perform() {
                    process.perform();
                    finish();
                }
            });
        }
    }
    
    // # Private
    
    private void perform()
    {
        _action.perform(_context);
    }
    
    private void finish()
    {
        synchronized (_lock)
        {
            _context = null;
        }
    }
}
