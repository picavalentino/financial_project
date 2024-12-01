// 인쇄기능
document.querySelector('.btn-print').addEventListener('click', function () {
                    window.print();
                });

document.addEventListener('DOMContentLoaded', function () {

    // 날짜 가져오기
    function getTodayDate() {
        const today = new Date();
        today.setMinutes(today.getMinutes() - today.getTimezoneOffset()); // UTC -> 로컬 시간으로 변환
        return today.toISOString().split('T')[0]; // yyyy-mm-dd
    }

    // 날짜 필드 설정 (오늘 날짜)
    const dsgnCreateAt = document.getElementById('dsgnCreateAt');
    const dsgnUpdateAt = document.getElementById('dsgnUpdateAt');
    const savgStrtDt = document.getElementById('savgStrtDt'); // 시작일자

    if (dsgnCreateAt) dsgnCreateAt.value = getTodayDate();
    if (dsgnUpdateAt) dsgnUpdateAt.value = getTodayDate();

    // 시작일자 기본값과 최소값 설정
    if (savgStrtDt) {
        const today = getTodayDate();
        savgStrtDt.value = today; // 기본값 설정
        savgStrtDt.min = today;  // 최소값 설정
    }


    // 만기일자 계산
    const savgGoalPrd = document.getElementById('savgGoalPrd'); // 목표기간
    const savgMtrDt = document.getElementById('savgMtrDt'); // 만기일자

    // 만기일자 계산 함수
    function calculateMaturityDate() {
        const startDateValue = savgStrtDt.value; // 시작일자 값
        const goalPeriodValue = parseInt(savgGoalPrd.value, 10); // 목표기간 값 (개월)

        console.log("시작일자 값:", startDateValue); // 시작일자 확인
        console.log("목표기간 값:", goalPeriodValue); // 목표기간 확인

        if (!startDateValue || isNaN(goalPeriodValue) || goalPeriodValue <= 0) {
            savgMtrDt.value = ''; // 입력값이 없거나 잘못된 경우 초기화
            return;
        }

        const startDate = new Date(startDateValue);
        startDate.setMonth(startDate.getMonth() + goalPeriodValue); // 목표기간을 더해 만기일 계산
        savgMtrDt.value = startDate.toISOString().split('T')[0]; // yyyy-mm-dd 형식으로 업데이트
        console.log("계산된 만기일:", savgMtrDt.value); // 계산된 만기일 확인
    }

    // 시작일자 변경 시 만기일 계산
    savgStrtDt.addEventListener('change', () => {
        console.log("시작일자가 변경되었습니다."); // 시작일 변경 확인
        calculateMaturityDate();
    });

    // 목표기간 변경 시 만기일 계산
    savgGoalPrd.addEventListener('input', () => {
        console.log("목표기간이 변경되었습니다."); // 목표기간 변경 확인
        calculateMaturityDate();
    });


    // 불입금액 버튼 처리
    const savgCircleAmtInput = document.getElementById('savgCircleAmt');
    const savgCircleAmtButtons = document.querySelectorAll('.button-plus-amt');
    const savgCircleAmtReset = document.querySelector('.button-reset-amt');

    savgCircleAmtButtons.forEach(button => {
        button.addEventListener('click', () => {
            // 버튼 텍스트에서 숫자 추출 (ex: "+ 10만원" -> "10")
            const amountToAdd = parseInt(button.innerText.replace(/[^0-9]/g, '')) * 10000;
            const currentAmount = parseInt(savgCircleAmtInput.value.replace(/,/g, '')) || 0;
            const newAmount = currentAmount + amountToAdd;

            savgCircleAmtInput.value = newAmount.toLocaleString(); // 3자리 콤마 추가
        });
    });

    savgCircleAmtReset.addEventListener('click', () => {
        savgCircleAmtInput.value = '0'; // 값 초기화
    });

    // 목표기간 처리
    const savgGoalPrdInput = document.getElementById('savgGoalPrd');
    const savgGoalPrdButtons = document.querySelectorAll('.button-plus-prd');
    const savgGoalPrdReset = document.querySelector('.button-reset-prd');

    savgGoalPrdButtons.forEach(button => {
        button.addEventListener('click', () => {
            // 버튼 텍스트에서 개월 수 추출 (ex: "+ 3개월" -> "3")
            const monthsToAdd = parseInt(button.innerText.replace(/[^0-9]/g, ''));
            const currentMonths = parseInt(savgGoalPrdInput.value) || 0;
            const newMonths = currentMonths + monthsToAdd;

            // input 값 설정
            savgGoalPrdInput.value = newMonths; // 값 반영
            console.log("목표기간 변경됨:", newMonths); // 디버깅 로그

            // 만기일 계산 함수 호출
            calculateMaturityDate();
        });
    });

    savgGoalPrdReset.addEventListener('click', () => {
        // input 값 초기화
        savgGoalPrdInput.value = '0'; // 값 초기화
        console.log("목표기간 초기화됨:", 0); // 디버깅 로그

        // 만기일 계산 함수 호출
        calculateMaturityDate();
    });


    // 설계유형 선택에 따른 화면 출력
    const calculationTypeRadios = document.querySelectorAll('input[name="calculationType"]');
    const mainFormContainer = document.querySelector('.main-form-container');
    const customerContainer = document.querySelector('.customer-container');

    calculationTypeRadios.forEach(radio => {
        radio.addEventListener('change', function () {
            if (this.value === 'detailed') {
                // 정상설계 선택: 고객 컨테이너 표시 및 가로 배치 활성화
                mainFormContainer.classList.add('horizontal');
                customerContainer.classList.add('visible');
            } else {
                // 간편설계 선택: 고객 컨테이너 숨기고 세로 배치로 복귀
                mainFormContainer.classList.remove('horizontal');
                customerContainer.classList.remove('visible');
            }
        });
    });


    // 라디오 버튼 선택에 따라 출력 변경
    const defaultRadio = document.querySelector('input[name="calculationType"]:checked');
    if (defaultRadio && defaultRadio.value === 'detailed') {
        mainFormContainer.classList.add('horizontal');
        customerContainer.classList.add('visible');
    } else {
        mainFormContainer.classList.remove('horizontal');
        customerContainer.classList.remove('visible');
    }


    // 상품 모달
    const productModal = document.getElementById("productModal");
    const productTableBody = document.getElementById("productList");
    const openProductModalButton = document.getElementById("openProductModalButton");
    const closeProductModalButton = document.getElementById("closeProductModalButton");
    const searchProductButton = document.querySelector(".btn-search-prod");

    // 상품 리스트 가져오기
    async function loadProductList(productCode = '', productName = '') {
        try {
            const response = await fetch(`/promotion/cal/AcmlProductList?prodCd=${encodeURIComponent(productCode)}&prodNm=${encodeURIComponent(productName)}`);
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            const productList = await response.json();
            renderProductTable(productList);
        } catch (error) {
            console.error('Error fetching product list:', error);
            alert('상품 데이터를 불러오는 중 오류가 발생했습니다.');
        }
    }

    // 상품 모달 - 테이블 렌더링
    function renderProductTable(productList) {
        productTableBody.innerHTML = '';
        if (productList.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="3">검색 결과가 없습니다.</td>`;
            productTableBody.appendChild(row);
            return;
        }
        productList.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${product.prodCd}</td>
                <td>${product.prodNm}</td>
                <td>${product.prodSbstgTy}</td>
            `;
            // 테이블 행 클릭 이벤트 추가
            row.addEventListener('click', () => {
                // 선택한 상품 정보를 폼에 뿌려줌
                populateProductForm(product);
                // 모달 닫기
                document.getElementById('productModal').style.display = 'none';
            });
            productTableBody.appendChild(row);
        });
    }

    // 설계 폼에 데이터 뿌려주는 함수
    function populateProductForm(product) {
        // 상품설계번호, 설계유형코드, 상품코드, 상품명
        document.getElementById('prodSn').value = product.prodSn || '';
        document.getElementById('prodDsTyCd').value = product.prodDsTyCd || '';
        document.getElementById('prodCd').value = product.prodCd || '';
        document.getElementById('prodNm').value = product.prodNm || '';

        // 납입주기
        // 표시용
        document.getElementById('prodPayTy').value = product.prodPayTy || '';
        // 저장용
        document.getElementById('prodPayTyCd').value = product.prodPayTyCd || '';

        // 적용금리
        const rateSelect = document.getElementById('selectSavgAplyRate');
        const rateInput = document.getElementById('savgAplyRate');
        // 금리 선택 변경 시 동작
        rateSelect.addEventListener('change', () => {
            const selectedValue = rateSelect.value; // 선택된 값 (prodairmin 또는 prodairmax)
            if (selectedValue === 'prodairmin') {
                rateInput.value = product.prodAirMin ? `${product.prodAirMin}` : '';
            } else if (selectedValue === 'prodairmax') {
                rateInput.value = product.prodAirMax ? `${product.prodAirMax}` : '';
            } else {
                rateInput.value = ''; // 기본값 (선택되지 않은 경우)
            }
        });
        // 초기값 설정 (최소금리로 시작)
        if (rateSelect.value === 'prodairmin') {
            rateInput.value = product.prodAirMin ? `${product.prodAirMin}` : '';
        }

        // 이자과세
        const prodIntTaxTy = document.getElementById('prodIntTaxTy'); // 표시용
        const prodIntTaxRate = document.getElementById('prodIntTaxRate'); // 계산용
        const savgIntTaxTyCd = document.getElementById('savgIntTaxTyCd'); // 저장용
        prodIntTaxTy.value = `${product.prodIntTaxTy} (${product.prodIntTaxRate}%)` || ''; // 예: 일반과세 (15.4%)
        prodIntTaxRate.value = product.prodIntTaxRate || ''; // 예: 15.4
        savgIntTaxTyCd.value = product.prodIntTaxTyCd || ''; // 예: 1
    }

    // 상품 모달 열기
    openProductModalButton.addEventListener("click", () => {
        // 모달 먼저 표시
        requestAnimationFrame(() => {
            productModal.style.display = "flex";
        });

        // 데이터 로드는 비동기로 처리
        setTimeout(async () => {
            // 모달 필드 초기화
            document.getElementById('prodCdSearch').value = '';
            document.getElementById('prodNmSearch').value = '';
            await loadProductList(); // 전체 리스트 로드
        }, 0);
    });

    // 상품 모달 닫기
    closeProductModalButton.addEventListener("click", () => {
        productModal.style.display = "none";
    });

    // 상품 검색 버튼 클릭 이벤트
    searchProductButton.addEventListener("click", () => {
        const productCode = document.getElementById('prodCdSearch').value.trim();
        const productName = document.getElementById('prodNmSearch').value.trim();
        console.log(`Searching for product: code=${productCode}, name=${productName}`);
        loadProductList(productCode, productName);
    });

    // 엔터키 입력 이벤트 추가
    document.addEventListener("keydown", (event) => {
        if (event.key === "Enter") { // 엔터키 확인
            const productCode = document.getElementById('prodCdSearch').value.trim();
            const productName = document.getElementById('prodNmSearch').value.trim();
            loadProductList(productCode, productName);
        }
    });

    // 고객 모달
    const customerModal = document.getElementById("customerModal");
    const customerTableBody = document.getElementById("customerList");
    const openCustomerModalButton = document.getElementById("openCustomerModalButton");
    const closeCustomerModalButton = document.getElementById("closeCustomerModalButton");
    const searchCustomerButton = document.querySelector(".btn-search-cust");

    // 고객 리스트 가져오기
    async function loadCustomerList(custNm = '', custTelno = '') {
        try {
            // 검색 조건에 따라 API 호출 URL 생성
            const url = `/promotion/cal/userInfoList?custNm=${encodeURIComponent(custNm)}&custTelno=${encodeURIComponent(custTelno)}`;
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const userInfoList = await response.json();
            renderCustomerList(userInfoList);
        } catch (error) {
            console.error('Error fetching customer list:', error);
            alert('고객 데이터를 불러오는 중 오류가 발생했습니다.');
        }
    }

    // 고객 모달 - 테이블 렌더링
    function renderCustomerList(userInfoList) {
        customerTableBody.innerHTML = ''; // 기존 데이터를 초기화
        if (userInfoList.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="4">검색 결과가 없습니다.</td>`;
            customerTableBody.appendChild(row);
            return;
        }
        userInfoList.forEach(user => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${user.custNm}</td>
                <td>${user.custBirth}</td>
                <td>${user.custTelno}</td>
                <td>${user.userName}</td>
            `;

            // 테이블 행 클릭 이벤트 추가
            row.addEventListener('click', () => {
                // 선택한 고객 정보를 폼에 뿌려줌
                populateCustomerForm(user);
                // 모달 닫기
                customerModal.style.display = "none";
            });
            customerTableBody.appendChild(row);
        });
    }

    // 고객 폼에 데이터 뿌려주는 함수
    function populateCustomerForm(user) {
        document.getElementById('custId').value = user.custId || ''; // 고객ID
        document.getElementById('custCreateAt').value = user.custCreateAt || ''; // 작성일자
        document.getElementById('custNm').value = user.custNm || ''; // 고객명
        document.getElementById('custBirth').value = user.custBirth || ''; // 생년월일
        document.getElementById('custEmail').value = user.custEmail || ''; // 이메일
        document.getElementById('custTelno').value = user.custTelno || ''; // 전화번호
        document.getElementById('custOccp').value = user.custOccp || ''; // 직업
        document.getElementById('custAddr').value = user.custAddr || ''; // 주소
        document.getElementById('userId').value = user.userId || ''; // 담당자ID
        document.getElementById('userNm').value = user.userName || ''; // 담당자
    }

    // 고객 모달 열기 (모달이 열릴 때 데이터 로드)
    openCustomerModalButton.addEventListener("click", async () => {
        customerModal.style.display = "flex";
        // 모달 필드 초기화
        document.getElementById('custNmSearch').value = '';
        document.getElementById('custTelnoSearch').value = '';
        await loadCustomerList(); // 전체 리스트 로드
    });

    // 고객 모달 닫기
    closeCustomerModalButton.addEventListener("click", () => {
        customerModal.style.display = "none";
    });

    // 검색 버튼 클릭 이벤트
    searchCustomerButton.addEventListener("click", async () => {
        const custNm = document.getElementById('custNmSearch').value.trim();
        const custTelno = document.getElementById('custTelnoSearch').value.trim();
        await loadCustomerList(custNm, custTelno); // 검색 조건으로 데이터 로드
    });

    // 모달 외부 클릭 시 닫기 (모달 공통)
    window.addEventListener("click", (event) => {
        if (event.target === customerModal) {
            customerModal.style.display = "none";
        }
        if (event.target === productModal) {
            productModal.style.display = "none";
        }
    });


    initializeChart(); // 차트 초기화
    console.log("Chart initialized:", resultChart); // 초기화 상태 확인

    // '이자 계산' 버튼 클릭 이벤트 추가
    document.querySelector('.btn-int-cal').addEventListener('click', handleInterestCalculation);
});

// 차트 초기화 함수
function initializeChart() {
    const ctx = document.getElementById('resultChart').getContext('2d');
    resultChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['불입금액합계', '세전이자', '세전수령액', '이자과세금', '세후수령액'],
            datasets: [{
                label: '계산 결과',
                data: [0, 0, 0, 0, 0], // 초기 데이터
                backgroundColor: [
                    '#4CAF50', // 불입금액합계
                    '#FF9800', // 세전이자
                    '#03A9F4', // 세전수령액
                    '#F44336', // 이자과세금
                    '#9C27B0'  // 세후수령액
                ],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return value.toLocaleString(); // 천 단위 쉼표 추가
                        }
                    }
                }
            },
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return `${context.dataset.label}: ${context.raw.toLocaleString()}원`;
                        }
                    }
                }
            }
        }
    });
}

// 이자 계산 핸들러 함수
function handleInterestCalculation() {

    if (!resultChart) {
        console.error("resultChart가 초기화되지 않았습니다.");
        return;
    }

    // 데이터 유효성 검사
    const savgCircleAmt = document.getElementById('savgCircleAmt').value.trim();
    const savgGoalPrd = document.getElementById('savgGoalPrd').value.trim();
    const savgAplyRate = document.getElementById('savgAplyRate').value.trim();

    // 유효성 검사
    if (!savgCircleAmt || parseFloat(savgCircleAmt) <= 0) {
        alert('불입금액을 입력하세요.');
        return;
    }
    if (!savgGoalPrd || parseInt(savgGoalPrd) <= 0) {
        alert('목표기간을 입력하세요.');
        return;
    }
    if (!savgAplyRate || parseFloat(savgAplyRate) <= 0) {
        alert('적용금리를 입력하세요.');
        return;
    }

    const requestData = {
        savgCircleAmt: parseFloat(savgCircleAmt.replace(/,/g, '')),
        savgGoalPrd: parseInt(savgGoalPrd),
        savgAplyRate: parseFloat(savgAplyRate),
        prodIntTaxRate: parseFloat(document.getElementById('prodIntTaxRate').value),
        savgStrtDt: document.getElementById('savgStrtDt').value,
    };

    // 서버 요청
    fetch('/promotion/cal/savg/calculate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json',
         'X-CSRF-Token': csrfToken,
         },
        body: JSON.stringify(requestData),
    })
        .then((response) => {
            if (!response.ok) throw new Error('서버 응답 오류');
            return response.json();
        })
        .then(updateCalculationResults) // 결과 업데이트
        .catch((error) => {
            console.error('Error:', error);
            alert('계산 중 오류가 발생했습니다.');
        });
}

// 계산 결과 업데이트 함수
function updateCalculationResults(data) {
    document.getElementById('savgTotDpstAmt').value = data.savgTotDpstAmt.toLocaleString();
    document.getElementById('savgTotDpstInt').value = data.savgTotDpstInt.toLocaleString();
    document.getElementById('savgTotRcveAmt').value = data.savgTotRcveAmt.toLocaleString();
    document.getElementById('savgIntTaxAmt').value = data.savgIntTaxAmt.toLocaleString();
    document.getElementById('savgAtxRcveAmt').value = data.savgAtxRcveAmt.toLocaleString();

    // 차트 데이터 업데이트
    if (resultChart && resultChart.data && resultChart.data.datasets) {
        resultChart.data.datasets[0].data = [
            data.savgTotDpstAmt,
            data.savgTotDpstInt,
            data.savgTotRcveAmt,
            data.savgIntTaxAmt,
            data.savgAtxRcveAmt
        ];
        resultChart.update(); // 차트 새로고침
        console.log("차트 업데이트 완료: ", resultChart.data.datasets[0].data);
    } else {
        console.error("resultChart가 초기화되지 않았습니다.");
    }

    // 상세 테이블 데이터 업데이트
    const detailTableBody = document.querySelector('.interest-details tbody');
    detailTableBody.innerHTML = '';
    data.detailList.forEach((detail) => {
        const row = `
            <tr>
                <td>${detail.installmentNo}</td>
                <td>${detail.installmentAmt.toLocaleString()}</td>
                <td>${detail.accumulatedAmt.toLocaleString()}</td>
                <td>${detail.installmentInt.toLocaleString()}</td>
                <td>${detail.installmentPrincipal.toLocaleString()}</td>
            </tr>`;
        detailTableBody.insertAdjacentHTML('beforeend', row);
    });
}

document.querySelector('.btn-register').addEventListener('click', () => {

    // 상품 선택 확인
    const prodSn = document.getElementById('prodSn').value.trim();
    if (!prodSn) {
        alert('상품 조회 후 저장이 가능합니다.');
        return;
    }

    // 현재 설계 유형 확인
    const calculationType = document.querySelector('input[name="calculationType"]:checked').value;
    if (calculationType !== 'detailed') {
        alert('정상설계만 저장이 가능합니다.');
        return;
    }

    // userId와 custId 값 확인
    const userId = document.getElementById('userId').value.trim();
    const custId = document.getElementById('custId').value.trim();
    if (!userId || !custId) {
        alert('고객 조회 후 저장이 가능합니다.');
        return;
    }

    // 이자 계산 확인
    const savgTotDpstInt = document.getElementById('savgTotDpstInt').value.trim();
    if (!savgTotDpstInt || parseFloat(savgTotDpstInt) <= 0) {
        alert('이자계산 후 저장이 가능합니다.');
        return;
    }

    // 진행상태 확인
    const dsgnPrgStcd = document.getElementById('dsgnPrgStcd').value.trim();
    if (!dsgnPrgStcd) {
        alert('진행상태 선택 후 저장이 가능합니다.');
        return;
    }

    // 유효성 검사가 통과되면 데이터 수집 및 저장
    const savingsSaveDto = {
        prodSn: prodSn, // 상품번호
        custId: custId, // 고객ID
        userId: userId, // 담당자ID
        dsgnCreateAt: document.getElementById('dsgnCreateAt').value, // 설계일자
        dsgnUpdateAt: document.getElementById('dsgnUpdateAt').value, // 수정일자
        prodDsTyCd: document.getElementById('prodDsTyCd').value, // 설계유형코드
        dsgnPrgStcd: dsgnPrgStcd, // 진행상태코드
        prodPayTyCd: document.getElementById('prodPayTyCd').value, // 납입주기코드
        savgGoalPrd: parseInt(document.getElementById('savgGoalPrd').value, 10), // 목표기간
        savgStrtDt: document.getElementById('savgStrtDt').value, // 시작일자
        savgMtrDt: document.getElementById('savgMtrDt').value, // 만기일자
        savgAplyRate: parseFloat(document.getElementById('savgAplyRate').value), // 적용금리
        savgIntTaxTyCd: document.getElementById('savgIntTaxTyCd').value, // 이자과세유형코드
        savgCircleAmt: parseInt(document.getElementById('savgCircleAmt').value.replace(/,/g, ''), 10), // 회차불입액
        savgTotDpstAmt: parseInt(document.getElementById('savgTotDpstAmt').value.replace(/,/g, ''), 10), // 불입금합계
        savgTotDpstInt: parseInt(savgTotDpstInt.replace(/,/g, ''), 10), // 세전이자
        savgIntTaxAmt: parseInt(document.getElementById('savgIntTaxAmt').value.replace(/,/g, ''), 10), // 이자과세금액
        savgAtxRcveAmt: parseInt(document.getElementById('savgAtxRcveAmt').value.replace(/,/g, ''), 10), // 세후수령액
    };

<!--            // 데이터 확인 로그-->
<!--            console.log('Collected Data:', data);-->

    // 서버로 POST 요청 전송
    fetch('/promotion/cal/acml/insert', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': csrfToken,
        },
        body: JSON.stringify(savingsSaveDto), // DTO 형태로 JSON 변환
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류');
            }
            return response.text(); // 서버 응답 텍스트 처리
        })
        .then(result => {
            alert(result); // 성공 메시지 출력
        })
        .catch(error => {
            console.error('Error:', error);
            alert('저장 중 오류가 발생했습니다.');
        });

});