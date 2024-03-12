package com.iscas.graph.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.PerformanceAnalysis.CostEffectivenessRatio;
import com.iscas.graph.PerformanceAnalysis.IndicatorsAnalysis;
import com.iscas.graph.Utils.ModelDataStorage;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/api")
public class ModelSetCostEffectivenessRatioController {

    @PostMapping(value = "/ModelSetCostEffectivenessRatioAnalysis")
    public ResponseEntity<String> ProjectCostEffectivenessRatio(@RequestBody String Data) throws JsonProcessingException {
        // 返回体
        JSONObject response = new JSONObject();
        ObjectMapper objectMapper = new ObjectMapper();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(Data, JsonObject.class);
        String Content = jsonObject.get("content").getAsString();
        JsonObject ContentJson = gson.fromJson(Content, JsonObject.class);
        SubsystemData subsystemData = gson.fromJson(ContentJson, SubsystemData.class);
        System.out.println("=====================================subsystemData========================================");
        System.out.println(subsystemData);
        System.out.println("==========================================================================================");

        delay(2000);

        try {
            // 指标计算
            IndicatorsAnalysis indicatorsAnalysis = new IndicatorsAnalysis();
            indicatorsAnalysis.IndicatorCalculate(indicatorsAnalysis, subsystemData);
            // 效费比计算
            CostEffectivenessRatio costEffectivenessRatio = new CostEffectivenessRatio();
            Map<String, Double> projectCostEffectivenessRatios = costEffectivenessRatio.CostEffectivenessRatioCalculate(indicatorsAnalysis);
            List<Map<String, Object>> res = costEffectivenessRatio.dataFormatting(indicatorsAnalysis, projectCostEffectivenessRatios);
            String resAsString = objectMapper.writeValueAsString(res);

            response.put("code", 200);
            response.put("msg", "success");
            response.put("data", res);

            // 模型状态管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("ProjectCostEffectivenessRatio", "/api/ModelSetContributeAnalysis", Content, resAsString, "success");
            modelDataStorage.saveModelData(ModelData);

        } catch (Exception e) {
            response.put("code", 400);
            response.put("msg", "error");
            JSONObject errorData = new JSONObject();
            errorData.put("error", e.toString());
            response.put("data", errorData);

            // 模型状态管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("ProjectCostEffectivenessRatio", "/api/ModelSetContributeAnalysis", Content, e.toString(), "error");
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
