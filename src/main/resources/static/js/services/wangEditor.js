
function initEditor(editor,placeholder="") {
    editor.config.uploadImgMaxSize = 10 * 1024 * 1024; // 10M
    editor.config.uploadImgAccept = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
    editor.config.uploadImgMaxLength = 5; // 一次最多上传 5 个图片
    editor.config.uploadFileName = "files";
    editor.config.uploadImgServer = "/uploadAssess/img";
    editor.config.placeholder = placeholder;
    sameConfigEditor(editor);
}

function sameConfigEditor(editor) {
    editor.config.uploadImgHooks = {
        // 图片上传并返回了结果，图片插入已成功
        success: function(xhr) {
            Notiflix.Notify.Success("上传成功")
        },
        // 图片上传并返回了结果，但图片插入时出错了
        fail: function(xhr, editor, resData) {
            console.log('fail', resData);
            Notiflix.Notify.Failure("图片插入出错");
        },
        // 上传图片出错，一般为 http 请求的错误
        error: function(xhr, editor, resData) {
            console.log('error', xhr, resData);
            Notiflix.Notify.Failure("上传图片出错");
        },
        // 上传图片超时
        timeout: function(xhr) {
            Notiflix.Notify.Failure("上传图片超时");
        }
    }
    editor.config.customAlert = function (s, t) {
        switch (t) {
            case 'success':
                Notiflix.Notify.Success(s);
                break
            case 'info':
                Notiflix.Notify.Info(s);
                break
            case 'warning':
                Notiflix.Notify.Warning(s);
                break
            case 'error':
                Notiflix.Notify.Failure(s);
                break
            default:
                Notiflix.Notify.Info(s);
                break
        }
    }
}
