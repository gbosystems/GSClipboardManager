package com.gbosystems.android;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Wraps the API 11 Clipboard man
 * 
 * @author Geoff O'Donnell
 */
public class API11ClipboardManagerWrapper {

    /* Declare class members */
    private ClipboardManager clipboardManager;

    /**
     * Public constructor.
     * 
     * @throws Exception if the ClipboardManager introduced in API 11 does not exist
     */
    public API11ClipboardManagerWrapper(Context context) throws Exception {

        try{
            Class.forName("android.content.ClipboardManager");
        } catch (Exception e) {
            throw(e);
        }

        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }
	
    public void addPrimaryClipChangedListener(ClipboardManager.OnPrimaryClipChangedListener listener){
        clipboardManager.addPrimaryClipChangedListener(listener);
    }

    public ClipData getPrimaryClip(){
        return clipboardManager.getPrimaryClip();
    }
    
    public ClipDescription getPrimaryClipDescription(){
        return clipboardManager.getPrimaryClipDescription();
    }
    
    public CharSequence getText(){
        return clipboardManager.getText();
    }
    
    public boolean hasPrimaryClip(){
        return clipboardManager.hasPrimaryClip();
    }
    
    public boolean hasText(){
        return clipboardManager.hasText();
    }
    
    public void removePrimaryClipChangedListener(ClipboardManager.OnPrimaryClipChangedListener listener){
        clipboardManager.removePrimaryClipChangedListener(listener);
    }
    
    public void setPrimaryClip(ClipData clip){
        clipboardManager.setPrimaryClip(clip);
    }

    public void setText(String text){
        clipboardManager.setText(text);
    }
}
