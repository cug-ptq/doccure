function addSuccessAppoint(appointment,doctorInfo) {
    let appoint_time = appointment.appoint_time.split("T");
    let date = appoint_time[0];
    let time = appoint_time[1].substr(0,8);
    let successHtml = '<tr id="appoint'+ appointment.id +'">\n' +
        '      <td>\n' +
        '        <h2 class="table-avatar">\n' +
        '          <a href="/doctor-profile?email='+ appointment.doctor_email +'" class="avatar avatar-sm mr-2"><img class="avatar-img rounded-circle" src="'+ doctorInfo.image_url +'" alt="User Image"></a>\n' +
        '          <a href="/doctor-profile?email='+ appointment.doctor_email +'">'+ doctorInfo.username +'</a>\n' +
        '        </h2>\n' +
        '      </td>\n' +
        '      <td>'+ date +'<span class="d-block text-info">'+ time +'</span></td>\n' +
        '      <td>'+ appointment.appoint_type +'</td>\n' +
        '      <td class="text-right">\n' +
        '        <div class="table-action">\n' +
        '          <a onclick="showDetails_BookingSuccess(this)" id="'+ appointment.id +'" class="btn btn-sm bg-info-light"  data-toggle="modal" data-target="#apt_details">\n' +
        '            <i class="far fa-eye"></i> View\n' +
        '          </a>\n' +
        '        </div>\n' +
        '      </td>\n' +
        '    </tr>';
    $("#aptSuccess").append(successHtml);
}

function addFailAppoint(appointment,doctorInfo) {
    let appoint_time = appointment.appoint_time.split("T");
    let date = appoint_time[0];
    let time = appoint_time[1].substr(0,8);
    let failHtml = '<tr id="appoint'+ appointment.id +'">\n' +
        '  <td>\n' +
        '    <h2 class="table-avatar">\n' +
        '      <a href="/doctor-profile?email='+ appointment.doctor_email +'" class="avatar avatar-sm mr-2"><img class="avatar-img rounded-circle" src="'+ doctorInfo.image_url +'" alt="User Image"></a>\n' +
        '      <a href="/doctor-profile?email='+ appointment.doctor_email +'">'+ doctorInfo.username +'</a>\n' +
        '    </h2>\n' +
        '  </td>\n' +
        '  <td>'+ date +'<span class="d-block text-info">'+ time +'</span></td>\n' +
        '  <td>'+ appointment.appoint_type +'</td>\n' +
        '  <td class="text-right">\n' +
        '    <div class="table-action">\n' +
        '      <a onclick="showDetails_BookingFail(this)" id="'+ appointment.id +'" class="btn btn-sm bg-info-light"  data-toggle="modal" data-target="#apt_details">\n' +
        '        <i class="far fa-eye"></i> View\n' +
        '      </a>\n' +
        '    </div>\n' +
        '  </td>\n' +
        '</tr>';
    $("#aptFail").append(failHtml);
}

function showBooking(successAptList, failAptList, doctorInfos) {
    let i = 0;
    for (let [id,value] of successAptList){
        addSuccessAppoint(successAptList.get(id),doctorInfos[i]);
        i++;
    }
    for (let [id,value] of failAptList){
        addFailAppoint(failAptList.get(id),doctorInfos[i]);
        i++;
    }
}

function showDetails_BookingSuccess(obj) {
    showDetails(successAptList,parseInt(obj.id));
    removeApt(parseInt(obj.id));
    updateIsRead(obj.id);
}

function showDetails_BookingFail(obj) {
    showDetails(failAptList,parseInt(obj.id));
    removeApt(parseInt(obj.id));
    updateIsRead(obj.id);
}

function removeApt(id) {
    let aptId = "#appoint" + id;
    $(aptId).remove();
}

function updateIsRead(id) {
    $.ajax({
        url:"/patient/readApt",
        type:"post",
        dataType:"json",
        data:{"id":id},
        success:function (data){
           Notiflix.Notify.Success(data.message);
        }
    });
}