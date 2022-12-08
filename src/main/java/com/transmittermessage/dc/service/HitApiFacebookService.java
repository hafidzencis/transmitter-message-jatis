package com.transmittermessage.dc.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class HitApiFacebookService {

    Logger logger = LoggerFactory.getLogger(HitApiFacebookService.class);

    String token = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    String phoneNumberId = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";


    String url = "https://graph.facebook.com/v14.0/"+phoneNumberId+"/messages";

    public Map hitApiFacebook(final HashMap<String,Object> hashMap, final String msgId){
        logger.info("[Message] Executing hit Api Facebook");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+token);

        String json;

        try {
            json = new ObjectMapper().writeValueAsString(hashMap);
        }catch (Exception e){
            logger.error("[Error] : {}",e.getMessage());
            return null;

        }

        HttpEntity entity = new HttpEntity<>(json,headers);
        RestTemplate restTemplate = new RestTemplate();
        Object response = restTemplate.exchange(url, HttpMethod.POST,entity,Object.class);
        logger.info("[Message] succesfully get response from Facebook Api");
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonToInsert = objectMapper.writeValueAsString(response);
            JsonNode jsonNode = objectMapper.readTree(jsonToInsert);
            JsonNode Body = jsonNode.get("body");
            JsonNode messageGenerate = Body.get("messages");
            ArrayNode arrayNode = (ArrayNode) messageGenerate;
            Iterator<JsonNode> itr = arrayNode.elements();
            Object[] savewaMessageId = new Object[1];
            while (itr.hasNext()){
                savewaMessageId[0] = itr.next().get("id");
            }

            String writeValueAsString = objectMapper.writeValueAsString(savewaMessageId[0]);
            String waMessageId = writeValueAsString.substring(1,writeValueAsString.length() - 1);

            HashMap textBody = (HashMap) hashMap.get("text");

            Map<String,Object> insertToDB = new HashMap<>();
            insertToDB.put("waMessageId",waMessageId);
            insertToDB.put("messageId",msgId);
            insertToDB.put("recepientType", hashMap.get("recipient_type"));
            insertToDB.put("msisdn",hashMap.get("to"));
            insertToDB.put("sender",phoneNumberId);
            insertToDB.put("message",textBody.get("body"));
            logger.info("[Message] succesfully Executing hitApi Facebook ");
            return insertToDB;

        }catch (Exception e){
            HashMap<String,Object> objectHashMapFailed = new HashMap<>();
            objectHashMapFailed.put("code",500);
            objectHashMapFailed.put("status","FAILED");
            logger.error("[Error] : {}",e.getMessage());
            return objectHashMapFailed;
        }

    }

}
