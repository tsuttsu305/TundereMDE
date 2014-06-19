/**
 * TundereMDE Package: net.tsuttsu305.tunderemde.parser
 * Created: 2014/06/17 11:13
 */
package net.tsuttsu305.tunderemde.parser;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;

/**
 * MarkedJSParser (MarkedJSParser.java)
 *
 * @author tsuttsu305
 */
public class MarkedJSParser implements IRender{
    private static MarkedJSParser Instance = new MarkedJSParser();
    private static String js;

    private MarkedJSParser(){
        try {
            init();
        } catch (ScriptException e) {
            e.printStackTrace();
            Instance = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws ScriptException, IOException {
        Class<? extends MarkedJSParser> c = this.getClass();
        InputStream input = c.getResourceAsStream("marked.js");
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine() + "\n");
        }
        js = sb.toString();
    }

    public String render(String md) throws ScriptException, NoSuchMethodException {
        ScriptEngine script = new ScriptEngineManager().getEngineByName("js");
        script.eval(js);

        Invocable function = (Invocable)script;

        Object obj = function.invokeFunction("marked", md);

        if (obj instanceof String){
            return (String)obj;
        }else{
            return "Error";
        }
    }

    public static MarkedJSParser getInstance(){
        return Instance;
    }
}
