package me.iitii.al;

import java.io.*;
import java.util.*;

public class ScoreRecord {

    private String no;

    private String name;

    private int score;


    public ScoreRecord() {


    }


    public ScoreRecord(String no, String name, int score) {

        this.no = no;

        this.name = name;

        this.score = score;

    }


    public String getNo() {

        return no;

    }

    public void setNo(String no) {

        this.no = no;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public int getScore() {

        return score;

    }

    public void setScore(int score) {

        this.score = score;

    }


    public byte[] toBytes() {

        byte[] recordBytes = new byte[29];

        byte[] bytes = asciiBytes(no);

        for (int i = 0; i < 8; i++) {

            recordBytes[i] = bytes[i];

        }

        bytes = name.getBytes();

        for (int i = 0; i < bytes.length; i++) {

            recordBytes[i + 8] = bytes[i];

        }

        recordBytes[bytes.length + 8] = 0;

        recordBytes[28] = (byte) score;

        return recordBytes;

    }


    public void printInfo() {

        System.out.printf("no:%s,name:%s,score:%d\n", no, name, score);

    }


    public void loadBytes(byte[] recordBytes) {

        char[] chars = new char[8];

        for (int i = 0; i < chars.length; i++)

            chars[i] = (char) recordBytes[i];

        no = new String(chars);

        int pos = 0;

        for (int i = 8; i < 28; i++) {

            if (recordBytes[i] == 0) {

                pos = i;

                break;

            }

        }

        name = new String(recordBytes, 8, pos - 8);

        score = (int) recordBytes[28];

    }


    public static byte[] asciiBytes(String s) {

        byte[] bytes = new byte[s.length()];

        for (int i = 0; i < bytes.length; i++)

            bytes[i] = (byte) s.charAt(i);

        return bytes;

    }
}

class ScoreApp {
    //public static final String PATH = "resources/excercise/c01/";
    public static final String PATH = "";
    private static final List<ScoreRecord> list = new ArrayList<>();

    // Load data from file
    public ScoreApp() {
        try {
            RandomAccessFile raf = new RandomAccessFile(PATH + "score.data", "r");
            byte[] buff = new byte[29];
            int len = 0;
            while ((len = raf.read(buff, 0, buff.length)) != -1) {
                ScoreRecord sr = new ScoreRecord();
                sr.loadBytes(buff);
                list.add(sr);
            }
            raf.close();
            removeDuplicate(list);
        } catch (Exception ignored) {
        }
    }

    private static void writeBack() {
        try {
            // Clean file at first
            File file = new File(PATH + "score.data");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            // write to file
            RandomAccessFile raf = new RandomAccessFile(PATH + "score.data", "rw");
            byte[] buff;
            for (ScoreRecord s : list) {
                buff = s.toBytes();
                raf.seek(raf.length());
                raf.write(buff);
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void removeDuplicate(List<ScoreRecord> list) {
        LinkedHashSet<ScoreRecord> set = new LinkedHashSet<ScoreRecord>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
    }

    /**
    help
     */
    public void add(ScoreRecord sr) {
        boolean flag = true;
        for (ScoreRecord tmp : list) {
            if (tmp.getNo().equals(sr.getNo())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            list.add(sr);
            writeBack();
        }
    }

    /*
    Delete
     */
    public void delByNo(String no) {
        list.removeIf(tmp -> tmp.getNo().equals(no));
        writeBack();
    }

    /*
    Modify
     */
    public void update(ScoreRecord sr) {
        for (ScoreRecord e : list) {
            if (e.getNo().equals(sr.getNo())) {
                list.set(list.indexOf(e), sr);
                writeBack();
                break;
            }
        }
    }

    /*
    query
     */
    public ScoreRecord findByNo(String no) {
        for (ScoreRecord e : list) {
            if (e.getNo().equals(no)) {
                return e;
            }
        }
        System.out.println("No match record...");
        return null;
    }
}

class Test {

    public static void main(String[] args) throws Exception {
        ScoreApp scoreApp = new ScoreApp();
        scoreApp.add(new ScoreRecord("17204117", "a", 100));
        scoreApp.add(new ScoreRecord("17204118", "a", 100));
        scoreApp.add(new ScoreRecord("17204119", "a", 100));
        ScoreRecord tmp = scoreApp.findByNo("17204117");
        System.out.println(tmp.getScore());
        scoreApp.update(new ScoreRecord("17204117", "abc", 1000));
        tmp = scoreApp.findByNo("17204117");
        System.out.println(tmp.getScore());
        scoreApp.delByNo("17204117");
        tmp = scoreApp.findByNo("17204117");
    }
}