package net.tsuttsu305.tunderemde;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import net.tsuttsu305.tunderemde.parser.GithubRawMarkdownRender;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;

public class BaseUIController implements Initializable{
    @SuppressWarnings("UnusedDeclaration")
    public static String Charset = "UTF-8";

    GithubRawMarkdownRender render;

    private File nowEditFile = null;

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

    }

    @FXML
    public void onSave(){

    }

    @FXML
    public void onNew(){

    }

    private void newFile(){
        try {
            File tmp = File.createTempFile("TundereMDE_TMP", ".md.tmp");
            nowEditFile = tmp;

            textArea.setText("");
            webView.getEngine().loadContent("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void preview(){
        Runnable r = () -> {
            try {
                webView.getEngine().loadContent(render.render(textArea.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Thread t = new Thread(r);
        t.start();
    }
}
