package edu.upenn.cis350.cisproject.data;

public class User {
    private String email;
    private String password;
    private String phoneNumber;
    private String identity;
    public User(){
    }

    public String getIdentity(){
        return identity;
    }
    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getPhone(){
        return phoneNumber;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setPhoneNumber(String phone){
        this.phoneNumber = phone;
    }
    @Override
    public String toString() {
        return "Username: "+ email + " password: "+password+" phone number: "+phoneNumber;
    }
}
