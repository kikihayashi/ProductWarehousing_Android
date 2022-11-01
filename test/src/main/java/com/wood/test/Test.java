package com.woody.test;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Test {

    public static void main(String[] args) {
        try {
            String a = AESTools.decrypt(TAG.UserPWD.getName());

            System.out.println(a);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
