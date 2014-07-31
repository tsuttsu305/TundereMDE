package net.tsuttsu305.tunderemde.ui;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.fxmisc.richtext.CodeArea;

/**
 * Created by tsuttsu305 on 2014/07/31.
 */
public class CodeTextArea extends CodeArea{

    public CodeTextArea(){
        super();
    }

    //Windowsでコピー処理を実施た時にすべての行で空白行が追加されてしまうバグの対処
    @Override
    public void copy() {
        String selectedText = getSelectedText();
        selectedText = selectedText.replace("\r\n", "\n");
        if (selectedText.length() > 0) {
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedText);
            Clipboard.getSystemClipboard().setContent(content);
        }
    }
}
