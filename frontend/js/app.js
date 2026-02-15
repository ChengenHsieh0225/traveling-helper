import { CurrencyPageHandler } from "./pages/currency.js";
import { WeatherHandler } from "./pages/weather.js";

let currentPageID = '';
const pages = {
    'currency-page': CurrencyPageHandler,
    'weather-page': WeatherHandler,
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

const currencyNavButton = document.getElementById('btn-currency');
const weatherNavButton = document.getElementById('btn-weather');

[currencyNavButton, weatherNavButton].forEach(navButton => {
    navButton.addEventListener('click', (e) => {
        router(navButton?.dataset.pageId);
    });
})