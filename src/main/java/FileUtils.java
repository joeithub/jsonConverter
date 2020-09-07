import lombok.extern.slf4j.Slf4j;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * @Author: tongq
 * @Date: 2020/5/11 15:46
 * @since：0.0.1
 */
@Slf4j
public class FileUtils {
    public static String readFile(String path) {
      String str = "";
        try {
                File file = new File(path);
                FileReader fileReader = new FileReader(file);
                Reader reader = new InputStreamReader(new FileInputStream(file),"Utf-8");
                int ch = 0;
                StringBuffer sb = new StringBuffer();
                while ((ch = reader.read()) != -1) {
                     sb.append((char) ch);
                }
                fileReader.close();
                reader.close();
                str = sb.toString();
               return str;
        } catch (Exception e) {
        return null;
        }
    }

    public static void writeToString(String filePath,String content){
        File file = new File(filePath);
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            FileWriter out = new FileWriter(file);
            out.write(content);
            out.close();
        } catch (IOException e) {
//            log.error("error msg: {}",e);
        }
    }
    public static String Json2Csv(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String csv = CDL.toString(jsonArray);
        return csv;
    }

    //当前文件夹下所有文件合并成一个csv
    public static void jsonToCsv(String path,boolean combine) throws Exception {
        if (! combine){
            jsonToCsv(path);
            return;
        }
        File file = new File(path);
        File[] tempList = file.listFiles();
        ArrayList<Map> arrayList = new ArrayList();
        for (File file1 : tempList) {
            if (! file1.getName().endsWith(".json")){
                continue;
            }
            String filename = file.getAbsolutePath() + File.separatorChar + file1.getName();
            String json = FileUtils.readFile(filename);
            ArrayList<Map> list = jsonToMap(json);
            for (Map map : list) {
                arrayList.add(map);
            }
        }
        //合并成一个文件
        String json = GsonUtils.convertToString(arrayList);
        String json2Csv = FileUtils.Json2Csv(json);
        FileUtils.writeToString(file.getAbsolutePath()+File.separatorChar+"zong.csv",json2Csv);
    }

    //一对一生成文件
    public static void jsonToCsv(String path) throws Exception {
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (File file1 : tempList) {
            if (! file1.getName().endsWith(".json")){
                continue;
            }
            String filename = file.getAbsolutePath() + File.separatorChar + file1.getName();
            String json = FileUtils.readFile(filename);
            ArrayList<Map> list = jsonToMap(json);
            //生成对应的文件
            json = GsonUtils.convertToString(list);
            String json2Csv = FileUtils.Json2Csv(json);
            String targetPath =filename.replace(".json", ".csv");
            FileUtils.writeToString(targetPath,json2Csv);
        }
    }

    public static ArrayList<Map> jsonToMap(String json){
        if (json.startsWith("{")){
            ArrayList<Map> list = new ArrayList();
            Map map = GsonUtils.convertToBean(json, Map.class);
            System.out.println(map);
            HashMap hashMap = getObjectObjectHashMap(map);
            list.add(hashMap);
            return list;
        }else if (json.startsWith("[")){
            return getMapsList(json);
        }
        throw new RequestException(HttpCode.ERR_PARAMS_INVALID.code(),"json格式错误");
    }

    private static ArrayList<Map> getMapsList(String json) {
        List<Map<String, Object>> maps = GsonUtils.jsonToListMaps(json);
        ArrayList<Map> list = new ArrayList();
        for (Map<String, Object> map : maps) {
            HashMap<Object, Object> hashMap = getObjectObjectHashMap(map);
            list.add(hashMap);
        }
        return list;
    }

    private static HashMap<Object, Object> getObjectObjectHashMap(Map<String, Object> map) {
        HashMap<Object, Object> mapz = new HashMap();
        ArrayList<Map> rebuild = new ArrayList();
        ArrayList<Map> all = rebuild(map, mapz, rebuild,"");
        HashMap<Object, Object> hashMap = new HashMap();
        for (Map o : all) {
            hashMap.putAll(o);
        }
        return hashMap;
    }

    public static ArrayList<Map> rebuild(Map map,Map mapz,ArrayList arrayList,String parentKey){
        Set<String> keys = map.keySet();
        for (String key : keys) {
            String keyname = key;
            if (! StringUtils.isEmpty(parentKey)){
                keyname  = parentKey+"."+key;
            }
            Object o = map.get(key);
            String s = GsonUtils.convertToString(o);
            if (o instanceof String || o instanceof Integer || o instanceof Double || o instanceof Boolean){
                //没有下层则直接放到mapz
                mapz.put(keyname,String.valueOf(map.get(key)));
                arrayList.add(mapz);
            }if (s.startsWith("[")){
                //TODO 当又嵌套一个数组该怎么做
                arrayList = getMapsList(s);
            } else if (s.startsWith("{")){
                //依然是个对象则继续遍历
                Map<String, Object> map2 = GsonUtils.jsonToMaps(s);
                arrayList = rebuild(map2,mapz,arrayList,keyname);
            }
        }
        return arrayList;
    }


}
