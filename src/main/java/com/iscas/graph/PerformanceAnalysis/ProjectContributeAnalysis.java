package com.iscas.graph.PerformanceAnalysis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.graph.Field.Ability;
import com.iscas.graph.Field.Project;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.PerformanceAnalysis.DataNormlization.RoutineNormalization;
import com.iscas.graph.Utils.ModelDataStorage;

import java.util.*;

/**
 *
 * 通过接入作战概念、能力需求、能力差距等数据，构建能力价值定位模型和使命手段框架模型，在此基础上，根据体系能力需求缺项确定贡献度配比，并以规划计划项目对体系能力需求缺项的满足情况来评估项目贡献度，实现作战体系驱动的项目贡献度评估与分析。
 *
 * 作战概念--定义能力
 * 能力需求--预期目标（实际指标）
 * 能力差距--实际指标与预期指标的差距
 * 能力价值定位模型--效能评估模型--构建项目指标 与能力指标邻接矩阵
 * 使命手段框架模型--（？）
 *
 * 根据体系能力需求缺项确定贡献度配比
 *
 * **/

public class ProjectContributeAnalysis {

    public Map<String, Double> ProjectContribute;

    public ProjectContributeAnalysis() {
        ProjectContribute = new HashMap<String, Double>();
    }

    public static Map<String, Double> normalize(Map<String, Double> projectContribute) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        //
        for (Map.Entry<String, Double> res: projectContribute.entrySet()) {
            double value = res.getValue();
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        Map<String, Double> normalizeData = new HashMap<>();
        for (Map.Entry<String, Double> res: projectContribute.entrySet()) {
            double value = res.getValue();
            double normalizedValue = (value - min) / (max - min);
            normalizeData.put(res.getKey(), normalizedValue);
            System.out.println("project:" + res.getKey() + "||" + "value:" + normalizedValue*100);
        }
        return normalizeData;
    }

    // 调整输出格式
    public List<Map<String, Object>> dataFormatting(IndicatorsAnalysis indicatorsAnalysis, Map<String, Double> projectContribute) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (Map.Entry<String, Double> res: projectContribute.entrySet()) {
            Map<String, Object> projectData = new HashMap<>();
            projectData.put("projectId", indicatorsAnalysis.project2id.get(res.getKey())); // id
            projectData.put("projectName", res.getKey());  // 名称
            projectData.put("projectContribution", res.getValue()); // 贡献度
            projectData.put("projectAmount", indicatorsAnalysis.project2Amount.get(res.getKey())); // 资金
            data.add(projectData);
        }
        return data;
    }

    // 项目贡献度计算
    public Map<String, Double> ContributeCalculate(IndicatorsAnalysis indicatorsAnalysis) {

        System.out.println("================================贡献度计算=====================================");
        // 项目指标效能计算
        Map<String, Double> projectContribute = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> project: indicatorsAnalysis.project2actualIndicator.entrySet()) {
            double contributeSum = 0.0;
            for (Map.Entry<String, Double> indicator: project.getValue().entrySet()) {
                String indicatorName = indicator.getKey();
                double indicatorValue = indicator.getValue();
                double weight = indicatorsAnalysis.Indicator2IndicatorWeight.get(indicatorName);
                contributeSum += weight * indicatorValue;
            }
            projectContribute.put(project.getKey(), contributeSum);
        }
        // 贡献度归一化
        RoutineNormalization routineNormalization = new RoutineNormalization();
        projectContribute = routineNormalization.routineNormalizationMap(projectContribute);
        for (Map.Entry<String, Double> data: projectContribute.entrySet()) {
            System.out.println("Key:" + data.getKey() + "||" + "Value:" + data.getValue());
        }
        return projectContribute;
    }

    public static void main(String[] args) throws JsonProcessingException {
        SubsystemData InputData = SubsystemData.createSubsystemData(4, "体系", Arrays.asList(
                Ability.createAnotherAbility(4, "能力4", 0.35, new HashMap<String, Double>(){{
                    put("指标1", 10.0);
                    put("指标2", 20.0);
                    put("指标3", 15.0);
                    put("指标10", 47.0);
                }}, Arrays.asList(
                        Project.createAnotherProject(4,"A", 1000.0,"aaa",
                                new HashMap<String, Double>() {{
                                    put("指标1", 8.0);
                                    put("指标8", 60.0);
                                    put("指标5", 6.0);
                                    put("指标7", 4.0);
                                }}),
                        Project.createAnotherProject(5,"B", 1000.0,"a",
                                new HashMap<String, Double>() {{
                                    put("指标1", 6.0);
                                    put("指标3", 30.0);
                                    put("指标11", 45.7);
                                }})
                )),
                Ability.createAnotherAbility(5,"能力5", 0.25, new HashMap<String, Double>(){{
                    put("指标4", 17.0);
                    put("指标5", 18.1);
                    put("指标6", 20.9);
                    put("指标7", 23.4);
                }}, Arrays.asList(
                        Project.createAnotherProject(6,"C", 1000.0,"aaa",
                                new HashMap<String, Double>(){{
                                    put("指标4", 5.0);
                                    put("指标2", 3.0);
                                    put("指标3", 11.0);
                                    put("指标11", 17.0);
                                }}),
                        Project.createAnotherProject(7,"D", 1000.0,"bb",
                                new HashMap<String, Double>(){{
                                    put("指标7", 12.0);
                                    put("指标6", 120.0);
                                    put("指标1", 100.0);
                                    put("指标3", 12.0);
                                }})
                )),
                Ability.createAnotherAbility(6,"能力6", 0.20, new HashMap<String, Double>(){{
                    put("指标8", 16.0);
                    put("指标9", 100.0);
                    put("指标11", 120.0);
                }}, Arrays.asList(
                        Project.createAnotherProject(8, "E", 1000.0,"bb",
                                new HashMap<String, Double>(){{
                                    put("指标1", 35.0);
                                    put("指标8", 75.0);
                                    put("指标9", 46.0);
                                    put("指标5", 5.0);
                                    put("指标11", 3.0);
                                }}),
                        Project.createAnotherProject(9, "F", 1000.0,"bbb",
                                new HashMap<String, Double>(){{
                                    put("指标4", 45.0);
                                    put("指标3", 55.0);
                                    put("指标7", 10.0);
                                    put("指标10", 40.0);
                                    put("指标2", 60.0);
                                }})
                ))
        ));

        // 指标计算
        IndicatorsAnalysis indicatorsAnalysis = new IndicatorsAnalysis();
        indicatorsAnalysis.IndicatorCalculate(indicatorsAnalysis, InputData);
        // 贡献度计算
        ProjectContributeAnalysis projectContributeAnalysis = new ProjectContributeAnalysis();
        Map<String, Double> projectContribute = projectContributeAnalysis.ContributeCalculate(indicatorsAnalysis);
        List<Map<String, Object>> datas = projectContributeAnalysis.dataFormatting(indicatorsAnalysis, projectContribute);

        // 转字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String dataAsString = objectMapper.writeValueAsString(InputData);
        String resAsString = objectMapper.writeValueAsString(datas);
        System.out.println("============================================= 输入数据:=====================================================");
        System.out.println(dataAsString);
        System.out.println("===========================================================================================================");
        System.out.println("============================================= 输出数据:=====================================================");
        System.out.println(resAsString);
        System.out.println("===========================================================================================================");
        // 模型管理
        ModelDataStorage modelDataStorage = new ModelDataStorage();
        Map<String, String> ModelData = modelDataStorage.ModelDataManagement("ContributeAnalysis-2","/api/XXXX", dataAsString, resAsString, "success");
        modelDataStorage.saveModelData(ModelData);
    }
}
