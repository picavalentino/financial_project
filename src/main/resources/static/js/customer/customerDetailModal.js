// 모든 모달을 닫는 함수
function closeAllModals() {
    const modals = document.querySelectorAll(".modal-overlay");
    modals.forEach(modal => {
        modal.style.display = "none";
    });
}
// ========================================================================
// 수정내역 기록
// =========================================================================
// 담당자 검색 모달 열기
function openSearchModal() {
    const modal = document.getElementById("searchModal");
    if (modal && modal.style.display !== "flex") {
        modal.style.display = "flex"; // 필요한 모달만 열기
        console.log("모달이 열렸습니다.");
    } else if (!modal) {
        console.error("모달 요소를 찾을 수 없습니다.");
    }
}

// 모달 닫기
function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = "none";
        console.log("모달이 닫혔습니다.");
    } else {
        console.error(`모달 ID ${modalId}를 찾을 수 없습니다.`);
    }
}

// 담당자 검색 기능 (AJAX로 서버에서 데이터 가져오기)
function searchManager() {
    var custId = document.getElementById('custId').value;
    var searchValue = document.querySelector('#searchModal .search-bar input').value.trim();

    if (!custId) {
        console.error("고객 ID가 존재하지 않습니다.");
        return;
    }

    console.log(`검색 요청: 고객 ID = ${custId}, 검색어 = ${searchValue}`);

    // AJAX 요청
    fetch(`/customer/detail/${custId}/searchManager?name=${searchValue}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`서버 오류 발생: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("검색 결과:", data);
            var resultListDiv = document.querySelector('#searchModal .result-list');
            resultListDiv.innerHTML = ''; // 기존 검색 결과 초기화

            if (data.length === 0) {
                resultListDiv.innerHTML = '<div>검색 결과가 없습니다.</div>';
            } else {
                data.forEach(function (manager) {
                    // 동적으로 검색 결과 버튼을 추가
                    var resultButton = document.createElement('button');
                    resultButton.className = 'result-btn';
                    resultButton.innerHTML = `
                        <span class="name">${manager.user_name}</span>
                        <span class="info">${manager.user_id} / ${manager.user_telno}</span>
                    `;
                    resultButton.onclick = function () {
                        selectManager(manager);
                    };
                    resultListDiv.appendChild(resultButton);
                });
            }
        })
        .catch(error => {
            console.error('검색 중 오류 발생:', error);
            var resultListDiv = document.querySelector('#searchModal .result-list');
            resultListDiv.innerHTML = '<div>오류가 발생했습니다. 다시 시도해주세요.</div>';
        });
}

// 담당자 선택 기능
function selectManager(manager) {
    console.log("선택된 담당자:", manager);

    // 부모 페이지의 필드에 선택한 담당자 정보 반영
    document.querySelector('.searchManager').value = manager.user_name;
    document.querySelector('input[name="user_id"]').value = manager.user_id;
    document.querySelector('input[name="user_telno"]').value = manager.user_telno;

    var deptPositionInput = document.querySelector('input[name="user_dept_Position"]');
    if (deptPositionInput) {
        deptPositionInput.value = manager.dept_name + ' / ' + manager.position_name;
    }

    // 모달창 닫기
    closeModal('searchModal');
}

// ===================================================================================================
// 변경 버튼
// 수정 내역을 저장할 객체
let customerRevisions = {};

// 현재 시간 형식 함수 (yyyy-MM-dd HH:mm)
function getCurrentTimestamp() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const date = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${date} ${hours}:${minutes}`;
}

// textarea를 업데이트하는 함수
function updateTextarea(custId) {
    const revisionHistory = document.getElementById('revisionHistory');
    const revisions = customerRevisions[custId] || [];
    revisionHistory.value = revisions.join('\n');
}

// 수정 내역을 저장하는 함수
function saveToLocalStorage() {
    localStorage.setItem('customerRevisions', JSON.stringify(customerRevisions));
}

// 수정 내역을 로드하는 함수
function loadFromLocalStorage() {
    const savedData = localStorage.getItem('customerRevisions');
    if (savedData) {
        customerRevisions = JSON.parse(savedData);
    }
}

// 변경 버튼 클릭 이벤트 등록
document.addEventListener("DOMContentLoaded", function () {
    // 페이지 로드 시 수정 내역 로드
    loadFromLocalStorage();

    // 고객 ID 가져오기
    const custId = document.getElementById("custId").value;

    // 해당 고객의 수정 내역 복원
    updateTextarea(custId);

    document.querySelector(".edit-btn").addEventListener("click", function (event) {
        event.preventDefault();

        // 수정한 사용자 정보 가져오기
        const userId = document.querySelector('input[name="user_id"]').value;

        // 필드 값 가져오기
        const telno = document.querySelector('input[name="custTelno"]').value;
        const email = document.querySelector('input[name="custEmail"]').value;
        const addr = document.querySelector('input[name="custAddr"]').value;
        const custOccpTyCdElement = document.querySelector('select[name="custOccpTyCd"]');
        const custOccpTyCd = custOccpTyCdElement?.value || '미정';

        // 수정 내역 추가
        const currentTimestamp = getCurrentTimestamp();
        const newEntry = `수정일자 : ${currentTimestamp} / 담당자ID: ${userId}`;

        // 고객별 수정 내역 관리
        if (!customerRevisions[custId]) {
            customerRevisions[custId] = [];
        }
        customerRevisions[custId].unshift(newEntry); // 가장 최근 내역을 상단에 추가

        // textarea와 localStorage 업데이트
        updateTextarea(custId);
        saveToLocalStorage();
    });
});

// =========================================================================
// 인쇄
function DetailPrint() {
    // 모달 안에서 인쇄할 영역을 선택
    var printContents = document.getElementById('leftSide').outerHTML;
    var originalContents = document.body.innerHTML;

    // 페이지의 내용을 인쇄할 내용으로 변경
    document.body.innerHTML = printContents;

    // 인쇄
    window.print();

    // 원래 페이지로 복구
    document.body.innerHTML = originalContents;
}
// =========================================================================
// 목록
function goList() {
    // Spring Controller의 목록 페이지 URL로 이동
    window.location.href = "/customer/list";
}