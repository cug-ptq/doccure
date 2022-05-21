package com.example.doccure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig serverEndpointConfig,
                                HandshakeRequest handshakeRequest, HandshakeResponse handshakeResponse){
        //获取httpsession
        HttpSession session = (HttpSession) handshakeRequest.getHttpSession();
        if (session!=null){
            serverEndpointConfig.getUserProperties().put(HttpSession.class.getName(), session);
        }
        super.modifyHandshake(serverEndpointConfig,handshakeRequest,handshakeResponse);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
