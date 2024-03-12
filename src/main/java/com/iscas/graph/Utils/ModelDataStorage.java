package com.iscas.graph.Utils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ModelDataStorage {

    public  Map<String, String> ModelDataManagement(String modelIdentity, String api, String InputData, String OutputData, String Status) {
        Map<String, String> res;
        if (Status.equals("success")) {
            res = new HashMap<String, String>() {{
                put("modelIdentifying", modelIdentity);
                put("apiUrl", api); // "/api/KeyProjectMining"
                put("status", "成功");
                put("inputContent", InputData);
                put("outputContent", OutputData);
            }};

        } else {
            res = new HashMap<String, String>() {{
                put("modelIdentifying", modelIdentity); // iscas-2
                put("apiUrl", api); // "/api/KeyProjectMining"
                put("status", "失败");
                put("inputContent", InputData);
                put("outputContent", OutputData);
            }};
        }

        return res;
    }
    public void saveModelData(Map<String, String> modelRes) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://172.16.20.46:8088/api/api/add/record",
                    modelRes,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("{\"code\": 200, \"message\": \"success\"}");
            }
        } catch (HttpClientErrorException e) {
            System.out.println("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.out.println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

}
