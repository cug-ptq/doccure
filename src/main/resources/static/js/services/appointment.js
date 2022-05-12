function add(patientInfo,appointment) {
    let appoint_time;let date;let time;
    if (appointment.appoint_time.indexOf("T")!==-1){
        appoint_time = appointment.appoint_time.split("T");
        time = appoint_time[1].substr(0,8);
    }
    else {
        appoint_time = appointment.appoint_time.split(" ");
        time = appoint_time[1];
    }
    date = appoint_time[0];

    let appointmentHtml = '<div class="appointment-list" id="appoint'+ appointment.id +'">\n' +
        '                  <div class="profile-info-widget">\n' +
        '                    <a href="/patient-profile?email='+ patientInfo.email +'" class="booking-doc-img">\n' +
        '                      <img src="'+ patientInfo.image_url +'" alt="User Image">\n' +
        '                    </a>\n' +
        '                    <div class="profile-det-info">\n' +
        '                      <h3><a href="/patient-profile?email='+ patientInfo.email +'">'+ patientInfo.username +'</a></h3>\n' +
        '                      <div class="patient-details">\n' +
        '                        <h5><i class="far fa-clock"></i>'+ date +" " + time +'</h5>\n' +
        '                        <h5><i class="fas fa-map-marker-alt"></i> '+ patientInfo.address +'</h5>\n' +
        '                        <h5><i class="fas fa-envelope"></i> '+ patientInfo.email +'</h5>\n' +
        '                        <h5 class="mb-0"><i class="fas fa-phone"></i> '+ patientInfo.telephone +'</h5>\n' +
        '                      </div>\n' +
        '                    </div>\n' +
        '                  </div>\n' +
        '                  <div class="appointment-action">\n' +
        '                    <a onclick="showViewDetails_Apt(this)" id="'+ appointment.id +'" class="btn btn-sm bg-info-light" data-toggle="modal" data-target="#apt_details">\n' +
        '                      <i class="far fa-eye"></i> View\n' +
        '                    </a>\n' +
        '                    <a class="btn btn-sm bg-success-light" onclick="accept(this)" id="accept'+ appointment.id +'">\n' +
        '                      <i class="fas fa-check"></i> Accept\n' +
        '                    </a>\n' +
        '                    <a class="btn btn-sm bg-danger-light" onclick="cancel(this)" id="cancel'+ appointment.id +'">\n' +
        '                      <i class="fas fa-times"></i> Cancel\n' +
        '                    </a>\n' +
        '                  </div>\n' +
        '                </div>';
    $("#appointments").append(appointmentHtml);
}

/**
 * 动态增加appointment
 * @param idAppointmentMap
 * @param patientInfoList
 */
function showAppointments(idAppointmentMap,patientInfoList) {
    let i = 0;
    for (let [id,value] of idAppointmentMap){
        let appointment = idAppointmentMap.get(id);
        add(patientInfoList[i++],appointment);
        let accept_id = "#accept" + appointment.id;
        $(accept_id).attr("value",appointment.id);
        let cancel_id = "#cancel" + appointment.id;
        $(cancel_id).attr("value",appointment.id);
        isShowAcceptOrPatient(appointment);
    }
}

/**
 * 显示appointment信息
 * @param obj
 */
function showViewDetails_Apt(obj) {
    showDetails(idAppointmentMap,parseInt(obj.id));
}

function showDetails(obj,id) {
    let appointment = obj.get(id);
    let appoint_time = timeDeal(appointment.appoint_time).split(" ");
    let date = appoint_time[0];
    let time = appoint_time[1].substr(0,8);

    $("#APTTime").text(date+" "+time);
    $("#status").text(appointment.appoint_result);
    $("#aptType").text(appointment.appoint_type);
    $("#aptContent").text(appointment.appoint_content);
    $("#dealStatus").attr("value",appointment.id);
}
/**
 * 点击result_type 跟换appointment显示
 */

function changeAppointByResultType(resultType) {
    $.ajax({
        url:"/doctor/appointmentByAptResult",
        type:"post",
        dataType:"json",
        data:{"resultType":resultType},
        success:function (data){
            let temp_idAppointmentMap = JSON.parse(data.idAppointmentMap);
            patientInfoList = JSON.parse(data.patientInfoList);
            let new_idAppointmentMap = new Map();
            for (let i = 0;i < temp_idAppointmentMap.length;i++){
                new_idAppointmentMap.set(temp_idAppointmentMap[i].id,temp_idAppointmentMap[i]);
            }
            idAppointmentMap = new_idAppointmentMap;
            $("#appointments").empty();
            showAppointments(idAppointmentMap,patientInfoList);
        },
        error:function (data){
            console.log(data);
        }
    });
}


/**
 * confirm点击，修改appointment状态
 */
$('#dealStatus').click(function (){
    $.ajax({
        url:"/doctor/changeAppointStatus",
        type:"post",
        dataType:"json",
        data:{"appointResult":JSON.stringify({"appoint_result":"已完成","id":this.value,
                "appoint_type":idAppointmentMap.get(parseInt(this.value)).appoint_type})},
        success:function (data){
            Notiflix.Notify.Success(data.message);
        },
        error:function (data) {
            Notiflix.Notify.Failure("请求失败");
        }
    });
    $("#status").text("已完成");
    let appoint_id = "#appoint" + this.value;
    $(appoint_id).remove();
});

function accept(obj) {
    let id = parseInt(obj.getAttribute("value"));
    $.ajax({
        url:"/doctor/changeAppointStatus",
        type:"post",
        dataType:"json",
        data:{"appointResult":JSON.stringify({"appoint_result":"已接收","id":id,
                "appoint_type":idAppointmentMap.get(id).appoint_type})},
        success:function (data){
            Notiflix.Notify.Success(data.message);
        },
        error:function (data) {
            Notiflix.Notify.Failure("请求失败");
        }
    });
    let accept_id = "#accept" + id;
    $(accept_id).hide();
}
function cancel(obj) {
    let id = parseInt(obj.getAttribute("value"));
    $.ajax({
        url:"/doctor/changeAppointStatus",
        type:"post",
        dataType:"json",
        data:{"appointResult":JSON.stringify({"appoint_result":"拒绝","id":id,
                "appoint_type":idAppointmentMap.get(id).appoint_type})},
        success:function (data){
            Notiflix.Notify.Success(data.message);
        },
        error:function (data) {
            Notiflix.Notify.Failure("请求失败");
        }
    });
    let appoint_id = "#appoint" + id;
    $(appoint_id).remove();
}


function isShowAcceptOrPatient(appointment) {
    if (appointment.appoint_result==="已接收" || appointment.appoint_result === "已完成"){
        let accept_id = "#accept" + appointment.id;
        $(accept_id).hide();
    }
    if (appointment.appoint_result==="拒绝"){
        let appoint_id = "#appoint" + appointment.id;
        $(appoint_id).remove();
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