package com.iscas.graph.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class TextController {

    @PostMapping("/api/data") // 定义POST请求路由为"/api/data"
    public String receiveJsonData(@RequestBody DataObject data) {
        // 处理接收到的JSON数据

        return "Success"; // 返回成功信息或其他需要的结果
    }
}

// 自定义用于存储JSON数据的对象类
class DataObject {
    private String name;
    private int age;

    // getter and setter methods for the properties of this object
}