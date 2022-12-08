package com.transmittermessage.dc.repository;

import com.transmittermessage.dc.model.MessageOut;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageOutRepository extends CrudRepository<MessageOut,Long> {
}
