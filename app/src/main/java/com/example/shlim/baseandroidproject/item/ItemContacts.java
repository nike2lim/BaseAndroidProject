package com.example.shlim.baseandroidproject.item;

/**
 * Created by Dongnam on 2017. 5. 23..
 */

public class ItemContacts {
    public long _id;            // 인덱스
    public String name = "";    // 이름
    public String hp = "";      // 휴대전화번호
    public String email = "";   // 이메일
    public boolean selected;    // 클릭 여부
    public boolean isNew;       // 새로 추가 여부

    public long getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getHp() {
        return hp;
    }

    public String getEmail() {
        return email;
    }

    public boolean getSelected() {
        return selected ;
    }

    public boolean getIsNew() {
        return isNew ;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
}