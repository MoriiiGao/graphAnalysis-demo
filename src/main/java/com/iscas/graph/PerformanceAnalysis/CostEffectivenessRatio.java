package com.iscas.graph.PerformanceAnalysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.graph.Field.Ability;
import com.iscas.graph.Field.Project;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.PerformanceAnalysis.NumCalFunc.LinearFunction;
import com.iscas.graph.Utils.ModelDataStorage;
import org.apache.commons.math3.analysis.function.Cos;

import java.util.*;

/****
 *
 * 项目效费比分析模型旨在从投入和产出两个角度对项目和项目群进行综合权衡分析。
 *
 * 投入角度主要考虑项目的费用计算方式，针对单个项目，采用不同的费用类别和费用周期来计算其费用；
 * 针对多个项目组合形成的项目群，采用单个项目费用与其数量乘积再求和方式进行计算。
 *
 * 产出角度主要考虑项目针对特定想定案例、采用某种兵力运用模式下的单项效能，在此基础上，对单项效能进行加权，得到项目群效能。
 *
 *
 * */

public class CostEffectivenessRatio {
    /**
     *
     * 单个体系下所有单项目效费比计算
     *
     * */
    // 调整返回格式
    public List<Map<String, Object>> dataFormatting(IndicatorsAnalysis indicatorsAnalysis, Map<String, Double> projectCostEffectivenessRatios) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (Map.Entry<String, Double> res: projectCostEffectivenessRatios.entrySet()) {
            Map<String, Object> projectData = new HashMap<>();
            projectData.put("ProjectId", indicatorsAnalysis.project2id.get(res.getKey())); // id
            projectData.put("projectName", res.getKey()); // 名称
            projectData.put("projectCostEffectivenessRatio", res.getValue()); // 消费比
            projectData.put("projectAmount", indicatorsAnalysis.project2Amount.get(res.getKey())); //
            data.add(projectData);
        }
        return data;
    }

    // 项目效费比计算
    public Map<String, Double> CostEffectivenessRatioCalculate(IndicatorsAnalysis indicatorsAnalysis) {
        Map<String, Double> projectCostEffectivenessRatios = new HashMap<>();
        LinearFunction linearFunction = new LinearFunction();
        for (Map.Entry<String, Map<String, Double>> project: indicatorsAnalysis.project2actualIndicator.entrySet()) {
            double contributeSum = 0.0;
            for (Map.Entry<String, Double> indicator: project.getValue().entrySet()) {
                String indicatorName = indicator.getKey();
                double indicatorValue = indicator.getValue();
                double weight = indicatorsAnalysis.Indicator2IndicatorWeight.get(indicatorName);
                contributeSum += weight * indicatorValue;
            }
            projectCostEffectivenessRatios.put(project.getKey(),
                    linearFunction.divide(contributeSum, indicatorsAnalysis.project2Amount.get(project.getKey())));
        }

        return projectCostEffectivenessRatios;
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
        // 消费比计算
        CostEffectivenessRatio costEffectivenessRatio = new CostEffectivenessRatio();
        Map<String, Double> projectCostEffectivenessRatios = costEffectivenessRatio.CostEffectivenessRatioCalculate(indicatorsAnalysis);
        List<Map<String, Object>> datas = costEffectivenessRatio.dataFormatting(indicatorsAnalysis, projectCostEffectivenessRatios);
//        for (Map.Entry<String, Double> res: projectCostEffectivenessRatios.entrySet()) {
//            System.out.println("key:" + res.getKey() + "||" + "Value:" + res.getValue());
//        }

//        for (int i = 0; i < datas.size(); i ++) {
//            System.out.println(datas.get(i));
//        }

        // 转字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String dataAsString = objectMapper.writeValueAsString(InputData);
        String resAsString = objectMapper.writeValueAsString(datas);
        System.out.println(dataAsString);
        // 模型管理
        ModelDataStorage modelDataStorage = new ModelDataStorage();
        Map<String, String> ModelData = modelDataStorage.ModelDataManagement("CostEffectivenessRatio-2","/api/XXXX", dataAsString, resAsString, "success");
        modelDataStorage.saveModelData(ModelData);

    }

}
