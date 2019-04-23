package com.yidankeji.cheng.ebuyhouse.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class SPUtil {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "practical_tool";


    public static SharedPreferences getSPFile(Context context){
        return context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
    }
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = getSPFile(context);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key,  object==null?"":(String)object);
        } else if (object instanceof Integer) {
            editor.putInt(key, object==null?0:(Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, object==null?false:(Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, object==null?0:(Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, object==null?0:(Long) object);
        } else {
            editor.putString(key, object==null?"":object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }
    public static void put(String key, Object object) {
        put(MyApplication.getContext(),key,object);
    }


    public static ArrayList<String> getHistoryList(Context context){
        SharedPreferences sp = getSPFile(context);
        SharedPreferences.Editor editor = sp.edit();
        String text=sp.getString("HistoryList","");
        if (text.isEmpty()){
            return new ArrayList<>();
        }else {
            String[] s=text.split(",");
            ArrayList<String> list=new ArrayList<>();
            Collections.addAll(list, s);
            return list;
        }
    }

    public static void saveHistoryList(Context context, ArrayList<String> list){
        SharedPreferences sp = getSPFile(context);
        SharedPreferences.Editor edit = sp.edit();
        String text="";
        for (String s:list){
            text=text+s+",";
        }
        if (!text.isEmpty()){
            text=text.substring(0,text.length()-1);
        }
        edit.putString("HistoryList",text).apply();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = getSPFile(context);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }
    public static Object get(String key, Object defaultObject) {
        return get(MyApplication.getContext(),key,defaultObject);
    }
    public static String getString(String key, String defaultObject) {
        return (String) get(key,defaultObject);
    }
    public static boolean getBoolean(String key, boolean defaultObject) {
        return (boolean) get(key,defaultObject);
    }
    public static int getInt(String key, int defaultObject) {
        return (int) get(key,defaultObject);
    }
    public static float getFloat(String key, float defaultObject) {
        return (float) get(key,defaultObject);
    }
    public static long getLong(String key, long defaultObject) {
        return (long) get(key,defaultObject);
    }
    public static double getDouble(String key, double defaultObject) {
        return (double) get(key,defaultObject);
    }
    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp =getSPFile(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }
    public static void remove(String key) {
        remove(MyApplication.getContext(),key);
    }
    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp =getSPFile(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }
    public static void clear() {
        clear(MyApplication.getContext());
    }
    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = getSPFile(context);
        return sp.contains(key);
    }
    public static boolean contains(String key) {
        return contains(MyApplication.getContext(),key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp =getSPFile(context);
        return sp.getAll();
    }
    public static Map<String, ?> getAll() {
        return getAll(MyApplication.getContext());
    }
    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
    public static void putBean(Context context, String key,
                               Object obj) {
        SharedPreferences.Editor editor = getSPFile(context).edit();
        String objString = new Gson().toJson(obj);
        // fastjson的方法，需要导包的
        editor.putString(key, objString).apply();
    }

    /**
     *
     * @param context
     * @param key
     * @param clazz
     *            这里传入一个类就是我们所需要的实体类(obj)
     * @return 返回我们封装好的该实体类(obj)
     */
    public static <T> T getBean(Context context, String key,
                                Class<T> clazz) {
        String objString = getSPFile(context).getString(key, "");
        return new Gson().fromJson(objString,clazz);

    }
}