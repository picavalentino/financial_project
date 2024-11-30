//notice-detail.js
$(document).ready(function () {
    // CSRF 토큰 가져오기
    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    const serverInqPwd = $("#serverInqPwd").val(); // 서버에서 제공된 비밀번호
    const inqId = $("#inqId").val(); // 게시글 ID (HTML에 hidden input으로 전달)

    // 목록 버튼 클릭
    $("#btn-list").on("click", function (e) {
        e.preventDefault(); // 기본 동작 방지
        window.location.href = "/system/inquire"; // 목록 페이지로 이동
    });

    // 수정 버튼 클릭
    $("#btn-register").on("click", function (e) {
        e.preventDefault(); // 기본 동작 방지
        const inputPwd = $("#inqPwd").val();
        const isValid = $("#isValid").val() === 'true'; // 작성자 유효성 확인

        if (inputPwd === serverInqPwd && isValid) {
            if (confirm("공지사항을 수정하시겠습니까?")) {
                // 수정 페이지로 이동
                window.location.href = `/system/inquire/detail/${inqId}/update`;
            }
        } else if (!isValid) {
            alert("작성자만 수정할 수 있습니다.");
        } else {
            alert("비밀번호가 일치하지 않습니다.");
        }
    })

    // 삭제 버튼 클릭
    $("#btn-delete").on("click", function (e) {
        e.preventDefault(); // 기본 동작 방지
        const inputPwd = $("#inqPwd").val();
        const serverPwd = $("#serverInqPwd").val();
        const isValid = $("#isValid").val() === 'true'; // 작성자 유효성 확인

        console.log("입력된 비밀번호:", inputPwd);
        console.log("서버 비밀번호:", serverPwd);

        if (inputPwd === serverPwd && isValid) {
            if (confirm("공지사항을 삭제하시겠습니까?")) {
                $.ajax({
                    url: `/system/inquire/detail/${inqId}/delete`,
                    type: "POST",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8", // 필수
                    data: { inqPwd: inputPwd },
                    beforeSend: function (xhr) {
                        // CSRF 토큰 설정
                        xhr.setRequestHeader(csrfHeader, csrfToken);
                    },
                    success: function (response) {
                        alert("공지사항이 삭제되었습니다.");
                        window.location.href = "/system/inquire"; // 목록 페이지로 이동
                    },
                    error: function (xhr) {
                        if (xhr.status === 403) {
                            alert("비밀번호가 일치하지 않습니다.");
                        } else {
                            alert("삭제 중 오류가 발생했습니다.");
                        }
                    }
                });
            }
        } else if (!isValid) {
            alert("작성자만 삭제할 수 있습니다.");
        } else {
            alert("비밀번호가 일치하지 않습니다.");
        }
    });
});
