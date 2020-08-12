package com.vicenteaguilera.integratec.helpers.utility;

public class StringHelper
{
    //ahhahah   @ ibm.com.mx
    //jhasjadja @ gmail.com

    private String[] parts;

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
        return hasArroba(email);
    }
    private boolean hasArroba(String email)
    {
        if(email.contains("@"))
        {
            parts = email.split("@");
            if(parts.length==2)
            {
                return isServerValid(parts[1]);
            }
        }
        return false;
    }
    private boolean isServerValid(String servidor)
    {
        if(servidor.contains("."))
        {
            parts=servidor.split("\\.");
            /*Log.e("tag",parts[0]+"");
            return true;*/
            if(parts.length==2)
            {
                if(parts[0].length()>=3 && parts[1].length()>=2){
                    return true;
                }
                else {
                    return false;
                }
            }
            else if(parts.length==3)
            {
                if(parts[0].length()>=3 && parts[1].length()>=2 && parts[2].length()==2)
                {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
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
}
