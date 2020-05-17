package me.iitii.al;

import com.sun.corba.se.impl.orbutil.ObjectWriter;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main implements Runnable {
    public static final String PATH = "resources/excercise/c01/";

    @Override
    public void run() {
        int a = 1;
        //wait(1);
    }

    public static void main(String[] args) throws Exception {
//        List<Shape> shapes = new ArrayList<>();
//        int i = 10;
//        while (i-- > 0) {
//            Shape shape = (int)(10.0 * Math.random()) % 2 == 0 ? new Rectangle(Math.random(), Math.random()) : new Circle(Math.random());
//            shape.printInfo();
//        }
//        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(PATH + "/shapes.data"));
//        output.writeObject(shapes);
//        output.close();

        List<Shape> shapes = (ArrayList<Shape>) (new ObjectInputStream(new FileInputStream(PATH + "/shapes.data"))).readObject();
        for (Shape shape : shapes) {
            shape.printInfo();
        }
    }

}

abstract class Shape implements Serializable {
    public abstract double getArea();

    public void printInfo() {
        System.out.println(getClass().getSimpleName() +
                " has area " + getArea());
    }
}

class Circle1 extends Shape {
    private double length = 0;

    public Circle1() {
    }

    public Circle1(double length) {
        this.length = length;
    }

    @Override
    public double getArea() {
        return length * length * Math.PI;
    }

    @Override
    public void printInfo() {
        super.printInfo();
    }
}

class Rectangle extends Shape {
    private double a = 0;
    private double b = 0;

    public Rectangle() {
    }

    public Rectangle(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double getArea() {
        return a * b;
    }
}

class ShapeReader {


    public static final String PATH = "";


    public static void main(String args[]) throws Exception {


        ObjectInputStream is = new ObjectInputStream(

                new FileInputStream(PATH + "shapes.data"));
        List<Shape> list = (List<Shape>) is.readObject();


        is.close();


        for (Shape shape : list) {
            shape.printInfo();
        }


    }

}

class ShapeWriter {


    public static final String PATH = "";


    public static void main(String[] args) throws Exception {

        Random rand = new Random();

        List<Shape> list = new ArrayList<Shape>();

        for (int i = 0; i < 10; i++) {

            Shape shape;

            int n = rand.nextInt(2);

            if (n == 0) {
                shape = new Circle1(rand.nextInt(10) + 1);
            } else {
                shape = new Rectangle(rand.nextInt(10) + 1, rand.nextInt(10) + 1);
            }

            shape.printInfo();

            list.add(shape);

        }

        ObjectOutputStream os = new ObjectOutputStream(

                new FileOutputStream(PATH + "shapes.data"));

        os.writeObject(list);

        os.close();
    }


}

class Problem2 {
    public static void main(String[] args) throws Exception {
        DataOutputStream dos = new DataOutputStream(
                new FileOutputStream("a.txt"));
        int i = -1;
        while (++i < 10) {
            dos.writeInt((int) (Math.random() * 1000));
        }
        dos.flush();
        dos.close();
        DataInputStream dis = new DataInputStream(
                new FileInputStream("a.txt"));
        while (dis.available() > 0) {
            System.out.print(dis.readInt() + " ");
        }
        dis.close();
    }
}

class B {

    static int n = 20;

    static Object o = new Object();

    public static void main(String[] args) {

        class T extends Thread {

            @Override

            public void run() {

                while (true) {

                    synchronized (o) {

                        if (n < 1) {

                            try {

                                o.wait();

                            } catch (InterruptedException e) {

                                e.printStackTrace();

                            }

                        } else {

                            n--;

                            System.out.println("sales...当前库存数为：= " + n);

                            o.notify();

                        }

                    }

                }


            }

        }

        class S extends Thread {

            @Override

            public void run() {

                while (true) {

                    synchronized (o) {

                        if (n > 19) {

                            try {

                                o.wait();

                            } catch (InterruptedException e) {

                                e.printStackTrace();

                            }

                        } else {

                            n++;

                            System.out.println("production...当前库存数为：= " + n);

                            o.notify();

                        }

                    }


                }

            }

        }

        T t1 = new T();

        S s1 = new S();

        t1.start();

        s1.start();

    }

}

class T implements Runnable {

    @Override

    public void run() {

        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);

        }

    }

}

class C {
    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 10; i++) {
            pool.schedule(new T(), 1, TimeUnit.SECONDS);
        }
        pool.shutdown();
    }
}