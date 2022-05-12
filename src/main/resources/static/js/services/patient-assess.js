//医生浏览、添加病人评估信息

function addAssessOthers(assess,doctorInfo) {
    let time = timeDeal(assess.time,8);
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
        '         <td class="text-right">\n' +
        '           <div class="table-action">\n' +
        '             <a id="'+ assess.id +'" onclick="showDetailsAssess(this)" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addAssessDiv">\n' +
        '               <i class="far fa-eye"></i> View\n' +
        '             </a>\n' +
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

function addMyAssess(assess,doctorInfo) {
    let time = timeDeal(assess.time,8);
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
        '         <td class="text-right">\n' +
        '           <div class="table-action">\n' +
        '             <a id="'+ assess.id +'" onclick="showDetailsAssess(this)" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addAssessDiv">\n' +
        '               <i class="far fa-eye"></i> View\n' +
        '             </a>\n' +
        '             <a onclick="editAssess(this)" id="edit'+ assess.id +'" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addAssessDiv">\n' +
        '               <i class="fas fa-edit"></i> Edit\n' +
        '             </a>\n' +
        '             <a onclick="deleteAssess(this)" id="delete'+ assess.id+'" class="btn btn-sm bg-danger-light">\n' +
        '               <i class="far fa-trash-alt"></i> Delete\n' +
        '             </a>'+
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

/**
 * 显示其他医生的assess
 * @param assessesOthersIdMap
 * @param doctorInfos
 */
function showAssessOthers(assessesOthersIdMap,doctorInfos) {
    let i = 0;
    for (let [id,value] of assessesOthersIdMap){
        $("#otherAssessBody").append(addAssessOthers(assessesOthersIdMap.get(id),doctorInfos[i]));
        // 显示时 判断时本人的评论还是其他人的
        let view = "#" + assessesOthersIdMap.get(id).id;
        $(view).attr("value",1);
        i++;
    }
}

/**
 * 本人的assess
 * @param assessesDoctorIdMap
 * @param doctorInfo
 */
function showAssessMyself(assessesDoctorIdMap,doctorInfo) {
    for (let [id,value] of assessesDoctorIdMap){
        $("#myAssessBody").append(addMyAssess(assessesDoctorIdMap.get(id),doctorInfo));
        initMyView_Edit_Delete(id); //对edit view delete value赋值-id
    }
}

/**
 * 显示assess
 * @param obj 获得value属性值id
 */
function showDetailsAssess(obj) {
    // 禁用编辑功能
    editor.disable();
    $("#confirmAddAssess").hide();
    $("#submitAssess").hide();
    $("#cancelAssess").hide();
    let type = obj.getAttribute("value");
    console.log(type);
    if (parseInt(type)===1){
        editor.txt.html(assessesOthersIdMap.get(parseInt(obj.id)).content);
        $("#description").val(assessesOthersIdMap.get(parseInt(obj.id)).description);
    }
    else {
        editor.txt.html(assessesDoctorIdMap.get(parseInt(obj.id)).content);
        $("#description").val(assessesDoctorIdMap.get(parseInt(obj.id)).description);
    }
    $('#description').prop("disabled",true);
}

/**
 * 编辑assess，隐藏confirm
 * @param obj 获得属性value的值-id
 */
function editAssess(obj) {
    $("#confirmAddAssess").hide();
    $("#submitAssess").show();
    $("#cancelAssess").show();
    // 开启编辑功能
    editor.enable();
    $('#description').prop("disabled",false);
    let id = parseInt(obj.getAttribute("value"));
    //渲染内容 考虑description...
    editor.txt.html(assessesDoctorIdMap.get(id).content);
    $("#description").val(assessesDoctorIdMap.get(id).description);
    $('#submitAssess').attr("value",id);
}

/**
 * 删除assess
 * @param obj alt对象-obj，获得value属性的值-id
 */
function deleteAssess(obj) {
    //删除时
    let id = parseInt(obj.getAttribute("value"));
    $.ajax({
        url: "/doctor/deleteAssess",
        type: "post",
        dataType: "json",
        data: {"id":id},
        success:function (data) {
            if (data.code === -1){
                Notiflix.Notify.Failure(data.message);
            }
            else {
                Notiflix.Notify.Success(data.message);
                let myAssess = "#myAssess" + id;
                $(myAssess).remove();
                sendAssessMSg(id,-1);
            }
        }
    });
}

/**
 * 添加评估，显示confirm，隐藏submit，清空内容，启用编辑
 */
$("#addAssess").click(function (){
    editor.txt.clear();
    editor.enable();
    $("#description").val("");
    $('#description').prop("disabled",false);
    $("#submitAssess").hide();
    $("#confirmAddAssess").show();
    $("#cancelAssess").show();
});

/**
 * Ajax异步提交数据
 */
$('#confirmAddAssess').click(function (){
    let content = editor.txt.html();
    let description = $("#description").val();
    $.ajax({
        url:"/doctor/addAssess",
        type:"post",
        dataType:"json",
        data:{"assessInfo":JSON.stringify({"patient_email":patient_email,"description":description,"content":content})},
        success:function (data) {
            Notiflix.Notify.Success(data.message);
            let assess = JSON.parse(data.assess);
            let doctorInfo = JSON.parse(data.doctorInfo);
            $("#myAssessBody").append(addMyAssess(assess,doctorInfo));
            //全局变量添加该assess
            assessesDoctorIdMap.set(parseInt(assess.id),assess);
            initMyView_Edit_Delete(parseInt(assess.id));
            sendAssessMSg(assess.id,1,assess);
        }
    });
});

/**
 * 修改assess，后端更新
 */
function submitAssess(obj){
    // $("#confirmAddAssess").hide();
    let id = parseInt(obj.getAttribute("value"));
    let content = editor.txt.html();
    let description = $("#description").val();
    $.ajax({
        url:"/doctor/updateAssess",
        type:"post",
        dataType:"json",
        data:{"assessInfo":JSON.stringify({"id":id,"content":content,"description":description})},
        success:function (data) {
            if (data.code===1){
                Notiflix.Notify.Success("修改成功");
                let assess = JSON.parse(data.message);
                assessesDoctorIdMap.set(id,assess);
                updateAssessTime(id,assess);
                sendAssessMSg(id,0);
            }
        },
        error: function () {
            Notiflix.Notify.Success("操作失败");
        }
    });
}

/**
 * add时 初始化相关value
 * @param id assess的id
 */
function initMyView_Edit_Delete(id) {
    let view = "#" + assessesDoctorIdMap.get(id).id;
    $(view).attr("value",0);
    //编辑时，显示原来文本
    let edit = "#edit" + id;
    $(edit).attr("value",id);

    //删除时
    let deleteAssess = "#delete" + id;
    $(deleteAssess).attr("value",id);
}

function updateAssessTime(id,assess) {
    let time = "#time" + id;
    $(time).text(assess.time);
}

function timeDeal(time,last=8,T=false) {
    let assess_time;let result;
    if(time.indexOf("T")!==-1){
        assess_time = time.split("T");
        result = assess_time[0] + " " + assess_time[1].substr(0,last);
        if (T){
            return assess_time[0] + "T" + assess_time[1].substr(0,last);
        }
    }
    else{
        result = time;
    }
    return result;
}

//主动推送信息
function sendAssessMSg(id,code,assess="") {
    websocket.send(JSON.stringify({"type":"assess","id":id,
        "code":code,"doctorInfo":doctorInfo,"to_email":patient_email,"assess":assess}));
}