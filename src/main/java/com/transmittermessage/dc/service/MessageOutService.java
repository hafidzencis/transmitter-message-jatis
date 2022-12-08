package com.transmittermessage.dc.service;

import com.transmittermessage.dc.model.MessageOut;
import com.transmittermessage.dc.repository.MessageOutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class MessageOutService {

    Logger logger = LoggerFactory.getLogger(MessageOutService.class);

    @Autowired
    private MessageOutRepository messageOutRepository;

    public Map addMessageToDB(Map request) {
        try {
            logger.info("[Message] Executing add message to DB");
            MessageOut messageOut = MessageOut.builder()
                    .waMessageId(request.get("waMessageId").toString())
                    .messageId(request.get("messageId").toString())
                    .recepientType(request.get("recepientType").toString())
                    .msisdn(request.get("msisdn").toString())
                    .sender(request.get("sender").toString())
                    .message(request.get("message").toString())
                    .build();

            messageOutRepository.save(messageOut);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Status", "Success Inserting to DB");
            hashMap.put("code", "200");
            logger.info("[Message] Succesfuly Insert Message To DB");
            return new HashMap<>(hashMap);
        } catch (Exception e) {
            logger.error("[ERROR] : {}", e.getMessage());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Status", "Failed");
            hashMap.put("code", "500");
            return new HashMap<>(hashMap);
        }

    }
}
