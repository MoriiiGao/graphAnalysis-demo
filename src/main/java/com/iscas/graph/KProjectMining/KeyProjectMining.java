package com.iscas.graph.KProjectMining;

import com.iscas.graph.Field.Ability;
import com.iscas.graph.Field.Project;
import com.iscas.graph.Field.SubsystemData;
import com.iscas.graph.Utils.Utils;

import java.util.*;

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

public class KeyProjectMining {

    // 能力指标列表 Float / String
    private List<String> abilityList;
    // 项目列表
    private List<String> projectList;
    // 项目到id映射
    private Map<String, Integer> project2idMap;
    // 项目到资金映射
    private Map<String, Double> project2projectAmountMap;
    // 项目到能力指标映射
    private Map<String, List<String>> project2abilityMap;
    // 项目到能力支撑度
    private Map<String, Double> ability2supportMap;
    // 项目到支撑度
    private Map<String, Double> project2supportMap;
    // 体系名称
    private String subsystemName;
    // 能力个数
//    private int abilityNum;

    public KeyProjectMining() {
        // 构造方法
        // 初始化值
        abilityList = new ArrayList<String>();
        projectList = new ArrayList<String>();
        project2idMap = new HashMap<String, Integer>();
        project2projectAmountMap = new HashMap<String, Double>();
        project2abilityMap = new HashMap<String, List<String>>();
        ability2supportMap = new HashMap<String, Double>();
        project2supportMap = new HashMap<String, Double>();
        subsystemName = "";
    }

    // 调整数据输出格式
    public List<Map<String, Object>> dataFormatting(List<String> projects) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (String project: projects) {
            Map<String, Object> projectData = new HashMap<>();
            projectData.put("ProjectId", project2idMap.get(project));
            projectData.put("ProjectName", project);
            projectData.put("projectAmount", project2projectAmountMap.get(project));
            data.add(projectData);
        }
        return data;
    }

    // 返回模型管理数据
    public  Map<String, String> ModelDataManagement(String api, String InputData, String OutputData, String Status) {
        Map<String, String> res;
        if (Status.equals("success")) {
            res = new HashMap<String, String>() {{
                put("modelIdentifying", "iscas-2");
                put("apiUrl", api); // "/api/KeyProjectMining"
                put("status", "成功");
                put("inputContent", InputData);
                put("outputContent", OutputData);
            }};

        } else {
            res = new HashMap<String, String>() {{
                put("modelIdentifying", "iscas-2");
                put("apiUrl", api); // "/api/KeyProjectMining"
                put("status", "失败");
                put("inputContent", InputData);
                put("outputContent", OutputData);
            }};
        }

        return res;
    }


    public List<Map<String, Object>> KProjectMining(SubsystemData data) {
        /**
         *问题1: 在静态方法中无法直接访问非静态成员（成员变量或成员方法）或使用 this 关键字，
         * 因为静态方法是与类相关联的，而非静态成员是与类的实例相关联的
         *
         * 问题2:Arrays.asList创建固定大小的列表 返回的列表不支持添加或删除
         * List<String> newAbilities = Arrays.asList(ability.getAbilityTargetName()) 创建的列表大小不变
         *
         *  针对 这个问题 可以修改为
         *  List<String> newAbilities = new ArrayList<String>();
         *  newAbility.add(XXXX)
         *
         * 问题3 打印输出Map中的kv对
         * for (Map.Entry<String, Double> entry : project2projectAmountList.entrySet()) {
         *             String project = entry.getKey();
         *             Double projectAmount = entry.getValue();
         * 问题4 打印输出错误：
         *                     try {
         *                         abilityList.add(ability.getAbilityTargetName());
         *                     } catch (ArithmeticException e) {
         *                         System.out.println("Error:" + e.getMessage());
         *                     }
         * 问题5 main方法中可以直接调用静态方法
         * 非静态方法需要先实例 然后调用
         * */

        subsystemName += data.getSubsystemName();
//        System.out.println("subsystemName:" + subsystemName);

        // 获取能力集合
        List<Ability> abilities = data.getAbilities();
        for (Ability ability : abilities) {
            // 保存能力
            abilityList.add(ability.getAbilityTargetName());
            // 获取项目集合
            List<Project> projects = ability.getProjects();
            for (Project project : projects) {
                // 保存项目
                projectList.add(project.getProjectName());
                // 保存项目到id映射
                project2idMap.put(project.getProjectName(), project.getProjectId());
                // 项目到资金映射
                project2projectAmountMap.put(project.getProjectName(),  project.getProjectAmount());

                //// 项目到能力映射
                // 获取项目对应能力列表
                List<String> abilityList = project2abilityMap.getOrDefault(project.getProjectName(), null);
                if (abilityList == null || abilityList.isEmpty()) {
                    // 如果能力列表为空，创建新能力并添加能力
                    List<String> newAbilities = new ArrayList<String>();
                    newAbilities.add(ability.getAbilityTargetName());
                    project2abilityMap.put(project.getProjectName(), newAbilities);
                } else {
                    // 如果能力列表不为空，直接添加能力
                    try {
                        abilityList.add(ability.getAbilityTargetName());
                    } catch (ArithmeticException e) {
                        System.out.println("Error:" + e.getMessage());
                    }
                }
            }
        }

        // 能力个数
        int abilityNum = abilityList.size();
        // 生成能力能力重要度
        Utils utils = new Utils();
        double[] ability_weights = utils.randomizedGeneratingCapacityIndicatorWeights(abilityNum);
        // 生成能力满足度
        double[] ability_fulfillment = utils.generationAbilityFulfillment(abilityNum);

//        System.out.println("ability_weights:" + Arrays.toString(ability_weights));
//        System.out.println("ability_fulfillment:" + Arrays.toString(ability_fulfillment));
        // 生成能力权重
        double[] contributeValue = new double[ability_weights.length];
        double sum = 0;

        for (int i = 0; i < ability_fulfillment.length; i ++) {
            contributeValue[i] = ability_weights[i] * ability_fulfillment[i];
            sum += contributeValue[i];
        }

        for (int i = 0; i < contributeValue.length; i ++) {
            contributeValue[i] /= sum; // 归一化 得到能力权重
        }

        /////// 此处需要通过构建 项目-能力指标满足度映射矩阵，结合熵权法 计算每个项目的权重 （当前未实现）
        utils.shuffleArray(contributeValue); // 随机打乱contributeValue数组

        // 能力指标到能力权重的映射
        for (int i = 0; i < abilityList.toArray().length; i ++) {
            ability2supportMap.put(abilityList.get(i), contributeValue[i]);
        }

        // 能力权重到项目映射
        for (Map.Entry<String, List<String>> entry: project2abilityMap.entrySet()) {
            String project = entry.getKey();
            List<String> abilitiesList = entry.getValue();

            for (String ability: abilitiesList) {
                if (!project2supportMap.containsKey(project)) {
                    project2supportMap.put(project, ability2supportMap.get(ability));
                } else {
                    double currentValue = project2supportMap.get(project);
                    project2supportMap.put(project, currentValue + ability2supportMap.get(ability));
                }
            }
        }

        // 计算项目权重
        double projectWeight_sum = 0.0; // 总分数
        for (double projectWeight: project2supportMap.values()) {
            projectWeight_sum += projectWeight;
        }

        for (Map.Entry<String, Double> entry: project2supportMap.entrySet()) {
            String project = entry.getKey();
            double weight = entry.getValue();
            double relativeWeight = weight / projectWeight_sum;
            project2supportMap.put(project, relativeWeight); // 更新归一化后的权重
        }
//        System.out.println(project2abilityMap);
        project2supportMap = project2supportMap.entrySet().stream() // 使用Stream API 对项目进行排序
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
        List<String> KeyProject = new ArrayList<>(project2supportMap.keySet());
//        System.out.println(project2supportMap);
        // 返回数据
        if (KeyProject.size() > 5) {

            return dataFormatting(KeyProject.subList(0, 5));
        } else {
            return dataFormatting(KeyProject);
        }
    }

    public static void main(String[] args) {
        // 链式方法+静态工厂创建树结构
//        SubsystemData data = SubsystemData.createSubsystemData(4, "体系", Arrays.asList(
//                Ability.createAbility(4, "能力指标4", Arrays.asList(
//                        Project.createProject(7, "A", 1000.0),
//                        Project.createProject(11, "E", 1000.0)
//                )),
//                Ability.createAbility(5, "能力指标5", Arrays.asList(
//                        Project.createProject(7, "A", 1000.0),
//                        Project.createProject(5, "F", 1000.0)
//                )),
//                Ability.createAbility(6, "能力指标6", Arrays.asList(
//                        Project.createProject(11, "E", 1000.0)
//                )),
//                Ability.createAbility(7, "能力指标7", Arrays.asList(
//                        Project.createProject(12, "C", 1000.0),
//                        Project.createProject(1, "D", 1000.0),
//                        Project.createProject(7, "A", 1000.0),
//                        Project.createProject(5, "F", 1000.0)
//                )),
//                Ability.createAbility(8, "能力指标8", Arrays.asList(
//                        Project.createProject(16, "Q", 1000.0),
//                        Project.createProject(17, "Z", 1000.0),
//                        Project.createProject(1, "D", 1000.0),
//                        Project.createProject(12, "C", 1000.0)
//                ))
//        ));
//
//        // mian函数中调用非静态方法 需要先实例化类 然后调用非静态方法
//        KeyProjectMining keyProjectMining = new KeyProjectMining();
//        keyProjectMining.KProjectMining(data);
//        System.out.println(data);

//        List<Project> projects1 = new ArrayList<>();
//        projects1.add(new Project(7, "A", "1000.0"));
//        projects1.add(new Project(11, "E", "1000.0"));
//        Ability ability1 = new Ability(4, "能力指标4", projects1);
//
//        List<Project> projects2 = new ArrayList<>();
//        projects2.add(new Project(7, "A", "1000.0"));
//        projects2.add(new Project(5, "F", "1000.0"));
//        Ability ability2 = new Ability(5, "能力指标5", projects2);
//
//        List<Project> projects3 = new ArrayList<>();
//        projects3.add(new Project(11, "E", "1000.0"));
//        Ability ability3 = new Ability(6, "能力指标6", projects3);
//
//        List<Project> projects4 = new ArrayList<>();
//        projects4.add(new Project(12, "C", "1000.0"));
//        projects4.add(new Project(1, "D", "1000.0"));
//        projects4.add(new Project(7, "A", "1000.0"));
//        projects4.add(new Project(5, "F", "1000.0"));
//        Ability ability4 = new Ability(7, "能力指标7", projects4);
//
//        List<Project> projects5 = new ArrayList<>();
//        projects5.add(new Project(16, "Q", "1000.0"));
//        projects5.add(new Project(17, "Z", "1000.0"));
//        projects5.add(new Project(1, "D", "1000.0"));
//        projects5.add(new Project(12, "C", "1000.0"));
//        Ability ability5 = new Ability(8, "能力指标8", projects5);
//
//        List<Ability> abilities = new ArrayList<>();
//        abilities.add(ability1);
//        abilities.add(ability2);
//        abilities.add(ability3);
//        abilities.add(ability4);
//        abilities.add(ability5);
//
//        SubsystemData data = new SubsystemData(4, "子体系", abilities);

    }
}
