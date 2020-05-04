package webadv.s99201105.p02;

import java.util.*;

/**
 * @author IITII
 */
public class Login {
    public final static String PW_NAME = "/password.txt";

    private static Map<String, String> init() {
        Map<String, String> userInfo = new HashMap<>();
        Scanner scanner = new Scanner(Login.class.getResourceAsStream(PW_NAME));
        String[] rawUserInfo;
        String tmp = "";
        while (scanner.hasNextLine()) {
            //去除首尾空格
            tmp = scanner.nextLine().trim();
            //去除中间空格
            rawUserInfo = tmp.split("\\s+");
            if (rawUserInfo.length != 2) {
                System.out.println("错误行:" + tmp);
            }
            userInfo.put(rawUserInfo[0], rawUserInfo[1]);
        }
        return userInfo;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> userInfo = init();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("User Name: ");
            String userName = scanner.nextLine().trim();
            if (userName.length() < 1) {
                throw new Exception("Please input your user name !!!");
            }
            System.out.print("Password: ");
            String password = scanner.nextLine();
            if (password.length() < 1) {
                throw new Exception("Please input your password !!!");
            }
            if (userInfo.containsKey(userName)) {
                if (userInfo.get(userName).equals(App.sha256hex(password))) {
                    System.out.println("Login success!!!\nHello " + userName);
                    continue;
                }
            }
            System.out.println("Login failed …");
        }
    }
}
