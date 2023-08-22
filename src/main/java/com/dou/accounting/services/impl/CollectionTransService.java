package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.CollectionTransMapper;
import com.dou.accounting.models.CollectionTransModel;
import com.dou.accounting.services.CollectionTransInterface;
import com.dou.adm.shared.ResponseObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CollectionTransService implements CollectionTransInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionTransService.class);

    @Autowired
    CollectionTransMapper collectionTransMapper;

    @Override
    public ResponseObject getCollTransData(CollectionTransModel model) {
        ResponseObject rs = new ResponseObject();
        rs.setData(collectionTransMapper.getCollTransData(model));
        rs.setSuccess(true);
        return rs;
    }

    @Override
    public ResponseObject getTransactions() {
        ResponseObject rs = new ResponseObject();
        RestTemplate restTemplate = new RestTemplate();;
        Map param = new HashMap();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(param, header);
        ResponseEntity<String> response = null;
        response = restTemplate.getForEntity("http://10.91.10.112:8088/despatchAPI/collectorTransaction", String.class);
        JSONObject array = new JSONObject(response.getBody());
        String isSuccess = array.get("success").toString();
        rs.setSuccess(Boolean.parseBoolean(isSuccess));
        rs.setMessage( array.get("message").toString());
        return rs;
    }
}
