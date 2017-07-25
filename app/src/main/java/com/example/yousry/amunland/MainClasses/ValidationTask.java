package com.example.yousry.amunland.MainClasses;

/**
 * Created by yousry on 4/9/2017.
 */

public class ValidationTask implements Validation {
    public ValidMessage valid=new ValidMessage();

    @Override
    public boolean loginChecking(String username, String password) {
        if(username.isEmpty()||password.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkisEmpty(String text) {
        if(text.isEmpty() ||text.length()==0)
            return false;
        return true;
    }

    @Override
    public boolean checkEmailIsValid(String text) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(text);
        return m.matches();

    }

    @Override
    public ValidMessage checkPasswordIsValid(String text) {
        ValidMessage valid=new ValidMessage();

        //return true if and only if password:
        //1. have at least eight characters.
        //2. consists of only letters and digits.
        //3. must contain at least two digits.
        if (text.length() < 8) {
            valid.message="your Password's Length must be greater than 8 letters";
            valid.state =false;
            return valid;
        } else {
            char c;
            int count = 1;
            for (int i = 0; i < text.length() - 1; i++) {
                c = text.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    valid.message="your Password's String must contain one Letter at least";
                    valid.state =false;
                    return valid;
                } else if (Character.isDigit(c)) {
                    count++;
                    if (count < 2)   {
                        valid.message="your Password's Length must consist of one Digit";
                        valid.state =false;
                        return valid;
                    }
                }
            }
        }
        valid.message ="true";
        valid.state =true;
        return valid;
    }

    @Override
    public boolean checkRePasswordValid(String pass1, String pass2) {
        if(pass1.equals(pass2))
            return true;
        return false;
    }

    @Override
    public boolean checkPhoneValid(String text) {
        if(text.length()==13)
        {
            if(text.charAt(0)=='+'){
                return true;
            }
            else
                return false;
        }
        return false;
    }
    public class ValidMessage
    {
        public ValidMessage(){
            message = "";
            state =false;
        }
        public String message;
        public boolean state;
    }
}
