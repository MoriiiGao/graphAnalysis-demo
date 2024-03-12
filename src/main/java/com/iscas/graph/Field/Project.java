package com.iscas.graph.Field;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.expression.spel.ast.Projection;

public class Project {
    private int projectId; // 项目ID
    private String projectName; // 项目名称
    private Double projectAmount; // 项目资金
    private String researchCooperative; // 承研单位
    private Map<String, Double> actualIndicator;

    // 无参构造函数
    public Project() {
    }

//    @JsonCreator
//    public Project(@JsonProperty("projectId") int projectId,
//                   @JsonProperty("projectName") String projectName,
//                   @JsonProperty("projectAmount") Double projectAmount) {
//        this.projectId = projectId;
//        this.projectName = projectName;
//        this.projectAmount = projectAmount;
//    }

    public String getResearchCooperative() {
        return researchCooperative;
    }

    public void setResearchCooperative(String researchCooperative) {
        this.researchCooperative = researchCooperative;
    }

    @JsonCreator
    public Project(@JsonProperty("projectId") int projectId,
                   @JsonProperty("projectName") String projectName,
                   @JsonProperty("projectAmount") Double projectAmount,
                   @JsonProperty("researchCooperative") String researchCooperative,
                   @JsonProperty("Indicator") Map<String, Double> actualIndicator) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectAmount = projectAmount;
        this.researchCooperative = researchCooperative;
        this.actualIndicator = actualIndicator;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Double getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(Double projectAmount) {
        this.projectAmount = projectAmount;
    }

    public Map<String, Double> getActualIndicator() {
        return actualIndicator;
    }

    public void setActualIndicator(Map<String, Double> actualIndicator) {
        this.actualIndicator = actualIndicator;
    }

    // 静态工厂方法
//    public static Project createProject(int projectId, String projectName, Double projectAmount) {
//         // 返回一个Porjetc对象
//         return new Project(projectId, projectName, projectAmount);
//    }

    public static Project createAnotherProject(int projectId, String projectName,
                                               Double projectAmount, String researchCooperative, Map<String, Double> actualIndicator) {
        return new Project(projectId, projectName, projectAmount, researchCooperative, actualIndicator);
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", projectAmount='" + projectAmount + '\'' +
                ", researchCooperative='" + researchCooperative + '\'' +
                ", actualIndicator='" + actualIndicator + '\'' +
                '}';
    }

}
