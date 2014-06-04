package net.tsuttsu305.tunderemde;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import net.tsuttsu305.tunderemde.parser.GithubRawMarkdownRender;
import net.tsuttsu305.tunderemde.util.TextUtil;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseUIController implements Initializable{
    @SuppressWarnings("UnusedDeclaration")
    public static String Charset = "UTF-8";

    GithubRawMarkdownRender render;

    private File nowEditFile = null;
    private boolean isTemp = false;
    private boolean isEdited = false;

    @FXML private TextArea textArea;
    @FXML private WebView webView;
    @FXML private Button btn;
    @FXML private MenuItem close;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            render = new GithubRawMarkdownRender();
        } catch (IOException e) {
            e.printStackTrace();
        }

        newFile();
    }

    @FXML
    public void onPreviewBtn(){
        System.out.println("onPreviewBtn");
        preview();
    }

    @FXML
    public void onClose(){
        System.exit(0);
    }

    @FXML
    public void onOpen(){

    open();
    }

    @FXML
    public void onSave(){
        save();
    }

    @FXML
    public void onNew(){
        if (chkEdited()){
            Action a  = showSaveOrDestroyDialog();
            if (a == Dialog.Actions.OK){
                save();
            }else if (a == Dialog.Actions.CANCEL){
                return;
            }
        }
        newFile();
    }

    public void newFile(){
        try {
            File tmp = File.createTempFile("TundereMDE_TMP", ".md.tmp");
            nowEditFile = tmp;
            isTemp = true;

            textArea.setText("");
            webView.getEngine().loadContent("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void preview(){
        try {
            webView.getEngine().loadContent(render.render(textArea.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean chkEdited() {

        String ftxt = null;
        String atxt = null;
        try {
            ftxt = TextUtil.readTxtFile(nowEditFile, Charset);
            atxt = textArea.getText();
            isEdited = ftxt.equals(atxt) ? isEdited : true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isEdited;
    }

    public boolean isEdited(){
        return isEdited;
    }

    private Action showSaveOrDestroyDialog(){
        return Dialogs.create()
                .owner(Main.MainStage)
                .title("保存しますか?")
                .message("変更が保存されていません。\n保存しますか?")
                .showConfirm();
    }

    public boolean save(){
        if (isTemp){
            FileChooser fc = new FileChooser();
            fc.setTitle("Save File");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MarkdownText", "*.md"),
                    new FileChooser.ExtensionFilter("ALL File", "*.*"));
            File saveFile = fc.showSaveDialog(Main.MainStage);

            if (saveFile == null)return false;

            try {
                saveFile.deleteOnExit();
                saveFile.createNewFile();
                nowEditFile = saveFile;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

        try (BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(nowEditFile))) {
            bout.write(textArea.getText().getBytes(Charset));
            bout.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        isEdited = false;
        isTemp = false;
        return true;
    }

    public void open(){
        if (chkEdited()){
            Action a  = showSaveOrDestroyDialog();
            if (a == Dialog.Actions.OK){
                save();
            }
        }
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(Main.MainStage);
        if (f != null){
            try {
                nowEditFile = f;
                String s = TextUtil.readTxtFile(f);
                textArea.setText(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}