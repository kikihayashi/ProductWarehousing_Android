package com.woody.productwarehousing.model.dao;

import com.google.gson.JsonObject;

public class LoginDao extends BaseDao implements Dao.LoginDaoInterface {

    public LoginDao() {
        super();
    }

    @Override
    public JsonObject setLoginJson(String account, String password) {
        JsonObject object = new JsonObject();
        try {
            object.addProperty("account", account);
            object.addProperty("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
