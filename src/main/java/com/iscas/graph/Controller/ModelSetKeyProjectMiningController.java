package com.iscas.graph.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.KProjectMining.KeyProjectMiningVersion;
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
public class ModelSetKeyProjectMiningController {
    @PostMapping(value = "/ModelSetModelSetKeyProjectMining")
    public ResponseEntity<String> KeyProjectMining(@RequestBody String Data) throws JsonProcessingException {
        System.out.println("=====================================输入Json数据========================================");
        System.out.println(Data);
        System.out.println("====================================================================================");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(Data, JsonObject.class);
        String Content = jsonObject.get("content").getAsString();
        JsonObject ContentJson = gson.fromJson(Content, JsonObject.class);
        SubsystemData subsystemData = gson.fromJson(ContentJson, SubsystemData.class);

        // 返回体
        JSONObject response = new JSONObject();
        // 转字符串
        ObjectMapper objectMapper = new ObjectMapper();
        // 输入数据转字符串
        String dataAsString = objectMapper.writeValueAsString(subsystemData);
        System.out.println(dataAsString);

        delay(2000);

        try {
            // 骨干项目挖掘
            KeyProjectMiningVersion keyProjectMiningVersion = new KeyProjectMiningVersion();
            keyProjectMiningVersion.run(subsystemData);
            List<Map<String, Object>> res = keyProjectMiningVersion.dataFormatting(keyProjectMiningVersion);

            // 输出数据转字符串
            String resAsString = objectMapper.writeValueAsString(res);
            // 数据体
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
