//提交医生信息
$("#submitDoctorSet").click(function (){
    let formData = new FormData(); //数据表单

    let degreeArr = $('input[name="degree"]'); let collegeArr = $('input[name=college]');
    let year_completeArr = $('input[name=year_complete]');
    let edu = [];//教育
    for (let i = 0;i < degreeArr.length;i++){
        let obj_edu = {};
        obj_edu.degree = $(degreeArr[i]).val();obj_edu.college = $(collegeArr[i]).val();
        obj_edu.year_complete = $(year_completeArr[i]).val();
        edu.push(obj_edu);
    }
    let exp = [];//工作
    let hospitalArr = $('input[name=hospital]'); let fromArr = $('input[name=from]');
    let designationArr = $('input[name=designation]'); let toArr = $('input[name=to]');
    for (let i = 0;i < hospitalArr.length;i++){
        let obj_exp = {};
        obj_exp.hospital = $(hospitalArr[i]).val();obj_exp.from_time = $(fromArr[i]).val();
        obj_exp.to_time = $(toArr[i]).val();
        obj_exp.designation = $(designationArr[i]).val();
        exp.push(obj_exp);
    }
    console.log(edu);
    let username = $("#name").val();let gender = $("#gender").find('option:selected').text();
    let telephone = $("#telephone").val();
    let address = $("#address").val();let birth = $("#birth").val();let services = $("#services").val();
    let about_me = $("#about_me").val();
    let infoBasic = JSON.stringify({"username":username,"birth":birth,"gender":gender,
        "address":address,"telephone":telephone,"services":services,"about_me":about_me});
    formData.append("infoBasic",infoBasic);
    formData.append("infoEdu",JSON.stringify(edu));
    formData.append("infoExp",JSON.stringify(exp));
    if ($('input#userImg')[0].files.length===0){
        Notiflix.Notify.Info("未上传图片");
        $.ajax({
            url:"/doctor/saveSettingInfo",
            type:"post",
            dataType:"json",
            data:formData,
            cache: false,
            processData: false,
            contentType: false,
            success:function (data) {
                Notiflix.Notify.Success("设置成功");
            },
            error:function (data){
                Notiflix.Notify.Failure("请求失败");
            }
        });
    }
    else {
        formData.append("userImg",$("input#userImg")[0].files[0]);
        $.ajax({
            url:"/doctor/saveSettingInfoImg",
            type:"POST",
            dataType:"json",
            data:formData,
            cache: false,
            processData: false,
            contentType: false,
            success:function (data){
                $("img").each(function(){
                    if ($(this).attr("class")!=="img-fluid"){
                        $(this).attr("src",data.url);
                    }
                });
                Notiflix.Notify.Success("设置成功");
            },
            error:function (data){
                Notiflix.Notify.Failure("请求失败");
            }
        });
    }
});

//增加
function add_edu() {
    let education_content = '<div class="row form-row education-cont">' +
        '<div class="col-12 col-md-10 col-lg-11">' +
        '<div class="row form-row">' +
        '<div class="col-12 col-md-6 col-lg-4">' +
        '<div class="form-group">' +
        '<label>Degree</label>' +
        '<input type="text" class="form-control" name="degree">' +
        '</div>' +
        '</div>' +
        '<div class="col-12 col-md-6 col-lg-4">' +
        '<div class="form-group">' +
        '<label>College/Institute</label>' +
        '<input type="text" class="form-control" name="college">' +
        '</div>' +
        '</div>' +
        '<div class="col-12 col-md-6 col-lg-4">' +
        '<div class="form-group">' +
        '<label>Year of Completion</label>' +
        '<input type="text" class="form-control" name="year_complete">' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<div class="col-12 col-md-2 col-lg-1"><label class="d-md-block d-sm-none d-none">&nbsp;</label><a href="#" class="btn btn-danger trash"><i class="far fa-trash-alt"></i></a></div>' +
        '</div>';

    $(".education-info").append(education_content);
}
function add_exp() {
    let experience_content = '<div class="row form-row experience-cont">' +
        '<div class="col-12 col-md-10 col-lg-11">' +
        '<div class="row form-row">' +
        '<div class="col-12 col-md-6 col-lg-4">' +
        '<div class="form-group">' +
        '<label>Hospital Name</label>' +
        '<input type="text" class="form-control" name="hospital">' +
        '</div>' +
        '</div>' +
        '<div class="col-12 col-md-6 col-lg-4">' +
        '<div class="form-group">' +
        '<label>From</label>' +
        '<input type="text" class="form-control" name="from">' +
        '</div>' +
        '</div>' +
        '<div class="col-12 col-md-6 col-lg-4">' +
        '<div class="form-group">' +
        '<label>To</label>' +
        '<input type="text" class="form-control" name="to">' +
        '</div>' +
        '</div>' +
        '<div class="col-12 col-md-6 col-lg-4">' +
        '<div class="form-group">' +
        '<label>Designation</label>' +
        '<input type="text" class="form-control" name="designation">' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<div class="col-12 col-md-2 col-lg-1"><label class="d-md-block d-sm-none d-none">&nbsp;</label><a href="#" class="btn btn-danger trash"><i class="far fa-trash-alt"></i></a></div>' +
        '</div>';

    $(".experience-info").append(experience_content);
}
//教育
function setEdu(doctorEdu) {
    for (let i = 0;i < doctorEdu.length-1;i++){
        add_edu();
    }
    let degreeArr = $('input[name="degree"]'); let collegeArr = $('input[name=college]');
    let year_completeArr = $('input[name=year_complete]');
    for (let i = 0;i < doctorEdu.length;i++){
        $(degreeArr[i]).attr("value",doctorEdu[i].degree);
        $(collegeArr[i]).attr("value",doctorEdu[i].college);
        $(year_completeArr[i]).attr("value",doctorEdu[i].year_complete);
    }
}
//工作
function setExp(doctorExp) {
    for (let i = 0;i < doctorExp.length-1;i++){
        add_exp();
    }
    let hospitalArr = $('input[name=hospital]'); let fromArr = $('input[name=from]');
    let designationArr = $('input[name=designation]'); let toArr = $('input[name=to]');
    for (let i = 0;i < doctorExp.length;i++){
        $(hospitalArr[i]).attr("value",doctorExp[i].hospital);
        $(fromArr[i]).attr("value",doctorExp[i].from_time);
        $(designationArr[i]).attr("value",doctorExp[i].designation);
        $(toArr[i]).attr("value",doctorExp[i].to_time);
    }
}