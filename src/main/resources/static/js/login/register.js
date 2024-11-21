$(document).ready(function(){
    // 닫기 버튼
    $("#btn-cross").on("click", function(){
        window.location.href = "/login";
    })
    // 등록 버튼이 동작하기 위한 조건들
    let selectedUserId = false;
    let pw_ok = false;
    let confirm_pw_ok = false;
    let email = false;
    let certify_phone = false;

    // 이름으로 사번 검색
    $("#user_id").on("input",function(){
        let keyword = $(this).val();
        if (keyword.length > 0) {
            $.ajax({
                type: 'GET',
                url: '/register/search',
                data: { keyword: keyword },
                success: function(users) {
                    $('#userResults').empty();

                    if (users.length > 0) {
                        $('#userResults').show();
                        users.forEach(function(user) {
                            $('#userResults').append('<div class="dropdown-item" data-name="' + user.user_name
                            + '" data-birthday="' + user.user_birthday
                            + '" data-id="' + user.user_id+ '">'

                            + user.user_name +" / "+ user.user_birthday + '</div>');
                        });
                        // 검색한 이름 선택시
                        $('.dropdown-item').on('click', function() {
                            let selectedUserId = $(this).data('id');
                            let selectedName = $(this).data('name');
                            let selectedBirthday = $(this).data('birthday');

                            $("#user_id").val(selectedUserId);
                            $("#user_name").text(selectedName);
                            $("#user_birthday").text(selectedBirthday);

                            $('#userResults').hide();
                            selectedUserId = true;
                        });
                    } else {
                        $('#userResults').hide();
                    }
                },
                error: function() {
                    $('#userResults').empty().hide();
                }
            });
        } else {
            $('#userResults').empty().hide();
        }
    })
    // 비밀번호 텍스트로 보이게
    $("#btn-eye").on("click", function(){
        const input = $('#user_pw');
        if(input.attr('type')==='password'){
            $("#btn-hide").css("display", "none");
            $("#btn-show").css("display", "block");
            input.attr('type', 'text');
        }else{
            $("#btn-show").css("display", "none");
            $("#btn-hide").css("display", "block");
            input.attr('type', 'password');
        }
    })
    // 비밀번호 유효성 검사
    $("#user_pw").on("input", function() {
        let pw = $(this).val();
        let pwPattern = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,13}$/;
        if (!pwPattern.test(pw)) {
            if ($("#msg-invalid-pw").text().length === 0) {
                $("#msg-invalid-pw").text("숫자와 영문 조합의 6~13자리여야 합니다");
                pw_ok = false;
            }
        } else {
            $("#msg-invalid-pw").text("");
            pw_ok = true;
        }
        if(pw.length==0){
            $("#msg-invalid-pw").text("");
            pw_ok = false;
        }
    })
    // 비밀번호 일치 확인
    $("#confirm-user_pw").on("input",function(){
        let pw = $("#user_pw").val();
        let confirm_pw = $(this).val();
        if(pw !== confirm_pw){
            if ($("#msg-not-match").text().length === 0) {
                $("#msg-not-match").text("일치하지 않습니다.");
                confirm_pw_ok = false;
            }
        }else if(pw.length != 0 && pw === confirm_pw){
            $("#msg-not-match").text("");
            confirm_pw_ok = true;
        }
        if(confirm_pw.length==0){
            $("#msg-not-match").text("");
            confirm_pw_ok = false;
        }
    })
    // 이메일 유효성 검사

    // 휴대전화 유효성 검사
    $("#phone1, #phone2, #phone3").on("input change", function() {
        $("#msg-already-exist").text("");
        certify_phone = false;
        let phone1 = $("#phone1").val();
        let phone2 = $("#phone2").val();
        let phone3 = $("#phone3").val();
        const phone = phone1 +"-"+ phone2 +"-"+ phone3;
        if(phone.length>11 && phone.length<14){
            $("#btn-certify").prop("disabled", false).val("인증");
        }else{
            $("#btn-certify").prop("disabled", true).val("인증");
        }
    })
    // 휴대전화 중복 확인
    $("#btn-certify").on("click", function(){
        let phone1 = $("#phon1").val();
        let phone2 = $("#phon2").val();
        let phone3 = $("#phon3").val();
        const phone = phone1 +"-"+ phone2 +"-"+ phone3;
        $.ajax({
            type: 'GET',
            url: '/register/certify',
            data: { telno: phone },
            success: function(response) {
                if(response){
                    // 밥 먹고와서 할것 버튼 비활성화 텍스트 인증완료로 바꾸기
                    $("#btn-certify").prop("disabled", true).val("인증완료");
                    alert("인증되었습니다.");
                    certify_phone = true;
                }else{
                    $("#msg-already-exist").text("이미 존재하는 번호입니다.");
                }
            },
            error: function() {
                alert("인증 요청 중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    })
    // 등록 버튼 활성화
    if(selectedUserId && pw_ok && confirm_pw_ok && email && certify_phone){
        $("#btn-register").prop("disabled", false);
    }else{
        $("#btn-register").prop("disabled", true);
    }
})
