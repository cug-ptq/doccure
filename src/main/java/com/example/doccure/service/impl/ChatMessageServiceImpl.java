package com.example.doccure.service.impl;

import com.example.doccure.dao.ChatMessageDao;
import com.example.doccure.entity.ChatListInfo;
import com.example.doccure.entity.ChatMessage;
import com.example.doccure.service.ChatMessageService;
import com.example.doccure.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    private ChatMessageDao chatMessageDao;

    /**
     * 插入对话列表
     * @param chatListInfo 列表信息
     */
    @Override
    public void insertChatListInfo(ChatListInfo chatListInfo) {
        chatMessageDao.insertChatListInfo(chatListInfo);
    }

    /**
     * 得到from的所有to(对话列表)
     * @param from_email from email
     * @return list
     */
    @Override
    public List<ChatListInfo> getAllToByFormE(String from_email) {
        return chatMessageDao.getAllToByFormE(from_email);
    }

    /**
     * 得到to的所有from(对话列表)
     * @param to_email from email
     * @return list
     */
    @Override
    public List<ChatListInfo> getAllToByToE(String to_email){
        return chatMessageDao.getAllToByToE(to_email);
    }

    /**
     * 得到对话列表信息
     * @param from_email from email
     * @param to_email to email
     * @return 信息
     */
    @Override
    public ChatListInfo getChatListInfoByFTE(String from_email, String to_email) {
        return chatMessageDao.getChatListInfoByFTE(from_email, to_email);
    }

    /**
     * 删除列表信息
     * @param from_email from email
     * @param to_email to email
     */
    @Override
    public void deleteChatListInfoByFTE(String from_email, String to_email) {
        chatMessageDao.deleteChatListInfoByFTE(from_email, to_email);
    }

    /**
     * 更新未读数
     * @param from_email from email
     * @param to_email to email
     * @param unread_num 未读数
     */
    @Override
    public void updateUnReadNum(String from_email, String to_email, int unread_num) {
        chatMessageDao.updateUnReadNum(from_email, to_email, unread_num);
    }

    @Override
    public void updateUnReadNumToZero(String from_email, String to_email) {
        chatMessageDao.updateUnReadNumToZero(from_email,to_email,0);
    }

    /**
     * 插入消息
     * @param chatMessage 消息字符串
     */
    @Override
    public void insertChatMessage(ChatMessage chatMessage) {
        chatMessageDao.insertChatMessage(chatMessage);
    }

    /**
     * 得到from发给to的所有消息
     * @param from_email from email
     * @param to_email to email
     * @return List
     */
    @Override
    public List<ChatMessage> getAllChatMessageByFTE(String from_email, String to_email) {
        return chatMessageDao.getAllChatMessageByFTE(from_email, to_email);
    }

    /**
     * 得到消息
     * @param id id
     * @return 消息
     */
    @Override
    public ChatMessage getChatMessageById(int id) {
        return chatMessageDao.getChatMessageById(id);
    }

    /**
     * 删除消息
     * @param id id
     */
    @Override
    public void deleteChatMessageByID(int id) {
        chatMessageDao.deleteChatMessageByID(id);
    }

    /**
     * 更新是否最后一条消息
     * @param id id
     * @param is_last 1/0
     */
    @Override
    public void updateIsLastByIdIsLast(int id, int is_last) {
        chatMessageDao.updateIsLastByIdIsLast(id,is_last);
    }

    /**
     * 根据from to email 更新is_last
     * @param from_email email
     * @param to_email email
     * @param is_last 1/0
     */
    @Override
    public void updateIsLastByFTEIsLast(String from_email, String to_email, int is_last) {
        chatMessageDao.updateIsLastByFTEIsLast(from_email, to_email, is_last);
    }

    /**
     * 得到最后的消息
     * @param from_email email
     * @param to_email email
     * @return 消息
     */
    @Override
    public ChatMessage getLastChatMessageByFTE(String from_email, String to_email) {
        return chatMessageDao.getLastChatMessageByFTE_IsLast(from_email,to_email, Constant.LAST_MESSAGE);
    }

    @Override
    public List<ChatMessage> getChatMessageByBETime(String from_email, String to_email, String beginTime, String endTime) {
        return chatMessageDao.getChatMessageByBETime(from_email, to_email, beginTime, endTime);
    }

    @Override
    public Integer sumUnReadByFE(String from_email) {
        if (chatMessageDao.getAllToByFormE(from_email).size()==0 || chatMessageDao.sumUnReadByFE(from_email)==0){
            return 0;
        }
        else {
            return chatMessageDao.sumUnReadByFE(from_email);
        }
    }
}
