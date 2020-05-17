package me.iitii.al;

import java.io.*;
import java.util.Random;


class RandomText {


    public static final String PATH = "resources/excercise/c01/";


    public static void main(String args[]) throws Exception {

        int[] numbers = new int[10];

        Random rand = new Random();

        for (int i = 0; i < numbers.length; i++)

            numbers[i] = rand.nextInt(100) + 1;


        PrintWriter pw = new PrintWriter(

                new OutputStreamWriter(new FileOutputStream(PATH + "random.txt")));


        for (int i = 0; i < numbers.length; i++) {

            pw.printf("Number %d : %d ", i + 1, numbers[i]);

            if (i < numbers.length - 1)

                pw.println();

        }

        pw.close();

    }


}
