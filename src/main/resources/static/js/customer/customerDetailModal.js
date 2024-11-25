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

/*document.getElementById("openSearchModalBtn").addEventListener("click", (event) => {
    event.preventDefault(); // 기본 동작 방지
    openSearchModal();
});*/

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
document.addEventListener("click", function(event) {
    if (event.target.classList.contains("edit-btn")) {
        event.preventDefault();
        updateCustomerInfo();
    }
});

document.addEventListener("DOMContentLoaded", function() {
    document.querySelector(".edit-btn").addEventListener("click", function() {
        // 폼 데이터를 가져오기
        var custId = document.getElementById("custId").value; // hidden input에서 가져옴

        var form = document.getElementById("customerUpdateForm");

        if (!form) {
            console.error("customer 폼이 존재하지 않습니다.");
            return;
        }

        // 필드 값 가져오기
        const custOccpTyCdElement = document.querySelector('select[name="custOccpTyCd"]');
        let custOccpTyCd = custOccpTyCdElement?.value;

        // custOccpTyCd 값이 비어 있거나 null인 경우, 기본값 설정
        if (!custOccpTyCd) {
            custOccpTyCd = custOccpTyCdElement?.getAttribute('th:selected');
        }
        var formData = new FormData(form);

        // 폼 데이터를 JSON으로 변환
        var updatedCustomerData = {
            custId: custId, // 수정할 고객 ID
            custTelno: document.querySelector('input[name="custTelno"]').value,
            custEmail: document.querySelector('input[name="custEmail"]').value,
            custOccpTyCd: custOccpTyCd,
            custAddr: document.querySelector('input[name="custAddr"]').value,
            users: { // users 객체로 user_id 포함
                   user_id: document.querySelector('input[name="user_id"]').value
               }
        };

        formData.forEach((value, key) => {
            updatedCustomerData[key] = value;
        });

        // 서버로 PUT 요청 전송
        fetch(`/customer/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedCustomerData)
           })
        .then(response => {
            if (response.ok) {
                alert('고객 정보가 성공적으로 수정되었습니다.');
                // 수정 완료 다시 정보페이지
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