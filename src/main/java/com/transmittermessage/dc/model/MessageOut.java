package com.transmittermessage.dc.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_out")
@Builder
public class MessageOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wa_message_id")
    private String waMessageId;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "recipient_type")
    private String recepientType;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "sender")
    private String sender;

    @Column(name = "message",columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate(){
        this.createdAt = LocalDateTime.now();
    }



}
