package com.vicenteaguilera.integratec.helpers.utility.helpers;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class StringHelper
{
    //ahhahah   @ ibm.com.mx
    //jhasjadja @ gmail.com

    private String[] parts;
    private String EXREGEMAIL="([a-z,0-9]+((\\.|_|-)[a-z0-9]+)*)@([a-z,0-9]+(\\.[a-z0-9]+)*)\\.([a-z]{2,})(\\.([a-z]{2}))?";
    public boolean  isEmpty(String string)
    {
        return string.isEmpty();
    }
    public boolean isEqual(String string1,String string2)
    {
        return string1.equals(string2);
    }
    public boolean isGreaterThanOrEqual(int lengthString,int number)
    {
        return lengthString>=number;
    }
    public boolean isEmail(String email)
    {
        return Pattern.matches(EXREGEMAIL,email);
    }



    public boolean isNotEmptyCredentials(String email,String password)
    {
        return isEmail(email) && !password.isEmpty();
    }

    public int loginHelper(String email, String password){

        if(isEmail(email) && !password.isEmpty()){
            return 1;//Todo correcto
        }else {
            if(email.isEmpty() && !password.isEmpty()){
                return 2;//Email vacío
            }else  if(!isEmail(email) && !password.isEmpty()){
                return 3;//Email invalido
            }else  if(isEmail(email) && password.isEmpty()){
                return 4;//Password vacío
            }else if(email.isEmpty() && password.isEmpty()){
                return 5;//Email vacío y pasword vacío
            }
        }

        return 0;
    }

    public boolean validateURL(String URL) {
        try {
            new URL(URL).toURI();
            return true;
        } catch (URISyntaxException e) {
            System.out.println(e);
            return false;
        } catch (MalformedURLException ex) {
            System.out.println(ex);
            return false;
        }
    }
}
