package com.iscas.graph.PerformanceAnalysis;

import com.iscas.graph.Field.Ability;
import com.iscas.graph.Field.Project;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.PerformanceAnalysis.DataNormlization.ArrayBatchNormalization;
import com.iscas.graph.PerformanceAnalysis.IndicatorsWeight.IndicatorsAdditive;
import com.iscas.graph.PerformanceAnalysis.IndicatorsWeight.IndicatorsEntropy;
import com.iscas.graph.PerformanceAnalysis.IndicatorsWeight.IndicatorsPCA;
import com.iscas.graph.PerformanceAnalysis.MatrixProcessing.NegativeMatrixValidator;
import com.iscas.graph.PerformanceAnalysis.MatrixProcessing.SparseMatrix;
import com.iscas.graph.PerformanceAnalysis.MatrixProcessing.MatrixSVD;
import com.iscas.graph.PerformanceAnalysis.NumCalFunc.LinearFunction;
import com.iscas.graph.PerformanceAnalysis.NumCalFunc.NonLinearFunction;
import org.yaml.snakeyaml.events.Event;

import java.util.*;

public class IndicatorsAnalysis {

    // 能力重要度 * 能力满足度 = 当前一级权重
    // isvip?10:5
    //
    // 项目
    public List<String> projects;
    public Map<String, Integer> project2id; //项目到id
    public Map<String, Double> project2Amount;// 项目和资金
    public Map<String, Map<String, Double>> ability2targetIndicator; // 能力和预期指标
    public Map<String, Double> targetIndicator;
    public Map<String, Map<String, Double>> project2actualIndicator; // 项目--实际指标
    public Map<String, Double> ability2abilityImportanceDegree; // 能力--能力重要度
    public Map<String, List<Double>> project2abilityImportanceDegree; // 项目--能力重要度
    public Map<String, String> project2ability;// 项目--能力
    public List<String> Indicators; // 指标
    public Map<String, String> Indicator2ability;
    public Map<String, Double> Indicator2IndicatorImportanceDegree; // 指标重要度
    public Map<String, List<Double>> Indicator2ProjectValue; // 同类指标的值
    public Map<String, Double> Indicator2IndicatorWeight;
    private String indicatorWeightMethod; // 权重计算方法
    private Double ArraySparsity; // 矩阵稀疏率
    public HashMap<String, String> researchCooperative;
    private HashMap<String, Double> indicatorRepeatability;
    public HashMap<String, List<String>> unitProject;
    private HashMap<String, Double> unitProjectNum;
    private HashMap<String, Double> unitFunding;
    private String subsystemName;

    public IndicatorsAnalysis(String indicatorWeightMethod, Double ArraySparsity) {

        // 如果参数传空
        if (indicatorWeightMethod == null || indicatorWeightMethod.isEmpty()) {
            indicatorWeightMethod = "Additive";
        }
        if (ArraySparsity == null || ArraySparsity.isInfinite() || ArraySparsity.isNaN()) {
            ArraySparsity = 0.60;
        }
        // 项目
        projects = new ArrayList<String>();
        // 项目-id
        project2id = new HashMap<String, Integer>();
        // 项目-资金
        project2Amount = new HashMap<String, Double>();
        // 能力-预期指标
        ability2targetIndicator = new HashMap<String, Map<String, Double>>();
        // 预期指标
        targetIndicator = new HashMap<String, Double>();
        // 项目-实际指标
        project2actualIndicator = new HashMap<String, Map<String, Double>>();
        // 能力-能力重要度
        ability2abilityImportanceDegree = new HashMap<String, Double>();
        // 项目-能力重要度
        project2abilityImportanceDegree = new HashMap<String, List<Double>>();
        // 项目--能力
        project2ability = new HashMap<String, String>();
        // 指标
        Indicators = new ArrayList<>();
        // 指标--能力
        Indicator2ability = new HashMap<String, String>();
        // 指标-指标重要度
        Indicator2IndicatorImportanceDegree = new HashMap<String, Double>();
        //
        Indicator2ProjectValue = new HashMap<String, List<Double>>();
        // 指标权重
        Indicator2IndicatorWeight = new HashMap<String, Double>();
        // 权重计算方法
        indicatorWeightMethod = indicatorWeightMethod;
        // 矩阵稀疏率
        ArraySparsity = ArraySparsity;
        // 承建单位<项目--承建单位>
        researchCooperative = new HashMap<String, String>();
        // 指标重复度<项目--指标重复度>
        indicatorRepeatability = new HashMap<String, Double>();
        // 单位项目
        unitProject = new HashMap<String, List<String>>();
        // 单位项目数量
        unitProjectNum = new HashMap<String, Double>();
        // 单位经费情况
        unitFunding = new HashMap<String, Double>();
        //体系名称
        subsystemName = "";
    }

    public IndicatorsAnalysis() {
        //项目
        projects = new ArrayList<String>();
        // 项目--id
        project2id = new HashMap<String, Integer>();
        // 项目--资金
        project2Amount = new HashMap<String, Double>();
        // 能力-预期指标
        ability2targetIndicator = new HashMap<String, Map<String, Double>>();
        // 预期指标
        targetIndicator = new HashMap<String, Double>();
        // 指标--能力
        Indicator2ability = new HashMap<String, String>();
        // 项目-实际指标
        project2actualIndicator = new HashMap<String, Map<String, Double>>();
        // 能力-能力重要度
        ability2abilityImportanceDegree = new HashMap<String, Double>();
        // 项目-能力重要度
        project2abilityImportanceDegree = new HashMap<String, List<Double>>();
        // 项目-能力
        project2ability = new HashMap<String, String>();
        // 指标
        Indicators = new ArrayList<>();
        // 指标-指标重要度
        Indicator2IndicatorImportanceDegree = new HashMap<String, Double>();
        //
        Indicator2ProjectValue = new HashMap<String, List<Double>>();
        // 指标权重
        Indicator2IndicatorWeight = new HashMap<String, Double>();
        // 权重计算方法
        indicatorWeightMethod = "Additive";
        // 矩阵稀疏率
        ArraySparsity = 0.60;
        // 承建单位<项目--承建单位>
        researchCooperative = new HashMap<String, String>();
        // 指标重复度<项目--指标重复度>
        indicatorRepeatability = new HashMap<String, Double>();
        // 单位项目
        unitProject = new HashMap<>();
        // 单位项目数量
        unitProjectNum = new HashMap<String, Double>();
        // 单位经费情况（承建单位 -- 经费）
        unitFunding = new HashMap<String, Double>();
        //体系名称
        subsystemName = "";
    }

    // 数据处理
    public void DataProcessing(SubsystemData data) {
        // 体系
        subsystemName += data.getSubsystemName();
        // 能力
        List<Ability> abilities = data.getAbilities();
        for (Ability ability : abilities) {
            // 能力名称
            String abilityName = ability.getAbilityTargetName();
            // 能力重要度
            Double abilityImportanceDegree = ability.getAbilityImportanceDegree();
            // 能力与能力重要度
            ability2abilityImportanceDegree.put(abilityName, abilityImportanceDegree);
            // 能力与预期指标
            ability2targetIndicator.put(abilityName, ability.getTargetIndicator());
            // 获取指标列表
            for (Map.Entry<String, Double> indicator: ability.getTargetIndicator().entrySet()) {
//                System.out.println("指标:" + indicator.getKey() + " " + "value:" +indicator.getValue());
                // 指标
                Indicators.add(indicator.getKey());
                // 指标 --能力
                Indicator2ability.put(indicator.getKey(), abilityName);
                // 预期指标
                try {
                    targetIndicator.put(indicator.getKey(), indicator.getValue());
                } catch (Exception e) {
                    //
                    System.err.println("在put操作时发生异常:" + e.getMessage());
                }
                // 指标重要度
                Indicator2IndicatorImportanceDegree.put(indicator.getKey(), abilityImportanceDegree);
            }

            // 项目
            for (Project project : ability.getProjects()) {
                // 项目--id
                project2id.put(project.getProjectName(), project.getProjectId());
                // 项目
                projects.add(project.getProjectName());
                // 承建单位
                researchCooperative.put(project.getProjectName(), project.getResearchCooperative());
                // 单位项目
                List<String> projectList = unitProject.get(project.getResearchCooperative());
                if (projectList == null) {
                    projectList = new ArrayList<>();
                    projectList.add(project.getProjectName());
                    unitProject.put(project.getResearchCooperative(), projectList);
                } else {
                    projectList.add(project.getProjectName());
                }
                // 单位总经费
                Double funding = unitFunding.get(project.getResearchCooperative());
                if (funding == null) {
                    unitFunding.put(project.getResearchCooperative(), project.getProjectAmount());
                } else {
                    funding += project.getProjectAmount();
                    unitFunding.put(project.getResearchCooperative(), funding);
                }
                // 项目--资金
                project2Amount.put(project.getProjectName(), project.getProjectAmount());
                // 项目--实际指标
                project2actualIndicator.put(project.getProjectName(), project.getActualIndicator());
                // 项目--能力重要度
                for (Map.Entry<String, Double> entity: project.getActualIndicator().entrySet()) {
                    String indicatorName = entity.getKey();
                }
            }

//            System.out.println("======================================重复指标度计算===========================================");
//            for (Map.Entry<String, Map<String, Double>> projectI: project2actualIndicator.entrySet()) {
//                String projectIName = projectI.getKey();
//                Map<String, Double> projectIInd = projectI.getValue();
//                for (Map.Entry<String, Map<String, Double>> projectJ: project2actualIndicator.entrySet()) {
//                    String projectJName = projectJ.getKey();
//                    Map<String, Double> projectJInd = projectJ.getValue();
//
//                    if (projectIName.equals(projectJName)) { // 同一个项目 pass
//                        continue;
//                    } else {
//                        // 创建临时集合
//                        Set<String> tmpSet = new HashSet<>();
//                        // 统计指标重复个数
//                        int duplicateCount = 0;
//                        // 判断指标Map的长度
//                        if (projectIInd.size() > projectJInd.size()) {
//                            continue;
//                        } else if (projectIInd.size() < projectJInd.size()) {
//                            continue;
//                        } else {
//                            continue;
//                        }
//
//
//                        // 遍历外层指标Map，将key存储到临时集合中
//                        for (String key: projectIInd.keySet()) {
//                            tmpSet.add(key);
//                        }
//                        // 遍历内层的Map，检查key是否存在于集合中
//                        for (String key: projectJInd.keySet()) {
//                            if (key.contains(key)) {
//                                duplicateCount ++;
//                            } else {
//                                continue;
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    // 归一化
    private double[][] normalize(double[][] projects2indicators) {
        /**
         * param:
         * indicators: 指标矩阵
         * */
        // 初始化归一化矩阵
        int projectNum = projects2indicators.length;
        int indicatorNum = projects2indicators[0].length;

        double[][] normalizeIndicator = new double[projects2indicators.length][projects2indicators[0].length];
        for (int i = 0; i < projects2indicators.length; i ++) {
//            System.out.println(Arrays.toString(projects2indicators[i]));

//            for (int j = 0; j < indicators[0].length; j ++) {
//
//                // 求最大最小值
//                int index = j;
//
//                double min = Double.MAX_VALUE;
//                double max = Double.MIN_VALUE;
//
//                for (double[] projectIndicator : indicators) {
//                    System.out.println(Arrays.toString(projectIndicator));
//                    if (projectIndicator.length > index) {
//                        double value = projectIndicator[index];
//                        if (value < min) {
//                            min = value;
//                        } else if (value > max) {
//                            max = value;
//                        }
//                    }
//                }
//                // 归一化指标矩阵
////                normalizeIndicator[i][j] = (indicators[i][j] - min) / (max - min);
//            }
        }

        return normalizeIndicator;
    }

    // 构建项目与实际指标数据的邻接矩阵
    public double[][] Project2actulIndicatorMatrixBuilder() {

        double[][] Project2actulIndicatorMatrix = new double[project2actualIndicator.size()][Indicators.size()];
        int row = 0;
        for (Map.Entry<String, Map<String, Double>> project: project2actualIndicator.entrySet()) {
            // 实际指标
            Map<String, Double> actualIndicators = project.getValue();
            int col = 0;
            for (String indicator: Indicators) {
                if (actualIndicators.containsKey(indicator)) {
                    Project2actulIndicatorMatrix[row][col] = actualIndicators.get(indicator);
                } else {
                    Project2actulIndicatorMatrix[row][col] = 0.0;
                }
                col ++;
            }
            row ++;
        }
        return Project2actulIndicatorMatrix;
    }

    // 构建实际指标数据与项目的邻接矩阵
    public double[][] actualIndicator2ProjectMatrixBuilder() {
        System.out.println("================================构建 指标-项目 邻接矩阵======================================");
        double[][] actualIndicatorMatrix2ProjectMatrix = new double[Indicators.size()][project2actualIndicator.size()];
        int row = 0;
//        for (Map.Entry<String, Map<String, Double>> project: project2actualIndicator.entrySet()) {
//            System.out.println(project.getValue());
//        }
        LinearFunction linearFunction = new LinearFunction();
        for (String indicator: Indicators) { // 迭代每一个指标
            int col = 0;
            List<Double> indicatorValue = new ArrayList<>();
            for (Map.Entry<String, Map<String, Double>> project: project2actualIndicator.entrySet()) { // 迭代出项目和对应的指标
                //
                Map<String, Double> actualIndicators = project.getValue();
                // 是否包含当前指标
                if (actualIndicators.containsKey(indicator)) { // 包含
                    indicatorValue.add(actualIndicators.get(indicator));
                    // divide(实际指标, 预期指标) -- 指标完成度
                    actualIndicatorMatrix2ProjectMatrix[row][col] = linearFunction.divide(actualIndicators.get(indicator), targetIndicator.get(indicator));
//                    actualIndicatorMatrix2ProjectMatrix[row][col] = actualIndicators.get(indicator);
                } else { // 不包含
                    indicatorValue.add(0.0);
                    actualIndicatorMatrix2ProjectMatrix[row][col] = 0.0;
                }
                col++;
            }
            // 同类的指标值
            Indicator2ProjectValue.put(indicator, indicatorValue);
            row++;
        }
        System.out.println("================================邻接矩阵结果======================================");
        for (int i = 0; i < actualIndicatorMatrix2ProjectMatrix.length; i ++) {
            System.out.println(Arrays.toString(actualIndicatorMatrix2ProjectMatrix[i]));
        }
        return actualIndicatorMatrix2ProjectMatrix;
    }

    // 指标计算模型
    public void IndicatorCalculate(IndicatorsAnalysis indicatorsAnalysis, SubsystemData data) {

        System.out.println("================================数据处理======================================");
        // 数据处理
        indicatorsAnalysis.DataProcessing(data);
        // 构建 项目-指标邻接矩阵 X--项目 Y--指标
        double[][] Project2IactulIndicatorMatrix = indicatorsAnalysis.Project2actulIndicatorMatrixBuilder();
        // 构建 指标-项目邻接矩阵 X--指标 Y--项目
        double[][] actualIndicatorMatrix2ProjectMatrix = indicatorsAnalysis.actualIndicator2ProjectMatrixBuilder();
//        for (int i = 0; i < actualIndicatorMatrix2ProjectMatrix.length; i ++) {
//            System.out.println(Arrays.toString(actualIndicatorMatrix2ProjectMatrix[i]));
//        }

        System.out.println("================================稀疏矩阵判断====================================");
        // 稀疏矩阵判断
        SparseMatrix sparseMatrix = new SparseMatrix();
        double sparsity = sparseMatrix.calculateDensity(actualIndicatorMatrix2ProjectMatrix);
        double[][] indiactorsMatrix;
        if (sparsity <= ArraySparsity) {
            System.out.println("================================矩阵稠密化=====================================");
            // 小于0.6 判定为稀疏矩阵 使用奇异值分解进行矩阵稠密化
            MatrixSVD matrixSVD = new MatrixSVD();
            indiactorsMatrix = matrixSVD.SVD(actualIndicatorMatrix2ProjectMatrix);
            System.out.println("================================非负矩阵判断=====================================");
            NegativeMatrixValidator negativeMatrixValidator = new NegativeMatrixValidator();
            if (negativeMatrixValidator.NegativeMatrix(indiactorsMatrix)) {
                System.out.println("是负矩阵");
                System.out.println("================================矩阵非线性映射=====================================");
                // 非线性映射 将做奇异矩阵映射到[0, 1]空间内
//                double[][] nonlinearMatrix = new double[indiactorsMatrix.length][indiactorsMatrix[0].length];
                NonLinearFunction sigmoid = new NonLinearFunction();
                for (int i = 0; i < indiactorsMatrix.length; i ++) {
                    for (int j = 0; j < indiactorsMatrix[i].length; j ++) {
                        indiactorsMatrix[i][j] = sigmoid.Sigmoid(indiactorsMatrix[i][j]);
                    }
                }
            }
        } else {
            System.out.println("=======================非稀疏矩阵=======================================================");
            System.out.println("=======================矩阵批标准化=======================================================");
            ArrayBatchNormalization arrayBatchNormalization = new ArrayBatchNormalization();
            indiactorsMatrix = arrayBatchNormalization.normalize(actualIndicatorMatrix2ProjectMatrix);
            NegativeMatrixValidator negativeMatrixValidator = new NegativeMatrixValidator();
            if (negativeMatrixValidator.NegativeMatrix(indiactorsMatrix)) {
                System.out.println("================================负矩阵判断============================================");
                System.out.println("================================矩阵非线性映射=====================================");
                // 非线性映射 将做奇异矩阵映射到[0, 1]空间内
//                double[][] nonlinearMatrix = new double[indiactorsMatrix.length][indiactorsMatrix[0].length];
                NonLinearFunction sigmoid = new NonLinearFunction();
                for (int i = 0; i < indiactorsMatrix.length; i ++) {
                    for (int j = 0; j < indiactorsMatrix[i].length; j ++) {
                        indiactorsMatrix[i][j] = sigmoid.Sigmoid(indiactorsMatrix[i][j]);
                    }
                }
            }
        }

        System.out.println("================================指标权重计算=====================================");
        // 初始化矩阵权重
        double[] indiactorsWeight = new double[indiactorsMatrix.length];

        if (indicatorWeightMethod.equals("Additive")) {
            // 指标权重计算--加性计算 Additive
            IndicatorsAdditive indicatorsAdditive = new IndicatorsAdditive();
            indiactorsWeight = indicatorsAdditive.IndicatorsWeight(indiactorsMatrix);
        } else if (indicatorWeightMethod.equals("Entropy")) {
            // 指标权重计算--熵值计算 Entropy
            IndicatorsEntropy indicatorsEntropy = new IndicatorsEntropy();
            indiactorsWeight = indicatorsEntropy.calculateEntropies(indiactorsMatrix);
        } else if (indicatorWeightMethod.equals("PCA")) {
            // 指标权重计算--主成分分析
            IndicatorsPCA indicatorsPCA = new IndicatorsPCA();
            indicatorsPCA.PCA(indiactorsMatrix);
        }

        System.out.println("================================指标-权重配对=====================================");
        // 指标-权重配对
        for (int i = 0; i < indicatorsAnalysis.Indicators.size(); i++) {
            indicatorsAnalysis.Indicator2IndicatorWeight.put(
                    indicatorsAnalysis.Indicators.get(i), indiactorsWeight[i]
            );
        }

        System.out.println(Arrays.toString(indiactorsWeight));

    }

    public void ContributeCalculate(IndicatorsAnalysis indicatorsAnalysis) {

        System.out.println("================================贡献度计算=====================================");
        // 项目指标分数计算
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

        IndicatorsAnalysis indicatorsAnalysis = new IndicatorsAnalysis();
        indicatorsAnalysis.IndicatorCalculate(indicatorsAnalysis, InputData);
        indicatorsAnalysis.ContributeCalculate(indicatorsAnalysis);

        // 对矩阵中的数值进行归一化
//        double[][] normalizeIndicator = projectContributionAnalysis.normalize(Project2IactulIndicatorMatrix);
        // 计算信息熵
//        double[] projectEntropies = projectContributionAnalysis.calculateEntropies(normalizeIndicator);

    }

}
