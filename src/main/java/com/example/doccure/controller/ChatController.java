package com.example.doccure.controller;

import com.example.doccure.entity.*;
import com.example.doccure.entity.info.DoctorInfo;
import com.example.doccure.entity.info.PatientInfo;
import com.example.doccure.service.ChatMessageService;
import com.example.doccure.service.DoctorInfoService;
import com.example.doccure.service.PatientInfoService;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.Msg;
import com.example.doccure.utils.ServiceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 聊天界面的渲染
 */
@Controller
public class ChatController {
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private DoctorInfoService doctorInfoService;
    @Autowired
    private PatientInfoService patientInfoService;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/chat-doctor")
    public String chatDoctor(Model model, HttpServletRequest request,RedirectAttributes redirectAttributes){
        User user = (User) request.getSession().getAttribute("user");
        if (user.getRole()==Constant.doctorRole){
            model.addAttribute("user",user);
            model.addAttribute("Info",doctorInfoService.getDoctorInfoByE(user.getEmail()));

            //user chatList
            List<ChatListInfo> chatListInfos = chatMessageService.getAllToByFormE(user.getEmail());
            model.addAttribute("chatListInfos",chatListInfos);

            //chatList patientInfos
            List<PatientInfo> patientInfos = new ArrayList<>();
            for (ChatListInfo chatListInfo:chatListInfos){
                patientInfos.add(patientInfoService.getPatientInfoByE(chatListInfo.getTo_email()));
            }
            model.addAttribute("InfosList",patientInfos);
            model.addAttribute("emailPatientMap", ServiceUtil.getEmailPatientMap(patientInfos));

            //first chatMessage
            List<ChatMessage> lastChatMessages = new ArrayList<>();
            for (ChatListInfo chatListInfo:chatListInfos){
                ChatMessage chatMessage = chatMessageService.getLastChatMessageByFTE(chatListInfo.getFrom_email(),
                        chatListInfo.getTo_email());
                if (chatMessage == null) {
                    chatMessage = chatMessageService.getLastChatMessageByFTE(chatListInfo.getTo_email(),
                            chatListInfo.getFrom_email());
                }
                lastChatMessages.add(chatMessage);
            }
            model.addAttribute("lastChatMessages",lastChatMessages);
            model.addAttribute("isDoctor",true);
            return "chat-doctor";
        }
        //chatList patientInfos chatMessage一一对应

        redirectAttributes.addFlashAttribute("msg","您不是医生");
        return "redirect:/chat/blank-page";
    }


    @RequestMapping("/chat")
    public String chatPatient(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
        User user = (User) request.getSession().getAttribute("user");

        if (user.getRole()==Constant.patientRole){
            model.addAttribute("user", user);
            model.addAttribute("Info", patientInfoService.getPatientInfoByE(user.getEmail()));

            //user chatList
            List<ChatListInfo> chatListInfos = chatMessageService.getAllToByFormE(user.getEmail());
            model.addAttribute("chatListInfos", chatListInfos);


            //allDoctors select选择
            List<DoctorInfo> allDoctorInfos = new ArrayList<>(doctorInfoService.getAllDoctor());
            model.addAttribute("allDoctorInfos", allDoctorInfos);
            //逻辑处理 前端
            model.addAttribute("emailDoctorMap", ServiceUtil.getEmailDoctorMap(allDoctorInfos));


            //first chatMessage
            List<ChatMessage> lastChatMessages = new ArrayList<>();
            //在列表中的医生
            List<DoctorInfo> doctorInfosList = new ArrayList<>();
            for (ChatListInfo chatListInfo : chatListInfos) {
                ChatMessage chatMessage = chatMessageService.getLastChatMessageByFTE(chatListInfo.getFrom_email(),
                        chatListInfo.getTo_email());
                if (chatMessage == null) {
                    chatMessage = chatMessageService.getLastChatMessageByFTE(chatListInfo.getTo_email(),
                            chatListInfo.getFrom_email());
                }
                lastChatMessages.add(chatMessage);
                doctorInfosList.add(doctorInfoService.getDoctorInfoByE(chatListInfo.getTo_email()));
            }
            model.addAttribute("InfosList", doctorInfosList);
            model.addAttribute("lastChatMessages", lastChatMessages);
            model.addAttribute("isDoctor",false);
            //chatList doctorInfos chatMessage一一对应
            return "chat";
        }
        redirectAttributes.addFlashAttribute("msg","您不是病人");
        return "redirect:/chat/blank-page";
    }

    /**
     * 第一次点击页面 得到和to 消息
     * @param request user
     * @param to_email email
     * @return 排序的消息数组JSON
     */
    @RequestMapping("/chat/getMessagesFirst")
    @ResponseBody
    public Map<String,ChatMessage> getMessagesFirst(HttpServletRequest request, String to_email){
        User user = (User) request.getSession().getAttribute("user");
        if (to_email!=null){

            ChatMessage lastChatMessage = chatMessageService.getLastChatMessageByFTE(user.getEmail(),
                    to_email);
            if (lastChatMessage == null) {
                lastChatMessage = chatMessageService.getLastChatMessageByFTE(to_email,
                        user.getEmail());
            }
            if (lastChatMessage!=null){
                //最后一条消息的时间
                String lastTime = lastChatMessage.getTime().toString();
                String[] lastTimeArray = lastTime.split(" ");
                String beginTime = ServiceUtil.getDateByTimeDays(lastTimeArray[0],-60)+" "+lastTimeArray[1];
                //获得前三天的消息
                List<ChatMessage> chatMessagesTo = chatMessageService.getChatMessageByBETime(user.getEmail(),
                        to_email,ServiceUtil.timeBeforeChat(beginTime,-100), lastTime);

                List<ChatMessage> chatMessagesFrom = chatMessageService.getChatMessageByBETime(to_email,
                        user.getEmail(),ServiceUtil.timeBeforeChat(beginTime,-100), lastTime);

                List<ChatMessage> chatMessages = new ArrayList<>(chatMessagesFrom);
                chatMessages.addAll(chatMessagesTo);

                //时间 message map
                Map<String,ChatMessage> timestampChatMessageMap = new TreeMap<>(new MyCompare());
                for (ChatMessage chatMessage:chatMessages){
                    timestampChatMessageMap.put(chatMessage.getTime().toString(),chatMessage);
                }
                chatMessageService.updateUnReadNumToZero(user.getEmail(),to_email);
                return timestampChatMessageMap;
            }
        }
        return new HashMap<>();
    }

    /**
     * 根据请求浏览更多信息 得到和to 消息
     * @param request user
     * @param to_email email
     * @param nowEarlyTime 浏览的最早一条消息
     * @return 排序的消息数组JSON
     */
    @RequestMapping("/chat/getMessagesMore")
    @ResponseBody
    public Map<String,ChatMessage> getMessagesMore(HttpServletRequest request, String to_email,String nowEarlyTime,int days){
        User user = (User) request.getSession().getAttribute("user");
        if (to_email!=null){

            //当前浏览的最早一条消息的时间
            nowEarlyTime = ServiceUtil.timeBeforeChat(nowEarlyTime,-1);
            String[] nowLastTimeArray = nowEarlyTime.split(" ");
            //获得前三天的消息
            String beginTime = ServiceUtil.getDateByTimeDays(nowLastTimeArray[0],days)+" "+nowLastTimeArray[1];
            List<ChatMessage> chatMessagesTo = chatMessageService.getChatMessageByBETime(user.getEmail(),
                    to_email,beginTime,
                    nowEarlyTime);

            List<ChatMessage> chatMessagesFrom = chatMessageService.getChatMessageByBETime(to_email,
                    user.getEmail(),beginTime,
                    nowEarlyTime);

            List<ChatMessage> chatMessages = new ArrayList<>(chatMessagesFrom);
            chatMessages.addAll(chatMessagesTo);

            //时间 message map
            Map<String,ChatMessage> timestampChatMessageMap = new TreeMap<>(new MyCompare());
            for (ChatMessage chatMessage:chatMessages){
                timestampChatMessageMap.put(chatMessage.getTime().toString(),chatMessage);
            }
            chatMessageService.updateUnReadNumToZero(user.getEmail(),to_email);
            return timestampChatMessageMap;
        }
        return new HashMap<>();
    }

    /**
     * 点击对话窗口后，更新未读数
     * @param request user
     * @param to_email email
     * @return 操偶结果
     */
    @RequestMapping("/chat/updateUnRead")
    @ResponseBody
    public String updateUnRead(HttpServletRequest request,String to_email){
        User user = (User) request.getSession().getAttribute("user");
        if (to_email!=null){
            chatMessageService.updateUnReadNumToZero(user.getEmail(),to_email);
            return Msg.getMsgJsonCode(1,"更新成功");
        }
        return Msg.getMsgJsonCode(-1,"请求失败");
    }

    @RequestMapping("/chat/getDoctorInfo")
    @ResponseBody
    public String getDoctorInfo(String email){
        if (email!=null){
            return DoctorInfo.getJson(doctorInfoService.getDoctorInfoByE(email));
        }
        return Msg.getMsgJsonCode(-1,"获取失败");
    }

    @RequestMapping("/chat/getPatientInfo")
    @ResponseBody
    public String getPatientInfo(String email){
        if (email!=null){
            return PatientInfo.getJson(patientInfoService.getPatientInfoByE(email));
        }
        return Msg.getMsgJsonCode(-1,"获取失败");
    }


    static class MyCompare implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }
}
