package cn.picc.com.volley.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static cn.picc.com.volley.utils.LogUtils.LOGV;


/**
 *
 */
public class Model {
    public static ModelGson modelGson = new ModelGson();

    public static <T extends Model> T create(JsonElement json, Class<T> classOfModel) {
        LOGV("Json", json.toString());
        T t = gson().fromJson(json, classOfModel);
        LOGV("Model", t.toString());
        return t;
    }



    /**
     * @param json JsonArray
     * @param classOfModel
     * @return 空数组将会返回空列表而不是null
     */
    public static <T extends Model> List<T> createList(JsonElement json, Class<T> classOfModel) {
        List<T> list = new ArrayList<>();
        JsonArray array = json.getAsJsonArray();
        Iterator<JsonElement> iterator = array.iterator();

        // 无法获取 List<T> 的运行时类型
        while (iterator.hasNext()) {
            list.add(create(iterator.next(), classOfModel));
        }
        return list;

//FIXME 无法获取 List<T> 的运行时类型
//		Type listType = new TypeToken<List<T>>() {
//			
//		}.getType();
//		
//		LOGD("Json", json.toString());
//		List<T> list = gson().fromJson(json, T[].class);
//		for (T t : list) {
//			LOGD("List:"+t.getClass().getName(), t.toString());
//		}
//		return list;
    }


    public static <T extends Model> List<T> createList(JsonElement json, Type type) {
        LOGV("Json", json.toString());
        List<T> list = gson().fromJson(json, type);
        LOGV("Model", list.toString());
        return list;
    }


    public static <T> T commonCreate(JsonElement json, Type type) {
        LOGV("Json", json.toString());
        T t = gson().fromJson(json, type);
        LOGV("Model", t.toString());
        return t;
    }

    /**
     * 把实体A转换成实体B
     * @param classA
     * @param type
     * @param <A>
     * @param <T>
     * @return
     */
    public static<A,T> T modelAtoB(A classA , Type type){
        Gson gson = new Gson();
        String gsonA = gson.toJson(classA);
        T  t=  gson.fromJson(gsonA,type);
        return t;
    }

    public static Gson gson() {
        return modelGson.gson();
    }

    public static String toJson(Object model) {
        Gson gson = gson();
        return gson.toJson(model);
    }

    public static <T> T fromJson(String json, Class<T> classOfModel) {
        Gson gson = gson();
        return gson.fromJson(json, classOfModel);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        // determine fields declared in this class only (no fields of
        // superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        // print field names paired with their values
        for (Field field : fields) {
            field.setAccessible(true);
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                // requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
