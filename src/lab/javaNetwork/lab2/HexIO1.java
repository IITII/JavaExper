package lab.javaNetwork.lab2;

import java.io.*;

public class HexIO1 {
    public static void main(String[] args) {
        try {
            File file = new File("Test.txt");
            if (!file.exists())
                return;
            FileInputStream fis = new FileInputStream(file);
            FileWriter fw = new FileWriter("TestOut.txt");
            int b, n = 0;
            while ((b = fis.read()) != -1) {
                fw.write(" " + Integer.toHexString(b));
                if (((++n) % 10) == 0) fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
