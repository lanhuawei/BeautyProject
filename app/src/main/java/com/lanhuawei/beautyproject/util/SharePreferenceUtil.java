package com.lanhuawei.beautyproject.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.lanhuawei.beautyproject.appBase.SharePreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/7.
 * SharePreference工具类用于存储
 *
 * 未写完
 */

public class SharePreferenceUtil {
    public static String UserType = "UserType";
    public static String HospitalId = "HospitalId";
    public static String DoctorId = "DoctorId";

    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharePreferenceUtil(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("com.dfhon.infor",
                Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setDifferTime(long time) {
        editor.putLong("time", time);
        editor.commit();
    }
    public long getDifferTime() {
        sp = context.getSharedPreferences("com.dfhon.infor", Context.MODE_PRIVATE);
        return sp.getLong("time", 0);
    }

    public void setToken(String token) {
        editor.putString("yymg_token", token);
        editor.commit();
    }

    public String getToken() {
        sp = context.getSharedPreferences("com.dfhon.infor", Context.MODE_PRIVATE);
        return sp.getString("yymg_token", "");
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        editor.putString("lastUpdateTime", lastUpdateTime);
        editor.commit();
    }

    public String getLastUpdateTime() {
        sp = context.getSharedPreferences("com.dfhon.infor",
                Context.MODE_PRIVATE);
        return sp.getString("lastUpdateTime", "");
    }

    public void setCurrentProvince(String province) {
        editor.putString("CurrentProvince", province);
        editor.commit();
    }

    public String getCurrentProvince() {
        sp = context.getSharedPreferences("com.dfhon.infor",
                Context.MODE_PRIVATE);
        return sp.getString("CurrentProvince", "");
    }

    public void setCurrentCitis(String citis) {
        editor.putString("Current", citis);
        editor.commit();
    }

    public String getCurrentCitis() {
        sp = context.getSharedPreferences("com.dfhon.infor",
                Context.MODE_PRIVATE);
        return sp.getString("Current", "");
    }

    public void setProvince(String province) {
        editor.putString("Province", province);
        editor.commit();
    }

    public String getProvince() {
        sp = context.getSharedPreferences("com.dfhon.infor",
                Context.MODE_PRIVATE);
        return sp.getString("Province", "");
    }

    public void setCitis(String citis) {
        editor.putString("citis", citis);
        editor.commit();
    }

    public String getCitis() {
        sp = context.getSharedPreferences("com.dfhon.infor",
                Context.MODE_PRIVATE);
        return sp.getString("citis", "");
    }

    public void setProvince2(String province) {
        editor.putString("Province2", province);
        editor.commit();
    }

    public String getProvince2() {
        sp = context.getSharedPreferences("com.dfhon.infor",
                Context.MODE_PRIVATE);
        return sp.getString("Province2", "");
    }

    public void setCitis2(String citis) {
        editor.putString("citis2", citis);
        editor.commit();
    }

    public String getCitis2() {
        sp = context.getSharedPreferences("com.dfhon.infor",
                Context.MODE_PRIVATE);
        return sp.getString("citis2", "");
    }

    public void setClassId(String calssid) {
        editor.putString("calssid", calssid);
        editor.commit();
    }

    public String getClassId() {
        sp = context.getSharedPreferences("com.dfhon.infor",
                Context.MODE_PRIVATE);
        return sp.getString("calssid", "");
   }

    /**
     *
     * @param info
     */
    public void setAccountInfo(JSONObject info) {
        try {
            JSONArray array = info.getJSONArray("Data");
            JSONObject object = null;
            for (int i = 0; i < array.length(); i++) {
                object = array.getJSONObject(i);
                editor.putString("UserType", object.getString("UserType"));
                editor.putString("UserId", object.getString("UserId"));
                editor.putString("NickName", object.getString("NickName"));
                editor.putString("UserName", object.getString("UserName"));
                editor.putString("UserFace", object.optString("UserFace"));
                editor.putString("Userinfo", object.optString("Userinfo"));
                editor.putString("Email", object.optString("Email"));
                editor.putString("province", object.optString("Province"));
                // editor.putString("Id", object.getString("Id"));
                editor.putString("Mobile", object.optString("Mobile"));
                editor.putString("FromType", object.optString("FromType"));
                editor.putString("City", object.optString("City"));
                editor.putString("Sex", object.optString("Sex"));
                editor.putString("IsInvited", object.optString("IsInvited"));
                editor.putString("ali_kefu", object.optString("KefuAccount"));
                editor.putString("ali_kefu_phone",
                        object.optString("KefuMobile"));
                editor.putString("FavLableIds", object.optString("FavLableIds"));
                editor.putString("AgeGroup", object.optString("AgeGroup"));

                LogUtil.d("Login", "save:" + object.optString("KefuAccount")
                        + ">phone:" + object.optString("KefuMobile"));

                if (object.getString("UserType").equals("1")
                        || object.getString("UserType").equals("2")) {
                    editor.putString("HospitalId",
                            object.optString("HospitalId"));
                    editor.putString("DoctorId", object.optString("DoctorId"));
                }
                editor.commit();

            }

            String userId = (String) SharePreferenceManager.GetSharePreferenceValue(
                    context, PreferenceUtil.SYS_PARAMETER, PreferenceUtil.USER_ID, "");//,com.dfhon.infor,用户id
            String mobile = (String) SharePreferenceManager.GetSharePreferenceValue(
                    context, PreferenceUtil.SYS_PARAMETER, PreferenceUtil.APP_MOBILE, "");//,com.dfhon.infor,app用户是否绑定手机
            String fromType = (String) SharePreferenceManager.GetSharePreferenceValue(
                    context, PreferenceUtil.SYS_PARAMETER, PreferenceUtil.APP_FROMTYPE, "");//,com.dfhon.infor,用户登陆类型



       } catch (JSONException e) {
            e.printStackTrace();

        }
    }

}
