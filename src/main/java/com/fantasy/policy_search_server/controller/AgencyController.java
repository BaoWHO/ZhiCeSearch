package com.fantasy.policy_search_server.controller;

import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.common.CommonRes;
import com.fantasy.policy_search_server.common.EmBusinessError;
import com.fantasy.policy_search_server.model.AgencyModel;
import com.fantasy.policy_search_server.model.AgencySuggestModel;
import com.fantasy.policy_search_server.model.SuggestModel;
import com.fantasy.policy_search_server.request.AgencyFilterReq;
import com.fantasy.policy_search_server.service.AgencyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/api/agency")
@RequestMapping("/api/agency")
public class AgencyController {
    @Autowired
    private AgencyService agencyService;

    @RequestMapping("/agency_suggest")
    @ResponseBody
    public CommonRes searchSuggest(@RequestParam(name = "prefix")String prefix)
            throws BusinessException, IOException {
        if (StringUtils.isEmpty(prefix)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        List<AgencySuggestModel> suggestModelList = agencyService.agencySug(prefix);
        return CommonRes.create(suggestModelList);
    }

    @RequestMapping("/agency_filter")
    @ResponseBody
    public CommonRes agencyFilter(@RequestBody AgencyFilterReq agencyFilterReq)
            throws BusinessException, IOException {
        List<AgencyModel> agencyModelList = agencyService.agencyFilter(agencyFilterReq);
        List<ValueLabel> collect = agencyModelList.stream().map(
                t -> new ValueLabel(t.getAgency_full_name(), t.getAgency_full_name())
        ).collect(Collectors.toList());
        List<ValueLabel> res = new ArrayList<>(collect);
        return CommonRes.create(res);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ValueLabel {
    String value;
    String label;
}