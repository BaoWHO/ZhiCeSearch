package com.fantasy.policy_search_server.algorithm;

import co.elastic.clients.json.JsonData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CountryAlgorithmModel {
    double titleWeight;
    double timeWeight;
    String time;
    Map<String, JsonData> params;
    String titleScoreScript =
            "int[] time=new int[]{100,80,60,40,20,5};\n" +
            "double score=0;\n" +
            "double titleMatch=_score;\n" +
            "double timeScore=0;\n" +
            "ZonedDateTime zdt = ZonedDateTime.parse(params.time);\n" +
            "long bet=ChronoUnit.DAYS.between(doc['pub_time'].value,zdt);\n" +
            "if(bet<=1) {\n" +
            "   timeScore=time[0];\n" +
            "}\n" +
            "if(bet<=7&&bet>1) {\n" +
            "   timeScore=time[1];\n" +
            "}\n" +
            "if(bet<=30&&bet>7) {\n" +
            "   timeScore=time[2];\n" +
            "}if(bet<=182.5&&bet>30) {\n" +
            "   timeScore=time[3];\n" +
            "}\n" +
            "if(bet<=365&&bet>182.5) {\n" +
            "   timeScore=time[4];\n" +
            "}\n" +
            "if(bet>365) {\n" +
            "   timeScore=time[5];\n" +
            "}\n" +
            "score=params.titleWeight*titleMatch+params.timeWeight*timeScore;\n" +
            "return score;\n" ;


    public CountryAlgorithmModel(double titleWeight, double timeWeight, String time) {
        this.titleWeight = titleWeight;
        this.timeWeight = timeWeight;
        this.time = time;
        this.params = new HashMap<>();
        params.put("titleWeight", JsonData.of(titleWeight));
        params.put("timeWeight", JsonData.of(timeWeight));
        params.put("time", JsonData.of(time));
    }

    public CountryAlgorithmModel() {
        this.titleWeight = 0.6;
        this.timeWeight = 0.4;
        this.time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
        this.params = new HashMap<>();
        params.put("titleWeight", JsonData.of(titleWeight));
        params.put("timeWeight", JsonData.of(timeWeight));
        params.put("time", JsonData.of(time));
    }

    public double getTitleWeight() {
        return titleWeight;
    }

    public void setTitleWeight(double titleWeight) {
        this.titleWeight = titleWeight;
        params.put("titleWeight", JsonData.of(titleWeight));
    }

    public double getTimeWeight() {
        return timeWeight;
    }

    public void setTimeWeight(double timeWeight) {
        this.timeWeight = timeWeight;
        params.put("timeWeight", JsonData.of(timeWeight));
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        params.put("time", JsonData.of(time));
    }

    public Map<String, JsonData> getParams() {
        return params;
    }

    public String getTitleScoreScript() {
        return titleScoreScript;
    }
}
