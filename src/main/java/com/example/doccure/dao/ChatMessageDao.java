package com.example.doccure.dao;

import com.example.doccure.entity.ChatListInfo;
import com.example.doccure.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMessageDao {
    //list
    void insertChatListInfo(ChatListInfo chatListInfo);

    List<ChatListInfo> getAllToByFormE(String from_email);
    List<ChatListInfo> getAllToByToE(String from_email);
    ChatListInfo getChatListInfoByFTE(String from_email,String to_email);
    void deleteChatListInfoByFTE(String from_email,String to_email);
    void updateUnReadNum(String from_email,String to_email,int unread_num);
    void updateUnReadNumToZero(String from_email,String to_email,int unread_num);

    //message
    void insertChatMessage(ChatMessage chatMessage);

    List<ChatMessage> getAllChatMessageByFTE(String from_email,String to_email);
    ChatMessage getChatMessageById(int id);
    ChatMessage getLastChatMessageByFTE_IsLast(String from_email, String to_email, int is_last);
    void deleteChatMessageByID(int id);
    void updateIsLastByIdIsLast(int id,int is_last);
    void updateIsLastByFTEIsLast(String from_email, String to_email, int is_last);

    //时间段
    List<ChatMessage> getChatMessageByBETime(String from_email, String to_email, String beginTime,String endTime);

    Integer sumUnReadByFE(String from_email);
}
