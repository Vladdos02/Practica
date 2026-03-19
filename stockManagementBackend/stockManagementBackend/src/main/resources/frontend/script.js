/**
 * Цей JavaScript файл відповідає за інтерактивність клієнтської частини
 * веб-додатка для управління запасами. Він обробляє взаємодію з користувачем,
 * надсилає запити до бекенду Spring Boot API та динамічно оновлює DOM.
 *
 * Основні функції включають:
 * - Вхід та вихід користувача з авторизацією на основі ролей.
 * - Відображення списку товарів з можливістю пошуку.
 * - Функціонал резервування товарів.
 * - Відображення списку резервацій з можливістю їх виконання або скасування.
 */
document.addEventListener('DOMContentLoaded', () => {
    // --- Елементи DOM ---
    // Секція входу
    const loginSection = document.getElementById('loginSection');
    const loginUsernameInput = document.getElementById('loginUsername');
    const loginPasswordInput = document.getElementById('loginPassword');
    const loginButton = document.getElementById('loginButton');
    const loginErrorMessage = document.getElementById('loginErrorMessage');

    // Основний контент після входу
    const mainContent = document.getElementById('mainContent');
    const usernameDisplay = document.getElementById('usernameDisplay');
    const logoutButton = document.getElementById('logoutButton');

    // Елементи для товарів
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');
    const productsTableBody = document.querySelector('#productsTable tbody');
    const loadingMessage = document.getElementById('loadingMessage');
    const errorMessage = document.getElementById('errorMessage');
    const noProductsMessage = document.getElementById('noProductsMessage');

    // Елементи для резервацій
    const reservationSection = document.getElementById('reservationSection');
    const reservationsTableBody = document.querySelector('#reservationsTable tbody');
    const loadingReservations = document.getElementById('loadingReservations');
    const errorReservationsMessage = document.getElementById('errorReservationsMessage');
    const noReservationsMessage = document.getElementById('noReservationsMessage');

    // --- Змінні стану ---
    const API_BASE_URL = 'http://localhost:8080/api'; // Базовий URL вашого Spring Boot API
    let currentUserRole = null; // Зберігає роль поточного авторизованого користувача
    let currentUserId = null;   // Зберігає ID поточного авторизованого користувача

    // --- Допоміжні функції UI ---

    /**
     * Показує DOM-елемент, видаляючи клас 'hidden'.
     * @param {HTMLElement} element Елемент для відображення.
     */
    const showElement = (element) => element.classList.remove('hidden');

    /**
     * Приховує DOM-елемент, додаючи клас 'hidden'.
     * @param {HTMLElement} element Елемент для приховування.
     */
    const hideElement = (element) => element.classList.add('hidden');

    /**
     * Очищає вміст тіла таблиці.
     * @param {HTMLTableSectionElement} tableBody Тіло таблиці (tbody), яке потрібно очистити.
     */
    const clearTable = (tableBody) => { tableBody.innerHTML = ''; };

    // Перехоплення непередбачених помилок у скриптах
    window.onerror = function(message, source, lineno, colno, error) {
        const errorData = {
            message: message,
            url: source,
            line: lineno,
            user: localStorage.getItem('username') || 'anonymous', // Контекст користувача
            stack: error ? error.stack : ''
        };

        // Надсилаємо лог на сервер
        fetch('http://localhost:8080/api/logs/error', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(errorData)
        }).catch(err => console.error('Не вдалося надіслати лог на сервер', err));
    };

    // Логування дій користувача (наприклад, натискання кнопок)
    document.addEventListener('click', (event) => {
        if (event.target.tagName === 'BUTTON') {
            console.log(`User action: Clicked button "${event.target.textContent}"`);
            // Можна також відправляти важливі дії на сервер через аналогічний fetch
        }
    });

    // --- Функції логіну/виходу ---

    /**
     * Оновлює інтерфейс користувача відповідно до статусу авторизації.
     * Перевіряє наявність даних користувача у localStorage і відображає
     * відповідні секції (входу або основного контенту).
     * Якщо користувач авторизований, завантажує списки товарів та резервацій.
     */
    const updateUIForAuth = () => {
        const storedRole = localStorage.getItem('userRole');
        const storedUserId = parseInt(localStorage.getItem('userId')); // Перетворюємо на число
        const storedUsername = localStorage.getItem('username');

        // Перевіряємо, чи всі необхідні дані присутні та userId є дійсним числом
        if (storedRole && !isNaN(storedUserId) && storedUsername) {
            currentUserRole = storedRole;
            currentUserId = storedUserId;
            usernameDisplay.textContent = `Ви увійшли як: ${storedUsername} (${currentUserRole})`;
            showElement(usernameDisplay);
            showElement(logoutButton);
            hideElement(loginSection);
            showElement(mainContent);
            fetchProducts();     // Завантажуємо товари після входу
            fetchReservations(); // Завантажуємо резервації після входу
        } else {
            // Користувач не авторизований або дані некоректні
            currentUserRole = null;
            currentUserId = null;
            usernameDisplay.textContent = '';
            hideElement(usernameDisplay);
            hideElement(logoutButton);
            showElement(loginSection);
            hideElement(mainContent);
            clearTable(productsTableBody);
            clearTable(reservationsTableBody);
        }
    };

    /**
     * Обробляє вхід користувача. Надсилає облікові дані на бекенд,
     * зберігає дані користувача в localStorage у разі успіху та оновлює UI.
     */
    const loginUser = async () => {
        const username = loginUsernameInput.value;
        const password = loginPasswordInput.value;
        hideElement(loginErrorMessage); // Приховуємо попередні повідомлення про помилки

        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                // Зберігаємо отримані дані користувача
                localStorage.setItem('userRole', data.role);
                localStorage.setItem('userId', data.userId);
                localStorage.setItem('username', username);
                updateUIForAuth(); // Оновлюємо UI після успішного входу
            } else {
                // Обробка помилок входу з бекенду
                const errorText = await response.text();
                loginErrorMessage.textContent = `Помилка входу: ${errorText}`;
                showElement(loginErrorMessage);
            }
        } catch (error) {
            // Обробка мережевих помилок або проблем із сервером
            console.error('Помилка мережі або сервера під час входу:', error);
            loginErrorMessage.textContent = 'Не вдалося підключитися до сервера. Спробуйте пізніше.';
            showElement(loginErrorMessage);
        }
    };

    /**
     * Виконує вихід користувача. Очищає дані авторизації з localStorage
     * та оновлює інтерфейс користувача.
     */
    const logoutUser = () => {
        localStorage.removeItem('userRole');
        localStorage.removeItem('userId');
        localStorage.removeItem('username');
        updateUIForAuth(); // Оновлюємо UI після виходу
    };

    // --- Функції для товарів ---

    /**
     * Завантажує список товарів з бекенду. Може виконувати пошук за запитом.
     * Оновлює таблицю товарів та повідомлення про стан завантаження/помилки.
     * @param {string} [query=''] Необов'язковий рядок запиту для пошуку товарів.
     */
    const fetchProducts = async (query = '') => {
        clearTable(productsTableBody);
        hideElement(errorMessage);
        hideElement(noProductsMessage);
        showElement(loadingMessage);

        try {
            const url = `${API_BASE_URL}/products/search?query=${encodeURIComponent(query)}`;
            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const products = await response.json();

            hideElement(loadingMessage); // Приховуємо повідомлення про завантаження

            if (products.length === 0) {
                showElement(noProductsMessage); // Показуємо повідомлення про відсутність товарів
                return;
            }

            // Рендеринг товарів у таблиці
            products.forEach(product => {
                const row = productsTableBody.insertRow();

                row.insertCell().textContent = product.code || 'N/A';
                row.insertCell().textContent = product.article || 'N/A';
                row.insertCell().textContent = product.name || 'N/A';
                row.insertCell().textContent = product.manufacturer || 'N/A';
                row.insertCell().textContent = product.categoryName || 'N/A';
                row.insertCell().textContent = product.quantity !== null ? product.quantity : 'N/A';
                const displayedReservedQuantity = product.reservedQuantity !== null && product.reservedQuantity !== undefined ? product.reservedQuantity : 0;
                row.insertCell().textContent = displayedReservedQuantity;
                row.insertCell().textContent = product.price !== null ? product.price.toFixed(2) : 'N/A';
                row.insertCell().textContent = product.locationName || 'N/A';
                row.insertCell().textContent = product.lastUpdated ? new Date(product.lastUpdated).toLocaleString() : 'N/A';

                const actionsCell = row.insertCell();
                actionsCell.classList.add('actions-column');

                // Розраховуємо доступну кількість для резервування
                const currentReservedQuantity = product.reservedQuantity !== null && product.reservedQuantity !== undefined ? product.reservedQuantity : 0;
                const availableQuantity = product.quantity - currentReservedQuantity;

                if (availableQuantity > 0 && (currentUserRole === 'EMPLOYEE' || currentUserRole === 'WAREHOUSE_MANAGER')) {
                    const reserveButton = document.createElement('button');
                    reserveButton.textContent = 'Зарезервувати';
                    reserveButton.classList.add('action-button', 'reserve-button');
                    reserveButton.onclick = () => showReserveModal(product.id, product.name, availableQuantity);
                    actionsCell.appendChild(reserveButton);
                }
            });

        } catch (error) {
            console.error('Помилка при отриманні товарів:', error);
            hideElement(loadingMessage);
            errorMessage.textContent = `Не вдалося завантажити товари: ${error.message}. Перевірте роботу бекенду.`;
            showElement(errorMessage);
        }
    };

    // --- Функції для резервування ---

    /**
     * Відображає модальне вікно (prompt) для введення кількості резервації.
     * Викликає `reserveProduct` після успішного введення.
     * @param {number} productId ID товару.
     * @param {string} productName Назва товару.
     * @param {number} maxQuantity Максимальна доступна кількість для резервування.
     */
    const showReserveModal = (productId, productName, maxQuantity) => {
        if (!currentUserId || isNaN(currentUserId)) {
            alert('Помилка: Не вдалося визначити ID користувача. Будь ласка, спробуйте увійти знову.');
            return;
        }

        const quantity = prompt(`Скільки одиниць товару "${productName}" ви хочете зарезервувати? (Доступно: ${maxQuantity})`);
        if (quantity === null) {
            return;
        }
        const parsedQuantity = parseInt(quantity);

        if (isNaN(parsedQuantity) || parsedQuantity <= 0 || parsedQuantity > maxQuantity) {
            alert(`Будь ласка, введіть дійсне число від 1 до ${maxQuantity}.`);
            return;
        }
        reserveProduct(productId, parsedQuantity);
    };

    /**
     * Надсилає запит на бекенд для створення нової резервації.
     * @param {number} productId ID товару для резервування.
     * @param {number} quantity Кількість товару для резервування.
     */
    const reserveProduct = async (productId, quantity) => {
        try {
            const response = await fetch(`${API_BASE_URL}/reservations/reserve`, {
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
                // Локалізація та зрозумілі інструкції (вимога 100%)
                let userMessage = data.message;
                if (response.status === 500) {
                    userMessage = "На сервері виникла проблема. Спробуйте пізніше.";
                }

                // Виводимо повідомлення та ID помилки для звіту в підтримку
                alert(`Увага: ${userMessage}\n\nЯкщо помилка повторюється, повідомте код техпідтримці: ${data.errorId}`);
            }
        } catch (error) {
            // Обробка мережевих помилок
            console.error('Network error:', error);
            alert("Не вдалося зв'язатися з сервером. Перевірте підключення до мережі.");
        }
    };

    /**
     * Завантажує список всіх резервацій з бекенду та відображає їх у таблиці.
     * Додає кнопки "Виконати" та "Скасувати" залежно від ролі користувача та статусу резервації.
     */
    const fetchReservations = async () => {
        clearTable(reservationsTableBody);
        hideElement(errorReservationsMessage);
        hideElement(noReservationsMessage);
        showElement(loadingReservations);

        try {
            const response = await fetch(`${API_BASE_URL}/reservations`);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const reservations = await response.json();

            hideElement(loadingReservations);

            if (reservations.length === 0) {
                showElement(noReservationsMessage);
                return;
            }

            // Рендеринг резервацій у таблиці
            reservations.forEach(reservation => {
                const row = reservationsTableBody.insertRow();
                row.insertCell().textContent = reservation.id;
                // Зверніть увагу на доступ до вкладених об'єктів (product.code, product.name)
                row.insertCell().textContent = reservation.product ? reservation.product.code : 'N/A';
                row.insertCell().textContent = reservation.product ? reservation.product.name : 'N/A';
                row.insertCell().textContent = reservation.quantity;
                row.insertCell().textContent = reservation.reservationDate ? new Date(reservation.reservationDate).toLocaleString() : 'N/A';
                row.insertCell().textContent = reservation.status;

                const actionsCell = row.insertCell();
                actionsCell.classList.add('actions-column');

                // Додаємо кнопки дій залежно від статусу резервації та ролі користувача
                if (reservation.status === 'PENDING') {
                    if (currentUserRole === 'WAREHOUSE_MANAGER') {
                        const fulfillButton = document.createElement('button');
                        fulfillButton.textContent = 'Виконати';
                        fulfillButton.classList.add('action-button', 'fulfill-button');
                        fulfillButton.onclick = () => fulfillReservation(reservation.id);
                        actionsCell.appendChild(fulfillButton);
                    }

                    if (currentUserRole === 'EMPLOYEE' || currentUserRole === 'WAREHOUSE_MANAGER') {
                        const cancelButton = document.createElement('button');
                        cancelButton.textContent = 'Скасувати';
                        cancelButton.classList.add('action-button', 'cancel-button');
                        cancelButton.onclick = () => cancelReservation(reservation.id);
                        actionsCell.appendChild(cancelButton);
                    }
                }
            });

        } catch (error) {
            console.error('Помилка при отриманні резервацій:', error);
            hideElement(loadingReservations);
            errorReservationsMessage.textContent = `Не вдалося завантажити резервації: ${error.message}.`;
            showElement(errorReservationsMessage);
        }
    };

    /**
     * Надсилає запит на бекенд для виконання резервації.
     * Доступно тільки для користувачів з роллю 'WAREHOUSE_MANAGER'.
     * @param {number} reservationId ID резервації для виконання.
     */
    const fulfillReservation = async (reservationId) => {
        if (currentUserRole !== 'WAREHOUSE_MANAGER') {
            alert('У вас немає дозволу на виконання резервацій.');
            return;
        }
        if (!confirm(`Ви впевнені, що хочете виконати резервацію ID ${reservationId}?`)) {
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/reservations/${reservationId}/fulfill`, {
                method: 'PUT', // Використовуємо PUT для оновлення ресурсу
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                alert(`Резервацію ID ${reservationId} успішно виконано.`);
                fetchProducts();     // Оновити список товарів
                fetchReservations(); // Оновити список резервацій
            } else {
                const errorText = await response.text();
                alert(`Помилка виконання резервації: ${errorText}`);
                console.error('Fulfill failed:', errorText);
            }
        } catch (error) {
            console.error('Помилка мережі або сервера під час виконання:', error);
            alert('Помилка мережі. Спробуйте пізніше.');
        }
    };

    /**
     * Надсилає запит на бекенд для скасування резервації.
     * Доступно для користувачів з ролями 'EMPLOYEE' або 'WAREHOUSE_MANAGER'.
     * @param {number} reservationId ID резервації для скасування.
     */
    const cancelReservation = async (reservationId) => {
        if (!confirm(`Ви впевнені, що хочете скасувати резервацію ID ${reservationId}?`)) {
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/reservations/${reservationId}/cancel`, {
                method: 'PUT', // Використовуємо PUT для оновлення ресурсу
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                alert(`Резервацію ID ${reservationId} успішно скасовано.`);
                fetchProducts();     // Оновити список товарів
                fetchReservations(); // Оновити список резервацій
            } else {
                const errorText = await response.text();
                alert(`Помилка скасування резервації: ${errorText}`);
                console.error('Cancel failed:', errorText);
            }
        } catch (error) {
            console.error('Помилка мережі або сервера під час скасування:', error);
            alert('Помилка мережі. Спробуйте пізніше.');
        }
    };

    // --- Обробники подій ---
    loginButton.addEventListener('click', loginUser);
    logoutButton.addEventListener('click', logoutUser);

    searchButton.addEventListener('click', () => {
        const query = searchInput.value.trim();
        fetchProducts(query);
    });

    searchInput.addEventListener('keypress', (event) => {
        if (event.key === 'Enter') {
            searchButton.click();
        }
    });

    // --- Ініціалізація при завантаженні сторінки ---
    updateUIForAuth(); // Перевіряємо статус авторизації при завантаженні
});