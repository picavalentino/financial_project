$(document).ready(function(){
    $(".btn-modify").on("click", function(){
        // Bootstrap 5 Modal을 JavaScript로 열기
        var editModal = new bootstrap.Modal(document.getElementById('editModal'));
        editModal.show();

        // 클릭된 버튼에서 userId 값을 가져옵니다.
        const userId = $(this).data("user-id");

        // 서버로부터 데이터 가져오기
        $.ajax({
            type: 'GET',
            url: '/management/employee/modal',
            data: { userId: userId },
            success: function(response) {
                // 서버에서 받은 데이터를 모달 필드에 채웁니다.
                $('#user_id').val(response.user_id);
                $('#user_name').val(response.user_name);
                $('#user_birthday').val(response.user_birthday);
                $('#department').val(response.user_dept_cd);
                $('#position').val(response.user_jbps_ty_cd);
                $('#auth').val(response.user_auth_cd);
                $('#status').val(response.user_status);

            },
            error: function(error) {
                console.error('Error fetching employee data:', error);
            }
        });
    });
});

//// 모든 readonly 요소에 대해 선택 방지
document.querySelectorAll('input[readonly]').forEach(input => {
    input.addEventListener('focus', (e) => e.target.blur()); // 포커스 제거
    input.addEventListener('mousedown', (e) => e.preventDefault()); // 클릭 방지
    input.addEventListener('selectstart', (e) => e.preventDefault()); // 텍스트 선택 방지
});


