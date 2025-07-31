// --- 전역 변수 초기화 ---
// 상품 데이터를 저장할 배열
let products = [];

// --- 상품 관리 기능 ---

function fetchProducts() {
    fetch('/api/products') // 상품 목록을 요청할 백엔드 API 엔드포인트
        .then(res => {
            if (!res.ok) {
                throw new Error(`상품 목록을 불러오는데 실패했습니다. 상태 코드: ${res.status}`);
            }
            return res.json(); // 응답 본문을 JSON 형태로 파싱
        })
        .then(data => {
            products = data;   // 파싱된 데이터를 전역 'products' 배열에 저장
            renderProducts();  // 저장된 상품 데이터를 기반으로 화면에 테이블 렌더링
        })
        .catch(err => {
            alert(`상품 목록 로드 오류: ${err.message}`);
        });
}

// 상품 목록에서 특정 상품을 클릭했을 때 해당 상품의 상세 정보를 수정 섹션에 표시
function renderProducts() {
    const tbody = document.getElementById('productTableBody');
    tbody.innerHTML = '';  // 기존 테이블 내용을 초기화하여 중복 렌더링 방지

    products.forEach(p => {
        const tr = document.createElement('tr'); // 새로운 테이블 행(tr) 요소 생성
        tr.classList.add('product-row'); // CSS 스타일링을 위한 클래스 추가
        tr.dataset.productId = p.productId; // data-productId 속성에 상품 ID 저장 (DOM에서 쉽게 접근 위함)

        // 테이블 행의 HTML 내용 설정 (상품 ID, 이름, 가격, 재고, 카테고리)
        tr.innerHTML = `
            <td>${p.productId}</td>
            <td>${p.productName}</td>
            <td>${p.price}</td>
            <td>${p.stock}</td>
            <td>${p.productTag}</td>
        `;

        // 테이블 행 클릭 시 'showProductDetails' 함수를 호출하여 상품 상세 정보 표시
        tr.addEventListener('click', () => showProductDetails(p.productId));
        tbody.appendChild(tr); // 테이블 본문(tbody)에 생성된 행 추가
    });
}

/**
 * 특정 상품 ID에 해당하는 상품의 상세 정보를 가져와 수정 폼에 채우고 표시합니다.
 * 이미지 미리보기 기능도 포함됩니다.
 * @param {string} productId - 상세 정보를 표시할 상품의 고유 ID
 */
function showProductDetails(productId) {
    // 'products' 배열에서 해당 productId를 가진 상품을 찾음
    const product = products.find(p => p.productId === productId);
    if (!product) {
        alert('선택된 상품을 찾을 수 없습니다.');
        return;
    }

    // 수정 폼의 각 입력 필드에 상품 데이터 채우기
    document.getElementById('editProductId').value = product.productId;
    document.getElementById('editProductName').value = product.productName;
    document.getElementById('editProductPrice').value = product.price;
    document.getElementById('editProductStock').value = product.stock;
    document.getElementById('editProductTag').value = product.productTag;

    // 이미지 미리보기 처리
    const imgPreview = document.getElementById('currentProductImagePreview');
    if (product.image) { // 상품에 이미지 URL이 있는 경우
        imgPreview.src = product.image; // 이미지 URL을 src 속성에 설정
        imgPreview.style.display = 'block'; // 이미지를 보이게 함
    } else { // 이미지가 없는 경우
        imgPreview.src = ''; // src 초기화
        imgPreview.style.display = 'none'; // 이미지를 숨김
        // 선택적으로 기본 이미지나 "이미지 없음" 메시지를 표시할 수 있습니다.
    }

    // 상품 수정 섹션을 보이게 하고 해당 섹션으로 부드럽게 스크롤 이동
    document.getElementById('editProductSection').style.display = 'block';
    document.getElementById('editProductSection').scrollIntoView({ behavior: 'smooth' });
}

/**
 * 상품 수정 폼 제출 이벤트를 처리합니다.
 * 입력값을 검증하고, 새 이미지가 있으면 업로드 후 상품 정보를 업데이트합니다.
 */
document.getElementById('editProductForm').addEventListener('submit', function(e) {
    e.preventDefault(); // 폼의 기본 제출 동작(페이지 새로고침) 방지

    // 수정 폼의 입력값 가져오기
    const id = document.getElementById('editProductId').value;
    const name = document.getElementById('editProductName').value.trim();
    const price = Number(document.getElementById('editProductPrice').value);
    const stock = Number(document.getElementById('editProductStock').value);
    const productTag = document.getElementById('editProductTag').value;
    const fileInput = document.getElementById('editProductImageFile');

    // 입력값 유효성 검증
    if (!name || price <= 0 || stock < 0 || !productTag) {
        alert('모든 필드를 올바르게 입력해주세요.');
        return;
    }

    // 현재 미리보기 이미지의 URL을 기본으로 설정 (기존 이미지 유지 시)
    let imageUrl = document.getElementById('currentProductImagePreview').src;

    // 새 이미지 파일이 선택되었는지 확인하고 처리
    if (fileInput.files.length > 0) { // 새로운 이미지 파일이 선택된 경우
        uploadImage(fileInput.files[0]) // 이미지 파일을 서버에 업로드
            .then(newImageUrl => {
                // 이미지 업로드 성공 후 반환된 새 이미지 URL로 상품 정보를 업데이트
                updateProduct(id, name, price, stock, productTag, newImageUrl);
            })
            .catch(err => {
                // 이미지 업로드 실패 시 에러 알림
                alert(`이미지 업로드 실패: ${err.message}`);
            });
    } else {
        // 새로운 이미지가 선택되지 않은 경우, 기존 이미지 URL로 상품 정보를 업데이트
        updateProduct(id, name, price, stock, productTag, imageUrl);
    }
});

/**
 * 서버에 특정 상품의 정보를 업데이트하는 PUT 요청을 보냅니다.
 * @param {string} id - 수정할 상품의 ID
 * @param {string} name - 새 상품명
 * @param {number} price - 새 가격
 * @param {number} stock - 새 재고 수량
 * @param {string} productTag - 새 카테고리 태그
 * @param {string} imageUrl - 새 이미지 URL (또는 기존 URL)
 */
function updateProduct(id, name, price, stock, productTag, imageUrl) {
    fetch(`/api/products/${id}`, { // 상품 ID를 포함한 PUT 요청 엔드포인트
        method: 'PUT', // HTTP PUT 메서드 사용
        headers: { 'Content-Type': 'application/json' }, // 요청 본문이 JSON 형식임을 명시
        body: JSON.stringify({ // JavaScript 객체를 JSON 문자열로 변환하여 전송
            productName: name,
            price: price,
            stock: stock,
            productTag: productTag,
            image: imageUrl
        })
    })
        .then(res => {
            if (!res.ok) {
                throw new Error(`상품 수정에 실패했습니다. 상태 코드: ${res.status}`);
            }
            return res.text(); // 서버 응답이 JSON이 아닐 수도 있으므로 text()로 받음
        })
        .then(message => {
            alert('상품이 성공적으로 수정되었습니다.'); // 성공 알림
            document.getElementById('editProductSection').style.display = 'none'; // 수정 영역 숨김
            fetchProducts(); // 상품 목록을 다시 불러와 화면 갱신
        })
        .catch(err => {
            alert(`상품 수정 실패: ${err.message}`); // 에러 발생 시 알림
        });
}

/**
 * 선택된 상품을 서버에서 삭제하는 DELETE 요청을 보냅니다.
 * 삭제 전에 사용자에게 확인 메시지를 표시합니다.
 */
function deleteSelectedProduct() {
    const id = document.getElementById('editProductId').value; // 수정 폼에 있는 상품 ID 가져오기
    if (!confirm('정말 이 상품을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
        return; // 사용자가 취소하면 함수 종료
    }

    fetch(`/api/products/${id}`, { method: 'DELETE' }) // 상품 ID를 포함한 DELETE 요청 엔드포인트
        .then(res => {
            if (!res.ok) {
                throw new Error(`상품 삭제에 실패했습니다. 상태 코드: ${res.status}`);
            }
            return res.text(); // 서버 응답이 JSON이 아닐 수도 있으므로 text()로 받음
        })
        .then(message => {
            alert('상품이 성공적으로 삭제되었습니다.'); // 성공 알림
            document.getElementById('editProductSection').style.display = 'none'; // 수정 영역 숨김
            fetchProducts(); // 상품 목록을 다시 불러와 화면 갱신
        })
        .catch(err => {
            alert(`상품 삭제 실패: ${err.message}`); // 에러 발생 시 알림
        });
}

/**
 * 상품 수정 폼을 숨기고 초기 상태로 되돌립니다.
 */
function cancelEdit() {
    document.getElementById('editProductSection').style.display = 'none';
}

/**
 * 새 상품 등록 폼 제출 이벤트를 처리합니다.
 * 입력값을 검증하고, 이미지가 있으면 업로드 후 새 상품을 등록합니다.
 */
document.getElementById('addProductForm').addEventListener('submit', function(e) {
    e.preventDefault(); // 폼의 기본 제출 동작 방지

    // 새 상품 등록 폼의 입력값 가져오기
    const name = document.getElementById('newProductName').value.trim();
    const price = Number(document.getElementById('newProductPrice').value);
    const stock = Number(document.getElementById('newProductStock').value);
    const productTag = document.getElementById('newProductTag').value;
    const fileInput = document.getElementById('newProductImageFile');

    // 입력값 유효성 검증
    if (!name || price <= 0 || stock < 0 || !productTag) {
        alert('모든 필드를 올바르게 입력해주세요 (상품명, 가격, 재고, 카테고리).');
        return;
    }

    // 이미지 파일이 선택된 경우 업로드 후 상품 등록, 아니면 바로 상품 등록
    if (fileInput.files.length > 0) {
        uploadImage(fileInput.files[0])
            .then(imageUrl => {
                registerProduct(name, price, stock, productTag, imageUrl);
            })
            .catch(err => {
                alert(`이미지 업로드 실패: ${err.message}`);
            });
    } else {
        registerProduct(name, price, stock, productTag, null); // 이미지 없이 등록
    }
});

/**
 * 서버에 새 상품을 등록하는 POST 요청을 보냅니다.
 * @param {string} name - 새 상품명
 * @param {number} price - 새 가격
 * @param {number} stock - 새 재고 수량
 * @param {string} productTag - 새 카테고리 태그
 * @param {string|null} imageUrl - 업로드된 이미지 URL 또는 null
 */
function registerProduct(name, price, stock, productTag, imageUrl) {
    fetch('/api/products', { // 상품 등록 POST 요청 엔드포인트
        method: 'POST', // HTTP POST 메서드 사용
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ // JavaScript 객체를 JSON 문자열로 변환하여 전송
            productName: name,
            price: price,
            stock: stock,
            productTag: productTag,
            image: imageUrl // 이미지 URL 포함 (null일 수 있음)
        })
    })
        .then(res => {
            if (!res.ok) {
                throw new Error(`상품 등록에 실패했습니다. 상태 코드: ${res.status}`);
            }
            return res.text(); // 서버 응답이 JSON이 아닐 수도 있으므로 text()로 받음
        })
        .then(message => {
            alert('새 상품이 성공적으로 등록되었습니다.'); // 성공 알림
            document.getElementById('addProductForm').reset();  // 폼 필드 초기화
            fetchProducts();  // 상품 목록을 다시 불러와 화면 갱신
        })
        .catch(err => {
            alert(`상품 등록 실패: ${err.message}`); // 에러 발생 시 알림
        });
}

/**
 * 이미지 파일을 서버에 업로드하고, 업로드된 이미지의 URL을 반환합니다.
 * FormData를 사용하여 파일을 전송합니다.
 * @param {File} file - 업로드할 File 객체
 * @returns {Promise<string>} - 업로드된 이미지의 URL을 resolve하는 Promise
 */
function uploadImage(file) {
    const formData = new FormData(); // FormData 객체 생성 (파일 업로드에 사용)
    formData.append('file', file); // 'file'이라는 이름으로 File 객체 추가

    return fetch('/api/files/upload', { // 파일 업로드 API 엔드포인트
        method: 'POST', // HTTP POST 메서드 사용
        body: formData // FormData 객체를 본문으로 전송 (Content-Type은 자동으로 multipart/form-data로 설정됨)
    })
        .then(res => {
            if (!res.ok) {
                throw new Error(`이미지 업로드 실패. 상태 코드: ${res.status}`);
            }
            return res.text(); // 서버가 반환한 이미지 URL을 텍스트로 받음
        });
}

// --- 공통/유틸리티 기능 ---

/**
 * '홈' 페이지로 사용자를 리다이렉션합니다.
 */
function goHome() {
    window.location.href = '/home'; // '/home' 경로로 이동
}

/**
 * 사용자를 로그아웃 처리하고 로그인 페이지로 리다이렉션합니다.
 * 로컬 스토리지에서 사용자 인증 정보를 제거합니다.
 */
function logout() {
    localStorage.removeItem('currentUser'); // 로컬 스토리지에서 'currentUser' 키 삭제
    alert('로그아웃 되었습니다.');
    window.location.href = '/login'; // '/login' 페이지로 이동
}

// --- 초기화 로직 ---

/**
 * 문서 로드가 완료되면 초기 데이터(상품 목록)를 불러오는 이벤트 리스너입니다.
 */
document.addEventListener('DOMContentLoaded', () => {
    fetchProducts(); // 페이지 로드 시 상품 목록을 자동으로 불러옴
    // fetchUsers(); // 회원 관리 기능 추가 시, 여기에 회원 목록 불러오는 함수도 추가
});