package com.iscas.graph.Field;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors()
public class SubsystemData implements Serializable {
    private int subsystemId;
    private String subsystemName;
    private List<Ability> abilities;

    // 无参构造函数
    public SubsystemData() {
    }

    @JsonCreator
    public SubsystemData(@JsonProperty("subsystemId") int subsystemId,
                         @JsonProperty("subsystemName") String subsystemName,
                         @JsonProperty("abilities") List<Ability> abilities) {
        this.subsystemId = subsystemId;
        this.subsystemName = subsystemName;
        this.abilities = abilities;
    }


    // 静态工厂方法
    public static SubsystemData createSubsystemData(int subsystemId, String subsystemName, List<Ability> abilities) {
        return new SubsystemData(subsystemId, subsystemName, abilities);
    }

    // 添加能力的方法链
    public SubsystemData addAbility(Ability ability) {
        abilities.add(ability);
        return this;
    }

    public int getSubsystemId() {
        return subsystemId;
    }

    public String getSubsystemName() {
        return subsystemName;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public String toString() {
        return "SubsystemData{" +
                "subsystemId=" + subsystemId +
                ", subsystemName='" + subsystemName + '\'' +
                ", abilities=" + abilities +
                '}';
    }

}
