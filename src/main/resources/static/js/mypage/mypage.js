$(document).ready(function () {
    const userId = $("#user_id").val(); // userId 값을 가져오기
    const apiUrl = `/mypage/${userId}`; // API 엔드포인트
    const $editButton = $("#editButton"); // "수정하기" 버튼
    const $adminButton = $("#adminButton"); // "등록하기" 버튼

    const $email = $("input[type='email']"); // 이메일 입력 필드
    const $form = $("#updateForm"); // 폼 객체


    const $eyeIcon = $("#eyeIcon"); // 눈 버튼
    const $newPassword = $("#newPassword"); // 입력 필드
    const $eyeOpen = $("#eye-open"); // 눈 열림 아이콘
    const $eyeClose = $("#eye-close"); // 눈 닫힘 아이콘

    // 눈 버튼 클릭 이벤트
    $eyeIcon.on("click", function () {
        const currentType = $newPassword.attr("type"); // 현재 입력 필드 타입

        if (currentType === "password") {
            $newPassword.attr("type", "text"); // 입력값 보이기
            $eyeOpen.hide(); // 열림 아이콘 숨기기
            $eyeClose.show(); // 닫힘 아이콘 보이기
        } else {
            $newPassword.attr("type", "password"); // 입력값 숨기기
            $eyeOpen.show(); // 열림 아이콘 보이기
            $eyeClose.hide(); // 닫힘 아이콘 숨기기
        }
    });


    // CSRF 토큰 및 헤더 가져오기
    const csrfTokenMeta = document.querySelector("meta[name='_csrf']");
    const csrfHeaderMeta = document.querySelector("meta[name='_csrf_header']");

    // CSRF 토큰과 헤더 값 확인
    if (!csrfTokenMeta || !csrfHeaderMeta) {
        console.error("CSRF 메타 태그를 찾을 수 없습니다.");
        return;
    }

    const csrfToken = csrfTokenMeta.getAttribute("content");
    const csrfHeader = csrfHeaderMeta.getAttribute("content");

    // "수정하기" 버튼 클릭 시
    $("#editButton").on("click", function () {
        console.log("수정하기 버튼 클릭됨");
        $(".phone").prop("disabled", false);
        $("#user_name").prop("disabled", false);
        $("#user_email").prop("disabled", false);
    });

    $("#updateForm").on("submit", function (event) {
        event.preventDefault();

        const formData = new FormData(this);

        // 디버깅: FormData 값 확인
        for (let [key, value] of formData.entries()) {
            console.log(`${key}: ${value}`);
        }

        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

        $.ajax({
            type: "POST",
            url: "/mypage/update",
            data: formData,
            processData: false,
            contentType: false,
            headers: { [csrfHeader]: csrfToken },
            success: function (response) {
                alert("사용자 정보가 성공적으로 수정되었습니다!");
            },
            error: function (xhr, status, error) {
                console.error("서버 오류:", xhr.responseText);
                alert("서버에서 오류가 발생했습니다. 관리자에게 문의하세요.");
            }
        });
    });
    $("#file").on("change", function (event) {
        const file = event.target.files[0]; // 선택된 파일
        if (file) {
            // 파일이 이미지인지 확인
            if (file.type.startsWith("image/")) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    // 이미지 미리보기 표시
                    $("#profile_img").attr("src", e.target.result);
                };
                reader.readAsDataURL(file); // 파일을 Data URL로 읽기
            } else {
                alert("이미지 파일만 업로드 가능합니다.");
                // 입력 값 초기화
                $(this).val("");
                $("#profile_img").attr("src", '/css/mypage/img/mypage_default_profile.jpg'); // 기본 이미지로 복원
            }
        }
    });

    const $modal = $("#passwordModal"); // 모달창
    const $openBtn = $("#pw_edit"); // 열기 버튼
    const $closeBtn = $(".close"); // 닫기 버튼
    const $changePasswordBtn = $("#changePasswordBtn"); // 비밀번호 변경 버튼


    // 모달 열기
    $openBtn.on("click", function () {
        $modal.fadeIn(); // 모달창을 페이드인
    });

    // 모달 닫기
    $closeBtn.on("click", function () {
        $modal.fadeOut(); // 모달창을 페이드아웃
    });

    // 모달 외부 클릭 시 닫기
    $(window).on("click", function (e) {
        if ($(e.target).is($modal)) {
            $modal.fadeOut();
        }
    });

$("#phone_btn").on("click", function() {
    // 전화번호 조합
    const phonePart1 = $("#phone_input1").val();
    const phonePart2 = $("#phone_input2").val();
    const phonePart3 = $("#phone_input3").val();
    const phoneNumber = phonePart1 + "-" + phonePart2 + "-" + phonePart3; // 전화번호 조합

    // 서버로 AJAX 요청
    $.ajax({
        url: "/mypage/certify",
        type: "GET",
        data: { telno: phoneNumber },
        success: function (response) {
            if (response) {
                $("#phone_btn").prop("disabled", true).val("인증완료");
                alert("인증되었습니다.");
            } else {
                alert("이미 등록된 번호입니다.");
            }
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
            alert("인증 요청 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
    });
});


// 전화번호 저장 함수
function savePhoneNumber(phoneNumber) {
    const userId = $("#userId").val(); // 사용자 ID 가져오기 (예시)

    $.ajax({
        url: "/mypage/savePhone",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ userId: userId, telno: phoneNumber }), // userId와 전화번호 JSON으로 전송
        success: function(response) {
            alert("전화번호가 저장되었습니다.");
        },
        error: function(xhr, status, error) {
            console.error("Error while saving phone number:", error);
            alert("전화번호 저장 중 오류가 발생했습니다.");
        }
    });
}
    // 데이터를 가져와 페이지에 렌더링
    function fetchAndRenderData() {
        $.ajax({
            type: "GET",
            url: mypage/update,
            success: function (data) {
                console.log("데이터 가져오기 성공:", data);

                // 기본 정보 렌더링
                $("#user_id").val(data.userId || "");
                $("#user_jncmp_ymd").val(data.userJncmpYmd || "");
                $("#user_dept_cd_code_cl").val(data.userDeptName || "");
                $("#user_jbps_ty_cd_code_cl").val(data.userJobName || "");
                $("#user_name").val(data.userName || "");

                // 전화번호 렌더링
                const phone = data.userTelno || "";
                const phoneParts = phone.match(/(\d{3})(\d{4})(\d{4})/);
                if (phoneParts) {
                    $("#phone_input1").val(phoneParts[1]);
                    $("#phone_input2").val(phoneParts[2]);
                    $("#phone_input3").val(phoneParts[3]);
                } else {
                    $("#phone_input1").val("");
                    $("#phone_input2").val("");
                    $("#phone_input3").val("");
                }

                $("#user_email").val(data.userEmail || "");

                // 고객 정보 렌더링
                const customerTable = $("#customer_data");
                if (data.custProds && data.custProds.length > 0) {
                    customerTable.html(
                        data.custProds
                            .map(
                                (customer) => `
                            <tr>
                                <td><a href="#">${customer.custNm}</a></td>
                                <td>${customer.dsgnDsTyCdCodeCl}</td>
                                <td>${customer.custEmail}</td>
                            </tr>
                        `
                            )
                            .join("")
                    );
                } else {
                    customerTable.html(`<tr><td colspan="3">고객 정보가 없습니다.</td></tr>`);
                }

                // 작성글 렌더링
                const boardTable = $("#board_data");
                if (data.inquiries && data.inquiries.length > 0) {
                    boardTable.html(
                        data.inquiries
                            .map(
                                (board) => `
                            <tr>
                                <td><a href="#">${board.inqTitle}</a></td>
                                <td>${data.userId}</td>
                                <td>${board.inqCreateAt}</td>
                            </tr>
                        `
                            )
                            .join("")
                    );
                } else {
                    boardTable.html(`<tr><td colspan="3">작성글이 없습니다.</td></tr>`);
                }
            },
            error: function (error) {
                console.error("데이터 가져오기 실패:", error);
            },
        });
    }

     // 비밀번호 변경 버튼 클릭 이벤트
    $("#changePasswordBtn").on("click", function () {
        const newPassword = $("#newPassword").val();
        const confirmPassword = $("#confirmPassword").val();

        // 비밀번호 유효성 검사
        if (!newPassword || !confirmPassword) {
            alert("비밀번호를 입력해주세요.");
            return;
        }
        if (newPassword !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
            return;
        }

        // CSRF 토큰 설정
        const csrfToken = $('meta[name="_csrf"]').attr("content");
        const csrfHeader = $('meta[name="_csrf_header"]').attr("content");

        // AJAX 요청으로 서버에 비밀번호 전달
        $.ajax({
            type: "POST",
            url: "/mypage/change-password",
            data: {
                newPassword: newPassword,
                confirmPassword: confirmPassword
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 헤더 추가
            },
            success: function (response) {
                alert("비밀번호가 성공적으로 변경되었습니다!");
                location.reload(); // 성공 시 페이지 새로고침
            },
            error: function (xhr) {
                console.error("오류 발생:", xhr.responseText);
                alert("비밀번호 변경 중 오류가 발생했습니다. 다시 시도해주세요.");
            },
        });
    });


    // 데이터 가져오기 호출
    fetchAndRenderData();
});
