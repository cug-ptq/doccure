package com.example.doccure.controller;

import com.example.doccure.config.WebSocketConfig;
import com.example.doccure.entity.ChatListInfo;
import com.example.doccure.entity.ChatMessage;
import com.example.doccure.entity.User;
import com.example.doccure.service.ChatMessageService;
import com.example.doccure.service.UserService;
import com.example.doccure.utils.ApplicationContextFactory;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.Msg;
import com.example.doccure.utils.MsgVideo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/chatWebSocket",configurator = WebSocketConfig.class)
@Component
@SuppressWarnings("all")
public class ChatWebSocket {
    //每个用户的ChatWebSocket对象
    private static CopyOnWriteArraySet<ChatWebSocket> chatWebSockets = new CopyOnWriteArraySet<ChatWebSocket>();
    //email与Session映射
    private static Map<String, Session> emailSessionMap = new HashMap<String,Session>();
    //本地会话连接，给其他用户发送信息
    private Session session;
    //本地用户email
    private String email;
    //全局容器
    private ApplicationContext applicationContext;
    private ChatMessageService chatMessageService;
    private UserService userService;


    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig){
        //获取登录时存放httpSession的用户数据
        HttpSession httpSession= (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());

        User user = (User) httpSession.getAttribute("user");

        //初始化数据
        this.applicationContext = ApplicationContextFactory.getApplicationContext();
        this.session = session;
        this.email = user.getEmail();
        this.chatMessageService = applicationContext.getBean(ChatMessageService.class);
        this.userService = applicationContext.getBean(UserService.class);
        //绑定username与session
        emailSessionMap.put(email, session);

        //加入set中
        chatWebSockets.add(this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //将当前的session删除
        chatWebSockets.remove(this);
        emailSessionMap.remove(email);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        //从客户端传过来的数据是json数据，所以这里使用jackson进行转换为chatMsg对象，
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(message);
            String type = jsonNode.get("type").asText();
            if (type.equals(Constant.MSG_RESUME)){
                //更新resume数据
                sendMsgToDoc(type,message);
            }
            else if (type.equals(Constant.MSG_ASSESS)){
                //更新assess
                assessService(type,message);
            }
            else if (type.equals(Constant.MSG_EXAM)){
                //更新体检
                sendMsgToDoc(type,message);
            }
            else if (type.equals(Constant.MSG_VISIT)){
                visitService(type,message);
            }
            else if(type.equals(Constant.MSG_CHAT)){
                chatService(jsonNode.get("message").asText());
            }
            else if (type.equals(Constant.MSG_VIDEO)){
                videoService(type,message);
            }
//            else {
//                videoService(message);
//            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    public void chatService(String message){
        ChatMessage chatMessage;
        String returnChatMsg = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //{"from_email":patientInfo.email,"to_email":to_email,"time":nowTime.toLocaleString().replaceAll("/","-")}
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
            chatMessage = objectMapper.readValue(message, ChatMessage.class);
            //对chatMessage进行装箱
            chatMessage.setFrom_email(email);chatMessage.setIs_last(Constant.LAST_MESSAGE);

            //更新上一个最后一条消息 可能是对方发给我的(我发给对方的)
            chatMessageService.updateIsLastByFTEIsLast(chatMessage.getFrom_email(),chatMessage.getTo_email(),
                    Constant.NOT_LAST_MESSAGE);
            chatMessageService.updateIsLastByFTEIsLast(chatMessage.getTo_email(),chatMessage.getFrom_email(),
                    Constant.NOT_LAST_MESSAGE);


            //聊天关系列表 如果双方没有聊天关系
            if (chatMessageService.getChatListInfoByFTE(chatMessage.getFrom_email(),
                    chatMessage.getTo_email())==null){
                //不存在聊天关系 插入 未读数为1
                //from to 0
                chatMessageService.insertChatListInfo(new ChatListInfo(chatMessage.getFrom_email(),
                        chatMessage.getTo_email(),Constant.CHAT,0));
                //to from 1
                chatMessageService.insertChatListInfo(new ChatListInfo(chatMessage.getTo_email(),
                        chatMessage.getFrom_email(),Constant.CHAT,1));
            }
            else {
                //如果已经存在聊天关系 对方未读数加1
                chatMessageService.updateUnReadNum(chatMessage.getTo_email(),
                        chatMessage.getFrom_email(),1);
            }

            //得到session
            Session fromSession = emailSessionMap.get(chatMessage.getFrom_email());
            Session toSession = emailSessionMap.get(chatMessage.getTo_email());

            returnChatMsg = objectMapper.writeValueAsString(chatMessage);
            // 判断接收方的toSession是否为null
            if (toSession != null && toSession.isOpen()) {
                //发送给发送者.
                toSession.getAsyncRemote().sendText(Msg.getMsgJsonType(Constant.MSG_CHAT,returnChatMsg));
            }

            //保存聊天记录信息
            chatMessageService.insertChatMessage(chatMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void assessService(String type,String message){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String to_email = jsonNode.get("to_email").asText();
            Session toSession = emailSessionMap.get(to_email);
            if (toSession!=null){
                toSession.getAsyncRemote().sendText(Msg.getMsgJsonType(type,message));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMsgToDoc(String type,String message){
        for (String email : emailSessionMap.keySet()){
            if (userService.getUserByE(email).getRole()==Constant.doctorRole){
                Session toSession = emailSessionMap.get(email);
                if (toSession!=null){
                    toSession.getAsyncRemote().sendText(Msg.getMsgJsonType(type,message));
                }
            }
        }
    }
    private void visitService(String type, String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String to_email = jsonNode.get("to_email").asText();
            Session toSession = emailSessionMap.get(to_email);
            if (toSession!=null){
                toSession.getAsyncRemote().sendText(Msg.getMsgJsonType(type,message));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void videoService(String type,String message){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Msg msg = objectMapper.readValue(message,Msg.class);
            MsgVideo msgVideo = objectMapper.readValue(msg.getMessage(),MsgVideo.class);
            Session toSession = emailSessionMap.get(msgVideo.getToUser());
            MsgVideo msgVideo1 = new MsgVideo();
            msgVideo1.setType(msgVideo.getType());
            //呼叫的用户不在线
            if(toSession == null){
                toSession = session;
                msgVideo1.setType("call_back");
                msgVideo1.setFromUser("系统消息");
                msgVideo1.setMsg("Sorry，呼叫的用户不在线！");
                try {
                    toSession.getBasicRemote().sendText(Msg.getMsgJsonType(type,objectMapper.writeValueAsString(msgVideo1)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            //对方挂断
            if ("call_end".equals(msgVideo.getType())) {
                msgVideo1.setFromUser(msgVideo.getFromUser());
                msgVideo1.setMsg("对方挂断！");
            }

            //视频通话请求
            if ("call_start".equals(msgVideo.getType())) {
                msgVideo1.setFromUser(msgVideo.getFromUser());
                msgVideo1.setMsg("1");
            }

            //视频通话请求回应
            if ("call_back".equals(msgVideo.getType())) {
                msgVideo1.setFromUser(msgVideo.getToUser());
                msgVideo1.setMsg(msgVideo.getMsg());
            }

            //offer
            if ("offer".equals(msgVideo.getType())) {
                msgVideo1.setFromUser(msgVideo.getToUser());
                msgVideo1.setSdp(msgVideo.getSdp());
            }

            //answer
            if ("answer".equals(msgVideo.getType())) {
                msgVideo1.setFromUser(msgVideo.getToUser());
                msgVideo1.setSdp(msgVideo.getSdp());
            }

            //ice
            if ("_ice".equals(msgVideo.getType())) {
                msgVideo1.setFromUser(msgVideo.getToUser());
                msgVideo1.setIceCandidate(msgVideo.getIceCandidate());
            }
            synchronized(toSession){
                try {
                    toSession.getBasicRemote().sendText(Msg.getMsgJsonType(type,objectMapper.writeValueAsString(msgVideo1)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        try{
//            //jackson
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//            //JSON字符串转 HashMap
//            HashMap hashMap = mapper.readValue(message, HashMap.class);
//
//            //消息类型
//            String type = (String) hashMap.get("type");
//            //to user
//            String toUser = (String) hashMap.get("toUser");
//            Session toUserSession = emailSessionMap.get(toUser);
//            String fromUser = (String) hashMap.get("fromUser");
//
//            //msg
//            String msg = (String) hashMap.get("msg");
//
//            //sdp
//            String sdp = (String) hashMap.get("sdp");
//
//            //ice
//            Map iceCandidate  = (Map) hashMap.get("iceCandidate");
//
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("type",type);
//
//            //呼叫的用户不在线
//            if(toUserSession == null){
//                toUserSession = session;
//                map.put("type","call_back");
//                map.put("fromUser","系统消息");
//                map.put("msg","Sorry，呼叫的用户不在线！");
//                synchronized(toUserSession){
//                    try {
//                        toUserSession.getBasicRemote().sendText(mapper.writeValueAsString(map));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return;
//            }
//
//            //对方挂断
//            if ("hangup".equals(type)) {
//                map.put("fromUser",fromUser);
//                map.put("msg","对方挂断！");
//            }
//
//            //视频通话请求
//            if ("call_start".equals(type)) {
//                map.put("fromUser",fromUser);
//                map.put("msg","1");
//            }
//
//            //视频通话请求回应
//            if ("call_back".equals(type)) {
//                map.put("fromUser",toUser);
//                map.put("msg",msg);
//            }
//
//            //offer
//            if ("offer".equals(type)) {
//                map.put("fromUser",toUser);
//                map.put("sdp",sdp);
//            }
//
//            //answer
//            if ("answer".equals(type)) {
//                map.put("fromUser",toUser);
//                map.put("sdp",sdp);
//            }
//
//            //ice
//            if ("_ice".equals(type)) {
//                map.put("fromUser",toUser);
//                map.put("iceCandidate",iceCandidate);
//            }
//
//            synchronized (toUserSession){
//                try {
//                    toUserSession.getBasicRemote().sendText(mapper.writeValueAsString(map));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }


    public static Map<String, Session> getEmailSessionMap(){
        return emailSessionMap;
    }
}
