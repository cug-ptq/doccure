
$("#receive").click(function (){
    is_video = true;
    dealCall();
});
$("#refuse").click(function (){
    is_video = false;
    dealCall();
});

function dealCall() {
    let msg = 0;
    if (is_video === true) { //如果同意对方电话
        WebRTCInit();
        msg = 1;
        // is_video = false;
    }

    let to_msg = JSON.stringify({
        type: "call_back",
        toUser: toUserEmail,
        fromUser: UserInfo.email,
        msg: msg
    });

    websocket.send(JSON.stringify({"type":"video","message":to_msg}));
}

ButtonFunInit();

async function videoMsgDeal(message) {
    message = JSON.parse(message);
    if (message.type === 'call_start') {
        is_video = false;
        let call_receive_div = $("#call_receive_div");
        $(call_receive_div).modal("show");
        setTimeout(() => {
            $(call_receive_div).modal("hide");
            if (!is_video){
                dealCall();
            }
        }, 10000);
    }

    if (message.type === 'call_back') {
        if (parseInt(message.msg) === 1) {
            //创建本地视频并发送offer
            $("#video_start").modal("hide");
            let stream = await navigator.mediaDevices.getUserMedia({video: true, audio: true})
            localVideo.srcObject = stream;
            stream.getTracks().forEach(track => {
                peer.addTrack(track, stream);
            });

            let offer = await peer.createOffer();
            await peer.setLocalDescription(offer);

            let newOffer = offer.toJSON();
            newOffer["fromUser"] = UserInfo.email;
            newOffer["toUser"] = toUserEmail;
            let to_msg = JSON.stringify(newOffer);
            websocket.send(JSON.stringify({"type":"video","message":to_msg}));
        } else if (parseInt(message.msg) === 0) {
            Notiflix.Notify.Info(toUserInfo.username + "拒绝视频通话");
            document.getElementById('call_end').click();
        } else {
            Notiflix.Notify.Info(message.msg);
            document.getElementById('call_end').click();
        }
        $("#call_start_div").modal("hide");
        return;
    }

    if (message.type === 'offer') {
        let stream = await navigator.mediaDevices.getUserMedia({video: true, audio: true});
        localVideo.srcObject = stream;
        stream.getTracks().forEach(track => {
            peer.addTrack(track, stream);
        });


        let sdp = message.sdp.replace(/\r/g,"\\r");
        let type = message.type;
        await peer.setRemoteDescription(new RTCSessionDescription({type, sdp}));
        let answer = await peer.createAnswer();
        let newAnswer = answer.toJSON();

        newAnswer["fromUser"] = UserInfo.email;
        newAnswer["toUser"] = toUserEmail;

        let to_msg = JSON.stringify(newAnswer);
        console.log(newAnswer);
        websocket.send(JSON.stringify({"type":"video","message":to_msg}));
        await peer.setLocalDescription(answer);
        return;
    }

    if (message.type === 'answer') {
        let sdp = message.sdp;
    // .replace(/\r/g,"\\r");
        let type = message.type;
        await peer.setRemoteDescription(new RTCSessionDescription({type, sdp}));
        return;
    }

    if (message.type === '_ice') {
        await peer.addIceCandidate(JSON.parse(message.iceCandidate));
    }
}

/* WebRTC */
function WebRTCInit(){
    let pc_config = {
        "iceServers": [{"urls": ['stun:stun.xten.com:3478']}]
    };
    peer = new RTCPeerConnection(pc_config);

    //ice
    peer.onicecandidate = function (e) {
        if (e.candidate) {
            let to_msg = JSON.stringify({
                type: '_ice',
                toUser:toUserEmail,
                fromUser:UserInfo.email,
                iceCandidate: JSON.stringify(e.candidate)
            });
            websocket.send(JSON.stringify({"type":"video","message":to_msg}));
        }
    };

    //track
    peer.ontrack = function (e) {
        if (e && e.streams) {
            remoteVideo.srcObject = e.streams[0];
        }
    };
}

/* 按钮事件 */
function ButtonFunInit(){
    //视频通话
    document.getElementById('video_start').onclick = function (e){

        let toUser = toUserEmail;
        if(!toUser){
            Notiflix.Notify.Info("请先指定好友账号，再发起视频通话！");
            return;
        }

        if(peer == null){
            WebRTCInit();
        }
        let to_msg = JSON.stringify({
            type:"call_start",
            fromUser:UserInfo.email,
            toUser:toUser,
        });
        websocket.send(JSON.stringify({"type":"video","message":to_msg}));
    }

    //挂断
    document.getElementById('call_end').onclick = function (e){
        if(localVideo.srcObject){
            const videoTracks = localVideo.srcObject.getVideoTracks();
            videoTracks.forEach(videoTrack => {
                videoTrack.stop();
                localVideo.srcObject.removeTrack(videoTrack);
            });
        }

        if(remoteVideo.srcObject){
            const videoTracks = remoteVideo.srcObject.getVideoTracks();
            videoTracks.forEach(videoTrack => {
                videoTrack.stop();
                remoteVideo.srcObject.removeTrack(videoTrack);
            });

            //挂断同时，通知对方
            let to_msg = JSON.stringify({
                type:"call_end",
                fromUser:UserInfo.email,
                toUser:toUserEmail,
            })

            websocket.send(JSON.stringify({"type":"video","message":to_msg}));
        }

        if(peer){
            peer.ontrack = null;
            peer.onremovetrack = null;
            peer.onremovestream = null;
            peer.onicecandidate = null;
            peer.oniceconnectionstatechange = null;
            peer.onsignalingstatechange = null;
            peer.onicegatheringstatechange = null;
            peer.onnegotiationneeded = null;

            peer.close();
            peer = null;
        }

        localVideo.srcObject = null;
        remoteVideo.srcObject = null;
    }
}