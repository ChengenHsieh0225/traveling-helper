import { CurrencyPageHandler } from "./pages/currency.js";

let currentPageID = '';
const pages = {
    'currency-page': CurrencyPageHandler
};

const pageContainer = document.getElementById('main-page-container');

function router(pageID) {
    const template = document.getElementById(pageID);
    const clone = template.content.cloneNode(true);

    pageContainer.innerHTML = '';
    pageContainer.appendChild(clone);
    currentPageID = pageID;

    // Initialize the page
    const pageHandler = pages[currentPageID];
    if (pageHandler && typeof pageHandler.init === 'function') {
        pageHandler.init();
    }
}

router('currency-page');

function getEventHandlerFunctionName(eventType) {
    return `handle${eventType.charAt(0).toUpperCase()}${eventType.slice(1)}`;
}

['click', 'change', 'input'].forEach(eventType => {
    pageContainer.addEventListener(eventType, (e) => {
        const pageHandler = pages[currentPageID];
        const handleFunctionName = getEventHandlerFunctionName(eventType);
        if (pageHandler?.[handleFunctionName]) {
            pageHandler[handleFunctionName](e);
        }
    });
});