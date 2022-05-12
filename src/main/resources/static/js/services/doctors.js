function showDoctorL_S_U(isLogin) {
    if (isLogin===true){
        $("#login-signup").hide();
        console.log(isLogin);
        //$("#userMenu").show();
    }
    else {
        //$("#login-signup").show();
        $("#userMenu").hide();
        console.log(isLogin);
    }
}
function showDoctors(doctorList) {
    for (let i = 0;i < doctorList.length;i++){
        addDoctor(doctorList[i]);
    }
}

function addDoctor(doctor) {
    console.log(doctor);
    let doctorHtml = '<div class="col-md-6 col-lg-4 col-xl-3">\n' +
        '              <div class="card widget-profile pat-widget-profile">\n' +
        '                <div class="card-body">\n' +
        '                  <div class="pro-widget-content">\n' +
        '                    <div class="profile-info-widget">\n' +
        '                      <a href="/doctor-profile?email=' +doctor.email +'" class="booking-doc-img">\n' +
        '                        <img src="' + doctor.image_url +'" alt="User Image">\n' +
        '                      </a>\n' +
        '                      <div class="profile-det-info">\n' +
        '                        <h3><a href="/doctor-profile?email=' +doctor.email+'">'+ doctor.username +'</a></h3>\n' +
        '\n' +
        '                        <div class="patient-details">\n' +
        '                          <h5><b>Speciality</b> '+ doctor.speciality +'</h5>\n' +
        '                          <h5 class="mb-0"><i class="fas fa-map-marker-alt"></i>'+ doctor.address +'</h5>\n' +
        '                        </div>\n' +
        '                      </div>\n' +
        '                    </div>\n' +
        '                  </div>\n' +
        '                  <div class="patient-info">\n' +
        '                    <ul>\n' +
        '                      <li>Phone <span>'+ doctor.telephone +'</span></li>\n' +
        '                      <li>Age <span>'+ doctor.birth + "," + doctor.gender +'</span></li>\n' +
        '                    </ul>\n' +
        '                  </div>\n' +
        '                  <br>'+
        '                  <div class="row row-sm">\n' +
        '                      <div class="col-6">\n' +
        '                          <a href="/doctor-profile?email=' +doctor.email+'"'+' class="btn view-btn">View Profile</a>\n' +
        '                      </div>\n' +
        '                      <div class="col-6">\n' +
        '                          <a href="/booking?email=' +doctor.email+'"'+' class="btn book-btn">Book Now</a>\n' +
        '                      </div>\n' +
        '                  </div>'+
        '                </div>\n' +
        '              </div>\n' +
        '            </div>';
    $("#doctorList").append(doctorHtml);
}