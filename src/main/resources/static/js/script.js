// const products = [
//     {id: 1, name: '클래식 청바지', category: '바지', price: 89000, image: '👖'},
//     {id: 2, name: '슬림핏 치노', category: '바지', price: 65000, image: '👖'},
//     {id: 3, name: '운동화', category: '신발', price: 120000, image: '👟'},
//     {id: 4, name: '구두', category: '신발', price: 150000, image: '👞'},
//     {id: 5, name: '면 티셔츠', category: '상의', price: 35000, image: '👕'},
//     {id: 6, name: '셔츠', category: '상의', price: 55000, image: '👔'},
//     {id: 7, name: '겨울 코트', category: '아우터', price: 200000, image: '🧥'},
//     {id: 8, name: '후드집업', category: '아우터', price: 85000, image: '🧥'},
//     {id: 9, name: '스키니 진', category: '바지', price: 75000, image: '👖'},
//     {id: 10, name: '하이탑 스니커즈', category: '신발', price: 95000, image: '👟'},
//     {id: 11, name: '폴로 셔츠', category: '상의', price: 45000, image: '👕'},
//     {id: 12, name: '바람막이', category: '아우터', price: 110000, image: '🧥'}
// ];

// 전역 변수 (모든 페이지에서 사용)
let currentUser = null; // 현재 로그인된 사용자 정보 (localStorage에서 로드)
let cart = [];          // 장바구니 아이템 (localStorage에서 로드)
let reviews = [];       // 상품 리뷰 (localStorage에서 로드)
let filteredProducts = products; // 상품 필터링 결과 (home.html에서 사용)
let selectedRating = 5; // 리뷰 작성 시 선택된 별점 (product-detail.html에서 사용)

// 페이지 로드 시 초기화 함수 (모든 HTML 페이지의 <script> 태그 내부에 포함)
document.addEventListener('DOMContentLoaded', function() {
    initializePage();
});

function initializePage() {
    // 로컬 스토리지에서 데이터 로드
    loadUserData();
    loadCartData();
    loadReviewData(); // 리뷰 데이터는 product-detail.html에서만 필요할 수 있음

    // 현재 페이지에 따라 초기화 로직 분기
    const currentPath = window.location.pathname;
    if (currentPath === '/' || currentPath.includes('home')) {
        // home.html (메인 페이지)
        displayProducts(products); // 상품 목록 표시
    } else if (currentPath.includes('cart')) {
        // cart.html (장바구니 페이지)
        displayCartItems(); // 장바구니 아이템 표시
    } else if (currentPath.includes('mypage')) {
        // mypage.html (마이페이지)
        displayUserInfo();      // 사용자 정보 표시
        displayOrderHistory();  // 주문 내역 표시
    } else if (currentPath.includes('product-detail')) {
        // product-detail.html (상품 상세 페이지)
        initializeProductDetail(); // 상품 상세 정보 및 리뷰 초기화
    }
    updateUI(); // 공통 UI (로그인/로그아웃 버튼, 장바구니 개수) 업데이트
}

// 사용자 데이터 로드 (모든 페이지에서 사용)
function loadUserData() {
    const userData = localStorage.getItem('currentUser');
    if (userData) {
        currentUser = JSON.parse(userData);
    }
}

// 장바구니 데이터 로드 (모든 페이지에서 사용)
function loadCartData() {
    const cartData = localStorage.getItem('cart');
    if (cartData) {
        cart = JSON.parse(cartData);
    }
}

// 리뷰 데이터 로드 (product-detail.html에서 주로 사용)
function loadReviewData() {
    const reviewData = localStorage.getItem('reviews');
    if (reviewData) {
        reviews = JSON.parse(reviewData);
    }
}

// UI 업데이트 (모든 페이지에서 사용)
function updateUI() {
    const loginBtn = document.getElementById('loginBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const cartCount = document.getElementById('cartCount');

    if (loginBtn && logoutBtn) {
        if (currentUser) {
            loginBtn.style.display = 'none';
            logoutBtn.style.display = 'inline-block';
        } else {
            loginBtn.style.display = 'inline-block';
            logoutBtn.style.display = 'none';
        }
    }
    if (cartCount) {
        cartCount.textContent = cart.reduce((total, item) => total + item.quantity, 0);
    }
}


// ====================================================================================================
// home.html (메인 페이지) 관련 JavaScript 함수
// ====================================================================================================

// 상품 표시 (home.html)
function displayProducts(productsToShow) {
    const productGrid = document.getElementById('productGrid');
    if (!productGrid) return;
    productGrid.innerHTML = '';
    productsToShow.forEach(product => {
        const productCard = document.createElement('div');
        productCard.className = 'product-card';
        productCard.innerHTML = `
            <div class="product-image">${product.image || '이미지 없음'}</div>
            <div class="product-info">
                <h3 onclick="goToProductDetail(${product.id})">${product.name}</h3> <!-- product.name으로 변경 -->
                <p>카테고리: ${product.category}</p> <!-- product.category로 변경 -->
                <div class="product-price">${product.price.toLocaleString()}원</div>
                <button class="add-to-cart-btn" onclick="addToCart(${product.id})">
                    장바구니 담기
                </button>
            </div>
        `;
        productGrid.appendChild(productCard);
    });
}

// 카테고리별 필터링 (home.html)
function filterByCategory(category) {
    filteredProducts = products.filter(product => product.category === category);
    displayProducts(filteredProducts);
}

// 전체 상품 보기 (home.html)
function showAllProducts() {
    filteredProducts = products;
    displayProducts(products);
}

// 상품 검색 (home.html)
function searchProducts() {
    const searchInput = document.getElementById('searchInput');
    const searchTerm = searchInput.value.toLowerCase().trim();
    if (searchTerm === '') {
        displayProducts(products);
        return;
    }
    const searchResults = products.filter(product =>
        product.name.toLowerCase().includes(searchTerm) ||
        product.category.toLowerCase().includes(searchTerm)
    );
    displayProducts(searchResults);
}


// ====================================================================================================
// product-detail.html (상품 상세 페이지) 관련 JavaScript 함수
// ====================================================================================================

// 상품 상세 페이지로 이동 (home.html, product-detail.html 등에서 사용)
function goToProductDetail(productId) {
    localStorage.setItem('selectedProductId', productId);
    window.location.href = '/product-detail';
}

// 뒤로가기 (product-detail.html)
function goBack() {
    window.location.href = '/';
}

// 상품 상세 페이지 초기화 (product-detail.html)
function initializeProductDetail() {
    const productId = parseInt(localStorage.getItem('selectedProductId'));
    if (!productId) {
        window.location.href = '/';
        return;
    }
    // products 배열은 home.html에서 정의된 임시 데이터입니다.
    // 실제로는 API 호출로 해당 상품 정보를 가져와야 합니다.
    const product = products.find(p => p.id === productId);
    if (!product) {
        window.location.href = '/';
        return;
    }
    displayProductDetail(product);
    displayProductReviews(productId);
    initializeReviewForm();
}

// 상품 상세 정보 표시 (product-detail.html)
function displayProductDetail(product) {
    document.getElementById('productImageLarge').textContent = product.image;
    document.getElementById('productCategory').textContent = product.category;
    document.getElementById('productTitle').textContent = product.name;
    document.getElementById('productPriceLarge').textContent = product.price.toLocaleString() + '원';
    // 상품 설명 설정
    const descriptions = {
        '바지': '편안한 착용감과 스타일을 동시에 만족시키는 바지입니다. 다양한 상황에서 활용할 수 있는 베이직한 디자인으로 제작되었습니다.',
        '신발': '발의 편안함을 최우선으로 고려한 신발입니다. 고품질 소재를 사용하여 내구성과 스타일을 모두 갖추었습니다.',
        '상의': '부드러운 소재와 세련된 디자인이 특징인 상의입니다. 일상복으로도, 외출복으로도 완벽한 아이템입니다.',
        '아우터': '추운 날씨에도 따뜻함을 유지해주는 아우터입니다. 실용성과 패션성을 모두 고려한 디자인입니다.'
    };
    document.getElementById('productDescription').textContent = descriptions[product.category] || '고품질 상품입니다.';
    // 평점 정보 업데이트
    updateProductRating(product.id);
}

// 상품 평점 정보 업데이트 (product-detail.html)
function updateProductRating(productId) {
    const productReviews = reviews.filter(review => review.productId === productId);
    const averageRating = productReviews.length > 0
        ? productReviews.reduce((sum, review) => sum + review.rating, 0) / productReviews.length
        : 0;
    const starsContainer = document.getElementById('averageStars');
    const ratingScore = document.getElementById('averageRating');
    const reviewCount = document.getElementById('reviewCount');
    starsContainer.innerHTML = renderStars(Math.round(averageRating));
    ratingScore.textContent = averageRating.toFixed(1);
    reviewCount.textContent = `(${productReviews.length}개 리뷰)`;
}

// 별점 렌더링 (product-detail.html)
function renderStars(rating) {
    let starsHTML = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= rating) {
            starsHTML += '<span class="star">★</span>';
        } else {
            starsHTML += '<span class="star empty">★</span>';
        }
    }
    return starsHTML;
}

// 상품 리뷰 표시 (product-detail.html)
function displayProductReviews(productId) {
    const productReviews = reviews.filter(review => review.productId === productId);
    const reviewList = document.getElementById('reviewList');
    if (productReviews.length === 0) {
        reviewList.innerHTML = `
            <div class="no-reviews">
                <p>아직 리뷰가 없습니다. 첫 번째 리뷰를 작성해보세요!</p>
            </div>
        `;
        return;
    }
    reviewList.innerHTML = '';
    productReviews.slice().reverse().forEach(review => {
        const reviewItem = document.createElement('div');
        reviewItem.className = 'review-item';
        reviewItem.innerHTML = `
            <div class="review-header">
                <div class="review-user-info">
                    <div class="review-user-name">${review.userName}</div>
                    <div class="review-date">${review.date}</div>
                </div>
                <div class="review-rating">${renderStars(review.rating)}</div>
            </div>
            <div class="review-comment">${review.comment}</div>
        `;
        reviewList.appendChild(reviewItem);
    });
}

// 리뷰 폼 초기화 (product-detail.html)
function initializeReviewForm() {
    const reviewWriteContainer = document.getElementById('reviewWriteContainer');
    const loginRequired = document.getElementById('loginRequired');
    if (currentUser) {
        reviewWriteContainer.style.display = 'block';
        loginRequired.style.display = 'none';
        // 별점 클릭 이벤트 설정
        const stars = document.querySelectorAll('.star-rating .star');
        stars.forEach((star, index) => {
            star.addEventListener('click', () => {
                selectedRating = index + 1;
                updateStarRating();
            });
            star.addEventListener('mouseover', () => {
                highlightStars(index + 1);
            });
        });
        document.querySelector('.star-rating').addEventListener('mouseleave', () => {
            updateStarRating();
        });
        updateStarRating();
    } else {
        reviewWriteContainer.style.display = 'none';
        loginRequired.style.display = 'block';
    }
}

// 별점 업데이트 (product-detail.html)
function updateStarRating() {
    const stars = document.querySelectorAll('.star-rating .star');
    stars.forEach((star, index) => {
        if (index < selectedRating) {
            star.classList.add('active');
        } else {
            star.classList.remove('active');
        }
    });
}

// 별점 하이라이트 (product-detail.html)
function highlightStars(rating) {
    const stars = document.querySelectorAll('.star-rating .star');
    stars.forEach((star, index) => {
        if (index < rating) {
            star.classList.add('active');
        } else {
            star.classList.remove('active');
        }
    });
}

// 리뷰 제출 (product-detail.html)
function submitReview(event) {
    event.preventDefault();
    if (!currentUser) {
        alert('로그인이 필요합니다.');
        return;
    }
    const productId = parseInt(localStorage.getItem('selectedProductId'));
    const comment = document.getElementById('reviewComment').value.trim();
    if (!comment) {
        alert('리뷰 내용을 입력해주세요.');
        return;
    }
    const newReview = {
        id: Date.now(),
        productId: productId,
        userId: currentUser.email,
        userName: currentUser.name,
        rating: selectedRating,
        comment: comment,
        date: new Date().toLocaleDateString()
    };
    reviews.push(newReview);
    localStorage.setItem('reviews', JSON.stringify(reviews));
    // 폼 초기화
    document.getElementById('reviewComment').value = '';
    selectedRating = 5;
    updateStarRating();
    // 리뷰 목록 및 평점 업데이트
    displayProductReviews(productId);
    updateProductRating(productId);
    alert('리뷰가 등록되었습니다.');
}

// 상세 페이지에서 장바구니 담기 (product-detail.html)
function addToCartFromDetail() {
    const productId = parseInt(localStorage.getItem('selectedProductId'));
    addToCart(productId);
}


// ====================================================================================================
// cart.html (장바구니 페이지) 관련 JavaScript 함수
// ====================================================================================================

// 장바구니에 상품 추가 (home.html, product-detail.html)
function addToCart(productId) {
    const product = products.find(p => p.id === productId);
    if (!product) return;
    const existingItem = cart.find(item => item.id === productId);
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({
            id: product.id,
            name: product.name,
            price: product.price,
            image: product.image,
            quantity: 1
        });
    }
    saveCartData();
    updateUI();
    alert(`${product.name}이(가) 장바구니에 추가되었습니다.`);
}

// 장바구니 데이터 저장 (cart.html, home.html, product-detail.html)
function saveCartData() {
    localStorage.setItem('cart', JSON.stringify(cart));
}

// 장바구니 아이템 표시 (cart.html)
function displayCartItems() {
    const cartItems = document.getElementById('cartItems');
    const totalPrice = document.getElementById('totalPrice');
    if (!cartItems) return;
    if (cart.length === 0) {
        cartItems.innerHTML = '<p>장바구니가 비어있습니다.</p>';
        if (totalPrice) totalPrice.textContent = '0';
        return;
    }
    cartItems.innerHTML = '';
    let total = 0;
    cart.forEach(item => {
        const cartItem = document.createElement('div');
        cartItem.className = 'cart-item';
        cartItem.innerHTML = `
            <div class="cart-item-image">${item.image}</div>
            <div class="cart-item-info">
                <h4>${item.name}</h4>
                <div class="cart-item-price">${item.price.toLocaleString()}원</div>
                <div class="quantity-controls">
                    <button onclick="updateQuantity(${item.id}, -1)">-</button>
                    <span>${item.quantity}</span>
                    <button onclick="updateQuantity(${item.id}, 1)">+</button>
                </div>
            </div>
            <button class="remove-btn" onclick="removeFromCart(${item.id})">삭제</button>
        `;
        cartItems.appendChild(cartItem);
        total += item.price * item.quantity;
    });
    if (totalPrice) {
        totalPrice.textContent = total.toLocaleString();
    }
}

// 수량 업데이트 (cart.html)
function updateQuantity(productId, change) {
    const item = cart.find(item => item.id === productId);
    if (!item) return;
    item.quantity += change;
    if (item.quantity <= 0) {
        removeFromCart(productId);
    } else {
        saveCartData();
        displayCartItems();
        updateUI();
    }
}

// 장바구니에서 상품 제거 (cart.html)
function removeFromCart(productId) {
    cart = cart.filter(item => item.id !== productId);
    saveCartData();
    displayCartItems();
    updateUI();
}

// 장바구니 비우기 (cart.html)
function clearCart() {
    if (confirm('장바구니를 비우시겠습니까?')) {
        cart = [];
        saveCartData();
        displayCartItems();
        updateUI();
    }
}

// 주문하기 (cart.html)
function checkout() {
    if (!currentUser) {
        alert('로그인이 필요합니다.');
        goToLogin();
        return;
    }
    if (cart.length === 0) {
        alert('장바구니가 비어있습니다.');
        return;
    }
    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const orderData = {
        id: Date.now(),
        date: new Date().toLocaleDateString(),
        items: [...cart],
        total: total
    };
    // 주문 내역 저장
    let orders = JSON.parse(localStorage.getItem('orders') || '[]');
    orders.push(orderData);
    localStorage.setItem('orders', JSON.stringify(orders));
    // 장바구니 비우기
    cart = [];
    saveCartData();
    alert(`주문이 완료되었습니다. 총 금액: ${total.toLocaleString()}원`);
    displayCartItems();
    updateUI();
}


// ====================================================================================================
// signup.html (회원가입 페이지) 관련 JavaScript 함수
// ====================================================================================================

// 회원가입 처리 (signup.html)
function handleSignup(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const password = formData.get('password');
    const confirmPassword = formData.get('confirmPassword');
    if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
    }
    const userData = {
        name: formData.get('name'),
        email: formData.get('email'),
        password: password,
        phone: formData.get('phone'),
        joinDate: new Date().toLocaleDateString()
    };
    // 기존 사용자 확인
    const existingUsers = JSON.parse(localStorage.getItem('users') || '[]');
    if (existingUsers.find(user => user.email === userData.email)) {
        alert('이미 가입된 이메일입니다.');
        return;
    }
    // 사용자 저장
    existingUsers.push(userData);
    localStorage.setItem('users', JSON.stringify(existingUsers));
    alert('회원가입이 완료되었습니다.');
    window.location.href = '/login';
}


// ====================================================================================================
// login.html (로그인 페이지) 관련 JavaScript 함수
// ====================================================================================================

// 로그인 처리 (login.html)
function handleLogin(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const email = formData.get('email');
    const password = formData.get('password');
    const users = JSON.parse(localStorage.getItem('users') || '[]');
    const user = users.find(u => u.email === email && u.password === password);
    if (user) {
        currentUser = user;
        localStorage.setItem('currentUser', JSON.stringify(user));
        alert(`${user.name}님, 환영합니다!`);
        window.location.href = '/';
    } else {
        alert('이메일 또는 비밀번호가 올바르지 않습니다.');
    }
}

// 로그아웃 (모든 페이지의 네비게이션에서 사용)
function logout() {
    currentUser = null;
    localStorage.removeItem('currentUser');
    alert('로그아웃되었습니다.');
    window.location.href = '/';
}


// ====================================================================================================
// mypage.html (마이페이지) 관련 JavaScript 함수
// ====================================================================================================

// 사용자 정보 표시 (mypage.html)
function displayUserInfo() {
    const userInfo = document.getElementById('userInfo');
    if (!userInfo || !currentUser) return;
    userInfo.innerHTML = `
        <div class="user-detail"><strong>이름:</strong> ${currentUser.name}</div>
        <div class="user-detail"><strong>이메일:</strong> ${currentUser.email}</div>
        <div class="user-detail"><strong>전화번호:</strong> ${currentUser.phone}</div>
        <div class="user-detail"><strong>가입일:</strong> ${currentUser.joinDate}</div>
    `;
}

// 주문 내역 표시 (mypage.html)
function displayOrderHistory() {
    const orderHistory = document.getElementById('orderHistory');
    if (!orderHistory) return;
    const orders = JSON.parse(localStorage.getItem('orders') || '[]');
    if (orders.length === 0) {
        orderHistory.innerHTML = '<p>주문 내역이 없습니다.</p>';
        return;
    }
    orderHistory.innerHTML = '';
    orders.reverse().forEach(order => {
        const orderItem = document.createElement('div');
        orderItem.className = 'order-item';
        orderItem.innerHTML = `
            <div class="order-date">주문일: ${order.date}</div>
            <div class="order-items">
                ${order.items.map(item => `${item.name} x ${item.quantity}`).join(', ')}
            </div>
            <div class="order-total">총 금액: ${order.total.toLocaleString()}원</div>
        `;
        orderHistory.appendChild(orderItem);
    });
}

// 프로필 수정 (mypage.html)
function editProfile() {
    alert('프로필 수정 기능은 추후 구현 예정입니다.');
}


// ====================================================================================================
// 네비게이션 함수들 (모든 HTML 페이지의 네비게이션 버튼에서 사용)
// ====================================================================================================

function goToLogin() {
    if (currentUser) {
        alert('이미 로그인되어 있습니다.');
        return;
    }
    window.location.href = '/login';
}

function goToSignup() {
    window.location.href = '/signup';
}

function goToCart() {
    window.location.href = '/cart';
}

function goToMyPage() {
    if (!currentUser) {
        alert('로그인이 필요합니다.');
        goToLogin();
        return;
    }
    window.location.href = '/mypage';
}

// 엔터 키로 검색 (home.html의 검색창에서 사용)
document.addEventListener('keypress', function(event) {
    if (event.key === 'Enter' && event.target.id === 'searchInput') {
        searchProducts();
    }
});