/**
 * 显示today upcoming appointment
 * @param todayAppointments
 * @param upcomingAppointments
 * @param patientInfos
 */
function showTUAppointments(todayAppointments,upcomingAppointments,patientInfos) {
    //first today
    let i = 0;
    for (let [id,value] of todayAppointments){
        addToday(todayAppointments.get(id),patientInfos[i]);
        i++;
    }
    //
    for (let [id,value] of upcomingAppointments){
        addUpcoming(upcomingAppointments.get(id),patientInfos[i - todayAppointments.size]);
        i++;
    }
}

function addUpcoming(appointment,patientInfo) {
    let appoint_time = appointment.appoint_time.split("T");
    let date = appoint_time[0];
    let time = appoint_time[1].substr(0,8);
    let upcomingHtml = '<tr>\n' +
        '                              <td>\n' +
        '                                <h2 class="table-avatar">\n' +
        '                                  <a href="/patient-profile?email='+ appointment.patient_email +'" class="avatar avatar-sm mr-2"><img class="avatar-img rounded-circle" src="'+ patientInfo.image_url +'" alt="User Image"></a>\n' +
        '                                  <a href="/patient-profile?email='+ appointment.patient_email +'">'+ patientInfo.username +'</a>\n' +
        '                                </h2>\n' +
        '                              </td>\n' +
        '                              <td>'+ date +'<span class="d-block text-info">'+ time +'</span></td>\n' +
        '                              <td>'+ appointment.appoint_type +'</td>\n' +
        '                              <td class="text-right">\n' +
        '                                <div class="table-action">\n' +
        '                                  <a onclick="showViewDetails_Upcoming(this)" id="'+ appointment.id +'" class="btn btn-sm bg-info-light"  data-toggle="modal" data-target="#apt_details">\n' +
        '                                    <i class="far fa-eye"></i> View\n' +
        '                                  </a>\n' +
        '                                </div>\n' +
        '                              </td>\n' +
        '                            </tr>';
    $("#upcomingBody").append(upcomingHtml);
}
function addToday(appointment,patientInfo) {
    let appoint_time = appointment.appoint_time.split("T");
    let date = appoint_time[0];
    let time = appoint_time[1].substr(0,8);
    let todayHtml = '<tr>\n' +
        '                              <td>\n' +
        '                                <h2 class="table-avatar">\n' +
        '                                  <a href="/patient-profile?email='+ appointment.patient_email +'" class="avatar avatar-sm mr-2"><img class="avatar-img rounded-circle" src="'+ patientInfo.image_url +'" alt="User Image"></a>\n' +
        '                                  <a href="/patient-profile?email='+ appointment.patient_email +'">'+ patientInfo.username +'</a>\n' +
        '                                </h2>\n' +
        '                              </td>\n' +
        '                              <td>'+ date +'<span class="d-block text-info">'+ time +'</span></td>\n' +
        '                              <td>'+ appointment.appoint_type +'</td>\n' +
        '                              <td class="text-right">\n' +
        '                                <div class="table-action">\n' +
        '                                  <a onclick="showViewDetails_Today(this)" id="'+ appointment.id +'" class="btn btn-sm bg-info-light"  data-toggle="modal" data-target="#apt_details">\n' +
        '                                    <i class="far fa-eye"></i> View\n' +
        '                                  </a>\n' +
        '                                </div>\n' +
        '                              </td>\n' +
        '                            </tr>';
    $("#todayBody").append(todayHtml);
}

function showViewDetails_Today(obj) {
    showDetails(todayAppointmentsIdMap,parseInt(obj.id));
}

function showViewDetails_Upcoming(obj) {
    showDetails(upcomingAppointmentsIdMap,parseInt(obj.id));
}

