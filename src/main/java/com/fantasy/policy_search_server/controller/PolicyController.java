package com.fantasy.policy_search_server.controller;

import com.alibaba.fastjson.JSONObject;
import com.fantasy.policy_search_server.common.BusinessException;
import com.fantasy.policy_search_server.common.CommonRes;
import com.fantasy.policy_search_server.common.CommonUtil;
import com.fantasy.policy_search_server.common.EmBusinessError;
import com.fantasy.policy_search_server.model.PolicyModel;
import com.fantasy.policy_search_server.model.UserModel;
import com.fantasy.policy_search_server.request.*;
import com.fantasy.policy_search_server.model.SuggestModel;
import com.fantasy.policy_search_server.service.PolicyService;
import com.fantasy.policy_search_server.service.RecommendService;
import com.fantasy.policy_search_server.service.SearchHistoryService;
import com.fantasy.policy_search_server.utils.TokenUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("/api/policy")
@RequestMapping("/api/policy")
@CrossOrigin(origins = "*")
public class PolicyController {
    @Autowired
    private PolicyService policyService;
    @Autowired
    private SearchHistoryService searchHistoryService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping("/search")
    @ResponseBody
    public CommonRes search(@Valid @RequestBody SimpleSearchReq req,
                            BindingResult bindingResult) throws BusinessException, IOException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        System.out.println(req.getKeyword());
        Map<String, Object> policyModelList = policyService.searchES(req.getKeyword(), req.getPage_no(), req.getPage_size());
        if (req.getToken() != null) {
            UserModel userModel = (UserModel) TokenUtil.getData(req.getToken());
            // 判断力userModel是否空
            addHistory(userModel, req.getKeyword());
        }
        return CommonRes.create(policyModelList);
    }

    @RequestMapping("/exact_search")
    @ResponseBody
    public CommonRes exactSearch(@Valid @RequestBody RequestModel requestModel,
                                 BindingResult bindingResult) throws BusinessException, IOException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        //List<PolicyModel> policyModelList = null;
        Map<String, Object> res = null;
        if (requestModel.getGrade().equals("country")) {
            res = policyService.searchESCountryScript(requestModel);
        } else if (requestModel.getGrade().equals("region") || requestModel.getGrade().equals("province") ||
                requestModel.getGrade().equals("city") || requestModel.getGrade().equals("county")) {
            res = policyService.searchESRegionScript(requestModel);
        } else {
            res = policyService.searchESAllScript(requestModel);
        }
        if (requestModel.getToken() != null) {
            UserModel userModel = (UserModel) TokenUtil.getData(requestModel.getToken());
            addHistory(userModel, requestModel.getKeyword());
        }
        return CommonRes.create(res);
    }

    @RequestMapping("/region_script")
    @ResponseBody
    public CommonRes regionScript(@Valid @RequestBody RequestModel requestModel,
                                BindingResult bindingResult) throws BusinessException, IOException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        Map<String, Object> res = policyService.searchESRegionScript(requestModel);
        if (requestModel.getToken() != null) {
            UserModel userModel = (UserModel) TokenUtil.getData(requestModel.getToken());
            addHistory(userModel, requestModel.getKeyword());
        }
        return CommonRes.create(res);
    }

    @RequestMapping("/country_script")
    @ResponseBody
    public CommonRes countryScript(@Valid @RequestBody RequestModel requestModel,
                                  BindingResult bindingResult) throws BusinessException, IOException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        Map<String, Object> res = policyService.searchESCountryScript(requestModel);
        if (requestModel.getToken() != null) {
            UserModel userModel = (UserModel) TokenUtil.getData(requestModel.getToken());
            addHistory(userModel, requestModel.getKeyword());
        }
        return CommonRes.create(res);
    }

    @RequestMapping("/search_suggest")
    @ResponseBody
    public CommonRes searchSuggest(@RequestParam(name = "prefix")String prefix)
            throws BusinessException, IOException {
        if (StringUtils.isEmpty(prefix)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        List<SuggestModel> suggestModelList = policyService.searchSug(prefix);
        return CommonRes.create(suggestModelList);
    }

    @RequestMapping("/get_policy")
    @ResponseBody
    public CommonRes getColdPolicy(@Valid @RequestBody GetPolicyReq getPolicyReq,
                                   BindingResult bindingResult)
            throws BusinessException, IOException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        PolicyModel policyModel = policyService.getPolicy(getPolicyReq.getId());
        //浏览量记录
        if (getPolicyReq.getToken() != null) {
            UserModel userModel = (UserModel) TokenUtil.getData(getPolicyReq.getToken());
            if (userModel != null) {
                policyService.saveViewToRedis(getPolicyReq.getId(), userModel.getId());
            }
        }
        return CommonRes.create(policyModel);
    }

    @RequestMapping("/get_hotpolicy")
    @ResponseBody
    public CommonRes getHotPolicy(@Valid @RequestBody GetHotPolicyReq getHotPolicyReq,
                                  BindingResult bindingResult)
            throws BusinessException, IOException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        Object o = redisTemplate.opsForValue().get("rank:"+getHotPolicyReq.getR());
        PolicyModel policyModel = JSONObject.parseObject(JSONObject.toJSONString(o), PolicyModel.class);

        //浏览量记录
        if (getHotPolicyReq.getToken() != null) {
            UserModel userModel = (UserModel) TokenUtil.getData(getHotPolicyReq.getToken());
            if (userModel != null) {
                policyService.saveViewToRedis(getHotPolicyReq.getId(), userModel.getId());
            }
        }
        return CommonRes.create(policyModel);
    }

    @RequestMapping("/get_rank")
    @ResponseBody
    public CommonRes getRank() throws BusinessException, IOException {
        policyService.getRank();
//        List<PolicyModel> policyModelList=new ArrayList<>();
//        for(int r=1;r<11;r++) {
//            Object o = redisTemplate.opsForValue().get("rank:"+r);
//            PolicyModel policyModel = JSONObject.parseObject(JSONObject.toJSONString(o), PolicyModel.class);
//            if (policyModel != null) {
//                policyModelList.add(policyModel);
//                policyModel.setView(redisTemplate.opsForZSet().score("policyRank","policyId:"+policyModel.getId()).intValue());
//            }
//        }
//        policyModelList.sort((a, b) -> b.getView() - a.getView());
        return CommonRes.create(policyService.getNewRank());
    }

    @RequestMapping("/recommend")
    @ResponseBody
    public CommonRes recommend(@Valid @RequestBody RecommendReq recommendReq,
                               BindingResult bindingResult)
            throws IOException, BusinessException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        List<PolicyModel> res;
        if (recommendReq.getToken() != null) {
            UserModel userModel = (UserModel) TokenUtil.getData(recommendReq.getToken());
            if (userModel != null) {
                res = recommendService.recommendToLogged(userModel, recommendReq.getSize());
            } else {
                res = (List<PolicyModel>) getRank().getData();
            }
        } else {
            res = (List<PolicyModel>) getRank().getData();
        }
        return CommonRes.create(res);
    }

    @RequestMapping("/other_words")
    @ResponseBody
    public CommonRes otherWords(@RequestParam(name = "keyword")String keyword)
            throws BusinessException {
        if (StringUtils.isEmpty(keyword)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        return CommonRes.create(getOtherWordsList(keyword));
    }

    public void addHistory(UserModel userModel, String keyword) throws IOException {
        if (userModel != null) {
            searchHistoryService.addHistory(userModel, keyword);
        }
    }

    public List<String> getOtherWordsList(String keyword) {
        List<String> res = new ArrayList<>();
//        String url = "https://www.baidu.com/s?ie=UTF-8&wd=" + URLEncoder.encode(keyword);
//        try {
//            // 模拟浏览器发送请求
//            Document doc = Jsoup.connect(url)
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
//                    .timeout(5000)
//                    .get();
//            // 解析搜索结果
//            System.out.println(doc.body().text());
//            Elements results = doc.select(".rs-link_2DE3Q");
//            for (Element result : results) {
//                String w = result.text();
//                if (w != null) res.add(w);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return res;
        res.add("人工智能的发展现状和趋势");
        res.add("新一代人工智能发展规划");
        res.add("人工智能高水平应用");
        res.add("促进经济高质量发展");
        res.add("人工智能产业发展条例");
        res.add("人工智能规划");
        res.add("人工智能的利与弊");
        res.add("人工智能人才发展");
        res.add("人工智能核心产业产值");
        res.add("算力基础设施布局");
        return res;
    }
}
