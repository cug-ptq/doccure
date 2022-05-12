package com.example.doccure.service;

import com.example.doccure.entity.ChatListInfo;
import com.example.doccure.entity.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatMessageService {
    //list
    void insertChatListInfo(ChatListInfo chatListInfo);

    List<ChatListInfo> getAllToByFormE(String from_email);

    List<ChatListInfo> getAllToByToE(String to_email);

    ChatListInfo getChatListInfoByFTE(String from_email, String to_email);
    void deleteChatListInfoByFTE(String from_email,String to_email);
    void updateUnReadNum(String from_email,String to_email,int unread_num);
    void updateUnReadNumToZero(String from_email,String to_email);

    //message
    void insertChatMessage(ChatMessage chatMessage);
    List<ChatMessage> getAllChatMessageByFTE(String from_email,String to_email);
    ChatMessage getChatMessageById(int id);
    void deleteChatMessageByID(int id);
    void updateIsLastByIdIsLast(int id,int is_last);
    void updateIsLastByFTEIsLast(String from_email,String to_email,int is_last);
    ChatMessage getLastChatMessageByFTE(String from_email, String to_email);

    //时间段
    List<ChatMessage> getChatMessageByBETime(String from_email, String to_email, String beginTime,String endTime);
    Integer sumUnReadByFE(String from_email);
}
