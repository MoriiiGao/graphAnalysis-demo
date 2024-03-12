package com.iscas.graph.GraphContribute;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class CapabilityProjectGraph {

    // neo4j驱动
    private Driver driver;

    public CapabilityProjectGraph() {
        String uri = "bolt://localhost:7687";
        String user = "neo4j";
        String password = "123456";
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void readFile(String dataPath) {
        // 实体
        Set<String> Systems = new HashSet<>(); // 体系
        Set<String> Abilities = new HashSet<>(); // 能力
        Set<String> Projects = new HashSet<>(); // 项目

        // 实体属性
        List<Object> AbilitiesInfos = new ArrayList<>(); // 能力属性
        List<Object> ProjectInfos = new ArrayList<>(); // 项目属性

        // 层级关系
        List<String[]> Ability2System = new ArrayList<>(); // 能力 -> 体系
        List<String[]> Project2Ability = new ArrayList<>(); // 项目 -> 能力

        // 读取数据
        List<String[]> all_data = read_csv(dataPath);
        for (String[] data: all_data) {
            System.out.println(Arrays.toString(data));

            //
        }

    }

    // 读取csv文件
    public List<String[]> read_csv(String file_path) {

        List<String[]> data = new ArrayList<>();
        // 读取过程 字节流-> 字符流 -> 缓冲区
        // FileInputStream 打开文件 将文件中的内容读取到内存
        // InputStreamReader 将字节流转换为字符流 参数：输入流 编码方式
        // BufferedReader 缓冲区 高效读取字符流
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_path), "gb18030"))) {
                String line;
                boolean skipFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (skipFirstLine) {
                        skipFirstLine = false; // 将标志变量设置为fasle 跳过第一行
                        continue; // 跳过第一行数据
                    }

                    String[] row = line.split(",");
                    data.add(row);
                }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }


    public static void main(String[] args) throws IOException{
        String DataPath = "/Users/Gao/Desktop/project/java_1.8/Graph/src/main/java/com/iscas/graph/GraphContribute/KG_ZZ.csv";

        CapabilityProjectGraph capabilityProjectGraph = new CapabilityProjectGraph();
        capabilityProjectGraph.readFile(DataPath);

    }

}
