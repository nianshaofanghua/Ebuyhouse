package com.yidankeji.cheng.ebuyhouse.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class SharedPreferencesUtils {

    private static final String FILE_NAME = "EBuyHouse";
    

    public static void setParam(Context context , String key, Object object){
    	 String type = object.getClass().getSimpleName();
         SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sp.edit();
         if("String".equals(type)){
             Log.e("local",key+"--local-"+object);
             editor.putString(key, (String)object);
         }else if("Integer".equals(type)){  
             editor.putInt(key, (Integer)object);
         }else if("Boolean".equals(type)){  
             editor.putBoolean(key, (Boolean)object);
         }else if("Float".equals(type)){  
             editor.putFloat(key, (Float)object);
         }else if("Long".equals(type)){  
             editor.putLong(key, (Long)object);
         }  
         editor.commit(); 
    }
    

    public static Object getParam(Context context , String key, Object defaultObject){
    	 String type = defaultObject.getClass().getSimpleName();
         SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
         if("String".equals(type)){  
             return sp.getString(key, (String)defaultObject);
         }else if("Integer".equals(type)){  
             return sp.getInt(key, (Integer)defaultObject);
         }else if("Boolean".equals(type)){  
             return sp.getBoolean(key, (Boolean)defaultObject);
         }else if("Float".equals(type)){  
             return sp.getFloat(key, (Float)defaultObject);
         }else if("Long".equals(type)){  
             return sp.getLong(key, (Long)defaultObject);
         }  
         return null;  
    }

    //判断是否登录
    public static boolean isLogin(Context context){
        String userID = (String) getParam(context, "token", "");
        if (userID == null || userID.equals("")){
            return false;
        }else{
            return true;
        }
    }

    //获取token
    public static String getToken(Context context){
        String token = (String) getParam(context, "token", "");
        return token;
    }

    //安全退出
    public static void setExit(Context context){
    	 SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sp.edit();
         editor.clear();
         editor.commit(); 
    }

}
