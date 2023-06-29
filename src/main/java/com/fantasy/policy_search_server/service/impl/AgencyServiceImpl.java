package com.fantasy.policy_search_server.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fantasy.policy_search_server.model.AgencyModel;
import com.fantasy.policy_search_server.model.AgencySuggestModel;
import com.fantasy.policy_search_server.request.AgencyFilterReq;
import com.fantasy.policy_search_server.service.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgencyServiceImpl implements AgencyService {
    @Autowired
    private ElasticsearchClient client;

    @Override
    public List<AgencySuggestModel> agencySug(String prefix) throws IOException {
        SearchResponse<AgencySuggestModel> searchResponse = client.search(s -> s.index("agency_suggest")
                        .query(q -> q
                                .bool(b -> b
                                        .must(
                                                Query.of(s1 -> s1.matchPhrasePrefix(m -> m.field("agency_full_name").query(prefix)))
                                        )
                                )
                        )
                , AgencySuggestModel.class);
        List<AgencySuggestModel> suggestModelList = new ArrayList<>();
        List<Hit<AgencySuggestModel>> hits = searchResponse.hits().hits();
        for (Hit<AgencySuggestModel> hit: hits) {
            AgencySuggestModel model = hit.source();
            model.setHtml_agency_full_name(addHighLight(model.getAgency_full_name(), prefix, "<strong>", "</strong>"));
            suggestModelList.add(model);
        }
        return suggestModelList;
    }

    @Override
    public List<AgencyModel> agencyFilter(AgencyFilterReq agencyFilterReq) throws IOException {
        SearchResponse<AgencyModel> searchResponse = client.search(s -> s.index("agency")
                        .query(q -> q
                                .bool(b -> b
                                        .must(getAgencyFilterTermQuery(agencyFilterReq))
                                )
                        ).size(4000)
                , AgencyModel.class);
        List<AgencyModel> agencyModelList = new ArrayList<>();
        List<Hit<AgencyModel>> hits = searchResponse.hits().hits();
        for (Hit<AgencyModel> hit: hits) {
            AgencyModel model = hit.source();
            agencyModelList.add(model);
        }
        return agencyModelList;
    }

    public List<Query> getAgencyFilterTermQuery(AgencyFilterReq agencyFilterReq) {
        List<Query> ret = new ArrayList<>();
        if (agencyFilterReq.getGrade().equals("国家级")) {
            ret.add(Query.of(m -> m.term(t -> t.field("grade").value(agencyFilterReq.getGrade()))));
        } else {
            if (agencyFilterReq.getProvince() != null && !agencyFilterReq.getProvince().equals("*")) {
                ret.add(Query.of(m -> m.term(t -> t.field("province").value(agencyFilterReq.getProvince()))));
                if (agencyFilterReq.getCity() != null && !agencyFilterReq.getCity().equals("*")) {
                    ret.add(Query.of(m -> m.term(t -> t.field("city").value(agencyFilterReq.getCity()))));
                    if (agencyFilterReq.getCounty() != null && !agencyFilterReq.getCounty().equals("*")) {
                        ret.add(Query.of(m -> m.term(t -> t.field("county").value(agencyFilterReq.getCounty()))));
                    }
                }
            }
        }
        return ret;
    }

    private String addHighLight(String ori, String target,String pre, String post) {
        StringBuffer sb = new StringBuffer(ori);
        int p = 0;
        while ((p = sb.indexOf(target, p)) != -1) {
            sb.insert(p + target.length(), post);
            sb.insert(p, pre);
            p += p + target.length() + pre.length() + post.length();
        }
        return sb.toString();
    }
}
