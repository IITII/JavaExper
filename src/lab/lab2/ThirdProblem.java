package lab.lab2;

import java.io.*;
import java.nio.file.*;

/**
 * @author IITII
 */
public class ThirdProblem {
    static class CopyFile extends Thread {

        private final Path src, dest;

        public CopyFile(Path src, Path dest) {
            this.src = src;
            this.dest = dest;
        }

        @Override
        public void run() {
            try {
                System.out.println("Copying " + src + " to " + dest);
                FileInputStream input = new FileInputStream(src.toFile());
                FileOutputStream output = new FileOutputStream(dest.toFile());
                byte[] buffer = new byte[10240];
                while (input.available() > 0) {
                    int n = input.read(buffer);
                    output.write(buffer, 0, n);
                }
                System.out.println("Copied " + src + " to " + dest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Path[] path = new Path[4];
        path[0] = Paths.get("tmp/1.txt");
        String tmp;
        for (int i = 1; i < path.length; i++) {
            tmp = "tmp" + File.separator + i;
            if (!(Paths.get(tmp).toFile().exists() && Paths.get(tmp).toFile().isDirectory())) {
                Paths.get(tmp).toFile().mkdirs();
            }
            path[i] = Paths.get(tmp + File.separator + path[0].getFileName());
        }
        try {
            CopyFile[] copyFiles = new CopyFile[3];
            for (int i = 0; i < 3; i++) {
                copyFiles[i] = new CopyFile(path[0], path[i + 1]);
                copyFiles[i].start();
            }
            for (CopyFile routine : copyFiles) {
                routine.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

