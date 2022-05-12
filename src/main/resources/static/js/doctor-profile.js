function showL_S_U(isLogin) {
    if (isLogin===true){
        $("#login-signup").hide();
    }
    else {
        $("#userMenu").hide();
    }
}

/**
 * 教育 工作 服务
 * @param doctorEduList
 * @param doctorExpList
 * @param doctorServices
 */
function showDoctorInfo(doctorEduList,doctorExpList,doctorServices) {
    for (let i = 0;i < doctorEduList.length;i++){
        addEdu(doctorEduList[i]);
    }
    for (let i = 0;i < doctorExpList.length;i++){
        addExp(doctorExpList[i]);
    }
    let services = doctorServices.split(",")
    for (let i = 0;i < services.length;i++){
        addService(services[i]);
    }
}

function addExp(doctorExp) {
    let expHtml = '<li>\n' +
        '                              <div class="experience-user">\n' +
        '                                <div class="before-circle"></div>\n' +
        '                              </div>\n' +
        '                              <div class="experience-content">\n' +
        '                                <div class="timeline-content">\n' +
        '                                  <a class="name">'+ doctorExp.hospital +'</a>\n' +
        '                                  <span class="time">'+ doctorExp.from_time + "-" + doctorExp.to_time +'</span>\n' +
        '                                </div>\n' +
        '                              </div>\n' +
        '                            </li>';
    $("#experience").append(expHtml);
}

function addEdu(doctorEdu) {
    let eduHtml = '<li>\n' +
        '                              <div class="experience-user">\n' +
        '                                <div class="before-circle"></div>\n' +
        '                              </div>\n' +
        '                              <div class="experience-content">\n' +
        '                                <div class="timeline-content">\n' +
        '                                  <a class="name">'+ doctorEdu.college +'</a>\n' +
        '                                  <div>'+ doctorEdu.degree +'</div>\n' +
        '                                  <span class="time">'+ doctorEdu.year_complete +'</span>\n' +
        '                                </div>\n' +
        '                              </div>\n' +
        '                            </li>';
    $("#education").append(eduHtml);
}

function addService(service) {
    let serviceHtml = '<li>'+ service +'</li>';
    $("#services").append(serviceHtml);
}