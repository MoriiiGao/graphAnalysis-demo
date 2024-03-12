package com.iscas.graph.Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.iscas.graph.Utils.ModelDataStorage;
import com.iscas.graph.KProjectMining.KeyProjectMining;
import com.iscas.graph.Field.SubsystemData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api")
public class KeyProjectMiningController {
    //KeyProjectMining
    @PostMapping("/KeyProjectMining")
    public JSONObject keyProjectMining(@RequestBody SubsystemData data) throws JsonProcessingException {

        // 返回体
        JSONObject response = new JSONObject();
        KeyProjectMining keyProjectMining = new KeyProjectMining();
        // 转字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String dataAsString = objectMapper.writeValueAsString(data);
        try {
            List<Map<String, Object>> res = keyProjectMining.KProjectMining(data);
            String resAsString = objectMapper.writeValueAsString(res);

//            System.out.println("data:" + data);
//            System.out.println("res:" + res);
            JSONObject responseData = new JSONObject();
            responseData.put("code", 200);
            responseData.put("msg", "success");
            responseData.put("data", res);
//            System.out.println(responseData);
            response = responseData;

            // 模型管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("iscas-2", "/api/KeyProjectMining", dataAsString, resAsString, "success");
            modelDataStorage.saveModelData(ModelData);

//            for (Map.Entry<String, String> entry: ModelData.entrySet()) {
//                System.out.println("key:" + entry.getKey() + "|" + "value:" + entry.getValue());
//            }

        }catch (Exception e) {
            JSONObject responseData = new JSONObject();
            responseData.put("code", 400);
            responseData.put("msg", "success");
            responseData.put("dat:", e.toString());
            response = responseData;

            // 模型管理
            ModelDataStorage modelDataStorage = new ModelDataStorage();
            Map<String, String> ModelData = modelDataStorage.ModelDataManagement("iscas-2", "/api/KeyProjectMining", dataAsString, e.toString(), "error");
            modelDataStorage.saveModelData(ModelData);
        }

        return  response;
    }

    @PostMapping("/echo")
    public String helloWorld(@RequestBody String messgae) {

        return messgae;
    }
}
