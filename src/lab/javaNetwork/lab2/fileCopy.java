package lab.javaNetwork.lab2;

import com.sun.istack.internal.NotNull;

import java.io.*;

public class fileCopy {
}

class HexIO2 {
    // 其实可以使用 Files.copy() 来快速实现文件的复制
    // Docs link: https://docs.oracle.com/javase/8/docs/api/java/nio/file/FileVisitor.html
    // 不过看题干要求应该是要我们熟悉一些文件的底层操作

    public static void main(String[] args) throws IOException {
        String from = "F:\\a";
        String copyTo = "F:\\aa";
        copyAll(new File(from), copyTo);
    }

    public static void copyAll(@NotNull File file, @NotNull String path) throws IOException {
        File outFile = new File(path);
        // 若输出文件存在且是文件
        if (outFile.exists() && outFile.isFile()) {
            return;
        }
        // 若文件夹创建失败，大概率是权限不足
        // 对其他文件的操作大概率也执行不了，直接退出
        if (!outFile.exists())
            if (!outFile.mkdirs()) {
                System.out.println("Create dir failed");
                System.exit(1);
            }
        File[] files = file.listFiles();
        //避免空文件夹
        if (files == null) {
            return;
        }
        for (File f : files) {
            // 只处理文件夹和文件，不考虑特殊情况
            if (f.isFile()) {
                copyFile(f, path + File.separator);
            } else if (f.isDirectory()) {
                copyAll(f, outFile.getPath() + File.separator + f.getName());
            } else {
                System.out.println("Unknown file type of '" + f.getName() + "'");
            }
        }
    }

    //复制文件
    public static void copyFile(@NotNull File file, @NotNull String path) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream(path + file.getName());
        byte[] bytes = new byte[512];
        while ((in.read(bytes)) != -1) {
            out.write(bytes, 0, bytes.length);
        }
        out.close();
        in.close();
    }

}
