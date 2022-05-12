function addAppointment(appointment,doctorInfo) {
    let appoint_time = timeDeal(appointment.appoint_time).split(" ");
    let date = appoint_time[0];
    let time = appoint_time[1];
    let aptHtml = '<tr>\n' +
        '       <td>\n' +
        '         <h2 class="table-avatar">\n' +
        '           <a href="/doctor-profile?email='+ doctorInfo.email +'" class="avatar avatar-sm mr-2">\n' +
        '             <img class="avatar-img rounded-circle" src="'+ doctorInfo.image_url +'" alt="User Image">\n' +
        '           </a>\n' +
        '           <a href="/doctor-profile?email='+ doctorInfo.email +'">'+ doctorInfo.username +'<span>'+ doctorInfo.speciality+'</span></a>\n' +
        '         </h2>\n' +
        '       </td>\n' +
        '       <td>'+ date +'<span class="d-block text-info">'+ time +'</span></td>\n' +
        '       <td><span class="badge badge-pill bg-success-light">'+appointment.appoint_result+'</span></td>\n' +
        '       <td class="text-right">\n' +
        '         <div class="table-action">\n' +
        '           <a onclick="showAptDetailsPatientDash(this)" id="'+ appointment.id +'" class="btn btn-sm bg-info-light" data-toggle="modal" data-target="#apt_details">\n' +
        '             <i class="far fa-eye"></i> View\n' +
        '           </a>\n' +
        '         </div>\n' +
        '       </td>\n' +
        '     </tr>';
    $("#aptPatient").append(aptHtml);
}

function addVisit(visit,doctorInfo) {
    let visit_time = timeDeal(visit.time,5);
    let color = "";
    if (visit.is_read){
        color = '<td><span id="isRead'+ visit.id +'">' + is_ReadStr(visit.is_read) + '</span></td>';
    }
    else {
        color = '<td><span class="d-block text-info" id="isRead'+ visit.id +'">' + is_ReadStr(visit.is_read) + '</span></td>'
    }
    return '<tr id="visit'+ visit.id +'">' +
        '            <td>\n' +
        '              <h2 class="table-avatar">\n' +
        '                <a href="/doctor-profile?email='+ doctorInfo.email +'" class="avatar avatar-sm mr-2">\n' +
        '                  <img class="avatar-img rounded-circle" src="'+ doctorInfo.image_url +'" alt="User Image">\n' +
        '                </a>\n' +
        '                <a href="/doctor-profile">'+ doctorInfo.username +'<span>'+ doctorInfo.speciality +'</span></a>\n' +
        '              </h2>\n' +
        '            </td>\n' +
        '            <td><span class="d-block text-info" id="time'+visit.id+'">' + visit_time + '</span></td>\n' +
        color +
        '            <td class="text-right">\n' +
        '              <div class="table-action">\n' +
        '                <a onclick="showVisitDetailsPatientDash(this)" class="btn btn-sm bg-info-light" id="visitView'+ visit.id +'" data-toggle="modal" data-target="#addVisitDiv">\n' +
        '                  <i class="far fa-eye"></i> View\n' +
        '                </a>\n' +
        '              </div>\n' +
        '            </td>' +
        '            </tr>';
}

function showAptPatientDash(appointments,doctorInfos) {
    let i = 0;
    for (let [id, value] of appointments) {
        addAppointment(appointments.get(id), doctorInfos[i]);
        i++;
    }
}

function showVisitPatientDash(visitIdMap,doctorInfos) {
    let i = 0;
    for (let [id,value] of visitIdMap){
        $("#visitBody").append(addVisit(visitIdMap.get(id),doctorInfos[i]));
        let visitView = "#visitView" + id;
        $(visitView).attr("value",id);
        i++;
    }
}



function showAptDetailsPatientDash(obj) {
    showDetails(appointmentsIdMap,parseInt(obj.id));
}

function showVisitDetailsPatientDash(obj) {
    let id = parseInt($("#"+obj.id).attr("value"));
    let isRead = "#isRead" + id;
    $(isRead).attr("class","");

    editor.disable();
    editor.txt.clear();
    console.log(visitDoctorIdMap.get(id).time);
    $("#time").val(timeDeal(visitDoctorIdMap.get(id).time,5,true));
    editor.txt.html(visitDoctorIdMap.get(id).visit_result);
    if (visitDoctorIdMap.get(id).is_read===0){
        $.ajax({
            url:"/patient/updateVisitIsRead",
            type:"post",
            dataType:"json",
            data:{"id":id,"is_read":1},
            success:function (data) {
                if (data.code === 1){
                    Notiflix.Notify.Success(data.message);
                    let isRead="#isRead" + id;
                    $(isRead).attr("class","" ).text("已读");
                }
                else {
                    Notiflix.Notify.Failure(data.message);
                }
            },
            error:function () {
                Notiflix.Notify.Failure("请求失败");
            }
        });
    }
}

function timeDeal(time,last=8,T=false) {
    let visit_time;let result;
    if(time.indexOf("T")!==-1){
        visit_time = time.split("T");
        result = visit_time[0] + " " + visit_time[1].substr(0,last);
        if (T){
            //含有T
            return visit_time[0] + "T" + visit_time[1].substr(0,last);
        }
    }
    else{
        visit_time = time.split(" ");
        result = visit_time[0] + " " + visit_time[1].substr(0,last);
        if (T){
            //含有T
            return visit_time[0] + "T" + visit_time[1].substr(0,last);
        }
    }
    return result;
}
function is_ReadStr(is_read) {
    if (is_read){
        return "已读";
    }
    else {
        return "未读";
    }
}

function receiveVisit(message) {
    Notiflix.Notify.Info("有家访信息哦!");
    message = JSON.parse(message);
    let id = message.id;
    if (message.code===-1){//删除
        let myVisit = "#myVisit" + id;
        $(myVisit).remove();
    }
    else if (message.code===1){//添加
        $("#visitBody").append(addVisit(message.visit,message.doctorInfo));
        if (!visitDoctorIdMap.has(id)){
            visitDoctorIdMap.set(id,message.visit);
        }
    }
    else if (message.code===0){//更新
        $.ajax({
            url: "/patient/getVisit",
            type: "post",
            dataType: "json",
            data: {"id":id},
            success:function (data){
                if (data.code===1){
                    visitDoctorIdMap.set(id,JSON.parse(data.message));
                    console.log(visitDoctorIdMap);
                    let isRead="#isRead" + id;
                    $(isRead).attr("class","d-block text-info" ).text("未读");
                    updateVisitTime(id,visitDoctorIdMap.get(id));
                }
                else {
                    Notiflix.Notify.Failure(data.message);
                }
            }
        });
    }
}
function updateVisitTime(id,visit) {
    let time = "#time" + id;
    $(time).text(visit.time);
}