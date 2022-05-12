
//提交病人信息
$("#submitPatientSet").click(function (){
    let form = new FormData();
    let username = $("#name").val();let birth = $("#birth").val();
    let gender = $("#gender").find('option:selected').text();
    let telephone = $("#telephone").val();
    let address = $("#address").val();
    let info = JSON.stringify({"username":username,"birth":birth,"gender":gender,
        "address":address,"telephone":telephone});
    form.append("info",info);
    if ($("input#userImg")[0].files.length===0){
        Notiflix.Notify.Info("未上传图片");
        $.ajax({
            url:"/patient/saveSettingInfo",
            type:"post",
            dataType:"json",
            data:form,
            cache: false,
            processData: false,
            contentType: false,
            success:function (data) {
                Notiflix.Notify.Success("修改成功");
            },
            error:function (data){
                Notiflix.Notify.Failure("请求失败");
            }
        });
    }
    else {
        form.append("userImg",$('input#userImg')[0].files[0])
        $.ajax({
            url:"/patient/saveSettingInfoImg",
            type:"POST",
            dataType:"json",
            data:form,
            cache: false,
            processData: false,
            contentType: false,
            success:function (data){
                $("img").each(function(){
                    if ($(this).attr("class")!=="img-fluid"){
                        $(this).attr("src",data.url);
                    }
                });
                Notiflix.Notify.Success("修改成功");
            },
            error:function (data){
                Notiflix.Notify.Failure("请求失败");
            }
        });
    }

});