package net.tsuttsu305.tunderemde.util;

import java.io.*;

/**
 * Created by tsuttsu305 on 14/05/26.
 */
public class TextUtil {
    private TextUtil(){}

    /**
     * テキストファイルから文字列を読み込んで返します
     * @param f TextFile
     * @param charset Charset
     * @return TextFileString
     * @throws IOException
     */
    public static String readTxtFile(File f, String charset) throws IOException {
        BufferedInputStream bufinput = new BufferedInputStream(new FileInputStream(f));
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        byte[] buf = new byte[512];
        int len = 0;
        while ((len = bufinput.read(buf)) != -1){
            bout.write(buf, 0, len);
        }

        bout.flush();
        bufinput.close();
        bout.close();


        return new String(bout.toByteArray(), charset);
    }

    /**
     * テキストファイルから文字列を読み込みます。</br>
     * UTF-8で読み込みます
     * @param f
     * @return
     * @throws IOException
     */
    public static String readTxtFile(File f) throws IOException {
        return readTxtFile(f, "UTF-8");
    }
}
