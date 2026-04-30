/**
 * Frontend Application for EcommerceAPI
 * 
 * Uses Fetch API to consume RESTful backend services
 * Demonstrates async/await, error handling, and DOM manipulation
 */

// Configuration
const API_BASE_URL = 'http://localhost:8080/api/v1/products';

// DOM Elements
const productGrid = document.getElementById('productGrid');
const filterType = document.getElementById('filterType');
const filterValue = document.getElementById('filterValue');
const filterValueGroup = document.getElementById('filterValueGroup');
const applyFilterBtn = document.getElementById('applyFilterBtn');
const clearFilterBtn = document.getElementById('clearFilterBtn');
const loadingSpinner = document.getElementById('loadingSpinner');
const errorMessage = document.getElementById('errorMessage');
const emptyState = document.getElementById('emptyState');

// State
let allProducts = [];
let currentFilter = null;

/**
 * Fetches all products from the API
 * Uses async/await pattern with proper error handling
 */
async function fetchProducts() {
    showLoading(true);
    hideError();
    hideEmptyState();

    try {
        // Make API request
        const response = await fetch(API_BASE_URL, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        });

        // Check if response is successful (2xx status)
        if (!response.ok) {
            // Manually throw error since fetch only rejects on network failure
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        // Parse JSON response
        const data = await response.json();
        
        // Handle empty result
        if (!Array.isArray(data) || data.length === 0) {
            allProducts = [];
            showEmptyState();
            showLoading(false);
            return;
        }

        // Store products and render
        allProducts = data;
        renderProducts(allProducts);
        showLoading(false);

        console.log('✅ Successfully fetched products:', allProducts);

    } catch (error) {
        // Handle different error types
        console.error('❌ Fetch error:', error);
        
        let errorMsg = 'Failed to load products. ';
        if (error instanceof TypeError) {
            errorMsg += 'Network error or CORS issue. Check console.';
        } else if (error instanceof SyntaxError) {
            errorMsg += 'Invalid JSON response from server.';
        } else {
            errorMsg += error.message;
        }

        showError(errorMsg);
        showLoading(false);
    }
}

/**
 * Fetches filtered products from the API
 * 
 * @param {string} type - Filter type (category, name, price)
 * @param {string} value - Filter value
 */
async function fetchFilteredProducts(type, value) {
    if (!type || !value) {
        showError('Please select a filter type and enter a value');
        return;
    }

    showLoading(true);
    hideError();
    hideEmptyState();

    try {
        const filterUrl = `${API_BASE_URL}/filter?filterType=${encodeURIComponent(type)}&filterValue=${encodeURIComponent(value)}`;
        
        const response = await fetch(filterUrl, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();

        if (!Array.isArray(data) || data.length === 0) {
            showEmptyState();
            showLoading(false);
            return;
        }

        renderProducts(data);
        showLoading(false);
        currentFilter = { type, value };

        console.log(`✅ Filtered products by ${type}:`, data);

    } catch (error) {
        console.error('❌ Filter error:', error);
        showError('Failed to filter products: ' + error.message);
        showLoading(false);
    }
}

/**
 * Fetches in-stock products
 */
async function fetchInStockProducts() {
    showLoading(true);
    hideError();
    hideEmptyState();

    try {
        // For in-stock filter, we use the existing filter endpoint
        // Since the backend service has a getProductsInStock() method
        // We'll filter by a custom approach or fetch all and filter client-side
        
        const filteredProducts = allProducts.filter(p => p.stockQuantity > 0);
        
        if (filteredProducts.length === 0) {
            showEmptyState();
            showLoading(false);
            return;
        }

        renderProducts(filteredProducts);
        showLoading(false);
        currentFilter = { type: 'stock', value: 'in-stock' };

        console.log('✅ Filtered in-stock products:', filteredProducts);

    } catch (error) {
        console.error('❌ In-stock filter error:', error);
        showError('Failed to filter in-stock products');
        showLoading(false);
    }
}

/**
 * Renders product cards to the DOM
 * Dynamically creates HTML for each product
 * 
 * @param {Array} products - Array of product objects
 */
function renderProducts(products) {
    // Clear existing content
    productGrid.innerHTML = '';

    if (!products || products.length === 0) {
        showEmptyState();
        return;
    }

    // Create product card for each product
    products.forEach(product => {
        const productCard = createProductCard(product);
        productGrid.appendChild(productCard);
    });
}

/**
 * Creates a product card DOM element
 * 
 * @param {Object} product - Product object from API
 * @returns {HTMLElement} Product card element
 */
function createProductCard(product) {
    const card = document.createElement('div');
    card.className = 'product-card';

    // Determine stock status
    const inStock = product.stockQuantity > 0;
    const stockClass = inStock ? 'in-stock' : 'out-of-stock';
    const stockText = inStock ? `${product.stockQuantity} in stock` : 'Out of stock';

    // Build HTML
    card.innerHTML = `
        <div class="product-image">
            ${product.imageUrl ? `<img src="${escapeHtml(product.imageUrl)}" alt="${escapeHtml(product.name)}">` : '📦'}
        </div>
        <div class="product-body">
            <h3 class="product-name">${escapeHtml(product.name)}</h3>
            <p class="product-category">${escapeHtml(product.category?.name || 'Unknown Category')}</p>
            <p class="product-description">${escapeHtml(product.description)}</p>
            <div class="product-footer">
                <span class="product-price">$${product.price.toFixed(2)}</span>
                <span class="product-stock ${stockClass}">${stockText}</span>
            </div>
        </div>
    `;

    return card;
}

/**
 * Escapes HTML special characters to prevent XSS
 * 
 * @param {string} text - Text to escape
 * @returns {string} Escaped text
 */
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}

/**
 * UI Helper Functions
 */

function showLoading(show) {
    loadingSpinner.style.display = show ? 'block' : 'none';
}

function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
}

function hideError() {
    errorMessage.style.display = 'none';
}

function showEmptyState() {
    emptyState.style.display = 'block';
    productGrid.innerHTML = '';
}

function hideEmptyState() {
    emptyState.style.display = 'none';
}

/**
 * Event Listeners
 */

// Show/hide filter value input based on selected filter type
filterType.addEventListener('change', (e) => {
    if (e.target.value) {
        filterValueGroup.style.display = 'flex';
        filterValue.focus();
    } else {
        filterValueGroup.style.display = 'none';
    }
});

// Apply filter
applyFilterBtn.addEventListener('click', () => {
    const type = filterType.value;
    const value = filterValue.value.trim();

    if (type === 'stock') {
        fetchInStockProducts();
    } else {
        fetchFilteredProducts(type, value);
    }
});

// Clear filters
clearFilterBtn.addEventListener('click', () => {
    filterType.value = '';
    filterValue.value = '';
    filterValueGroup.style.display = 'none';
    currentFilter = null;
    fetchProducts();
});

/**
 * Initialization
 * Fetch products when page loads
 */
document.addEventListener('DOMContentLoaded', () => {
    console.log('📚 Initializing EcommerceAPI Frontend');
    fetchProducts();
});

// Optional: Add keyboard support for Enter key in filter
filterValue.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        applyFilterBtn.click();
    }
});

console.log('✅ Frontend script loaded successfully');
