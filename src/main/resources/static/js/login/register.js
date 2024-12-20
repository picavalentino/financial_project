$(document).ready(function(){
    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeader = $('meta[name="_csrf_header"]').attr('content');
    let isSelectedId = false;
    let isPwValid = false;
    let isMatchConfirmPw = false;
    let isEmailValid = false;
    // 입력항목들이 유효할때 등록버튼 활성화시키는 함수
    function validateForm(){
        let isValid = isSelectedId && isPwValid && isMatchConfirmPw && isEmailValid && $("#btn-certify").val() === "인증완료"
        $("#btn-register").prop("disabled", !isValid);
    }
    // 닫기 버튼
    $("#btn-cross").on("click", function(){
        window.location.href = "/login";
    })
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
                        $('#userResults').css("display", "block");
                        users.forEach(function(user) {
                            $('#userResults').append('<div class="dropdown-item" data-name="' + user.user_name
                            + '" data-birthday="' + user.user_birthday
                            + '" data-id="' + user.user_id+ '">'
                            + user.user_name +" / "+ user.user_birthday +'<span>'+ user.user_id +'</span></div>');
                        });
                        // 검색한 이름 선택시
                        $('.dropdown-item').on('click', function() {
                            let selectedUserId = $(this).data('id');
                            let selectedName = $(this).data('name');
                            let selectedBirthday = $(this).data('birthday');

                            $("#user_id").val(selectedUserId);
                            $("#user_name").text(selectedName);
                            $("#user_birthday").text(selectedBirthday);

                            alert("⚠️본인정보만 선택해주세요⚠️");
                            $("#user_id").prop("readonly",true);
                            $('#userResults').css("display", "none");
                            isSelectedId = true;
                            validateForm();
                        });
                    } else {
                        $('#userResults').hide();
                    }
                },
                error: function() {
                    $('#userResults').empty().css("display", "none");
                }
            });
        } else {
            $('#userResults').empty().css("display", "none");
        }
    })
    // 사원번호 선택 후 다시 인풋박스 클릭시
    $("#user_id").on("click",function(){
        let id = $(this).val();
        if(id.length===11 && window.confirm("사원번호를 다시 검색하시겠습니까?")){
            $("#user_id").prop("readonly", false).val("");
            $("#user_name").text(" ");
            $("#user_birthday").text(" ");
        }
    })
    // 검색창, 드롭박스 외 다른 구역 클릭시
    $(document).on('click', function(event) {
        // 검색 컨테이너 내부를 클릭하지 않은 경우
        if (!$(event.target).closest("#id-container").length) {
            $("#userResults").css("display", "none");
        }
    });
    // 사진등록 누르면 숨겨진 파일 입력 태그 동작
    $("#btn-photoReg").on("click", function (e) {
        e.preventDefault();
        $("#fileInput").click();
    });
    // 파일 선택 시 이미지 미리보기 업데이트
    $("#fileInput").on("change", function () {
        const fileInput = this;
        if (fileInput.files && fileInput.files.length === 1) {
            const file = fileInput.files[0];
            const reader = new FileReader();
            reader.onload = function (e) {
                $("#previewImg").attr("src", e.target.result);
            };
            reader.readAsDataURL(file);
        } else {
            alert("하나의 파일만 선택해주세요.");
            $(fileInput).val("");
        }
    });
    // 초기화 버튼 클릭 시
    $("#btn-reset").on("click", function () {
        $("#previewImg").attr("src", "/images/common/default-img.png"); // 기본 이미지로 변경
        $("#fileInput").val(""); // 파일 입력 초기화
    });
    // Enter 동작 설정 차단
    $('#fileInput').keydown(function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
        }
    });
    // 비밀번호 text->password & password->text
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
    $("#user_pw, #confirm-user_pw").on("input", function() {
        let pw = $("#user_pw").val();
        let confirm_pw = $("#confirm-user_pw").val();
        const pwPattern = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,13}$/;
        if (pw === confirm_pw) {
            isMatchConfirmPw = true;
            $("#msg-not-match").css("display", "none");
            if (pwPattern.test(pw)) {
                $("#msg-invalid-pw").css("display", "none");
                isPwValid = true;
            } else {
                $("#msg-invalid-pw").css("display", "inline");
                isPwValid = false;
            }
        }else {
            isMatchConfirmPw = false;
            $("#msg-not-match").css("display", "inline");
            if (pwPattern.test(pw)) {
                $("#msg-invalid-pw").css("display", "none");
                isPwValid = true;
            } else {
                $("#msg-invalid-pw").css("display", "inline");
                isPwValid = false;
            }
        }
        validateForm();
    })
    // 이메일 유효성 검사
    $("#user_email").on("input", function (){
        let email = $(this).val();
        const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
        if (!emailRegex.test(email)) {
            $("#msg-invalid-email").css("display", "inline");
            isEmailValid = false;
        } else {
            $("#msg-invalid-email").css("display", "none");
            isEmailValid = true;
        }
        if(email.length==0){
            $("#msg-invalid-email").css("display", "none");
            isEmailValid = false;
        }
        validateForm();

    });
    // 전화번호 숫자만
    $("#phone2, #phone3").on('keypress', function (event) {
        // 숫자 (0-9)만 입력 가능하게 설정
        if (event.which < 48 || event.which > 57) {
            // 백스페이스 (8), 탭 (9), 화살표 키 등은 허용
            if (event.which !== 8 && event.which !== 9) {
                event.preventDefault();
            }
        }
    });
    // 휴대전화 유효성 검사
    $("#phone1, #phone2, #phone3").on("input change", function() {
        $("#msg-already-exist").css("display", "none");
        $("#user_telno").val("");
        let phone1 = $("#phone1").val();
        let phone2 = $("#phone2").val();
        let phone3 = $("#phone3").val();
        let phone = phone1 +"-"+ phone2 +"-"+ phone3;
        let phoneRegex = /^01[0-9]-\d{3,4}-\d{4}$/;
        if(phoneRegex.test(phone)){
            $("#btn-certify").prop("disabled", false).val("인증");
        }else{
            $("#btn-certify").prop("disabled", true).val("인증");
        }
    })
    // 휴대전화 중복 확인
    $("#btn-certify").on("click", function(){
        let phone1 = $("#phone1").val();
        let phone2 = $("#phone2").val();
        let phone3 = $("#phone3").val();
        let phone = phone1 +"-"+ phone2 +"-"+ phone3;
        $.ajax({
            type: 'GET',
            url: '/register/certify',
            data: { telno: phone },
            success: function(response) {
                if(response){
                    $("#user_telno").val(phone);
                    $("#btn-certify").prop("disabled", true).val("인증완료");
                    alert("인증되었습니다.");
                    validateForm();
                }else{
                    $("#msg-already-exist").css("display", "inline");
                }
            },
            error: function() {
                alert("인증 요청 중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    })

    // 등록버튼 동작
    $("#btn-register").on("click", function(){
        let user = new FormData();
        user.append("user_id", $("#user_id").val());
        user.append("user_pw", $("#user_pw").val());
        user.append("user_email", $("#user_email").val());
        user.append("user_telno", $("#user_telno").val());
        let fileInput = $('#fileInput')[0].files[0];
        if (fileInput) {
            user.append("user_imgpath", fileInput);
        }

        $.ajax({
            url: "/register",
            type: "PATCH",
            data: user,
            processData: false,
            contentType: false,
            // CSRF 헤더와 토큰을 설정
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
                alert(response);
                window.location.href = "/login";
            },
            error: function(xhr, status, error) {
                console.error("Error: " + error);
                alert("오류가 발생했습니다.");
            }
        });
    })
})
