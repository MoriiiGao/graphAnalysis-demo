package com.iscas.graph.Field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Ability {

    private int abilityTargetId;
    private String abilityTargetName;
    private Double abilityImportanceDegree; // 能力重要度
    private Map<String, Double> targetIndicator; // 指标
    private List<Project> projects;


    // 无参构造函数
    public Ability() {
    }

    public Double getAbilityImportanceDegree() {
        return abilityImportanceDegree;
    }

    public void setAbilityImportanceDegree(Double abilityImportanceDegree) {
        this.abilityImportanceDegree = abilityImportanceDegree;
    }

    @JsonCreator
    public Ability(@JsonProperty("abilityTargetId") int abilityTargetId,
                         @JsonProperty("abilityTargetName") String abilityTargetName,
                         @JsonProperty("abilityImportanceDegree") Double abilityImportanceDegree,
                         @JsonProperty("Indicator") Map<String, Double> targetIndicator,
                         @JsonProperty("projects") List<Project> projects) {
        this.abilityTargetId = abilityTargetId;
        this.abilityTargetName = abilityTargetName;
        this.abilityImportanceDegree = abilityImportanceDegree;
        this.targetIndicator = targetIndicator;
        this.projects = projects;
    }

//    @JsonCreator
//    public Ability(@JsonProperty("abilityTargetId") int abilityTargetId,
//                   @JsonProperty("abilityTargetName") String abilityTargetName,
//                   @JsonProperty("projects") List<Project> projects) {
//        this.abilityTargetId = abilityTargetId;
//        this.abilityTargetName = abilityTargetName;
//        this.projects = projects;
//    }

    public int getAbilityTargetId() {
        return abilityTargetId;
    }

    public void setAbilityTargetId(int abilityTargetId) {
        this.abilityTargetId = abilityTargetId;
    }

    public String getAbilityTargetName() {
        return abilityTargetName;
    }

    public void setAbilityTargetName(String abilityTargetName) {
        this.abilityTargetName = abilityTargetName;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Map<String, Double> getTargetIndicator() {
        return targetIndicator;
    }

    public void setTargetIndicator(Map<String, Double> targetIndicator) {
        this.targetIndicator = targetIndicator;
    }

    // 静态工厂方法
//    public static Ability createAbility(int abilityTargetId, String abilityTargetName, List<Project> projects) {
//        return new Ability(abilityTargetId, abilityTargetName, projects);
//    }

    public static Ability createAnotherAbility(int abilityTargetId, String abilityTargetName, Double abilityImportanceDegree,
                                               Map<String, Double> targetIndicator, List<Project> projects) {
        return new Ability(abilityTargetId, abilityTargetName, abilityImportanceDegree, targetIndicator, projects);
    }

    // 添加项目的方法链
    public Ability addProject(Project project) {
        projects.add(project);
        return this;
    }

    @Override
    public String toString() {
        return "Ability{" +
                "abilityTargetId=" + abilityTargetId +
                ", abilityTargetName='" + abilityTargetName + '\'' +
                ", abilityImportanceDegree='" + abilityImportanceDegree + '\'' +
                ", targetIndicator='" + targetIndicator + '\'' +
                ", projects=" + projects +
                '}';
    }
}
