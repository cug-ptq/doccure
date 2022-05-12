//医生浏览、添加病人家访信息
function addMyVisit(visit,patientInfo) {
    let time = timeDeal(visit.time,8);
    return '<tr id="myVisit'+ visit.id+'">\n' +
        '         <td>\n' +
        '           <h2 class="table-avatar">\n' +
        '             <a href="/patient-profile?email=' + patientInfo.email + '" class="avatar avatar-sm mr-2">\n' +
        '               <img class="avatar-img rounded-circle" src="' + patientInfo.image_url + '" alt="User Image">\n' +
        '             </a>\n' +
        '             <a href="/patient-profile?email=' + patientInfo.email + '">' + patientInfo.username + '</a>\n' +
        '           </h2>\n' +
        '         </td>\n' +
        '         <td><span class="d-block text-info" id="time'+visit.id+'">' + time + '</span></td>\n' +
        '         <td class="text-right">\n' +
        '           <div class="table-action">\n' +
        '             <a id="'+ visit.id +'" onclick="showDetailsVisit(this)" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addVisitDiv">\n' +
        '               <i class="far fa-eye"></i> View\n' +
        '             </a>\n' +
        '             <a onclick="editVisit(this)" id="edit'+ visit.id +'" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addVisitDiv">\n' +
        '               <i class="fas fa-edit"></i> Edit\n' +
        '             </a>\n' +
        '             <a onclick="deleteVisit(this)" id="delete'+ visit.id+'" class="btn btn-sm bg-danger-light">\n' +
        '               <i class="far fa-trash-alt"></i> Delete\n' +
        '             </a>'+
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

/**
 * 本人的visit
 * @param visitDoctorIdMap
 * @param patientInfo
 */
function showVisitMyself(visitDoctorIdMap,patientInfo) {
    for (let [id,value] of visitDoctorIdMap){
        $("#myVisitBody").append(addMyVisit(visitDoctorIdMap.get(id),patientInfo));
        initMyView_Edit_Delete(id); //对edit view delete value赋值-id
    }
}


function changePatientVisit(patient_email) {
    $.ajax({
        url:"doctor/changePatientVisit",
        type:"post",
        dataType:"json",
        data:{"patient_email":patient_email},
        success:function (data) {
            if (data.code===-1){
                Notiflix.Notify.Failure("请求失败");
            }
            else {
                let visits = JSON.parse(data.message);
                let temp_VisitDoctorIdMap = new Map();
                Object.keys(visits).forEach((key) => temp_VisitDoctorIdMap.set(parseInt(key), visits[key]))
                visitDoctorIdMap = temp_VisitDoctorIdMap;
                $("#visitBody").html("");
                $.ajax({
                    url:"/chat/getPatientInfo",
                    type:"post",
                    dataType:"json",
                    data:{"email":patient_email},
                    success:function (data){
                        patientInfo = data;
                        showVisitMyself(visitDoctorIdMap,data);
                    }
                });
            }
        }
    });
}


/**
 * 显示visit
 * @param obj 获得value属性值id
 */
function showDetailsVisit(obj) {
    // 禁用编辑功能
    editor.disable();
    $("#confirmAddVisit").hide();
    $("#submitVisit").hide();
    $("#cancelVisit").hide();
    $("#time").val(timeDeal(visitDoctorIdMap.get(parseInt(obj.id)).time,5,true));
    editor.txt.html(visitDoctorIdMap.get(parseInt(obj.id)).visit_result);
    // $("#description").val(visitDoctorIdMap.get(parseInt(obj.id)).description);
    // $('#description').prop("disabled",true);
}

/**
 * 编辑visit，隐藏confirm
 * @param obj 获得属性value的值-id
 */
function editVisit(obj) {
    $("#confirmAddVisit").hide();
    $("#submitVisit").show();
    $("#cancelVisit").show();
    // 开启编辑功能
    editor.enable();
    // $('#description').prop("disabled",false);
    let id = parseInt(obj.getAttribute("value"));
    //渲染内容 考虑description...
    $("#time").val(timeDeal(visitDoctorIdMap.get(id).time,5,true))
    editor.txt.html(visitDoctorIdMap.get(id).visit_result);
    // $("#description").val(visitDoctorIdMap.get(id).description);
    $('#submitVisit').attr("value",id);
}

/**
 * 删除visit
 * @param obj alt对象-obj，获得value属性的值-id
 */
function deleteVisit(obj) {
    //删除时
    let id = parseInt(obj.getAttribute("value"));
    $.ajax({
        url: "/doctor/deleteVisit",
        type: "post",
        dataType: "json",
        data: {"id":id},
        success:function (data) {
            if (data.code === -1){
                Notiflix.Notify.Failure(data.message);
            }
            else {
                Notiflix.Notify.Success(data.message);
                let myVisit = "#myVisit" + id;
                $(myVisit).remove();
                sendVisitMSg(id,-1);
            }
        }
    });
}

/**
 * 添加评估，显示confirm，隐藏submit，清空内容，启用编辑
 */
$("#addVisit").click(function (){
    editor.txt.clear();
    editor.enable();
    $("#time").val("");
    $("#submitVisit").hide();
    $("#confirmAddVisit").show();
    $("#cancelVisit").show();
});

/**
 * Ajax异步提交数据
 */
$('#confirmAddVisit').click(function (){
    let content = editor.txt.html();
    patient_email = $("#patient_email").val();
    let time = $("#time").val();
    if (time.length!==0){
        let time_val = timeDeal(time + ":00",8);
        $.ajax({
            url:"/doctor/addVisit",
            type:"post",
            dataType:"json",
            data:{"visitInfo":JSON.stringify({"time":time_val,"patient_email":patient_email,"visit_result":content})},
            success:function (data) {
                Notiflix.Notify.Success("添加成功");
                let visit = JSON.parse(data.message);
                $("#myVisitBody").append(addMyVisit(visit,patientInfo));
                //全局变量添加该visit
                visitDoctorIdMap.set(parseInt(visit.id),visit);
                initMyView_Edit_Delete(parseInt(visit.id));
                sendVisitMSg(visit.id,1,visit);
            }
        });
    }
    else {
        Notiflix.Notify.Failure("时间未填写");
    }
});

/**
 * 修改visit，后端更新
 */
function submitVisit(obj){
    // $("#confirmAddVisit").hide();
    let id = parseInt(obj.getAttribute("value"));
    let content = editor.txt.html();

    let time = $("#time").val();
    if (time.length!==0) {
        let time_val = timeDeal(time + ":00", 8);
        $.ajax({
            url: "/doctor/updateVisit",
            type: "post",
            dataType: "json",
            data: {"visitInfo": JSON.stringify({"id": id, "time": time_val, "visit_result": content})},
            success: function (data) {
                if (data.code === 1) {
                    Notiflix.Notify.Success("修改成功");
                    let visit = JSON.parse(data.message);
                    visitDoctorIdMap.set(id, visit);
                    updateVisitTime(id, visit);
                    sendVisitMSg(id,0,visit);
                }
            },
            error: function () {
                Notiflix.Notify.Failure("操作失败");
            }
        });
    }
    else {
        Notiflix.Notify.Failure("时间未填写");
    }
}

/**
 * add时 初始化相关value
 * @param id visit的id
 */
function initMyView_Edit_Delete(id) {
    let view = "#" + visitDoctorIdMap.get(id).id;
    $(view).attr("value",0);
    //编辑时，显示原来文本
    let edit = "#edit" + id;
    $(edit).attr("value",id);

    //删除时
    let deleteVisit = "#delete" + id;
    $(deleteVisit).attr("value",id);
}

function updateVisitTime(id,visit) {
    let time = "#time" + id;
    $(time).text(visit.time);
}

function timeDeal(time,last=8,T=false) {
    let visit_time;let result;
    if(time.indexOf("T")!==-1){
        visit_time = time.split("T");
        result = visit_time[0] + " " + visit_time[1].substr(0,last);
        if (T){
            return visit_time[0] + "T" + visit_time[1].substr(0,last);
        }
    }
    else{
        result = time;
        visit_time = time.split(" ");
        if (T){
            return visit_time[0] + "T" + visit_time[1].substr(0,last);
        }
    }
    return result;
}

//主动推送信息
function sendVisitMSg(id,code,visit="") {
    patient_email = $("#patient_email").find("option:selected").val();
    websocket.send(JSON.stringify({"type":"visit","id":id,
        "code":code,"doctorInfo":doctorInfo,"to_email":patient_email,"visit":visit}));
}