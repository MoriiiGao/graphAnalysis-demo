package com.iscas.graph.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iscas.graph.Utils.ModelDataStorage;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@ResponseBody
@RequestMapping("/api")
public class ModelSetProjectInformationExtractionController {

    @PostMapping(value = "/ModelSetProjectInformationExtraction")
    public ResponseEntity<String> tProjectInformationExtraction(@RequestBody String Data) throws JsonProcessingException {
        System.out.println(Data);
        // 创建 JSON 对象
        JSONObject response = new JSONObject();

        // 创建 ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper();

        // 创建 Gson 对象
        Gson gson = new Gson();

        // 从字符串 Data 中解析出 JsonObject 对象
        JsonObject jsonObject = gson.fromJson(Data, JsonObject.class);

        // 从 jsonObject 中获取名为 "content" 的属性值并转换为字符串
        String Content = jsonObject.get("content").getAsString();

        // 将 Content 字符串解析为另一个 JsonObject 对象
        JsonObject ContentJson = gson.fromJson(Content, JsonObject.class);
        System.out.println(ContentJson);

//        // 延时7s
        delay(7000);

        try {

            Map<String, List<String>> res = new HashMap<>();
            res.put("项目名称", new ArrayList<>(Arrays.asList("战略通信网络升级计划")));
            res.put("项目描述", new ArrayList<>(Arrays.asList("该项目旨在对军队的通信网络进行全面升级和改造，提高军队的通信保障能力和信息交互效率，以支持信息化战斗指挥体系建设。")));
            res.put("项目目标", new ArrayList<>(Arrays.asList("通信网络进行全面升级和改造", "提高军队的通信保障能力和信息交互效率", "支持信息化战斗指挥体系建设")));
            // res.put("性能指标", new ArrayList<>(List.of("可用性提高到99.99%以上", "通信带宽提高至每秒10Gbps以上", "平均响应时间控制在100毫秒以下")));
            res.put("发起人", new ArrayList<>(Arrays.asList("王强主任")));
            res.put("承研单位", new ArrayList<>(Arrays.asList("国防科技研究院", "信息作战研究院")));
            res.put("战略分布", new ArrayList<>(Arrays.asList("陆军战区", "海军战区")));
            res.put("成果", new ArrayList<>(Arrays.asList("5篇高水平专利", "5篇国防技术专利")));
            res.put("项目周期", new ArrayList<>(Arrays.asList("3年")));
            res.put("项目资金", new ArrayList<>(Arrays.asList("1亿美元")));
            res.put("物质资源", new ArrayList<>(Arrays.asList("通信设备", "软件系统", "测试设备")));
            res.put("项目关键节点", new ArrayList<>(Arrays.asList("系统架构设计", "设备采购", "系统集成测试通过")));
            res.put("项目计划", new ArrayList<>(Arrays.asList("正在编制中")));
            res.put("项目里程碑", new ArrayList<>(Arrays.asList("第一阶段设备更新完成", "系统验收合格")));
            res.put("项目风险", new ArrayList<>(Arrays.asList("技术风险", "供应链风险", "安全风险")));
            res.put("项目交付物", new ArrayList<>(Arrays.asList("升级后的通信网络系统", "技术文档", "培训材料")));
            res.put("质量目标", new ArrayList<>(Arrays.asList("确保系统性能满足要求", "数据传输安全可靠")));
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

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);

    }

    // 定义延时函数
    public static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("延时函数被终端");
        }
    }
}
