package com.iscas.graph.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.PerformanceAnalysis.IndicatorsAnalysis;
import com.iscas.graph.PerformanceAnalysis.ProjectContributeAnalysis;
import com.iscas.graph.Utils.ModelDataStorage;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/api")
public class ProjectContributeAnalysisController {

    @PostMapping(value = "/ProjectContributeAnalysis")
    public JSONObject ProjectContributeAnalysis(@RequestBody SubsystemData data) throws JsonProcessingException {
        System.out.println("=====================================输入Json数据========================================");
        System.out.println(data);
        System.out.println("====================================================================================");

        // 返回体
        JSONObject response = new JSONObject();
        // 转字符串
        ObjectMapper objectMapper = new ObjectMapper();
        // 输入数据转字符串
        String dataAsString = objectMapper.writeValueAsString(data);
        System.out.println(dataAsString);
        try {
            // 指标计算
            IndicatorsAnalysis indicatorsAnalysis = new IndicatorsAnalysis();
            indicatorsAnalysis.IndicatorCalculate(indicatorsAnalysis, data);
            // 贡献度计算
            ProjectContributeAnalysis projectContributeAnalysis = new ProjectContributeAnalysis();
            Map<String, Double> projectContribute = projectContributeAnalysis.ContributeCalculate(indicatorsAnalysis);
            List<Map<String, Object>> res = projectContributeAnalysis.dataFormatting(indicatorsAnalysis, projectContribute);
            // 输出数据转字符串
            String resAsString = objectMapper.writeValueAsString(res);

            JSONObject responseData = new JSONObject();
            responseData.put("code", 200);
            responseData.put("msg", "success");
            responseData.put("data", res);
            response = responseData;
            System.out.println("=======================================返回结果======================================");
            System.out.println(response);
            System.out.println("====================================================================================");

            // 模型状态管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("ProjectContributeAnalysis", "/api/ProjectContributeAnalysis", dataAsString, resAsString, "success");
            modelDataStorage.saveModelData(ModelData);
        } catch (Exception e) {

            JSONObject responseData = new JSONObject();
            responseData.put("code", 400);
            responseData.put("msg", "success");
            responseData.put("dat:", e.toString());
            response = responseData;

            // 模型状态管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("ProjectContributeAnalysis", "/api/ProjectContributeAnalysis", dataAsString, e.toString(), "error");
            modelDataStorage.saveModelData(ModelData);
        }

        return response;
    }

}
