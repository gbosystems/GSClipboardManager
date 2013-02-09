/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gbosystems.android;

import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import java.io.Closeable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Geoff O'Donnell
 */
public class GSClipboardManager implements Closeable {

    /* Debug */
    private static final String TAG = "GSClipboardManager";
    private static final boolean D = true;
    
    /* Declare class constants */
    private static final int CLIPBOARD_SCANNER_PERIOD_MS = 3000;
    
    /* Declare class members */
    private API1ClipboardManagerWrapper mAPI1ClipboardManagerWrapper;
    private API11ClipboardManagerWrapper mAPI11ClipboardManagerWrapper;
    private ScheduledThreadPoolExecutor executor;
    private Listener listener;
    private String clipboardText;
    
    /**
     * Public constructor.
     * 
     * @param context the Activity or Application context
     */
    public GSClipboardManager(Context context){
    
        try {
            
            /* First, attempt to access the API 11 Clipboard Manager */
            mAPI11ClipboardManagerWrapper = new API11ClipboardManagerWrapper(context);

        } catch (Exception e) {
            
            /* Otherwise, get the API 1 Clipboard Manager */
            if (D) { Log.d(TAG, "Falling back to API 1 ClipboardManager"); }
            mAPI11ClipboardManagerWrapper = null;
            mAPI1ClipboardManagerWrapper = new API1ClipboardManagerWrapper(context);
            clipboardText = mAPI1ClipboardManagerWrapper.getText().toString();
            executor = new ScheduledThreadPoolExecutor(1);
        }
    }
    
    /**
     * Put text on the clipboard.
     * 
     * @param text new clipboard text.
     */
    public void setText(String text){
        
        if (mAPI11ClipboardManagerWrapper != null){
            mAPI11ClipboardManagerWrapper.setText(text);
        } else if (mAPI1ClipboardManagerWrapper != null){
            mAPI1ClipboardManagerWrapper.setText(text);
        }
    }
    
    /**
     * Get text from the clipboard
     * 
     * @return current clipboard contents, if text.
     */
    public CharSequence getText(){
        
        /* Declare local variables */
        CharSequence text = null;
        
        if (mAPI11ClipboardManagerWrapper != null){
            text = mAPI11ClipboardManagerWrapper.getText();
        } else if (mAPI1ClipboardManagerWrapper != null){
            text = mAPI1ClipboardManagerWrapper.getText();
        }

        return text;
    }
    
    /**
     * Returns true if the clipboard contains text; false otherwise.
     */
    public boolean hasText(){
        
        /* Declare local variables */
        boolean hasText = false;
        
        if (mAPI11ClipboardManagerWrapper != null){
            hasText = mAPI11ClipboardManagerWrapper.hasText();
        } else if (mAPI1ClipboardManagerWrapper != null){
            hasText = mAPI1ClipboardManagerWrapper.hasText();
        }
        
        return hasText;
    }
    
    /**
     * Closes the object and release any system resources it holds.
     */
    public void close(){
        removeListener();
    }
    
    /**
     * Register a listener for clipboard changes.
     * 
     * @param listener 
     */
    public void setListener(Listener listener){
        
        /* Save the listener */
        this.listener = listener;
        
        if (mAPI11ClipboardManagerWrapper != null){
            mAPI11ClipboardManagerWrapper.addPrimaryClipChangedListener((ClipboardManager.OnPrimaryClipChangedListener)listener);
        } else if (mAPI1ClipboardManagerWrapper != null){
            
            /* Schedule a task to periodically check the clipboard for changes */
            executor.scheduleAtFixedRate(API1Task,
                                         CLIPBOARD_SCANNER_PERIOD_MS,
                                         CLIPBOARD_SCANNER_PERIOD_MS,
                                         TimeUnit.MILLISECONDS);
        }
    }
    
    /**
     * Remove a previously registered listener.
     */
    public void removeListener(){
                
        if (mAPI11ClipboardManagerWrapper != null){
            mAPI11ClipboardManagerWrapper.removePrimaryClipChangedListener((ClipboardManager.OnPrimaryClipChangedListener)listener);
        } else if (mAPI1ClipboardManagerWrapper != null){
            executor.shutdown();
        }
    }
    
    /**
     *  Periodic task scheduled to run when service has only API level 1 clipboard access.
     */
    private final Runnable API1Task = new Runnable()
    {
        public void run() 
        {
            /* Declare local variables */
            String tmp = mAPI1ClipboardManagerWrapper.getText().toString();
            
            /* Check if the clipboard text has changed */
            if (mAPI1ClipboardManagerWrapper.hasText() == true){
                if (!tmp.equals(clipboardText)){
                    clipboardText = tmp;
                    if (listener != null){
                        listener.onPrimaryClipChanged();
                    }
                }
            }
        }
    };
    
    /**
     * Defines a listener callback that is called when the state of the clipboard
     * changes.
     */
    public interface Listener {
        
        /**
         * Invoked when the primary clip on the clipboard changes.
         */
        public void onPrimaryClipChanged();

    }
}
