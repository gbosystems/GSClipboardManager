package com.gbosystems.android;

import android.content.Context;
import android.text.ClipboardManager;

@SuppressWarnings("deprecation")
public class API1ClipboardManagerWrapper {
    
    /* Declare class members */
    private ClipboardManager clipboardManager;
    
    /**
     * Access to the text-only interface to the clipboard.
     * 
     * @param context the active context
     */
    public API1ClipboardManagerWrapper(Context context){
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public CharSequence getText(){
        return clipboardManager.getText();
    }

    public boolean hasText(){		
        return clipboardManager.hasText();
    }

    public void setText(String mText){
        clipboardManager.setText(mText);
    }
}
