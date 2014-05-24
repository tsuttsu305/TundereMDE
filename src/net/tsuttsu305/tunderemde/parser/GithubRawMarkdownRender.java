/**
 * TundereMDE - Package: net.tsuttsu305.tunderemde.parser
 * Created: 2014/05/25 4:18:55
 */
package net.tsuttsu305.tunderemde.parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * GithubRawMarkdownRender (GithubRawMarkdownRender.java)
 * @author tsuttsu305
 */
public class GithubRawMarkdownRender {
    private String css;
    
    public GithubRawMarkdownRender() throws IOException {
        Class<? extends GithubRawMarkdownRender> c = this.getClass();
        InputStream input = c.getResourceAsStream("github.css");
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine());
        }
        css = sb.toString();
    }
    
    public String render(String markdowntxt) throws IOException{
        URL url = new URL("https://api.github.com/markdown/raw");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/x-markdown");
        conn.setDoOutput(true);
        
        OutputStream output = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        writer.write(markdowntxt);
        writer.flush();
        writer.close();
        
        BufferedInputStream bufin = new BufferedInputStream(conn.getInputStream());
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        
        byte[] buf = new byte[512];
        int len = 0;
        while ((len = bufin.read(buf)) != -1){
            byteout.write(buf, 0, len);
        }
        
        bufin.close();
        byte[] data = byteout.toByteArray();
        String html = new String(data, "UTF-8");
        
        html = "<html><STYLE type=\"text/css\"> <!-- " + css +" --> </STYLE><meta charset=\"utf-8\"></head><body>"
                + html + "</body></html>";
        
        
        return html;
    }
}
