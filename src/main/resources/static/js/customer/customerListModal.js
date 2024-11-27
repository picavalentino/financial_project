/* 모달 닫기 */
function closeModal(modalId) {
  document.getElementById(modalId).style.display = 'none';
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

function openModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.style.display = "flex"; // 모달을 중앙에 배치
}

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
/* checkSelected 함수 전역 범위에 정의 */
window.checkSelected = function () {
  const allCheckboxes = document.querySelectorAll('input[name="selectedCustomers"]');
  const checkedCheckboxes = document.querySelectorAll('input[name="selectedCustomers"]:checked');
  const masterCheckbox = document.querySelector('input[name="selectAllCustomers"]');

  // 현재 페이지의 모든 체크박스 상태 확인
  const currentPageAllSelected = allCheckboxes.length > 0 && allCheckboxes.length === checkedCheckboxes.length;

  // 모든 페이지의 체크박스 상태를 가져오기
  const savedState = JSON.parse(localStorage.getItem('selectedCustomersState')) || {};
  const allSavedSelected = Object.values(savedState).every((isChecked) => isChecked);

  // "전체 선택" 체크박스는 모든 페이지의 상태에 따라 활성화
  masterCheckbox.checked = currentPageAllSelected && allSavedSelected;

  // 상태 저장
  saveSelectedCustomers();
};

/* 전체선택 체크박스 동작 */
window.selectAll = function (masterCheckbox) {
  const allCheckboxes = document.querySelectorAll('input[name="selectedCustomers"]');
  const isChecked = masterCheckbox.checked;

  // 현재 페이지의 모든 체크박스 상태를 마스터 체크박스 상태에 동기화
  allCheckboxes.forEach((checkbox) => {
    checkbox.checked = isChecked;
  });

  // 모든 페이지의 체크박스 상태를 동기화
  selectAllAcrossPages(isChecked);
};

/* DOM 로드 시 초기화 */
document.addEventListener('DOMContentLoaded', () => {
  const masterCheckbox = document.querySelector('input[name="selectAllCustomers"]');
  const allCheckboxes = document.querySelectorAll('input[name="selectedCustomers"]');

  if (!masterCheckbox || allCheckboxes.length === 0) {
    console.warn('Master checkbox or individual checkboxes not found.');
    return;
  }

  // 상태 복원
  restoreCheckboxState();

  // 마스터 체크박스 클릭 이벤트 등록
  masterCheckbox.addEventListener('click', () => {
    selectAll(masterCheckbox);
  });

  // 개별 체크박스 클릭 이벤트 등록
  allCheckboxes.forEach((checkbox) => {
    checkbox.addEventListener('click', () => {
      checkSelected();
    });
  });
});

/* 모든 페이지 기준으로 "전체 선택" 상태를 업데이트 */
function updateMasterCheckboxState() {
  const savedState = JSON.parse(localStorage.getItem('selectedCustomersState')) || {};
  const masterCheckbox = document.querySelector('input[name="selectAllCustomers"]');
  const allCheckboxes = document.querySelectorAll('input[name="selectedCustomers"]');

  // 현재 페이지의 모든 체크박스가 선택되었는지 확인
  const currentPageAllSelected = allCheckboxes.length > 0 && Array.from(allCheckboxes).every((checkbox) => checkbox.checked);

  // 모든 페이지의 체크박스 상태 확인
  const allSavedSelected = Object.values(savedState).every((isChecked) => isChecked);

  // "전체 선택" 체크박스는 모든 페이지 기준으로 활성화
  masterCheckbox.checked = currentPageAllSelected && allSavedSelected;
}

/* 모든 페이지의 체크박스 상태를 선택/해제 */
function selectAllAcrossPages(isChecked) {
  const savedState = JSON.parse(localStorage.getItem('selectedCustomersState')) || {};

  // 모든 체크박스 상태를 선택/해제
  Object.keys(savedState).forEach((key) => {
    savedState[key] = isChecked;
  });

  // 현재 페이지 체크박스 상태 업데이트
  const allCheckboxes = document.querySelectorAll('input[name="selectedCustomers"]');
  allCheckboxes.forEach((checkbox) => {
    savedState[checkbox.value] = isChecked;
  });

  // 저장
  localStorage.setItem('selectedCustomersState', JSON.stringify(savedState));
}

// 체크박스 상태를 LocalStorage에 저장
function saveSelectedCustomers() {
  const allCheckboxes = document.querySelectorAll('input[name="selectedCustomers"]');
  const state = JSON.parse(localStorage.getItem('selectedCustomersState')) || {};

  allCheckboxes.forEach((checkbox) => {
    state[checkbox.value] = checkbox.checked;
  });

  localStorage.setItem('selectedCustomersState', JSON.stringify(state));
}

/* 로컬스토리지에서 체크박스 상태 복원 */
function restoreCheckboxState() {
  const savedState = JSON.parse(localStorage.getItem('selectedCustomersState')) || {};
  const allCheckboxes = document.querySelectorAll('input[name="selectedCustomers"]');

  // 현재 페이지의 체크박스 상태 복원
  allCheckboxes.forEach((checkbox) => {
    if (savedState[checkbox.value] !== undefined) {
      checkbox.checked = savedState[checkbox.value];
    }
  });

  // 마스터 체크박스 상태 동기화
  updateMasterCheckboxState();
}

// 페이지 로드 시 현재 페이지 데이터를 LocalStorage에 추가
function saveAllCustomerData() {
  const allCustomerRows = document.querySelectorAll('tr[data-id]');
  const existingData = JSON.parse(localStorage.getItem('allCustomerData')) || {};

  allCustomerRows.forEach((row) => {
    const custId = row.dataset.custid;
    existingData[custId] = {
      custId: row.dataset.custid,
      custNm: row.dataset.custnm,
      birthDate: row.dataset.birthdate,
      custTelno: row.dataset.custtelno,
      custEmail: row.dataset.custemail,
      custCreateAt: row.dataset.custcreateat,
      custStateCd: row.dataset.custstatecd,
      usersName: row.dataset.usersname || '없음',
    };
  });

  localStorage.setItem('allCustomerData', JSON.stringify(existingData));
}

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
// 인쇄페이지로 이동

// 모달 열기 및 선택된 데이터 출력
function openPrintModal() {
  const printTableBody = document.getElementById('print-table-body');
  const savedState = JSON.parse(localStorage.getItem('selectedCustomersState')) || {};
  const allCustomerData = JSON.parse(localStorage.getItem('allCustomerData')) || {};
  const selectedData = [];

  // LocalStorage에서 선택된 데이터만 가져오기
  Object.keys(savedState)
    .filter((key) => savedState[key]) // 체크된 상태만 필터링
    .forEach((custId) => {
      const customer = allCustomerData[custId];
      if (customer) {
         selectedData.push(customer); // 데이터가 있는 경우에만 추가
       }
    });

  // 테이블 초기화 및 데이터 추가
  printTableBody.innerHTML = '';
  if (selectedData.length === 0) {
    printTableBody.innerHTML = '<tr><td colspan="8">선택된 데이터가 없습니다.</td></tr>';
  } else {
    selectedData.forEach((customer) => {
      const newRow = document.createElement('tr');
      newRow.innerHTML = `
        <td>${customer.custId}</td>
        <td>${customer.custNm}</td>
        <td>${customer.birthDate}</td>
        <td>${customer.custTelno}</td>
        <td>${customer.custEmail}</td>
        <td>${customer.usersName}</td>
        <td>${customer.custStateCd}</td>
        <td>${customer.custCreateAt}</td>
      `;
      printTableBody.appendChild(newRow);
    });
  }

  // 모달 열기
  document.getElementById('printModal').style.display = 'flex';
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
  saveAllCustomerData(); // 현재 페이지 데이터를 LocalStorage에 저장
  restoreCheckboxState(); // 체크박스 상태 복원

  const checkboxes = document.querySelectorAll('input[name="selectedCustomers"]');
  checkboxes.forEach((checkbox) => {
    checkbox.addEventListener('change', saveSelectedCustomers); // 체크박스 변경 시 상태 저장
  });
});

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
  saveAllCustomerData(); // 현재 페이지 데이터를 LocalStorage에 저장
  restoreCheckboxState(); // 체크박스 상태 복원

  const checkboxes = document.querySelectorAll('input[name="selectedCustomers"]');
  checkboxes.forEach((checkbox) => {
    checkbox.addEventListener('change', saveSelectedCustomers); // 체크박스 변경 시 상태 저장
  });
});

// =======================================================================================================================
//메세지 발송 페이지로 이동
// 모달 열기 및 선택된 데이터 출력
function openMessageModal() {
    // LocalStorage에서 선택된 고객 데이터 불러오기
    const tagBody = document.getElementById('tagContainer');
    const savedState = JSON.parse(localStorage.getItem('selectedCustomersState')) || {};
    const allCustomerData = JSON.parse(localStorage.getItem('allCustomerData')) || {};
    const selectedData = [];

    // LocalStorage에서 선택된 데이터만 필터링
    Object.keys(savedState)
        .filter((key) => savedState[key]) // 체크된 상태만 필터링
        .forEach((custId) => {
            const customer = allCustomerData[custId];
            if (customer) {
                selectedData.push(customer); // 데이터가 있는 경우에만 추가
            }
        });

    // 태그 컨테이너 초기화 및 데이터 추가
    tagBody.innerHTML = '';
    if (selectedData.length === 0) {
        tagBody.innerHTML = '<p id="noCustomerMessage">선택된 데이터가 없습니다. 고객을 선택하세요.</p>';
    } else {
        selectedData.forEach((customer) => {
            const newRow = document.createElement('div');
            newRow.className = 'tag-btn';
            newRow.innerHTML = `
                ${customer.custNm} (${customer.custTelno})
                <span class="close-tag" onclick="removeTag(event)">X</span>
            `;
            tagBody.appendChild(newRow);
        });
    }

    // 모달 열기
    document.getElementById('MessageModal').style.display = 'flex';
}

// 태그 제거 함수
function removeTag(event) {
    event.stopPropagation(); // 클릭 이벤트 전파 방지
    const tagButton = event.target.closest('.tag-btn'); // 부모 태그 버튼 찾기
    if (tagButton) {
        tagButton.remove(); // 태그 버튼 삭제

        // 선택된 고객이 없으면 안내 메시지 표시
        const tagContainer = document.getElementById('tagContainer');
        if (tagContainer.querySelectorAll('.tag-btn').length === 0) {
            const noCustomerMessage = document.createElement('p');
            noCustomerMessage.id = 'noCustomerMessage';
            noCustomerMessage.textContent = '선택된 데이터가 없습니다. 고객을 선택하세요.';
            tagContainer.appendChild(noCustomerMessage);
        }
    }
}



