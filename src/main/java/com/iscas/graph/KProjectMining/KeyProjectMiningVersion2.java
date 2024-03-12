package com.iscas.graph.KProjectMining;

import com.iscas.graph.Field.Ability;
import com.iscas.graph.Field.Project;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.PerformanceAnalysis.IndicatorsAnalysis;
import com.iscas.graph.PerformanceAnalysis.Investmentvolumeanalysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KeyProjectMiningVersion2 {

    private Double ROIWeight; // 投资回报率权重
    private Double actualBenefitWeight; // 实际效益权重
    private Double supportCapabilityNumWeight; // 支撑能力个数权重
    private Double capabilitySupportDegreeWeight; // 能力支撑度权重
    private Double capacityFulfillmentWeight; // 能力满足度权重
    private Double characterizationParametersWeight; // 特征参数权重
    private HashMap<String, Double> projectImportanceDegree; // 项目重要度

    public KeyProjectMiningVersion2() {

        // 投资回报率权重
        ROIWeight = 0.2;
        // 实际效益权重
        actualBenefitWeight = 0.2;
        // 支撑能力个数权重
        supportCapabilityNumWeight = 0.1;
        // 能力支撑度权重
        capabilitySupportDegreeWeight = 0.2;
        // 能力满足度权重
        capacityFulfillmentWeight = 0.2;
        // 特征参数权重
        characterizationParametersWeight = 0.1;
        // 项目重要度
        projectImportanceDegree = new HashMap<String, Double>();
    }

//    public KeyProjectMiningVersion2(Double ROIWeight,
//                                    Double actualBenefitWeight,
//                                    Double supportCapabilityNumWeight,
//                                    Double capabilitySupportDegreeWeight,
//                                    Double capacityFulfillmentWeight,
//                                    Double characterizationParametersWeight) {
//        // 投资回报率权重
//        ROIWeight = (ROIWeight == null || ROIWeight.isInfinite() || ROIWeight.isNaN()) ? 0.20 : ROIWeight;
//        // 实际收益权重
//        actualBenefitWeight = (actualBenefitWeight == null || actualBenefitWeight.isInfinite() || actualBenefitWeight.isNaN()) ? 0.20 : actualBenefitWeight;
//        // 能力支个数权重
//        supportCapabilityNumWeight = (supportCapabilityNumWeight == null || supportCapabilityNumWeight.isInfinite() || supportCapabilityNumWeight.isNaN()) ? 0.10 : supportCapabilityNumWeight;
//        // 能力支撑度权重
//        capabilitySupportDegreeWeight = (capabilitySupportDegreeWeight == null || capabilitySupportDegreeWeight.isInfinite() || capabilitySupportDegreeWeight.isNaN()) ? 0.20 : capabilitySupportDegreeWeight;
//        // 能力满足度权重
//        capacityFulfillmentWeight = (capacityFulfillmentWeight == null || capacityFulfillmentWeight.isInfinite() || capacityFulfillmentWeight.isNaN()) ? 0.20 : capacityFulfillmentWeight;
//        // 特征参数权重
//        characterizationParametersWeight = (characterizationParametersWeight == null || characterizationParametersWeight.isInfinite() || characterizationParametersWeight.isNaN()) ? 0.20 : characterizationParametersWeight;
//        // 项目重要度
//        projectImportanceDegree = new HashMap<String, Double>();
//    }

    // 辅助方法，用于检查权重是否null 无穷大或者NAN，如果是则返回默认值
    private static Double checkWeight(Double weight) {
        // final 表示该变量不可变
        final Double DEFAULT_WEIGHT = 0.8;
        if (weight == null || weight.isInfinite() || weight.isNaN()) {
            return DEFAULT_WEIGHT;
        }
        return weight;
    }

    public void run(SubsystemData subsystemData) {
        /**
         *
         * 基于能力-项目关联网络，
         * （1）利用复杂网络特征参数，分析挖掘支撑能力最多的骨干项目。
         * （2）利用复杂网络分析中的禁忌搜索算法等智能优化算法，挖掘贡献度最大的项目最小子集，作为骨干项目群，
         * （3）同时对项目群众的承研单位进行分析，挖掘骨干单位
         *
         * 骨干项目（排序） 骨干单位（排序） 骨干项目群
         *
         * */
        //////// 一.骨干项目挖掘
        //// 利用复杂网络特征参数 分析挖掘支撑能力最多的骨干项目
        //// 项目重要程度(y) = w1*投资回报率 + w2*实际效益 + w3*支撑能力个数 + w4*能力支撑度 + w6*能力满足度 + w7*网络特征参数
        //// 网络特征参数需要通过构建项目层节点的关联关系
        // 效能计算
        IndicatorsAnalysis indicatorsAnalysis = new IndicatorsAnalysis();
        indicatorsAnalysis.IndicatorCalculate(indicatorsAnalysis, subsystemData);
        // 经费投向投量计算
        Investmentvolumeanalysis investmentvolumeanalysis = new Investmentvolumeanalysis();
        investmentvolumeanalysis.run(indicatorsAnalysis, investmentvolumeanalysis, subsystemData, 0.15);

        for (Map.Entry<String, Double> entity: investmentvolumeanalysis.ROI.entrySet()) {
            System.out.println("Key:" + entity.getKey() + "||" + "Value:" + entity.getValue());
        }

        //// 三.骨干单位挖掘
        // 结合项目重要度，进行分数相加，分数越高，单位越重要


        //// 三.骨干项目群挖掘
        //


    }

    public static void main(String[] args) {
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
    }

    KeyProjectMiningVersion keyProjectMiningVersion = new KeyProjectMiningVersion();
    
}
