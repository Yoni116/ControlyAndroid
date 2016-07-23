package net.controly.controly.model;

import net.controly.controly.util.DateUtils;

import java.util.Date;
import java.util.Map;

/**
 * Created by Itai on 23-Jul-16.
 */
public class User {

    private long id;
    private String userName;
    private String email;
    private String nickName;
    private String avatarPicture;
    private Date registrationDate;

    public User(Map<String, String> map) {
        this.id = Integer.parseInt(map.get("ID"));
        this.userName = map.get("UserName");
        this.email = map.get("Email");
        this.nickName = map.get("NickName");
        this.avatarPicture = map.get("AvatarPic");
        this.registrationDate = DateUtils.parse(map.get("RegistrationDate"));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarPicture() {
        return avatarPicture;
    }

    public void setAvatarPicture(String avatarPicture) {
        this.avatarPicture = avatarPicture;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarPicture='" + avatarPicture + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
