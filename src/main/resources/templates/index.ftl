<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>发送邮件</title>
    <link rel="stylesheet" href="/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/simditor/site/assets/styles/simditor.css" />

    <script type="text/javascript" src="/simditor/site/assets/scripts/jquery.min.js"></script>
    <script type="text/javascript" src="/simditor/site/assets/scripts/module.js"></script>
    <script type="text/javascript" src="/simditor/site/assets/scripts/hotkeys.js"></script>
    <script type="text/javascript" src="/simditor/site/assets/scripts/uploader.js"></script>
    <script type="text/javascript" src="/simditor/site/assets/scripts/simditor.js"></script>
</head>
<body class="container">
    <form id="mailForm">
        <div class="form-group row">
            <label for="colFormLabel" class="col-sm-2 col-form-label">收件人</label>
            <div class="col-sm-10">
                <input type="text" class="form-control"  placeholder="收件人" name="email">
            </div>
        </div>
        <div class="form-group row">
            <label for="colFormLabel" class="col-sm-2 col-form-label">主题</label>
            <div class="col-sm-10">
                <input type="text" class="form-control"  placeholder="主题" name="subject">
            </div>
        </div>
        <div class="form-group row">
            <label for="colFormLabel" class="col-sm-2 col-form-label">正文</label>
            <div class="col-sm-10">
                <textarea id="editor" placeholder="Balabala" autofocus name="content"></textarea>
            </div>
        </div>
        <div class="form-group row">
            <label for="colFormLabel" class="col-sm-2 col-form-label">附件</label>
            <div class="col-sm-10">
                <input id="file" type="file" class="form-control-file" multiple="multiple" />
            </div>
        </div>
        <div class="form-group row">
            <button type="button" class="btn btn-primary">发送</button>
        </div>
    </form>
</body>
<script>
    (function() {
        var editor = null;
        $(function() {
            var $preview, mobileToolbar, toolbar;
            Simditor.locale = 'en-US';
            toolbar = ['title', 'bold', 'italic', 'underline', 'strikethrough', 'fontScale', 'color', '|', 'ol', 'ul', 'blockquote', 'code', 'table', '|', 'link', 'image', 'hr', '|', 'indent', 'outdent', 'alignment'];
            editor = new Simditor({
                textarea: $('#editor'),
                placeholder: '这里输入文字...',
                toolbar: toolbar,
                pasteImage: true,
                defaultImage: '/simditor/site/assets/images/image.png',
                upload:{
                    url: '/mail/uploadImg',
                    params: null,
                    fileKey: 'file',
                    connectionCount: 3,
                    leaveConfirm: 'Uploading is in progress, are you sure to leave this page?'
                }
            });
            $preview = $('#preview');
            if ($preview.length > 0) {
                return editor.on('valuechanged', function(e) {
                    return $preview.html(editor.getValue());
                });
            }
        });

        $("#mailForm input[type='file']").each(function () {
            $(this).click(function () {
                console.log("d")
            })
        })

        $("#mailForm button").click(function () {
            debugger;
            var attrValue = {};
            var fields = $("#mailForm").serializeArray();
            $.each( fields, function(i, field){
                attrValue[field.name] = field.value;
            });
            if(!attrValue["email"]){
                alert("收件人不能为空！");
                return;
            }
            if(!attrValue["subject"]){
                alert("主题不能为空！");
                return;
            }
            if(!editor.getValue()){
                alert("正文不能为空！");
                return;
            }
            var formData = new FormData();
            for(var key in attrValue){
                formData.append(key, attrValue[key]);
            }
            $.each( $("#mailForm #file")[0].files, function(i, file){
                formData.append("files["+ i +"]", file);
            });
            $.ajax({
                type: "POST",
                url: "/mail/sendMail",
                data: formData,
                processData: false,
                contentType: false,
                success: function(msg){
                    alert( "邮件发送成功！");
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("邮件发送失败！");
                }
            });
        })
    }).call(this);
</script>
</html>