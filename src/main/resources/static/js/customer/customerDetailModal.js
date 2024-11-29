// 모든 모달을 닫는 함수
function closeAllModals() {
    const modals = document.querySelectorAll(".modal-overlay");
    modals.forEach(modal => {
        modal.style.display = "none";
    });
}
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
    const userNameInput = document.querySelector('.searchManager');
    const userIdInput = document.querySelector('input[name="user_id"]');
    const userTelnoInput = document.querySelector('input[name="user_telno"]');
    const deptPositionInput = document.querySelector('input[name="user_dept_Position"]');

    if (userNameInput) userNameInput.value = manager.user_name;
    if (userIdInput) userIdInput.value = manager.user_id;
    if (userTelnoInput) userTelnoInput.value = manager.user_telno;
    if (deptPositionInput) {
        deptPositionInput.value = `${manager.dept_name} / ${manager.position_name}`;
    }

    // "변경" 버튼 활성화 트리거
    const editButton = document.querySelector('.edit-btn');
    if (editButton) {
        const event = new Event('input'); // 입력 이벤트 트리거
        userNameInput.dispatchEvent(event);
    }

    // 모달창 닫기
    closeModal('searchModal');
}


// ===================================================================================================
// 변경 버튼 [후]
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("customerUpdateForm");
    const editButton = document.querySelector(".edit-btn");
    const custId = document.getElementById("custId").value;
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute("content") || "";
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute("content") || "";

    // 초기화
    initForm();

    // 변경 여부 확인 함수
    function isFormChanged() {
        const originalData = new FormData(form);
        const currentData = new FormData(form);

        for (let [key, value] of originalData.entries()) {
            if (currentData.get(key) !== value) {
                return true;
            }
        }
        return false;
    }

    // 폼 초기화 및 변경 감지 리스너 추가
    function initForm() {
        form.querySelectorAll("input, select, textarea").forEach((input) => {
            if (!input.disabled) {
                input.addEventListener("input", () => {
                    editButton.disabled = !isFormChanged();
                });
            }
        });
    }

    // 수정 내역 텍스트 업데이트
    function updateTextarea(revisions) {
        const revisionHistory = document.getElementById("revisionHistory");
        revisionHistory.value = revisions
            .map(rev => `수정일자: ${rev.timestamp} / 수정자: ${rev.user}\n내용: ${rev.detail}`)
            .join("\n\n");
    }

    // 서버에서 수정 내역 로드
    function loadRevisionHistory() {
        fetch(`/customer/update/history/${custId}`)
            .then(response => {
                if (!response.ok) throw new Error("수정 내역 로드 실패");
                return response.json();
            })
            .then(revisions => updateTextarea(revisions))
            .catch(error => console.error("수정 내역 로드 중 오류 발생:", error));
    }

    // 수정 내역 저장
    function saveRevisionDetail(detail) {
        const currentTimestamp = new Date().toISOString();
        const revision = {
            custId: custId,
            user: document.querySelector('input[name="staffId"]').value,
            detail: detail,
            timestamp: currentTimestamp
        };

        // 서버로 수정 내역 저장 요청
        fetch("/customer/update/revision", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken,
            },
            body: JSON.stringify(revision)
        }).then(response => {
            if (response.ok) {
                console.log("수정 내역이 성공적으로 저장되었습니다.");
            } else {
                console.error("수정 내역 저장 실패");
            }
        }).catch(error => console.error("수정 내역 저장 중 오류 발생:", error));
    }

    // 변경 버튼 클릭 이벤트
    editButton.addEventListener("click", function (event) {
        event.preventDefault();

        // 입력 데이터 가져오기
        const telno = document.querySelector('input[name="custTelno"]').value;
        const email = document.querySelector('input[name="custEmail"]').value;
        const addr = document.querySelector('input[name="custAddr"]').value;
        const custOccpTyCd = document.querySelector('select[name="custOccpTyCd"]')?.value || "미정";

        // 수정 내역 작성
        const updateDetail = `
            전화번호: ${telno}
            이메일: ${email}
            주소: ${addr}
            직업 코드: ${custOccpTyCd}
        `.trim();

        // 수정 내역 저장
        saveRevisionDetail(updateDetail);

        // 폼 데이터 구성
        const updatedCustomerData = {
            custId: custId,
            custTelno: telno,
            custEmail: email,
            custAddr: addr,
            custOccpTyCd: custOccpTyCd
        };

        // 서버로 PUT 요청
        fetch("/customer/update", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken,
            },
            body: JSON.stringify(updatedCustomerData)
        })
            .then(response => {
                if (response.ok) {
                    alert("고객 정보가 성공적으로 수정되었습니다.");
                    loadRevisionHistory(); // 최신 수정 내역 로드
                } else {
                    return response.text().then(errorMessage => {
                        throw new Error(errorMessage);
                    });
                }
            })
            .catch(error => {
                console.error("수정 요청 중 오류 발생:", error);
                alert("수정 요청에 실패했습니다.");
            });
    });

    // 초기 수정 내역 로드
    loadRevisionHistory();
});


// 변경 버튼 [전]
/*
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('customerUpdateForm');
    const editButton = document.querySelector('.edit-btn');
    const originalData = new FormData(form);

    // 변경 여부 확인 함수
    function isFormChanged() {
        const currentData = new FormData(form);
        for (let [key, value] of originalData.entries()) {
            if (currentData.get(key) !== value) {
                return true;
            }
        }
        return false;
    }

    // 입력값 변경 시 버튼 상태 업데이트
    function addChangeListeners() {
        form.querySelectorAll('input, select, textarea').forEach((input) => {
            if (!input.disabled) { // 비활성화된 필드 제외
                input.addEventListener('input', () => {
                    editButton.disabled = !isFormChanged(); // 변경 여부에 따라 버튼 활성화/비활성화
                });
            }
        });
    }

    // 초기화
    addChangeListeners();
});

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
document.addEventListener("DOMContentLoaded", function () {
    // 수정 내역 로드
    loadFromLocalStorage();

    // 고객 ID 가져오기
    const custId = document.getElementById("custId").value;

    // 고객별 수정 내역 복원
    updateTextarea(custId);

    // 변경 버튼 클릭 이벤트 등록
    document.querySelector(".edit-btn").addEventListener("click", function (event) {
        event.preventDefault();


        // 필드 값 가져오기
        const telno = document.querySelector('input[name="custTelno"]').value;
        const email = document.querySelector('input[name="custEmail"]').value;
        const addr = document.querySelector('input[name="custAddr"]').value;
        const custOccpTyCdElement = document.querySelector('select[name="custOccpTyCd"]');
        let custOccpTyCd = custOccpTyCdElement?.value;

        // custOccpTyCd 값이 비어 있거나 null인 경우, 기본값 설정
        if (!custOccpTyCd) {
            custOccpTyCd = custOccpTyCdElement?.getAttribute('th:selected') || '미정';
        }

         // 수정한 사용자 정보 가져오기
        const staffId = document.querySelector('input[name="staffId"]').value;
        const userId = document.querySelector('input[name="user_id"]').value;

        // 수정 내역 추가
        const currentTimestamp = getCurrentTimestamp();
        const newEntry = `수정일자 : ${currentTimestamp} / by - ${staffId}`;

        // 고객별 수정 내역 관리
        if (!customerRevisions[custId]) {
            customerRevisions[custId] = [];
        }
        customerRevisions[custId].unshift(newEntry); // 가장 최근 내역을 상단에 추가

        // textarea와 localStorage 업데이트
        updateTextarea(custId);
        saveToLocalStorage();

        // 폼 데이터 구성
        const updatedCustomerData = {
            custId: custId, // 수정할 고객 ID
            custTelno: telno,
            custEmail: email,
            custOccpTyCd: custOccpTyCd,
            custAddr: addr,
            users: { // users 객체로 user_id 포함
                user_id: userId
            }
        };
         const csrfToken = $('meta[name="_csrf"]').attr('content');
         const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

        // 서버로 PUT 요청 전송
        fetch(`/customer/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken,
            },
            body: JSON.stringify(updatedCustomerData)
        })
        .then(response => {
            if (response.ok) {
                alert('고객 정보가 성공적으로 수정되었습니다.');
                // 수정 완료 후 상세 페이지로 이동
                window.location.href = `/customer/detail/${custId}`;
            } else {
                return response.text().then((errorMessage) => {
                    throw new Error(errorMessage);
                });
            }
        })
        .catch(error => {
            console.error('수정 요청 중 오류 발생:', error);
            alert('담당자를 입력해주세요');
        });
    });
});
*/

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
// =========================================================================
// 상품설계 페이지로
function goPromotion() {
    // Spring Controller의 목록 페이지 URL로 이동
    window.location.href = "/promotion/list";
}