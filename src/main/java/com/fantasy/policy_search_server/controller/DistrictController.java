package com.fantasy.policy_search_server.controller;

import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.common.CommonRes;
import com.fantasy.policy_search_server.common.CommonUtil;
import com.fantasy.policy_search_server.common.EmBusinessError;
import com.fantasy.policy_search_server.request.DistrictReq;
import com.fantasy.policy_search_server.response.DistrictRes;
import com.fantasy.policy_search_server.service.DistrictService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

@Controller("/api/district")
@RequestMapping("/api/district")
public class DistrictController {
    @Autowired
    DistrictService districtService;

    @RequestMapping("/get_district")
    @ResponseBody
    public CommonRes getDistrict(@Valid @RequestBody DistrictReq districtReq,
                                 BindingResult bindingResult) throws BusinessException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        DistrictRes res = districtService.getDistrict(districtReq);
        return CommonRes.create(res);
    }

    @RequestMapping("/get_all")
    @ResponseBody
    public CommonRes getAll() throws IOException {
        ClassPathResource resource = new ClassPathResource("/static/json/pca.json");
        String data = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(data);
        Map<String, Object> map = objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>(){});
        return CommonRes.create(map);
    }
}
