import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author IITII
 */
public class ArrayTest {
    static class Stu {
        private int stuNo;
        private String stuName;

        public Stu(int stuNo, String stuName) {
            this.stuNo = stuNo;
            this.stuName = stuName;
        }

        public int getStuNo() {
            return stuNo;
        }

        public void setStuNo(int stuNo) {
            this.stuNo = stuNo;
        }

        public String getStuName() {
            return stuName;
        }

        public void setStuName(String stuName) {
            this.stuName = stuName;
        }

        @Override
        public String toString() {
            return "Stu{" +
                    "stuNo=" + stuNo +
                    ", stuName='" + stuName + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }

    static void test1() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("test");
        ArrayList<String> arrayList1 = new ArrayList<>(arrayList);
        //内存地址
        System.out.println(arrayList == arrayList1);
        //内容
        System.out.println(arrayList.equals(arrayList1));
        arrayList1.set(0, "1");
        System.out.println(arrayList.get(0));
        System.out.println(arrayList1.get(0));
    }

    static void test2() {
        ArrayList<Stu> arrayList = new ArrayList<>();
        arrayList.add(new Stu(1, "1"));
        ArrayList<Stu> arrayList1 = new ArrayList<>(arrayList);
        //内存地址
        System.out.println(arrayList == arrayList1);
        //内容
        System.out.println(arrayList.equals(arrayList1));
        arrayList1.set(0, new Stu(1, "1"));
        System.out.println(arrayList.get(0).toString());
        System.out.println(arrayList1.get(0).toString());
        //内存地址
        System.out.println(arrayList == arrayList1);
        //内容
        System.out.println(arrayList.equals(arrayList1));
    }

    static void test3() {
        System.out.println(new ArrayList<Stu>().size());
    }
}
