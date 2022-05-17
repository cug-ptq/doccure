/**
 * 点击select显示聊天界面
 * @param email filter
 */
function changeChatList(email) {
    //如果不在聊天列表
    if (nowChatTo.get(email)==null){
        //第一次聊天
        nowChatTo.set(email,"#"+email);

        //添加进入ToList 新的聊天没有最后一条消息
        addToInList(emailToMap[email],"");
        isFirstClick.set(email,0);

        //处理class
        $(nowTo).attr("class","media");
        nowTo = "#" + email;
        $(nowTo).attr("class","media read-chat active");
        //处理chatBody
        $(nowDivChatBody).hide();
        addChatBody(email,true);
        nowDivChatBody = "#chatBody" + email;
    }
    else {
        //显示界面
        $(nowDivChatBody).hide();
        nowDivChatBody = "#chatBody" + email;
        $(nowDivChatBody).show();
        //<a>
        $(nowTo).attr("class","media");
        nowTo = "#" + email;
        $(nowTo).attr("class","media read-chat active");
    }
    //未读显示
    let unread = "#unread" + email;
    $(unread).text(0);
    unReadEmailNum.set(email,0);

    //message 容器
    nowMessageBody = "#messageBody" + email;
    //messageBody的上层div
    addScrollListener(email);

    //提交时，得到id->得到正确email
    $(sendMessage).attr("value",email);
    $(video_start).attr("href",call());
    $("#userNameChat").text(emailToMap[email].username);
    $("#userNameChatImage").attr("src",emailToMap[email].image_url).attr("alt",emailToMap[email].username);
    toZero(emailToMap[email].email);
}
/**
 * 点解<a>，修改class,显示界面
 * @param obj id->email filter
 */
function clickTo(obj) {
    unReadEmailNum.set(obj.id,0);
    let unread = "#unread" + obj.id;
    $(unread).text(0);
    //当前聊天 class 显示
    $(nowTo).attr("class","media");
    nowTo = "#"+obj.id;
    $(nowTo).attr("class","media read-chat active");

    //显示界面
    $(nowDivChatBody).hide();
    nowDivChatBody = "#chatBody" + obj.id;
    $(nowDivChatBody).show();

    //message 容器
    nowMessageBody = "#messageBody" + obj.id;

    //messageBody的上层div
    addScrollListener(obj.id);
    //提交时，得到id->得到正确email
    $(sendMessage).attr("value",obj.id);
    $(video_start).attr("href",call());
    $("#userNameChat").text(emailToMap[obj.id].username);
    $("#userNameChatImage").attr("src",emailToMap[obj.id].image_url).attr("alt",emailToMap[obj.id].username);

    //如果是第一次点击
    if (isFirstClick.get(obj.id)===0){
        //设置为已经点击
        console.log(obj.id);
        isFirstClick.set(obj.id,1);
        firstClickChat(obj.id);
    }
    toZero(emailToMap[obj.id].email);
}

/**
 * 消息清零
 */
function toZero(to_mail) {
    $.ajax({
        url:"/chat/updateUnRead",
        type:"post",
        dataType:"json",
        data:{"to_email":to_mail},
        success:function (data){
            if (data.code===-1){
                console.log("更新失败");
            }
        }
    });
}

/**
 * 显示列表中信息->医生、最后一条消息、增加chatBody
 * @param ToInfos
 * @param lastChatMessages
 * @param chatListInfos
 */
function showChatToList(ToInfos,lastChatMessages,chatListInfos) {
    let isLast = true;
    if (lastChatMessages.length===0){
        isLast = false;
    }
    for (let i = 0;i < ToInfos.length;i++){
        let filterEmail = emailFilter(ToInfos[i].email);
        nowChatTo.set(filterEmail,"#"+filterEmail);
        isFirstClick.set(filterEmail,0);
        unReadEmailNum.set(filterEmail,chatListInfos[i].unread_num);
        emailMessageEarly.set(ToInfos[i].email,"");
        console.log(ToInfos[i].email);
        $("#"+filterEmail).attr("value",0);
        addChatBody(filterEmail);
        if (isLast){
            addToInList(ToInfos[i],lastChatMessages[i],chatListInfos[i].unread_num);
        }
        else {
            addToInList(ToInfos[i],"",0);
        }
    }
    console.log(unReadEmailNum);
}


/**
 * 添加To列表表项
 * @param ToInfo
 * @param lastMessage
 * @param unread
 */
function addToInList(ToInfo,lastMessage,unread) {
    let time = "";let content = "";
    if (lastMessage!=null){
        if (lastMessage.length!==0){
            time = timeDealShow(lastMessage.time);
            content = lastMessage.content;
        }
    }
    //时间 未读数
    let ToListHtml = '<a onclick="clickTo(this)" id="'+ emailFilter(ToInfo.email) +'" class="media">\n' +
        '         <div class="media-img-wrap">\n' +
        '           <div class="avatar avatar-away">\n' +
        '             <img src="'+ ToInfo.image_url +'" alt="User Image" class="avatar-img rounded-circle">\n' +
        '           </div>\n' +
        '         </div>\n' +
        '         <div class="media-body">\n' +
        '           <div>\n' +
        '             <div class="user-name">'+ ToInfo.username +'</div>\n' +
        '             <div class="user-last-chat" id="lastMsg'+emailFilter(ToInfo.email)+'">'+ content +'</div>\n' +
        '           </div>\n' +
        '           <div>\n' +
        '             <div class="last-chat-time block">'+time+'</div>\n' +
        '             <div class="badge badge-success badge-pill" id="unread'+ emailFilter(ToInfo.email) +'">'+unread+'</div>\n' +
        '           </div>\n' +
        '         </div>\n' +
        '       </a>';
    $("#chatUserList").append(ToListHtml);
}

/**
 * 添加新的div 聊天界面
 * @param email
 * @param isShow 是否显示body
 */
function addChatBody(email,isShow=false) {
    let chatBodyHtml =
        '        <div class="chat-body" id="chatBody'+ email +'">\n' +
        '          <div class="chat-scroll" id="scroll'+ email +'">\n' +
        '              <ul class="list-unstyled" id="messageBody'+ email +'">\n' +
        '              </ul>\n' +
        '          </div>\n' +
        '        </div>\n' +
        '      </div>'
    $("#divMessage").append(chatBodyHtml);
    //在添加新聊天时，直接显示
    if (!isShow){
        let chatBody ="#chatBody" + email;
        $(chatBody).hide();
    }
}

/**
 * 过滤特殊字符
 * @param email
 * @returns {*}
 */
function emailFilter(email) {
    const pattern = /[`~!@#$%^&*()+=|{}':;',\[\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]/g;
    return email.replace(pattern,"");
}

/**
 * 显示时间处理
 * @param time
 */
function timeDealShow(time) {
    let nowTime = new Date();
    let nowTimeString = nowTime.format();
    time = timeDeal(time);
    if (nowTimeString.split(" ")[0]>time.split(" ")[0]){
        return time;
    }
    else {
        return time.split(" ")[1];
    }
}
function timeDeal(time,last=8,T=false) {
    let exam_time;let result;
    if(time.indexOf("T")!==-1){
        exam_time = time.split("T");
        result = exam_time[0] + " " + exam_time[1].substr(0,last);
        if (T){
            //含有T
            return exam_time[0] + "T" + exam_time[1].substr(0,last);
        }
    }
    else{
        exam_time = time.split(" ");
        result = exam_time[0] + " " + exam_time[1].substr(0,last);
        if (T){
            //含有T
            return exam_time[0] + "T" + exam_time[1].substr(0,last);
        }
    }
    return result;
}


//数据请求

/**
 * 第一次点击聊天,请求数据形式一样
 */
function firstClickChat(to_email) {
    //第一次点击请求数据
    $.ajax({
        url:"/chat/getMessagesFirst",
        type:"post",
        dataType:"json",
        data:{"to_email":emailToMap[to_email].email},
        success:function (data) {
            let messageEarly = "";
            let new_html = "";let i = 0 ;
            //时间从大到小
            for (let time in data){
                if (i===0){messageEarly = time;i++};
                if (data[time].from_email===mySelfInfo.email){
                    new_html += addMessage(data[time].content,true,timeDealShow(time));
                }else {
                    new_html += addMessage(data[time].content,false,timeDealShow(time));
                }
            }
            //email ->Map(time,message)
            emailMessageEarly.set(emailToMap[to_email].email,messageEarly);
            console.log(emailMessageEarly);
            $(nowMessageBody).prepend(new_html);
        }
    });
}

//获取数据
function addScrollListener(to_email) {
   nowScroll =  document.getElementById("scroll"+to_email);
   nowScroll.addEventListener('scroll', function() {
       let scrollTop = nowScroll.scrollTop;
       let wholeHeight = nowScroll.scrollHeight;
       let divHeight = nowScroll.clientHeight;
       let nowEarlyTime;
       //map是从大到小遍历的
       nowEarlyTime = emailMessageEarly.get(emailToMap[to_email].email);
       if ((scrollTop+divHeight)>=wholeHeight){
       }
       if (scrollTop===0){
           $.ajax({
               url: "/chat/getMessagesMore",
               type: "post",
               dataType: "json",
               data: {"to_email":emailToMap[to_email].email,"nowEarlyTime":timeDeal(nowEarlyTime),"days":days},
               success:function (data) {
                   let isZero = true;
                   let new_html = "";let i = 0;
                   let messageEarly = "";
                   for (let time in data){
                       isZero = false;if (i===0){messageEarly=data[time].time;i++}
                       if (data[time].from_email===mySelfInfo.email){
                           new_html += addMessage(data[time].content,true,timeDealShow(time));
                       }else {
                           new_html += addMessage(data[time].content,false,timeDealShow(time));
                       }
                   }
                   if (isZero){days+=-60;}
                   else {
                       days=0;
                       emailMessageEarly.set(emailToMap[to_email].email,messageEarly);
                       console.log(emailMessageEarly);
                       $(nowMessageBody).prepend(new_html);
                   }
               }
           });
       }
   });
}



//send
$(sendMessage).click(function (){
    let to_email = emailToMap[$(sendMessage).attr("value")].email;
    let content = editorChat.txt.html();
    let nowTime = new Date();
    $(nowMessageBody).append(addMessage(content,true,timeDealShow(nowTime.format())));
    editorChat.txt.clear();

    let message = {"from_email":mySelfInfo.email,"to_email":to_email,"content":content,
        "time":nowTime.format()};
    console.log(websocket);
    websocket.send(JSON.stringify({"type":"chat","message":JSON.stringify(message)}));
    //设置第一条消息
    let lastMsg = "#lastMsg" + emailFilter(to_email);
    console.log(lastMsg);
    $(lastMsg).html("").html(content);
});
//receive
function insertMessage(receive_message) {
    let message = JSON.parse(receive_message);
    console.log(message);
    //如果对方是新聊天，加入新的email->Message
    let isNew = false;let from_email = "";
    console.log(emailMessageEarly.has(message.from_email));
    if (!emailMessageEarly.has(message.from_email)){//是新聊天
        unReadEmailNum.set(message.from_email,1);
        from_email = message.from_email;
        isNew = true;
        console.log(isNew,from_email);
    }
    else {
        //否则判断对方是否是自己正在聊天的人
        let to_email = ""; //自己的聊天->对方
        if (nowTo.length!==0){to_email = nowTo.split("#")[1];}

        if (emailFilter(message.from_email)===to_email){
            //是则直接添加消息
            $(nowMessageBody).append(addMessage(message.content,false,timeDealShow(message.time)));
            toZero(emailToMap[to_email].email);//自己的未读消息数更新为0
            unReadEmailNum.set(to_email,0);
        }
        else {//否则
            // 增加未读数
            to_email = emailFilter(message.from_email);
            let unread_num = unReadEmailNum.get(to_email);
            console.log(unread_num);
            unReadEmailNum.set(to_email,unread_num+1);
            let unread = "#unread" + to_email;
            $(unread).text(unread_num+1);
            //body添加
            let toMessageBody = "#messageBody" + to_email;
            console.log(toMessageBody);
            if (isFirstClick.get(to_email)===1){//如果已经点击过，加入body，否则不用
                $(toMessageBody).append(addMessage(message.content,false,timeDealShow(message.time)))
            }
        }
        //设置第一条消息
        let lastMsg = "#lastMsg" + to_email;
        console.log(lastMsg);
        $(lastMsg).html("").html(message.content);
    }
    if (isNew){//如果是新聊天,得到基本信息
        let urlInfo = "";
        if (isDoctor){
            urlInfo = "/chat/getPatientInfo";
        }else {
            urlInfo = "/chat/getDoctorInfo";
        }
        $.ajax({
            url:urlInfo,
            type:"post",
            dataType:"json",
            data:{"email":from_email},
            success:function (data){
                let obj = {};obj[emailFilter(from_email)] = data;
                Object.assign(emailToMap,obj);
                //所有的div初始化 userList messageBody message
                addToInList(emailToMap[emailFilter(from_email)],message,1);
                addChatBody(emailFilter(from_email));
                let messageBody = "#messageBody" + emailFilter(from_email);
                $(messageBody).append(addMessage(message.content,false,timeDealShow(message.time)));
            }
        });
    }
}

//实时消息显示
function addMessage(content,isSend,time) {
    let newMessageHtml;
    let timeHtml = '<ul class="chat-msg-info">\n' +
        '        <li>\n' +
        '            <div class="chat-time">\n' +
        '                <span>'+  time +'</span>\n' +
        '            </div>\n' +
        '        </li>\n' +
        '    </ul>';
    if (isSend){
        newMessageHtml = '<li class="media sent">\n' +
            '    <div class="media-body">\n' +
            '        <div class="msg-box">\n' +
            '            <div>\n' +
            content + timeHtml +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>\n' +
            '</li>';
    }else{
        newMessageHtml = '<li class="media received">\n' +
            '    <div class="media-body">\n' +
            '        <div class="msg-box">\n' +
            '            <div>\n' +
            content + timeHtml +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>\n' +
            '</li>';
    }
    return newMessageHtml;
}

// 入参 fmt-格式
Date.prototype.format = function(fmt = "yyyy-MM-dd HH:mm:ss"){
    let o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "H+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };

    if(/(y+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }

    for(let k in o){
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(
                RegExp.$1, (RegExp.$1.length===1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }

    return fmt;
}

function call(){
    let href = "";
    if (isDoctor){
       href  = "/video-call-doctor?patient_email=" + emailToMap[$(sendMessage).attr("value")].email;
    }
    else {
        href = "/video-call-patient?doctor_email=" + emailToMap[$(sendMessage).attr("value")].email;
    }
    return href;
}