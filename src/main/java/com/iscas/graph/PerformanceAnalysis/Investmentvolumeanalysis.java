package com.iscas.graph.PerformanceAnalysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iscas.graph.Field.Ability;
import com.iscas.graph.Field.Project;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.PerformanceAnalysis.NumCalFunc.LinearFunction;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import javafx.beans.binding.MapExpression;
import org.json.JSONObject;
import org.springframework.core.ReactiveAdapterRegistry;

import javax.naming.spi.DirObjectFactory;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 经费投向投量分析
 * 对经费规模大、效益水平不高的、经费投向重复的项目进行分析
 *
 * 经费投向投量分析模型旨在结合项目效费比分析，
 * 对经费投向量的合理性进行分析，通过横向比较不同项目的成本效益水平、项目的能力支撑情况以及承研单位承担项目数量和经费情况，对经费规模较大、效益水平不高、经费投向重复的项目进行分析。
 *
 * 需求：横向比较不同项目的成本效益水平、项目的能力支撑情况、承研单位承担项目数量和经费情况
 * 分析目标：对经费规模大、效益水平不高、经费投向重复的项目进行分析
 *
 * 项目的能力支撑度：（单个指标权重*能力权重）---求和
 * 从成本效益水平（效费比）、项目能力支撑情况（计算项目的能力支撑度）、承建单位承担项目数量及经费情况
 *
 * */

public class Investmentvolumeanalysis {

    public Map<String, Double> ROI; // 投资回报率
    public Map<String, Double> actualBenefits; // 实际收益
    public Map<String, Double> capacitySupportDegree; // 能力支撑度
    public Map<String, List<String>> project2ability; // 项目--能力

    public Investmentvolumeanalysis() {
        // 投资回报率
        ROI = new HashMap<String, Double>();
        // 实际收益
        actualBenefits = new HashMap<String, Double>();
        // 能力支撑度
        capacitySupportDegree = new HashMap<String, Double>();
        // 项目--能力
        project2ability = new HashMap<String, List<String>>();
    }

    // 贡献度计算
    public Map<String, Double> ContributeCalculate1(IndicatorsAnalysis indicatorsAnalysis) {
        // 项目效能指标计算
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
        return projectContribute;
    }

    public Map<String, Double> ContributeCalculate(IndicatorsAnalysis indicatorsAnalysis) {
        Map<String, Double> projectContribute = new HashMap<>();
        // 遍历每个项目的实际指标
        for (Map.Entry<String, Map<String, Double>> project : indicatorsAnalysis.project2actualIndicator.entrySet()) {
            String projectName = project.getKey();
            Map<String, Double> actualIndicators = project.getValue();
            // 计算项目贡献度
            double contributeSum = actualIndicators.entrySet().stream()
                    .mapToDouble(entry -> { // lambda表达式 对entry进行操作
                        String indicatorName = entry.getKey();
                        double indicatorValue = entry.getValue();
                        Double weight = indicatorsAnalysis.Indicator2IndicatorWeight.get(indicatorName);
                        return weight != null ? weight * indicatorValue : 0.0;
                    })
                    .sum();

            projectContribute.compute(projectName, (key, value) -> {
                if (value == null) {
                    return contributeSum;
                }
                return value + contributeSum;
            });
        }
        return projectContribute;
    }

    // 主程序
    public void run(IndicatorsAnalysis indicatorsAnalysis, Investmentvolumeanalysis investmentvolumeanalysis, SubsystemData subsystemData, double threshold_roi) {
        /**
         * 返回结果：
         * {
         *     项目名称: 1
         *     项目实际效益: 1
         *     项目当前投资回报率: 1
         *     项目总体经费: 1
         *     项目赋能能力: 1
         *     项目能力支撑度:1
         *     承建单位:1
         *     承研单位其他项目:
         *     承研单位总体资金:
         * }
         * */

        // 数据处理
        // threshold_roi 投资回报率阈值
        // 贡献度计算
        Map<String, Double> projectContributes = investmentvolumeanalysis.ContributeCalculate(indicatorsAnalysis);
        // 效费比计算
        CostEffectivenessRatio costEffectivenessRatio = new CostEffectivenessRatio();
        Map<String, Double> projectCostEffectivenessRatios = costEffectivenessRatio.CostEffectivenessRatioCalculate(indicatorsAnalysis);
        // 投入费用
        Map<String, Double> projectAmount = indicatorsAnalysis.project2Amount;
        // 项目--能力支撑度
        System.out.println("============================================项目能力支撑度计算===========================================================");
        for (Map.Entry<String, Map<String, Double>> project2indiactor: indicatorsAnalysis.project2actualIndicator.entrySet()) {
            // 项目名称
            String projectName = project2indiactor.getKey();
            // 能力支撑度
            Double abilitySupportDegree = 0.0;
            // 能力列表
            Set<String> uniqueAbilities = new HashSet<>();
            for (Map.Entry<String, Double> indicator: project2indiactor.getValue().entrySet()) {
                // 指标名称
                String indicatorName = indicator.getKey();
                // 指标权重
                Double indicatorWeight = indicatorsAnalysis.Indicator2IndicatorWeight.get(indicatorName);
                // 能力名称
                String abilityName = indicatorsAnalysis.Indicator2ability.get(indicatorName);
                // 添加列表
                uniqueAbilities.add(abilityName);
                // 能力重要度
                Double abilityImportanceDegree = indicatorsAnalysis.ability2abilityImportanceDegree.get(abilityName);
                // 能力支撑度
                abilitySupportDegree += indicatorWeight * abilityImportanceDegree;
            }

            //转回 List 类型
            List<String> uniqueAbilityList = new ArrayList<>(uniqueAbilities);
            project2ability.put(projectName, uniqueAbilityList);
            capacitySupportDegree.put(projectName, abilitySupportDegree);
        }

//        for (Map.Entry<String, List<String>> entity: project2ability.entrySet()) {
//            System.out.println("key:" + entity.getKey() + "||" + "Value:" + entity.getValue());
//        }

        System.out.println("============================================项目实际效益计算===========================================================");
        // 实际效益 = 贡献度 * 效费比
        actualBenefits = projectContributes.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    String projectName = entry.getKey();
                    double projectContribute = entry.getValue();
                    double projectCostEffectivenessRatio = projectCostEffectivenessRatios.get(projectName);
                    return projectContribute * projectCostEffectivenessRatio;
                }));
        System.out.println("============================================项目投资回报率计算===========================================================");
        // 投资回报率
        LinearFunction linearFunction = new LinearFunction();
        ROI = actualBenefits.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    String projectName = entry.getKey();
                    double actualBenefit = entry.getValue();
                    double Amount = projectAmount.get(projectName);
                    return linearFunction.divide(actualBenefit, Amount);
                }));

//        for (Map.Entry<String, Double> roi: ROI.entrySet()) {
//            System.out.println("Key:" + roi.getKey() + "||" + "Value:" + roi.getValue());
//        }

        // 投资大收益小的项目--承建单位
//        ArrayList<String> selected_projects = new ArrayList<>();
//        for (Map.Entry<String, Double> roi: ROI.entrySet()) {
//            String projectName = roi.getKey();
//            if (roi.getValue() < threshold_roi) {
//                selected_projects.add(projectName);
//            }
//        }
//        ROI.entrySet().stream();
//
//        System.out.println(selected_projects);

//        return Investmentvolumeanalysis.dataFormatting(indicatorsAnalysis);
    }

    public List<Map<String, Object>> dataFormatting(IndicatorsAnalysis indicatorsAnalysis, Investmentvolumeanalysis investmentvolumeanalysis){ // 投资回报率
        /**
         * 返回结果：
         * {
         *     项目名称: 1
         *     项目实际效益: 1
         *     项目当前投资回报率: 1
         *     项目总体经费: 1
         *     项目赋能能力: 1
         *     项目能力支撑度:1
         *     承建单位:1
         *     承研单位其他项目:
         *     承研单位总体资金:
         * }
         * */
        List<Map<String, Object>> data = new ArrayList<>();
        for (Map.Entry<String, Double> entity: investmentvolumeanalysis.ROI.entrySet()) {
            Map<String, Object> projectData = new HashMap<>();
            projectData.put("projectId", indicatorsAnalysis.project2id.get(entity.getKey())); // id
            projectData.put("projectName", entity.getKey());  // 名称
            projectData.put("actualBenefit", investmentvolumeanalysis.actualBenefits.get(entity.getKey())); // 实际效益
            projectData.put("ROI", entity.getValue()); // 投资回报率
            projectData.put("projectAmount", indicatorsAnalysis.project2Amount.get(entity.getKey())); // 资金
            projectData.put("ProjectEnabledCapacity", investmentvolumeanalysis.capacitySupportDegree.get(entity.getKey())); // 项目赋能能力
            projectData.put("researchCooperative", indicatorsAnalysis.researchCooperative.get(entity.getKey())); // 承建单位
            data.add(projectData);
        }

//        System.out.println("============================================测试数据===========================================================");
//        for (Map<String, Object> map: data) {
//            System.out.println(map);
//        }

        return data;
    }

//    private static void dataFormatting(IndicatorsAnalysis indicatorsAnalysis) {
//    }

    public static void main(String[] args) {
        SubsystemData InputData = SubsystemData.createSubsystemData(4, "火力打击体系", Arrays.asList(
                Ability.createAnotherAbility(4, "火力支援", 0.35, new HashMap<String, Double>(){{
                    put("指标1", 10.0);
                    put("指标2", 20.0);
                    put("指标3", 15.0);
                    put("指标10", 30.0);
                }}, Arrays.asList(
                        Project.createAnotherProject(4,"先进通信技术", 1500.0,"中国电子",
                                new HashMap<String, Double>() {{
                                    put("指标1", 8.0);
                                    put("指标8", 30.0);
                                    put("指标5", 6.0);
                                    put("指标7", 4.0);
                                }}),
                        Project.createAnotherProject(5,"卫星定位系统", 1000.0,"航天二院",
                                new HashMap<String, Double>() {{
                                    put("指标1", 20.0);
                                    put("指标3", 40.0);
                                    put("指标11", 30.7);
                                }}),
                        Project.createAnotherProject(6,"实时情报共享系统", 800.0,"情报院",
                                new HashMap<String, Double>() {{
                                    put("指标2", 18.0);
                                    put("指标10", 35.0);
                                    put("指标11", 41.0);
                                }})
                )),
                Ability.createAnotherAbility(5,"精确打击", 0.25, new HashMap<String, Double>(){{
                    put("指标4", 17.0);
                    put("指标5", 18.1);
                    put("指标6", 20.9);
                    put("指标7", 23.4);
                }}, Arrays.asList(
                        Project.createAnotherProject(7,"火控雷达", 1200.0,"中国电子",
                                new HashMap<String, Double>(){{
                                    put("指标4", 17.0);
                                    put("指标2", 32.0);
                                    put("指标3", 11.0);
                                    put("指标11", 17.0);
                                }}),
                        Project.createAnotherProject(8,"卫星导航系统", 1000.0,"航天科工",
                                new HashMap<String, Double>(){{
                                    put("指标7", 30.0);
                                    put("指标6", 26.0);
                                    put("指标1", 16.0);
                                    put("指标3", 12.0);
                                }}),
                        Project.createAnotherProject(9,"实时目标检测技术", 800.0,"自动化所",
                                new HashMap<String, Double>(){{
                                    put("指标2", 15.0);
                                    put("指标6", 20.0);
                                    put("指标1", 40.0);
                                    put("指标11", 10.0);
                                }}),
                        Project.createAnotherProject(10,"精确测距设备", 700.0,"航天二院",
                                new HashMap<String, Double>(){{
                                    put("指标3", 15.0);
                                    put("指标6", 32.0);
                                    put("指标8", 17.0);
                                    put("指标5", 20.0);
                                }})
                )),
                Ability.createAnotherAbility(6,"火力密集", 0.20, new HashMap<String, Double>(){{
                    put("指标8", 16.0);
                    put("指标9", 30.0);
                    put("指标11", 20.0);
                }}, Arrays.asList(
                        Project.createAnotherProject(11, "火力调度系统", 1000.0,"航天科工",
                                new HashMap<String, Double>(){{
                                    put("指标1", 35.0);
                                    put("指标8", 60.0);
                                    put("指标9", 46.0);
                                    put("指标5", 5.0);
                                    put("指标11", 3.0);
                                }}),
                        Project.createAnotherProject(12, "指挥控制系统", 1000.0,"中国电科",
                                new HashMap<String, Double>(){{
                                    put("指标4", 45.0);
                                    put("指标3", 55.0);
                                    put("指标7", 10.0);
                                    put("指标10", 40.0);
                                    put("指标2", 50.0);
                                }})
                ))
        ));
        System.out.println("=====================================原数据===========================================");
        System.out.println(InputData);
        System.out.println("=====================================================================================");
        Gson gson = new Gson();
        System.out.println("=====================================转Json字符串======================================");
        String json = gson.toJson(InputData);
        System.out.println(json);
        System.out.println("=====================================================================================");
        System.out.println("=====================================Json字符串转Json==================================");
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        System.out.println(jsonObject);
        System.out.println("=====================================================================================");
//        Investmentvolumeanalysis investmentvolumeanalysis = new Investmentvolumeanalysis();
//        investmentvolumeanalysis.run(investmentvolumeanalysis, InputData, 0.15);

    }
}
