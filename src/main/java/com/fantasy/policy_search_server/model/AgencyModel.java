package com.fantasy.policy_search_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyModel {
    private String agency_full_name;
    private String grade;
    private String province;
    private String city;
    private String county;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AgencyModel) {
            AgencyModel model = (AgencyModel)obj;
            if (agency_full_name == model.agency_full_name || agency_full_name.equals(model.getAgency_full_name())) {
                if (grade == model.getGrade() || grade.equals(model.getGrade())) {
                    if (province == model.getProvince() || province.equals(model.getProvince())) {
                        if (city == model.getCity() || city.equals(model.getCity())) {
                            if (county == model.getCounty() || county.equals(model.getCounty())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (agency_full_name == null ? "null".hashCode(): agency_full_name.hashCode());
        result = 37 * result + (grade == null ? "null".hashCode(): grade.hashCode());
        result = 37 * result + (province == null ? "null".hashCode(): province.hashCode());
        result = 37 * result + (city == null ? "null".hashCode(): city.hashCode());
        result = 37 * result + (county == null ? "null".hashCode(): county.hashCode());
        return result;
    }
}
