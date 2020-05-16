package webadv.example.model;

import javax.validation.constraints.Pattern;

/**
 * @author IITII
 */
public class User {
    @Pattern(regexp = "[a-z]{3}", message = "请输入3个字母")
    private String account;
    @Pattern(regexp = "[0-9|]{6}", message = "请输入6位数字")
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
