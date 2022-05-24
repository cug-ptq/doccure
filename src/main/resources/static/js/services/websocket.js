/**
 * websocket连接
 */
function connectWebSocket(websocket) {
    //这里需要的路径需要配置相对应的路径
    // const target = "wss://localhost:8888/chatWebSocket";
    // const target = "ws://110.40.154.137:8080/chatWebSocket";
    const target = "wss://www.doccure.cn:8080/chatWebSocket";
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        if (websocket!=null){

        }
        else {
            websocket = new WebSocket(target);
        }
        console.log(websocket);
    } else {
        Notiflix.Notify.Failure("不支持WebSocket");
    }
    //连接发生错误的回调方法
    websocket.onerror = function () {
        Notiflix.Notify.Failure("请求错误");
    };
    //连接成功建立的回调方法
    websocket.onopen = function (event) {
        console.log(event);
    }
    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        let obj = JSON.parse(event.data);
        let type = obj["type"];
        if (type === "chat"){
            if (typeof (insertMessage) === "function"){
                insertMessage(obj["message"]);
            }
            else {
                Notiflix.Notify.Info("有聊天信息!");
            }
        }
        else if (type === "assess"){
            if (typeof (receiveAssessMsg) === "function"){
                receiveAssessMsg(obj["message"]);
            }
            else {
                Notiflix.Notify.Info("有评估信息!");
            }
        }
        else if (type === "exam"){
            if (typeof (receiveExamMsg) === "function"){
                receiveExamMsg(obj["message"]);
            }
            else {
                Notiflix.Notify.Info("有体检信息!");
            }
        }
        else if (type === "resume"){
            if (typeof (receiveResume) === "function"){
                receiveResume(obj["message"]);
            }
            else {
                Notiflix.Notify.Info("有健康履历信息!");
            }
        }
        else if (type === "visit"){
            if (typeof (receiveVisit) === "function"){
                receiveVisit(obj["message"]);
            }
            else {
                Notiflix.Notify.Info("有家访信息!");
            }
        }
        else if (type === "data"){
            if (typeof (monitorCharts) === "function"){
                monitorCharts(obj["message"]);
            }
        }
        else if (type === "other"){
            if (typeof (setOtherCharts) === "function"){
                setOtherCharts(obj["message"]);
            }
        }
        else{
            if (typeof (videoMsgDeal) === "function"){
                videoMsgDeal(obj["message"]).then(r => console.log());
            }
        }
    }
    //连接关闭的回调方法
    websocket.onclose = function (e) {
        // connectWebSocket();
        console.log('websocket 断开: ' + e.code + ' ' + e.reason + ' ' + e.wasClean)
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        websocket.close();
        websocket = null;
        console.log("关闭");
    }
    return websocket;
}