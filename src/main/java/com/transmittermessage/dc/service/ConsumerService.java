package com.transmittermessage.dc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConsumerService {
    Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private MessageOutService messageOutService;

    @JmsListener(destination = "${spring.activemq.queue}")
    public void receivedMessage(final Message jsonMessage) throws JMSException{
        if (jsonMessage instanceof MapMessage){
            MapMessage mapMessage = (MapMessage) jsonMessage;
            String messageId = (String) mapMessage.getObject("message_id");
            HashMap payload = (HashMap) mapMessage.getObject("payload");
            logger.info("[Message] Succesfully Received Queue, The queue is = Message_id : {} and  Payload : {}",
                    messageId,payload);
            try {
                HitApiFacebookService hitApiFacebookService = new HitApiFacebookService();

                Map res = hitApiFacebookService.hitApiFacebook(payload,messageId);

                messageOutService.addMessageToDB(res);

            }catch (Exception e){
                logger.error("Error : {}",e.getMessage());
            }

        }
    }
}
