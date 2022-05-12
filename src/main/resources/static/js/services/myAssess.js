
//病人的评估信息
//第一次显示
function showPatientAssess(assessIdMap,doctorInfos) {
    let i = 0;
    for (let [id,value] of assessIdMap){
        doctorInfoIdMap.set(id,doctorInfos[i]);
        $("#myAssessBody").append(addAssessPatient(assessIdMap.get(id),doctorInfos[i],assessIdMap.get(id).is_read));
        i++;
    }
}

//添加HTML
function addAssessPatient(assess,doctorInfo,is_read) {
    let time = timeDeal(assess.time,8);

    let view;let color;
    if (is_read){
        color = '<td><span id="isRead'+ assess.id +'">' + is_ReadStr(assess.is_read) + '</span></td>';
    }
    else {
        color = '<td><span class="d-block text-info" id="isRead'+ assess.id +'">' + is_ReadStr(assess.is_read) + '</span></td>'
    }
    return '<tr id="myAssess'+ assess.id+'">\n' +
        '         <td>\n' +
        '           <h2 class="table-avatar">\n' +
        '             <a href="/doctor-profile?email=' + doctorInfo.email + '" class="avatar avatar-sm mr-2">\n' +
        '               <img class="avatar-img rounded-circle" src="' + doctorInfo.image_url + '" alt="User Image">\n' +
        '             </a>\n' +
        '             <a href="/doctor-profile?email=' + doctorInfo.email + '">' + doctorInfo.username + '<span>' + doctorInfo.speciality + '</span></a>\n' +
        '           </h2>\n' +
        '         </td>\n' +
        '         <td><span class="d-block text-info" id="time'+assess.id+'">' + time + '</span></td>\n' +
        color +
        '         <td class="text-right">\n' +
        '           <div class="table-action">\n' +
        '<a id="'+ assess.id +'" onclick="showDetailsAssessPatient(this)" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addAssessDiv">' +
        '               <i class="far fa-eye"></i> View\n' +
        '             </a>\n' +
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

//点击事件，更新是否已读

function showDetailsAssessPatient(obj) {
    let id = parseInt(obj.id);
    content.txt.clear();
    $("#description").val(assessIdMap.get(id).description).prop("disabled",true);
    content.txt.html(assessIdMap.get(id).content);
    content.disable();

    let assess = assessIdMap.get(id);
    if (assess.is_read===0){
        let isRead = "#isRead" + id;
        $(isRead).attr("class","").text("已读");
        assess.time = timeDeal(assess.time);
        updateIs_Read(assess);
        assessIdMap.get(id).is_read = 1;
    }
}

//后端更新
function updateIs_Read(assess) {
    $.ajax({
        url:"/patient/readAssess",
        type:"post",
        dataType:"json",
        data:{"assess":JSON.stringify(assess)},
        success:function (data){

        }
    });
}
function is_ReadStr(is_read) {
    if (is_read){
        return "已读";
    }
    else {
        return "未读";
    }
}


function updateAssess(id,assess) {
    let time = "#time" + id;
    $(time).text(assess.time);
}

function receiveAssessMsg(message) {
    Notiflix.Notify.Info("有评估信息哦!");
    message = JSON.parse(message);
    let id = message.id;
    if (message.code===-1){//删除
        let myAssess = "#myAssess" + id;
        $(myAssess).remove();
    }
    else if (message.code===1){//添加
        $("#myAssessBody").prepend(addAssessPatient(message.assess,message.doctorInfo,false));
        if (!assessIdMap.has(id)){
            assessIdMap.set(id,message.assess);
        }
    }
    else if (message.code===0){//更新
        $.ajax({
            url: "/patient/getAssess",
            type: "post",
            dataType: "json",
            data: {"id":id},
            success:function (data){
                if (data.code===1){
                    assessIdMap.set(id,JSON.parse(data.message));
                    console.log(assessIdMap);
                    let isRead="#isRead" + id;
                    $(isRead).attr("class","d-block text-info" ).text("未读");
                    updateAssess(id,assessIdMap.get(id));
                }
                else {
                    Notiflix.Notify.Failure(data.message);
                }
            }
        });
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