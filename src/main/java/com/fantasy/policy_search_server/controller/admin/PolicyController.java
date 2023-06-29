package com.fantasy.policy_search_server.controller.admin;

import com.fantasy.policy_search_server.common.AdminPermission;
import com.fantasy.policy_search_server.common.CommonRes;
import com.fantasy.policy_search_server.model.PolicyDBModel;
import com.fantasy.policy_search_server.request.PageQuery;
import com.fantasy.policy_search_server.service.PolicyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller("/admin/policy")
@RequestMapping("/admin/policy")
public class PolicyController {
    @Autowired
    private PolicyService policyService;

    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());
        List<PolicyDBModel> policyDBModelList = policyService.selectAll();
        PageInfo<PolicyDBModel> policyDBModelPageInfo = new PageInfo<>(policyDBModelList);
        ModelAndView modelAndView = new ModelAndView("/admin/policy/index");
        modelAndView.addObject("data", policyDBModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","policy");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/create_page")
    @AdminPermission
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("/admin/policy/create");
        modelAndView.addObject("CONTROLLER_NAME","policy");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    @RequestMapping("/upload_page")
    @AdminPermission
    public ModelAndView uploadPage() {
        ModelAndView modelAndView = new ModelAndView("/admin/policy/upload");
        modelAndView.addObject("CONTROLLER_NAME","policy");
        modelAndView.addObject("ACTION_NAME","upload");
        return modelAndView;
    }

    @RequestMapping("/upload")
    @ResponseBody
    @AdminPermission
    public CommonRes upload(@RequestParam(name = "file") MultipartFile file)
            throws IOException, InterruptedException {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setMaxCharsPerColumn(409600);
        CsvParser parser = new CsvParser(settings);
        InputStreamReader in = new InputStreamReader(
                file.getInputStream(),
                Charset.forName("GBK")
        );
        parser.beginParsing(in);
        String[] row;
        int count = 0, progress = 0;
        parser.parseNext();
        while ((row = parser.parseNext()) != null) {
            for (String s: row) {
                if (s != null)
                    System.out.println(s);
            }
        }
        Thread.sleep(10000);
        return CommonRes.create(1);
    }
}
