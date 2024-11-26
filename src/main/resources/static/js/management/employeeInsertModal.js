//$(document).ready(function () {
//    $("#btn-register").on("click", function () {
//        alert("작동중");
//        // Bootstrap 5 Modal을 JavaScript로 열기
//        var insertModal = new bootstrap.Modal(document.getElementById('InsertModal'));
//        insertModal.show();
//
//
//
//        // 서버로부터 데이터 가져오기
//        $.ajax({
//            type: 'GET',
//            url: '/management/employee/insertModal',
//            success: function (response) {
//
//                // 부서 정보 채우기
//                let departmentSelect = $('#department');
//                departmentSelect.empty();
//                departmentSelect.append('<option value="" selected>부서</option>');
//                response.departmentList.forEach(function (department) {
//                    departmentSelect.append('<option value="' + department.user_dept_cd + '">' + department.dept_name + '</option>');
//                });
//
//                // 직위 정보 채우기
//                let positionSelect = $('#position');
//                positionSelect.empty();
//                positionSelect.append('<option value="" selected>직위</option>');
//                response.jobPositionList.forEach(function (position) {
//                    positionSelect.append('<option value="' + position.user_jbps_ty_cd + '">' + position.position_name + '</option>');
//                });
//
//                // 권한 정보 채우기
//                let authSelect = $('#auth');
//                authSelect.empty();
//                authSelect.append('<option value="" selected>권한</option>');
//                response.authList.forEach(function (auth) {
//                    authSelect.append('<option value="' + auth.user_auth_cd + '">' + auth.auth_name + '</option>');
//                });
//
//                // 상태 정보 채우기
//                let statusSelect = $('#status');
//                statusSelect.empty();
//                statusSelect.append('<option value="" selected>상태</option>');
//                response.statusList.forEach(function (status) {
//                    statusSelect.append('<option value="' + status.user_status + '">' + status.status_name + '</option>');
//                });
//            },
//            error: function () {
//                 console.error('Error fetching employee data:', error);
//            }
//        });
//
//
//
//    });
//
//    $(".btn-userIdGenerate").on("click", function() {
//            let joiningDate = $("#user_jncmp_ymd").val(); // 입사일자 값 가져오기
//
//            if (!joiningDate) {
//                alert("입사일자를 먼저 입력해주세요.");
//                return;
//            }
//
//            // 서버로부터 사원번호 생성 요청
//            $.ajax({
//                type: "POST",
//                url: "/management/generate/userId",
//                data: { joiningDate: joiningDate },
//                success: function(user_id) {
//                    $("#user_id").val(user_id); // 생성된 사원번호를 입력 필드에 채움
//                },
//                error: function() {
//                    alert("사원번호를 생성하는데 실패했습니다.");
//                }
//            });
//        });
//
//    $(".btn-close").on("click", function () {
//        var insertModal = bootstrap.Modal.getInstance(document.getElementById('InsertModal'));
//        if (insertModal) {
//            insertModal.hide(); // 모달 닫기
//        }
//
//        // 모달이 닫힌 후 페이지를 리디렉션
//        window.location.href = '/management/insert';
//    });
//});
//$(document).ready(function () {
//    $(".btn-insertEmployee").on("click", function () {
//        // Bootstrap 5 Modal을 JavaScript로 열기
//        var insertModal = new bootstrap.Modal(document.getElementById('InsertModal'));
//        insertModal.show();
//
//        // 서버로부터 데이터 가져오기
//        $.ajax({
//            type: 'GET',
//            url: '/management/employee/insertModal',
//            success: function (response) {
//
//                // 부서 정보 채우기
//                let departmentSelect = $('#department');
//                departmentSelect.empty();
//                departmentSelect.append('<option value="" selected>부서</option>');
//                response.departmentList.forEach(function (department) {
//                    departmentSelect.append('<option value="' + department.user_dept_cd + '">' + department.dept_name + '</option>');
//                });
//
//                // 직위 정보 채우기
//                let positionSelect = $('#position');
//                positionSelect.empty();
//                positionSelect.append('<option value="" selected>직위</option>');
//                response.jobPositionList.forEach(function (position) {
//                    positionSelect.append('<option value="' + position.user_jbps_ty_cd + '">' + position.position_name + '</option>');
//                });
//
//                // 권한 정보 채우기
//                let authSelect = $('#auth');
//                authSelect.empty();
//                authSelect.append('<option value="" selected>권한</option>');
//                response.authList.forEach(function (auth) {
//                    authSelect.append('<option value="' + auth.user_auth_cd + '">' + auth.auth_name + '</option>');
//                });
//
//                // 상태 정보 채우기
//                let statusSelect = $('#status');
//                statusSelect.empty();
//                statusSelect.append('<option value="" selected>상태</option>');
//                response.statusList.forEach(function (status) {
//                    statusSelect.append('<option value="' + status.user_status + '">' + status.status_name + '</option>');
//                });
//            },
//            error: function () {
//                 console.error('Error fetching employee data:', error);
//            }
//        });
//    });
//
//    $(".btn-userIdGenerate").on("click", function() {
//            let joiningDate = $("#user_jncmp_ymd").val(); // 입사일자 값 가져오기
//
//            if (!joiningDate) {
//                alert("입사일자를 먼저 입력해주세요.");
//                return;
//            }
//
//            // 서버로부터 사원번호 생성 요청
//            $.ajax({
//                type: "POST",
//                url: "/management/generate/userId",
//                data: { joiningDate: joiningDate },
//                success: function(user_id) {
//                    $("#user_id").val(user_id); // 생성된 사원번호를 입력 필드에 채움
//                },
//                error: function() {
//                    alert("사원번호를 생성하는데 실패했습니다.");
//                }
//            });
//        });
//
//    $(".btn-close").on("click", function () {
//        var insertModal = bootstrap.Modal.getInstance(document.getElementById('InsertModal'));
//        if (insertModal) {
//            insertModal.hide(); // 모달 닫기
//        }
//
//        // 모달이 닫힌 후 페이지를 리디렉션
//        window.location.href = '/management/insert';
//    });
//});