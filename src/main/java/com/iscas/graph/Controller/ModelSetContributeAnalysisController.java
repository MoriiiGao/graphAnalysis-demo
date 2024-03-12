package com.iscas.graph.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import org.springframework.http.HttpStatus;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.PerformanceAnalysis.IndicatorsAnalysis;
import com.iscas.graph.PerformanceAnalysis.ProjectContributeAnalysis;
import com.iscas.graph.Utils.ModelDataStorage;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/api")
public class ModelSetContributeAnalysisController {

    @PostMapping(value = "/ModelSetContributeAnalysis1")
    public ResponseEntity<JSONObject> ProjectContributeAnalysis1(@RequestBody String Data) throws JsonProcessingException {


        // 返回体
        JSONObject response = new JSONObject();

        // 创建一个ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        // 将字符串转换为json对象
//        Object jsonObject = objectMapper.readValue(Data, Object.class);
//        System.out.println(jsonObject);

        // 手动进行类型转换
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(Data, JsonObject.class);
        System.out.println(jsonObject);
        // 获取 content 字段的值
//        JsonObject content = jsonObject.getAsJsonObject("content");
        String Content = jsonObject.get("content").getAsString();
        System.out.println(Content);
        JsonObject ContentJson = gson.fromJson(Content, JsonObject.class);
        System.out.println(ContentJson);
        SubsystemData subsystemData = gson.fromJson(ContentJson, SubsystemData.class);
        System.out.println(subsystemData);
        try {
            // 指标计算
            IndicatorsAnalysis indicatorsAnalysis = new IndicatorsAnalysis();
            indicatorsAnalysis.IndicatorCalculate(indicatorsAnalysis, subsystemData);
            // 贡献度计算
            ProjectContributeAnalysis projectContributeAnalysis = new ProjectContributeAnalysis();
            Map<String, Double> projectContribute = projectContributeAnalysis.ContributeCalculate(indicatorsAnalysis);
            List<Map<String, Object>> res = projectContributeAnalysis.dataFormatting(indicatorsAnalysis, projectContribute);
            // 输出数据转字符串
            String resAsString = objectMapper.writeValueAsString(res);

            JSONObject responseData = new JSONObject();
            response.put("code", 200);
            response.put("msg", "success");
            response.put("data", res);
//            response = responseData;


            // 模型状态管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("ProjectContributeAnalysis", "/api/ProjectContributeAnalysis", Content, resAsString, "success");
            modelDataStorage.saveModelData(ModelData);
        } catch (Exception e) {

            JSONObject responseData = new JSONObject();
            response.put("code", 400);
            response.put("msg", "success");
            response.put("dat:", e.toString());
//            response = responseData;

            // 模型状态管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("ProjectContributeAnalysis", "/api/ProjectContributeAnalysis", Content, e.toString(), "error");
            modelDataStorage.saveModelData(ModelData);
        }
        System.out.println("=======================================返回结果======================================");
        System.out.println(response);
        System.out.println("====================================================================================");
        String outputData = objectMapper.writeValueAsString(response);
        return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
    }


    @PostMapping(value = "/ModelSetContributeAnalysis")
    public ResponseEntity<String> ProjectContributeAnalysis(@RequestBody String Data) throws JsonProcessingException {
        System.out.println("=====================================输入数据========================================");
        System.out.println(Data);
        System.out.println("====================================================================================");
        // 返回体
        JSONObject response = new JSONObject();
        ObjectMapper objectMapper = new ObjectMapper();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(Data, JsonObject.class);
        System.out.println("=====================================输入数据解析后的json========================================");
        System.out.println(jsonObject);
        System.out.println("====================================================================================");
        String Content = jsonObject.get("content").getAsString();
        System.out.println("=====================================获取content========================================");
        System.out.println(jsonObject);
        System.out.println("====================================================================================");
        JsonObject ContentJson = gson.fromJson(Content, JsonObject.class);
        System.out.println("=====================================content解析为json========================================");
        System.out.println(ContentJson);
        System.out.println("====================================================================================");
        // 将ContentJson转为SubsystemData
        SubsystemData subsystemData = gson.fromJson(ContentJson, SubsystemData.class);
        System.out.println("=====================================contentJson转为SubsystemData========================================");
        System.out.println(ContentJson);
        System.out.println("====================================================================================");

        delay(2000);
        try {
            // 指标计算
            IndicatorsAnalysis indicatorsAnalysis = new IndicatorsAnalysis();
            indicatorsAnalysis.IndicatorCalculate(indicatorsAnalysis, subsystemData);
            // 贡献度计算
            ProjectContributeAnalysis projectContributeAnalysis = new ProjectContributeAnalysis();
            Map<String, Double> projectContribute = projectContributeAnalysis.ContributeCalculate(indicatorsAnalysis);
            List<Map<String, Object>> res = projectContributeAnalysis.dataFormatting(indicatorsAnalysis, projectContribute);
            String resAsString = objectMapper.writeValueAsString(res);

            response.put("code", 200);
            response.put("msg", "success");
            response.put("data", res);

            // 模型状态管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("ProjectContributeAnalysis", "/api/ProjectContributeAnalysis", Content, resAsString, "success");
            modelDataStorage.saveModelData(ModelData);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("msg", "error");
            JSONObject errorData = new JSONObject();
            errorData.put("error", e.toString());
            response.put("data", errorData);

            // 模型状态管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("ProjectContributeAnalysis", "/api/ProjectContributeAnalysis", Content, e.toString(), "error");
            modelDataStorage.saveModelData(ModelData);
        }

        System.out.println("=======================================返回结果======================================");
        System.out.println(response);
        System.out.println("====================================================================================");
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    public static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("延时函数被终端");
        }
    }

}
