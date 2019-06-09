package buildnlive.com.buildlive.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import android.preference.PreferenceManager



/**
 * Created by Server on 2/28/2018.
 */

class PrefernceFile private constructor(context: Context) {

    private val language: String? = null
    val gcmFromPreference: String
        get() = prefs!!.getString("FCM", "")

    fun clearData() {
        ed!!.clear()
        ed!!.commit()
    }

    fun setBoolean(key: String, value: Boolean) {
        ed!!.putBoolean(key, value)
        ed!!.commit()
    }

    fun setInt(key: String, value: Int) {
        ed!!.putInt(key, value)
        ed!!.commit()
    }

    fun setString(key: String, value: String) {
        ed!!.putString(key, value)
        ed!!.commit()
    }

    fun clearKeyData(key: String) {
        ed!!.remove(key)
        ed!!.commit()
    }


/*    fun setEmailsList(key: String, value: List<String>) {
        val gson = Gson()
        val json = gson.toJson(value)
        ed!!.putString(key, json)
        ed!!.commit()
    }

    fun getEmailsList(key: String): ArrayList<String> {
        val gson = Gson()
        val json = prefs!!.getString(key, null)
        val type = object : TypeToken<ArrayList<String>>() {

        }.getType()

        return gson.fromJson(json, type)
    }*/

    /*  public void setCategoryList(String key, ArrayList<HomeResponseResultCategory> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        ed.putString(key, json);
        ed.commit();
    }

    public void setProductList(String key, ArrayList<Category_AllProductResponseAll_products> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        ed.putString(key, json);
        ed.commit();
    }

    public ArrayList<Category_AllProductResponseAll_products> getProductList(String key) {
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Category_AllProductResponseAll_products>>() {
        }.getType();
        ArrayList<Category_AllProductResponseAll_products> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    public ArrayList<HomeResponseResultCategory> getCategoryList(String key) {
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<HomeResponseResultCategory>>() {
        }.getType();
        ArrayList<HomeResponseResultCategory> arrayList = gson.fromJson(json, type);
        return arrayList;
    }*/
    /* public void setlist(String key, ArrayList<DraftItem> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        ed.putString(key, json);
        ed.commit();
    }

    public ArrayList<DraftItem> getComment(String key) {
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<DraftItem>>() {
        }.getType();
        ArrayList<DraftItem> arrayList = gson.fromJson(json, type);

        return arrayList;
    }


    public void setlist1(String key, List<CartList> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        ed.putString(key, json);
        ed.commit();
    }

    public ArrayList<CartList> getList1(String key) {
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<CartList>>() {
        }.getType();
        ArrayList<CartList> arrayList = gson.fromJson(json, type);

        return arrayList;
    }
*/


    fun saveGCMIDToPreference(gcmIds: String) {
        ed!!.putString("FCM", gcmIds)

        ed!!.commit()
    }


    fun setFloat(key: String, value: Float) {
        ed!!.putFloat(key, value)
        ed!!.commit()
    }

    fun getBoolean(key: String): Boolean {
        return prefs!!.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        return prefs!!.getString(key, null)
    }

    fun getInt(key: String): Int {
        return prefs!!.getInt(key, 0)
    }

    fun getFloat(key: String): Float {
        return prefs!!.getFloat(key, 0.0f)
    }

    fun deleteRecord(key: String) {
        ed!!.remove(key)
        ed!!.commit()
    }

    fun saveArrayList(list: ArrayList<String>, key: String) {
        val gson = Gson()
        val json = gson.toJson(list)
        ed!!.putString(key, json)
        ed!!.commit()     // This line is IMPORTANT !!!
    }

    fun getArrayList(key: String): ArrayList<String> {
        val gson = Gson()
        val json = prefs!!.getString(key, null)
        val type = object : TypeToken<ArrayList<String>>() {

        }.type
        return gson.fromJson<ArrayList<String>>(json, type)
    }

    /*   fun setCategoriesList(key: String, value: List<HomeResponseCategories>) {
           val gson = Gson()
           val json = gson.toJson(value)
           ed?.putString(key, json)
           ed?.commit()
       }

       fun getCategoriesList(key: String): ArrayList<HomeResponseCategories> {
           val gson = Gson()
           val json = prefs?.getString(key, null)
           val type = object : TypeToken<ArrayList<HomeResponseCategories>>() {

           }.type

           return gson.fromJson(json, type)
       }*/

    companion object {
        private var prefs: SharedPreferences? = null
        private var ed: SharedPreferences.Editor? = null

        private var mInstance: PrefernceFile? = null

        fun getInstance(mContext: Context): PrefernceFile {
            if (mInstance == null) {
                mInstance = PrefernceFile(mContext)
            }
            prefs = mContext.getSharedPreferences("MyPrefrence", Context.MODE_PRIVATE)
            ed = prefs!!.edit()
            return mInstance as PrefernceFile
        }
    }


}
