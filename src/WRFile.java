import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: athena
 * Date: 11/11/13
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class WRFile {
    public static void main(String arg[]) throws Exception{
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream("Miao.txt"), "UTF-8"));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("Duo.txt"), "UTF-8"));
        while((line = in.readLine()) != null) {
            System.out.println(line);
            out.write(line + "\n");
        }
        in.close();
        out.close();
    }
}
