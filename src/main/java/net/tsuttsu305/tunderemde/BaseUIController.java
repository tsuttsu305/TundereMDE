package net.tsuttsu305.tunderemde;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import net.tsuttsu305.tunderemde.parser.GithubRawMarkdownRender;
import net.tsuttsu305.tunderemde.util.TextUtil;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseUIController implements Initializable{
    public static void setCharset(String charset) {
        Charset = charset;
        instance.charsetLabel.setText(charset);
    }

    @SuppressWarnings("UnusedDeclaration")
    public static String Charset = "UTF-8";
    public static BaseUIController instance;

    GithubRawMarkdownRender render;

    private File nowEditFile = null;
    private boolean isTemp = false;
    private boolean isEdited = false;

    @FXML private TextArea textArea;
    @FXML private WebView webView;
    @FXML private Button btn;
    @FXML private MenuItem close;
    @FXML private Label charsetLabel;
    @FXML private ToolBar toolbar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        setCharset("UTF-8");

        Platform.runLater(() -> Main.MainStage.setOnCloseRequest(event -> {
            if (chkEdited()){
                Action a  = Dialogs.create()
                        .owner(Main.MainStage)
                        .title("保存しますか?")
                        .masthead("保存しますか?")
                        .message("変更を保存しますか?")
                        .actions(Dialog.Actions.YES, Dialog.Actions.NO)
                        .showConfirm();
                if (a == Dialog.Actions.YES){
                    save();
                }
            }
        }));

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
        if (chkEdited()){
            Action a  = showSaveOrDestroyDialog();
            if (a == Dialog.Actions.OK){
                save();
            }else if (a == Dialog.Actions.CANCEL){
                return;
            }
        }
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
            BaseUIController.Charset = "UTF-8";
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

    //FIXME SJISの時判定がうまく行かず、trueが返る
    public boolean chkEdited() {

        String ftxt = null;
        String atxt = null;
        try {
            ftxt = TextUtil.readTxtFile(nowEditFile, Charset);
            atxt = textArea.getText();
            isEdited = !ftxt.equals(atxt) || isEdited;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(ftxt.equals(atxt));

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
            }else if (a == Dialog.Actions.CANCEL){
                return;
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