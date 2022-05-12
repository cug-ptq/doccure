function showPatients(patientList) {
    for (let i = 0;i < patientList.length;i++){
        addPatient(patientList[i],i);
        let rescind_id = "#rescind" + i;
        $(rescind_id).attr("value",i);
    }
}

function addPatient(patientInfo, order) {
    let patientHtml = '<div class="col-md-6 col-lg-4 col-xl-3" id="patient'+ order +'">\n' +
        '       <div class="card widget-profile pat-widget-profile">\n' +
        '        <div class="card-body">\n' +
        '         <div class="pro-widget-content">\n' +
        '          <div class="profile-info-widget">\n' +
        '           <a href="/patient-profile?email='+ patientInfo.email +'" class="booking-doc-img">\n' +
        '            <img src="'+ patientInfo.image_url +'" alt="User Image">\n' +
        '           </a>\n' +
        '           <div class="profile-det-info">\n' +
        '            <h3><a href="/patient-profile?email='+ patientInfo.email +'">'+ patientInfo.username +'</a></h3>\n' +
        '\n' +
        '            <div class="patient-details">\n' +
        // '             <h5><b>Patient ID :</b> P0016</h5>\n' +
        '             <h5 class="mb-0"><i class="fas fa-map-marker-alt"></i> '+ patientInfo.address +'</h5>\n' +
        '            </div>\n' +
        '           </div>\n' +
        '          </div>\n' +
        '         </div>\n' +
        '         <div class="patient-info">\n' +
        '          <ul>\n' +
        '           <li>Phone <span>'+ patientInfo.telephone +'</span></li>\n' +
        '           <li>Age <span>'+ patientInfo.birth + " "+ patientInfo.gender +'</span></li>\n' +
        '          </ul>\n' +
        '         </div>\n' +
        '         <br>'+
        '         <div class="row row-sm">\n' +
        '           <div class="col-6">\n' +
        '             <a href="/patient-profile?email=' +patientInfo.email+'" class="btn view-btn">View Profile</a>\n' +
        '           </div>\n' +
        '           <div class="col-6">\n' +
        '             <a onclick="rescind(this)" id="rescind'+ order +'" class="btn book-btn">Rescind</a>\n' +
        '           </div>\n' +
        '         </div>'+
        '        </div>\n' +
        '       </div>\n' +
        '      </div>';
    $("#patientWidget").append(patientHtml);
}

function rescind(obj) {
    let order = parseInt(obj.getAttribute("value"));
    $.ajax({
        url: "/doctor/rescind",
        type: "post",
        dataType: "json",
        data: {"email": patientInfoList[order].email},
        success: function (data) {
            if (data.code===1){
                Notiflix.Notify.Success(data.message);
            }
            else {
                Notiflix.Notify.Failure(data.message);
            }
        },
        error:function () {
            Notiflix.Notify.Failure("请求失败");
        }
    });
    let patient_id = "#patient" + order;
    $(patient_id).remove();
}