package com.mediator_service.message.serializer;

import com.mediator_service.domain.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("textSerializer")
@RequiredArgsConstructor
public class TextMessageSerializer implements MessageSerializer {

    @Override
    public String serialize(MessageResponse message) throws Exception {
        return message.type() + ":" + message.content();
    }
}
