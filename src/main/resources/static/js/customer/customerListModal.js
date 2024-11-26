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

function openModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.style.display = "flex"; // 모달을 중앙에 배치
}


// 특정 모달 닫기 함수
function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

/* ========================================================================= */
/* 고객등록 */
$(document).ready(function () {
    // 고객 등록 버튼 클릭 이벤트
    $(".insert-button").on("click", function () {
    console.log("버튼 눌렀잖아");
        // openModal 함수로 모달 열기
        openModal('insertModal');
    });
});

document.addEventListener("DOMContentLoaded", function() {
    var submitButton = document.getElementById("submitCustomer");

    submitButton.addEventListener("click", function(e) {
        // 기본 폼 제출 동작 방지
        e.preventDefault();

        // 입력 값 가져오기
        var custNm = document.getElementById("custNm").value;
        var custRrn = document.getElementById("custRrn").value;
        var custTelno = document.getElementById("custTelno").value;
        var custEmail = document.getElementById("custEmail").value;
        var custOccpTyCd = document.getElementById("custOccpTyCd").value;
        var custAddr = document.getElementById("custAddr").value;

        // 고객 정보 객체 생성 (custId는 서버에서 생성되므로 제외)
        var customerData = {
            custNm: custNm,
            custRrn: custRrn,
            custTelno: custTelno,
            custEmail: custEmail,
            custOccpTyCd: custOccpTyCd,
            custAddr: custAddr
        };

        // 비동기적으로 데이터를 서버에 전송
        fetch('/customer/list/insert', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customerData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('고객이 성공적으로 등록되었습니다.');
                // 필요한 경우 모달을 닫거나, 폼 초기화 등을 수행
                document.getElementById("insertModal").style.display = "none";
                document.getElementById("customerForm").reset();
            } else {
                alert('고객 등록에 실패했습니다: ' + data.message);
            }
        })
        .catch(error => {
            console.error('고객 등록 중 오류가 발생했습니다:', error);
            alert('오류가 발생했습니다. 다시 시도해주세요.');
        });
    });
});

/* ========================================================================= */
/* 체크 박스 */

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


