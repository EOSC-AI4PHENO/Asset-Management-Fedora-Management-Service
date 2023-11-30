package com.siseth.management.module.socket;

import com.siseth.management.component.entity.D_File;
import com.siseth.management.component.image.ImageUtil;
import com.siseth.management.module.fedora.model.GetImage;
import com.siseth.management.module.socket.dto.SocketContentResDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebSocketContentService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final static String WEB_SOCKET_CONTENT_PATH = "/queue/content/";

    @Async
    @SneakyThrows
    public void sendContent(String channelId, List<D_File> fileList){
        Thread.sleep(2000L);
        for (D_File file : fileList) {
            simpMessagingTemplate.convertAndSend(WEB_SOCKET_CONTENT_PATH + channelId,
                    new SocketContentResDTO(file.getId(),
                            ImageUtil.getBaseWithConvert(new GetImage(file.getPath()).getResponse(), 200, 0)
                            ));
            Thread.sleep(2000L);
        }
    }


}
