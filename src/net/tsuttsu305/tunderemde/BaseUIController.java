package net.tsuttsu305.tunderemde;

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
    GithubRawMarkdownRender render;
    
	@FXML private TextArea textArea;
	@FXML private WebView webView;
	@FXML private Button btn;
	@FXML private MenuItem close;
	
	@FXML
	public void onPreviewBtn(){
	    System.out.println("onPreviewBtn");
	    preview();
	}
	
	@FXML
	public void onClose(){
	    System.exit(0);
	}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            render = new GithubRawMarkdownRender();
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
}
