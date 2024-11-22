// 모든 모달을 닫는 함수
function closeAllModals() {
    const modals = document.querySelectorAll(".modal-overlay");
    modals.forEach(modal => {
        modal.style.display = "none";
    });
}

// 특정 모달 열기 함수
function openPrintModal() {
    closeAllModals();  // 다른 모달 닫기
    document.getElementById("printModal").style.display = "flex";
}

function openInsertModal() {
    closeAllModals();  // 다른 모달 닫기
    document.getElementById("insertModal").style.display = "flex";
}

// 특정 모달 닫기 함수
function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

/* ========================================================================= */

$(document).ready(function () {
    // 메시지 발송 버튼 클릭 이벤트
    $(".message-button").on("click", function () {
        // 선택된 체크박스 값 수집
        const selectedCustomers = [];
        $(".customer-checkbox:checked").each(function () {
            // 고객의 ID와 관련된 데이터 수집
            const row = $(this).closest('tr');
            const custId = $(this).val();
            const custNm = row.find('td:nth-child(3)').text(); // 고객명 컬럼의 텍스트
            const custTelno = row.find('td:nth-child(5)').text(); // 전화번호 컬럼의 텍스트

            // 고객 객체 생성 및 배열에 추가
            selectedCustomers.push({ custId: custId, custNm: custNm, custTelno: custTelno });
        });

        // 모달의 태그 컨테이너 초기화 (이전에 추가된 태그들 제거)
        $('#tagContainer').empty();

        // 선택된 고객들을 태그 형태로 모달에 추가
        if (selectedCustomers.length > 0) {
            selectedCustomers.forEach(customer => {
                const tagHtml = `
                    <button class="tag-btn" data-cust-id="${customer.custId}">
                        ${customer.custNm} (${customer.custTelno})
                        <span class="close-tag" onclick="removeTag(event)">X</span>
                    </button>`;
                $('#tagContainer').append(tagHtml);
            });
        } else {
            $('#tagContainer').append('<p>선택된 고객이 없습니다.</p>');
        }

        // openModal 함수로 모달 열기
        openModal('messageModal');
    });
});

// 태그 삭제 함수 (태그 버튼의 X 클릭 시 실행)
function removeTag(event) {
    event.stopPropagation(); // 이벤트 전파 방지
    const tagButton = event.target.closest('.tag-btn');
    if (tagButton) {
        tagButton.remove();
    }
}

// 모달 열기 함수
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.style.display = "flex"; // 모달을 중앙에 배치
}

/* ========================================================================= */
/* 고객등록 */

/* ========================================================================= */
/* 인쇄 */
function printTable() {
    // 모달 안에서 인쇄할 영역을 선택
    var printContents = document.getElementById('print-table').outerHTML;
    var originalContents = document.body.innerHTML;

    // 페이지의 내용을 인쇄할 내용으로 변경
    document.body.innerHTML = printContents;

    // 인쇄
    window.print();

    // 원래 페이지로 복구
    document.body.innerHTML = originalContents;
}

/* =========================================================================== */
$(document).ready(function () {
    let selectedCustomerIds = JSON.parse(localStorage.getItem('selectedCustomerIds')) || []; // 선택된 고객 ID 저장 변수
    let isAllSelected = JSON.parse(localStorage.getItem('isAllSelected')) || false; // 전체 선택 여부 플래그

    // 페이지 로드 시, 저장된 선택 상태 복원
    restoreCheckState();

    // 전체 선택 체크박스 클릭 이벤트
    $("#selectAll").on("click", function () {
        const isChecked = $(this).is(":checked");
        isAllSelected = isChecked;

        if (isChecked) {
            // 모든 고객을 선택한 것으로 처리하기 위해 서버에 요청
            $.ajax({
                type: 'GET',
                url: '/customer/list/all', // 모든 고객 데이터를 가져오는 엔드포인트
                success: function (response) {
                    // 서버에서 받은 모든 고객 ID를 선택된 상태로 추가
                    selectedCustomerIds = response.map(customer => customer.custId);

                    // 현재 페이지의 체크박스도 모두 선택
                    $(".customer-checkbox").each(function () {
                        $(this).prop("checked", true);
                    });

                    // 전체 선택 플래그를 저장하고 선택 상태 저장
                    isAllSelected = true;
                    saveCheckState();
                },
                error: function (error) {
                    console.error('Error fetching all customers:', error);
                }
            });
        } else {
            // 전체 선택 해제 처리
            selectedCustomerIds = [];
            isAllSelected = false;

            // 현재 페이지의 체크박스 해제
            $(".customer-checkbox").each(function () {
                $(this).prop("checked", false);
            });

            // 선택 상태 저장
            saveCheckState();
        }
    });

    // 개별 체크박스 클릭 이벤트
    $(document).on("click", ".customer-checkbox", function () {
        const custId = $(this).val();
        if ($(this).is(":checked")) {
            if (!selectedCustomerIds.includes(custId)) {
                selectedCustomerIds.push(custId);
            }
        } else {
            selectedCustomerIds = selectedCustomerIds.filter(id => id !== custId);
            isAllSelected = false; // 개별 선택 해제 시 전체 선택 플래그 해제
            $("#selectAll").prop("checked", false); // 전체 선택 체크박스도 해제
        }

        saveCheckState(); // 상태 저장
    });

    // 페이지 변경 시 체크 상태 유지
    $(document).on("pageChange", function () {
        restoreCheckState(); // 페이지 변경 시 저장된 체크 상태 복원
    });

    // 선택 상태 저장 (LocalStorage 사용)
    function saveCheckState() {
        localStorage.setItem('selectedCustomerIds', JSON.stringify(selectedCustomerIds));
        localStorage.setItem('isAllSelected', JSON.stringify(isAllSelected));
    }

    // 선택 상태 복원
    function restoreCheckState() {
        // 현재 페이지의 체크박스 상태 복원
        $(".customer-checkbox").each(function () {
            const custId = $(this).val();
            if (isAllSelected || selectedCustomerIds.includes(custId)) {
                $(this).prop("checked", true);
            } else {
                $(this).prop("checked", false);
            }
        });

        // 전체 선택 체크박스 상태 갱신
        const allChecked = $(".customer-checkbox").length > 0 && $(".customer-checkbox:checked").length === $(".customer-checkbox").length;
        $("#selectAll").prop("checked", isAllSelected && allChecked);
    }
});
