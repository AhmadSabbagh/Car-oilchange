package ebtkar.app.oilex2.models;

/**
 * Created by Luminance on 11/2/2018.
 */

public class User {

    public  static final String CUSTOMER="0" ,SERVICE_PROVIDER="1";
    private String fname  , lname ,password ,phone ,user_type,id;
public boolean isCustomer(){return user_type.equals(CUSTOMER);}



    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String fname, String lname, String password, String phone, String user_type, String id) {
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.phone = phone;
        this.user_type = user_type;
        this.id = id;
    }
}
