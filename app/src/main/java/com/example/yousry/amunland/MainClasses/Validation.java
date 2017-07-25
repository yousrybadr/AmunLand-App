package com.example.yousry.amunland.MainClasses;

/**
 * Created by yousry on 4/9/2017.
 */

public interface Validation {
    boolean loginChecking(String username,String password);
    boolean checkisEmpty(String text);
    boolean checkEmailIsValid(String text);
    ValidationTask.ValidMessage checkPasswordIsValid(String text);
    boolean checkRePasswordValid(String pass1,String pass2);
    boolean checkPhoneValid(String text);

}
