package com.lanhuawei.beautyproject.appBase;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/7.
 * 全局配置文件
 */

public class SharePreferenceManager {
    /**
     * 保存某个字段的值
     * @param context
     *              上下文
     * @param fileName
     *              配置文件名称
     * @param name
     *               字段名
     * @param value
     *               字段值
     */

    public synchronized static void SaveBatchSharedPreference(Context context,
                                                              String fileName, Object name, Object value) {
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (name instanceof String) {
            if (value instanceof Boolean) {
                editor.putBoolean(String.valueOf(name), (Boolean) value);
            } else if (value instanceof Float) {
                editor.putFloat(String.valueOf(name), (Float) value);
            } else if (value instanceof Integer) {
                editor.putInt(String.valueOf(name), (Integer) value);
            } else if (value instanceof Long) {
                editor.putLong(String.valueOf(name), (Long) value);
            } else {
                editor.putString(String.valueOf(name), String.valueOf(value));
            }

        } else if (name instanceof String[]) {
            String str[] = (String[]) name;
            if (value instanceof Boolean[]) {
                Boolean bvalue[] = (Boolean[]) value;
                for (int i = 0; i < str.length; i++) {
                    editor.putBoolean(str[i], bvalue[i]);
               }

            } else if (value instanceof Float[]) {
                Float fvalue[] = (Float[]) value;
                for (int i = 0; i < str.length; i++) {
                    editor.putFloat(str[i], fvalue[i]);
                }
            } else if (value instanceof Integer[]) {
                Integer ivalue[] = (Integer[]) value;
                for (int i = 0; i < str.length; i++) {
                    editor.putFloat(str[i], ivalue[i]);
                }
            } else if (value instanceof Long[]) {
                Long lvalue[] = (Long[]) value;
                for (int i = 0; i < str.length; i++) {
                    editor.putLong(str[i], lvalue[i]);
                }
            } else {
                String svalue[] = (String[]) value;
                for (int i = 0; i < str.length; i++) {
                    editor.putString(str[i], svalue[i]);
                }
            }

        }
        editor.apply();

    }

    /**
     * 获取某个字段的值
     * @param context
     *               上下文
     * @param fileName
     *              配置文件名称
     * @param name
     *              字段名
     * @param defValue
     *              默认值
     * @return
     */

    public synchronized static Object GetSharePreferenceValue(
            Context context, String fileName, Object name, Object defValue) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(fileName, 0);
        Object value;
        if (name instanceof String) {
            if (defValue instanceof Boolean) {
                value = sharedPreferences.getBoolean(String.valueOf(name), (Boolean) defValue);
            } else if (defValue instanceof Float) {
                value = sharedPreferences.getFloat(String.valueOf(name), (Float) defValue);
            } else if (defValue instanceof Integer) {
                value = sharedPreferences.getInt(String.valueOf(name), (Integer) defValue);
            } else if (defValue instanceof Long) {
                value = sharedPreferences.getLong(String.valueOf(name), (Long) defValue);
            } else {
                value = sharedPreferences.getString(String.valueOf(name), (String) defValue);
            }
        } else {
            String str[] = (String[]) name;
            if (defValue instanceof Boolean[]) {
                Boolean defbvalue[] = (Boolean[]) defValue;
                Boolean bvalue[] = new Boolean[defbvalue.length];
                for (int i = 0; i < str.length; i++) {
                    bvalue[i] = new Boolean(sharedPreferences.getBoolean(str[i], defbvalue[i]));
                }
                value = bvalue;
            } else if (defValue instanceof Float[]) {
                Float deffvalue[] = (Float[]) defValue;
                Float fvalue[] = new Float[deffvalue.length];
                for (int i = 0; i < str.length; i++) {
                    fvalue[i] = new Float(sharedPreferences.getFloat(str[i], deffvalue[i]));
                }
                value = fvalue;
            } else if (defValue instanceof Integer) {
                Integer defivalue[] = (Integer[]) defValue;
                Integer ivalue[] = new Integer[defivalue.length];
                for (int i = 0; i < str.length; i++) {
                    ivalue[i] = new Integer(sharedPreferences.getInt(str[i], defivalue[i]));
                }
                value = ivalue;
            } else if (defValue instanceof Long) {
                Long deflvalue[] = (Long[]) defValue;
                Long fvalue[] = new Long[deflvalue.length];
                for (int i = 0; i < str.length; i++) {
                    fvalue[i] = new Long(sharedPreferences.getLong(str[i], deflvalue[i]));
                }
                value = fvalue;
            } else {
                String defsvalue[] = (String[]) defValue;
                String svalue[] = new String[defsvalue.length];
                for (int i = 0; i < str.length; i++) {
                    svalue[i] = new String(sharedPreferences.getString(str[i], defsvalue[i]));
                }
                value = svalue;
            }

        }

        return value;

    }

    /**
     * 移除某个字段的值
     * @param context
     *                上下文
     * @param fileName
     *                配置文件名称
     * @param name
     *            字段名
     */
    public synchronized static void RemoveSharedPreference(Context context, String fileName, Object name) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(name.toString());
        editor.commit();
    }

    /**
     * 清空配置文件的值
     * @param context
     *              上下文
     * @param fieName
     *              配置文件名称
     */
    public synchronized static void ClearSharePreference(Context context, String fieName) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(fieName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }




}
