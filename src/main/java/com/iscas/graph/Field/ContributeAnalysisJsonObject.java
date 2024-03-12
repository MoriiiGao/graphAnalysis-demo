package com.iscas.graph.Field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class ContributeAnalysisJsonObject {

    private String ArraySparsity;
    private String Context;

    public ContributeAnalysisJsonObject() {

    }

    public String getArraySparsity() {
        return ArraySparsity;
    }

    public void setArraySparsity(String arraySparsity) {
        ArraySparsity = arraySparsity;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    @JsonCreator
    public ContributeAnalysisJsonObject(@JsonProperty("ArraySparsity") String ArraySparsity,
                   @JsonProperty("Context") String Context) {
        this.ArraySparsity = ArraySparsity;
        this.Context = Context;
    }

}
