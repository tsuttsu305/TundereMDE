package net.tsuttsu305.tunderemde.util;

import net.tsuttsu305.tunderemde.BaseUIController;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.nio.charset.Charset;

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
     * 文字エンコードは自動判定します
     * @param f
     * @return
     * @throws IOException
     */
    public static String readTxtFile(File f) throws IOException {
        BufferedInputStream bufin = new BufferedInputStream(new FileInputStream(f));
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();

        byte[] buf = new byte[512];
        int len = 0;
        while ((len = bufin.read(buf)) != -1){
            byteout.write(buf, 0, len);
        }

        bufin.close();
        byte[] data = byteout.toByteArray();
        byteout.close();

        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(data, 0, data.length);
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        if (encoding == null){
            encoding = Charset.defaultCharset().toString();
        }

        BaseUIController.setCharset(encoding);

        return new String(data, encoding);
    }
}
