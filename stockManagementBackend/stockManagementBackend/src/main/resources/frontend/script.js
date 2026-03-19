/**
 * Цей JavaScript файл відповідає за інтерактивність клієнтської частини
 * веб-додатка для управління запасами.
 * * Оновлено: додано трасування запитів, централізоване логування помилок
 * та покращену обробку винятків.
 */
document.addEventListener('DOMContentLoaded', () => {
    // --- Елементи DOM ---
    const loginSection = document.getElementById('loginSection');
    const loginUsernameInput = document.getElementById('loginUsername');
    const loginPasswordInput = document.getElementById('loginPassword');
    const loginButton = document.getElementById('loginButton');
    const loginErrorMessage = document.getElementById('loginErrorMessage');

    const mainContent = document.getElementById('mainContent');
    const usernameDisplay = document.getElementById('usernameDisplay');
    const logoutButton = document.getElementById('logoutButton');

    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');
    const productsTableBody = document.querySelector('#productsTable tbody');
    const loadingMessage = document.getElementById('loadingMessage');
    const errorMessage = document.getElementById('errorMessage');
    const noProductsMessage = document.getElementById('noProductsMessage');

    const reservationsTableBody = document.querySelector('#reservationsTable tbody');
    const loadingReservations = document.getElementById('loadingReservations');
    const errorReservationsMessage = document.getElementById('errorReservationsMessage');
    const noReservationsMessage = document.getElementById('noReservationsMessage');

    // --- Змінні стану ---
    const API_BASE_URL = 'http://localhost:8080/api';
    let currentUserRole = null;
    let currentUserId = null;

    // --- СИСТЕМА ЛОГУВАННЯ ТА ТРАСУВАННЯ ---

    /**
     * Глобальне перехоплення JavaScript помилок.
     * Надсилає деталі помилки на бекенд для централізованого аналізу.
     */
    window.onerror = function(message, source, lineno, colno, error) {
        const errorData = {
            message: message,
            url: source,
            line: lineno,
            user: localStorage.getItem('username') || 'anonymous',
            stack: error ? error.stack : ''
        };

        fetch(`${API_BASE_URL}/logs/error`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(errorData)
        }).catch(err => console.error('Критична помилка: не вдалося надіслати лог на сервер', err));
    };

    /**
     * Допоміжна функція для запитів із підтримкою Tracing (Correlation ID).
     * Додає унікальний заголовок до кожного запиту для прослідковуваності.
     */
    const fetchWithTracing = async (url, options = {}) => {
        const traceId = crypto.randomUUID(); // Генеруємо унікальний Trace ID

        const tracingHeaders = {
            'X-Correlation-ID': traceId
        };

        options.headers = {
            ...options.headers,
            ...tracingHeaders
        };

        // Логуємо дію користувача в консоль (Audit)
        console.log(`[Tracing] Виконується запит з ID: ${traceId}`);

        return fetch(url, options);
    };

    // --- Допоміжні функції UI ---
    const showElement = (element) => element.classList.remove('hidden');
    const hideElement = (element) => element.classList.add('hidden');
    const clearTable = (tableBody) => { tableBody.innerHTML = ''; };

    // --- Функції логіну/виходу ---

    const updateUIForAuth = () => {
        const storedRole = localStorage.getItem('userRole');
        const storedUserId = parseInt(localStorage.getItem('userId'));
        const storedUsername = localStorage.getItem('username');

        if (storedRole && !isNaN(storedUserId) && storedUsername) {
            currentUserRole = storedRole;
            currentUserId = storedUserId;
            usernameDisplay.textContent = `Ви увійшли як: ${storedUsername} (${currentUserRole})`;
            showElement(usernameDisplay);
            showElement(logoutButton);
            hideElement(loginSection);
            showElement(mainContent);
            fetchProducts();
            fetchReservations();
        } else {
            currentUserRole = null;
            currentUserId = null;
            hideElement(usernameDisplay);
            hideElement(logoutButton);
            showElement(loginSection);
            hideElement(mainContent);
        }
    };

    const loginUser = async () => {
        const username = loginUsernameInput.value;
        const password = loginPasswordInput.value;
        hideElement(loginErrorMessage);

        try {
            const response = await fetchWithTracing(`${API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('userRole', data.role);
                localStorage.setItem('userId', data.userId);
                localStorage.setItem('username', username);
                updateUIForAuth();
            } else {
                const errorText = await response.text();
                loginErrorMessage.textContent = `Помилка входу: ${errorText}`;
                showElement(loginErrorMessage);
            }
        } catch (error) {
            loginErrorMessage.textContent = 'Сервер недоступний. Перевірте з’єднання.';
            showElement(loginErrorMessage);
        }
    };

    const logoutUser = () => {
        localStorage.clear();
        updateUIForAuth();
    };

    // --- Функції для товарів ---

    const fetchProducts = async (query = '') => {
        clearTable(productsTableBody);
        hideElement(errorMessage);
        hideElement(noProductsMessage);
        showElement(loadingMessage);

        try {
            const url = `${API_BASE_URL}/products/search?query=${encodeURIComponent(query)}`;
            const response = await fetchWithTracing(url);

            if (!response.ok) throw new Error(`Status: ${response.status}`);

            const products = await response.json();
            hideElement(loadingMessage);

            if (products.length === 0) {
                showElement(noProductsMessage);
                return;
            }

            products.forEach(product => {
                const row = productsTableBody.insertRow();
                row.insertCell().textContent = product.code || 'N/A';
                row.insertCell().textContent = product.article || 'N/A';
                row.insertCell().textContent = product.name || 'N/A';
                row.insertCell().textContent = product.manufacturer || 'N/A';
                row.insertCell().textContent = product.categoryName || 'N/A';
                row.insertCell().textContent = product.quantity ?? '0';

                const reserved = product.reservedQuantity ?? 0;
                row.insertCell().textContent = reserved;
                row.insertCell().textContent = product.price ? product.price.toFixed(2) : '0.00';
                row.insertCell().textContent = product.locationName || 'N/A';
                row.insertCell().textContent = product.lastUpdated ? new Date(product.lastUpdated).toLocaleString() : 'N/A';

                const actionsCell = row.insertCell();
                actionsCell.classList.add('actions-column');

                const available = product.quantity - reserved;
                if (available > 0 && (currentUserRole === 'EMPLOYEE' || currentUserRole === 'WAREHOUSE_MANAGER')) {
                    const btn = document.createElement('button');
                    btn.textContent = 'Зарезервувати';
                    btn.className = 'action-button reserve-button';
                    btn.onclick = () => showReserveModal(product.id, product.name, available);
                    actionsCell.appendChild(btn);
                }
            });
        } catch (error) {
            hideElement(loadingMessage);
            errorMessage.textContent = `Помилка завантаження товарів.`;
            showElement(errorMessage);
        }
    };

    // --- Функції для резервування ---

    const showReserveModal = (productId, productName, maxQuantity) => {
        const quantity = prompt(`Скільки одиниць "${productName}" зарезервувати? (Доступно: ${maxQuantity})`);
        if (quantity === null) return;

        const parsedQuantity = parseInt(quantity);
        if (isNaN(parsedQuantity) || parsedQuantity <= 0 || parsedQuantity > maxQuantity) {
            alert(`Введіть число від 1 до ${maxQuantity}`);
            return;
        }
        reserveProduct(productId, parsedQuantity);
    };

    const reserveProduct = async (productId, quantity) => {
        try {
            const response = await fetchWithTracing(`${API_BASE_URL}/reservations/reserve`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ productId, userId: currentUserId, quantity })
            });

            const data = await response.json();

            if (response.ok) {
                alert("Товар успішно зарезервовано!");
                fetchProducts();
                fetchReservations();
            } else {
                // Вимоги 100%: Інформативне повідомлення з Trace ID
                const userMsg = response.status === 500
                    ? "Внутрішня помилка сервера. Спробуйте пізніше."
                    : (data.message || "Помилка резервування.");

                alert(`Увага: ${userMsg}\n\nКод для підтримки (Trace ID): ${data.traceId || 'N/A'}`);
            }
        } catch (error) {
            alert("Помилка мережі. Перевірте підключення.");
        }
    };

    const fetchReservations = async () => {
        clearTable(reservationsTableBody);
        hideElement(loadingReservations);
        showElement(loadingReservations);

        try {
            const response = await fetchWithTracing(`${API_BASE_URL}/reservations`);
            if (!response.ok) throw new Error();

            const reservations = await response.json();
            hideElement(loadingReservations);

            if (reservations.length === 0) {
                showElement(noReservationsMessage);
                return;
            }

            reservations.forEach(res => {
                const row = reservationsTableBody.insertRow();
                row.insertCell().textContent = res.id;
                row.insertCell().textContent = res.product?.code || 'N/A';
                row.insertCell().textContent = res.product?.name || 'N/A';
                row.insertCell().textContent = res.quantity;
                row.insertCell().textContent = res.reservationDate ? new Date(res.reservationDate).toLocaleString() : 'N/A';
                row.insertCell().textContent = res.status;

                const actionsCell = row.insertCell();
                if (res.status === 'PENDING') {
                    if (currentUserRole === 'WAREHOUSE_MANAGER') {
                        const fBtn = document.createElement('button');
                        fBtn.textContent = 'Виконати';
                        fBtn.className = 'action-button fulfill-button';
                        fBtn.onclick = () => processReservation(res.id, 'fulfill');
                        actionsCell.appendChild(fBtn);
                    }
                    const cBtn = document.createElement('button');
                    cBtn.textContent = 'Скасувати';
                    cBtn.className = 'action-button cancel-button';
                    cBtn.onclick = () => processReservation(res.id, 'cancel');
                    actionsCell.appendChild(cBtn);
                }
            });
        } catch (error) {
            hideElement(loadingReservations);
            showElement(errorReservationsMessage);
        }
    };

    const processReservation = async (id, action) => {
        if (!confirm(`Ви впевнені?`)) return;

        try {
            const response = await fetchWithTracing(`${API_BASE_URL}/reservations/${id}/${action}`, {
                method: 'PUT'
            });

            if (response.ok) {
                alert(`Операція успішна.`);
                fetchProducts();
                fetchReservations();
            } else {
                const err = await response.text();
                alert(`Помилка: ${err}`);
            }
        } catch (e) {
            alert("Помилка зв'язку.");
        }
    };

    // --- Обробники подій ---
    loginButton.addEventListener('click', loginUser);
    logoutButton.addEventListener('click', logoutUser);
    searchButton.addEventListener('click', () => fetchProducts(searchInput.value.trim()));
    searchInput.addEventListener('keypress', (e) => { if (e.key === 'Enter') searchButton.click(); });

    // Ініціалізація
    updateUIForAuth();
});