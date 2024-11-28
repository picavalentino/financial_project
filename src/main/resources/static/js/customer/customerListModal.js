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
        openModal("insertModal");
    });
});

function openModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.style.display = "flex"; // 모달을 중앙에 배치
}

document.addEventListener("DOMContentLoaded", function () {
    var submitButton = document.getElementById("submitCustomer");

    submitButton.addEventListener("click", function (e) {
        // 기본 폼 제출 동작 방지
        e.preventDefault();

        // 유효성 검사
        if (!validateForm()) {
            return; // 유효성 검사 실패 시 서버 요청 중단
        }

        // 입력 값 가져오기
        var custNm = document.getElementById("custNm").value.trim();
        var custRrn = document.getElementById("custRrn").value.trim();
        var custTelno = document.getElementById("custTelno").value.trim();
        var custEmail = document.getElementById("custEmail").value.trim();
        var custOccpTyCd = document.getElementById("custOccpTyCd").value;
        var custAddr = document.getElementById("custAddr").value.trim();

        // 로그인 정보 가져오기
        const staffId = document.querySelector('input[name="staffId"]').value;

        // 고객 정보 객체 생성 (custId는 서버에서 생성되므로 제외)
        var customerData = {
            custNm: custNm,
            custRrn: custRrn,
            custTelno: custTelno,
            custEmail: custEmail,
            custOccpTyCd: custOccpTyCd,
            custAddr: custAddr,
            users: {
                user_id: staffId,
            },
        };

        const csrfToken = $('meta[name="_csrf"]').attr("content");
        const csrfHeader = $('meta[name="_csrf_header"]').attr("content");

        // 비동기적으로 데이터를 서버에 전송
        fetch("/customer/list/insert", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken,
            },
            body: JSON.stringify(customerData),
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    alert("고객이 성공적으로 등록되었습니다.");
                    // 모달 닫기 및 폼 초기화
                    closeModal("insertModal");
                    document.getElementById("customerForm").reset();
                } else {
                    alert("고객 등록에 실패했습니다: " + data.message);
                }
            })
            .catch((error) => {
                console.error("고객 등록 중 오류가 발생했습니다:", error);
                alert("오류가 발생했습니다. 다시 시도해주세요.");
            });
    });
});

// 폼 유효성 검사 함수
function validateForm() {
    // 각 필드 값 가져오기
    const custNm = document.getElementById("custNm").value.trim();
    const custRrn = document.getElementById("custRrn").value.trim();
    const custTelno = document.getElementById("custTelno").value.trim();
    const custEmail = document.getElementById("custEmail").value.trim();
    const custOccpTyCd = document.getElementById("custOccpTyCd").value;
    const custAddr = document.getElementById("custAddr").value.trim();

    // 정규식 패턴
    const rrnPattern = /^\d{6}-\d{7}$/; // 주민등록번호 형식
    const telPattern = /^010-\d{4}-\d{4}$/; // 휴대폰 번호 형식

    // 유효성 검사
    if (custNm.length < 2 || custNm.length > 50) {
        alert("이름은 2자 이상 50자 이하로 입력해주세요.");
        return false;
    }

    if (!rrnPattern.test(custRrn)) {
        alert("주민등록번호는 '123456-1234567' 형식으로 입력해주세요.");
        return false;
    }

    if (!telPattern.test(custTelno)) {
        alert("전화번호는 '010-1234-5678' 형식으로 입력해주세요.");
        return false;
    }

    if (!custEmail.includes("@") || custEmail.length < 5) {
        alert("올바른 이메일 주소를 입력해주세요.");
        return false;
    }

    if (custOccpTyCd === "") {
        alert("직업을 선택해주세요.");
        return false;
    }

    if (custAddr === "") {
        alert("주소를 입력해주세요.");
        return false;
    }

    return true; // 모든 유효성 검사를 통과한 경우
}

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
document.addEventListener("DOMContentLoaded", function() {
    // 기본 메시지 정의
    const defaultMessage = `[Web 발신]
안녕하십니까 GS-BANK입니다.
고객님 평안한 하루 보내고 계십니까
저는 퇴근 좀 시켜주세요
제발...
감사합니다.`;

    // 이벤트 리스너 추가
    document.querySelector('.message-button').addEventListener('click', function() {
        openMessageModal(defaultMessage);
    });
    document.getElementById('birthday-button').addEventListener('click', showBirthdayCustomers);
    document.getElementById('general-button').addEventListener('click', function() {
        showSelectedCustomers();
        resetMessageBox(defaultMessage);
    });

    // 페이지 로드 시 기본 메시지 박스 초기화
    resetMessageBox(defaultMessage);
});

// 생일인 고객들만 태그로 표시하고 생일 메시지를 준비하는 함수 (생일 버튼 클릭 시)
function showBirthdayCustomers() {
    const tagBody = document.getElementById('tagContainer');
    const allCustomerData = JSON.parse(localStorage.getItem('allCustomerData')) || {};
    const today = new Date();
    const monthDay = `${String(today.getMonth() + 1).padStart(2, '0')}${String(today.getDate()).padStart(2, '0')}`;

    // 생일 고객 필터링
    const birthdayCustomers = Object.values(allCustomerData).filter(customer => {
        if (customer.birthDate && customer.birthDate.length >= 8) {
            const customerMonthDay = customer.birthDate.slice(4, 8); // MMDD 부분 추출
            return customerMonthDay === monthDay;
        }
        return false;
    });

    // 태그 컨테이너 초기화 및 데이터 추가
    tagBody.innerHTML = '';
    if (birthdayCustomers.length === 0) {
        tagBody.innerHTML = '<p id="noCustomerMessage">오늘 생일인 고객이 없습니다.</p>';
    } else {
        birthdayCustomers.forEach((customer) => {
            const newRow = document.createElement('div');
            newRow.className = 'tag-btn';
            newRow.innerHTML = `
                ${customer.custNm} (${customer.custTelno})
                <span class="close-tag" onclick="removeTag(event)">X</span>
            `;
            tagBody.appendChild(newRow);
        });
    }

    // 생일 메시지 준비 및 텍스트박스에 설정
    const messageBox = document.querySelector('.message-box textarea');
    if (messageBox) {
        messageBox.value = `[Web 발신]
안녕하세요, ${today.getFullYear()}년 생일을 맞이하신 고객님!
GS-BANK에서 생일을 진심으로 축하드립니다.
건강과 행복이 가득한 한 해 되시기를 기원합니다.
감사합니다.
        `;
    }
}

// 일반 버튼 클릭 시 선택된 고객 복원 및 메시지 상태 복원 함수
function showSelectedCustomers() {
    openMessageModal();

    // 메시지 박스를 기본 메시지로 리셋하는 것은 resetMessageBox()에서 진행됨
}

// 메시지 박스 초기화 함수
function resetMessageBox(message) {
    const messageBox = document.querySelector('.message-box textarea');
    if (messageBox) {
        messageBox.value = message;
    }
}

// 모달 열기 및 선택된 데이터 출력 (기존 코드 유지)
function openMessageModal(defaultMessage) {
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

    // 메시지 박스를 기본 메시지로 초기화
    resetMessageBox(defaultMessage);

    // 모달 열기
    document.getElementById('messageModal').style.display = 'flex';
}

// 태그 제거 함수 (기존 코드 유지)
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
            noCustomerMessage.textContent = '선택된 고객이 없습니다.';
            tagContainer.appendChild(noCustomerMessage);
        }
    }
}

// ===============================================================
// 찐 메세지 발송
function sendMessage() {
    const tagBody = document.getElementById('tagContainer');
    const messageBox = document.querySelector('.message-box textarea');

    if (!tagBody || !messageBox) {
        alert('메시지를 보낼 데이터가 없습니다.');
        return;
    }

    // 선택된 고객 정보 추출
    const tags = tagBody.querySelectorAll('.tag-btn');
    const selectedCustomers = Array.from(tags).map(tag => {
        const customerInfo = tag.textContent.trim().split(' (');
        return {
            custNm: customerInfo[0],
            custTelno: customerInfo[1]?.replace(')', '').trim() || ''
        };
    });

    if (selectedCustomers.length === 0) {
        alert('선택된 고객이 없습니다.');
        return;
    }

    // 메시지 내용 추출
    const messageContent = messageBox.value.trim();
    if (!messageContent) {
        alert('메시지 내용이 비어 있습니다.');
        return;
    }

    // 최종 메시지 준비
    let finalMessage = `--- 메시지 발송 ---\n`;
    selectedCustomers.forEach(customer => {
        finalMessage += `고객: ${customer.custNm} / ${customer.custTelno}`;
    });
    finalMessage += `${messageContent}`;

    // 발송 전 확인
    console.log(finalMessage);
    alert('메시지가 콘솔에 출력되었습니다. 실제 발송 로직을 추가하세요.');
}

