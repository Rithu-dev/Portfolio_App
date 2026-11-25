// Stock Portfolio frontend
// All protected API calls use the JWT saved by the login page.

const SAMPLE_PRICES = {
  TCS: 3700,
  INFY: 1700,
  RELIANCE: 1450,
  HDFCBANK: 1680,
  ITC: 440,
};

let currentUser = null;
let apiHoldings = [];
let apiSummary = null;
let apiTransactions = [];

const money = (amount) =>
  "₹ " +
  Number(amount || 0).toLocaleString("en-IN", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });

function getToken() {
  return localStorage.getItem("token");
}

async function apiRequest(url, options = {}) {
  const token = getToken();

  if (!token) {
    window.location.href = "/login.html";
    throw new Error("Your session has expired. Please log in again.");
  }

  const headers = {
    ...(options.headers || {}),
    Authorization: "Bearer " + token,
  };

  if (options.body && !headers["Content-Type"]) {
    headers["Content-Type"] = "application/json";
  }

  const response = await fetch(url, { ...options, headers });

  if (response.status === 401) {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
    throw new Error("Your session has expired. Please log in again.");
  }

  let responseBody = null;
  const responseText = await response.text();

  if (responseText) {
    try {
      responseBody = JSON.parse(responseText);
    } catch {
      responseBody = responseText;
    }
  }

  if (!response.ok) {
    const message =
      responseBody?.message ||
      responseBody?.error ||
      (typeof responseBody === "string" ? responseBody : null) ||
      `Request failed (${response.status})`;
    throw new Error(message);
  }

  return responseBody;
}

function toast(message) {
  const element = document.getElementById("toast");
  if (!element) return;

  element.textContent = message;
  element.style.display = "block";

  window.clearTimeout(toast.timeoutId);
  toast.timeoutId = window.setTimeout(() => {
    element.style.display = "none";
  }, 3000);
}

function dateFmt(value) {
  if (!value) return "—";

  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "—";

  return date.toLocaleString("en-IN", {
    dateStyle: "medium",
    timeStyle: "short",
  });
}

function getHoldings() {
  return apiHoldings.map((stock) => {
    const symbol = String(stock.stockName || "").toUpperCase();
    const currentPrice = SAMPLE_PRICES[symbol] ?? Number(stock.price || 0);
    const quantity = Number(stock.quantity || 0);
    const averagePrice = Number(stock.price || 0);
    const cost = quantity * averagePrice;
    const value = quantity * currentPrice;

    return {
      symbol,
      qty: quantity,
      avg: averagePrice,
      current: currentPrice,
      cost,
      value,
      pnl: value - cost,
    };
  });
}

function render() {
  const holdings = getHoldings();

  const totalInvestment = apiSummary
    ? Number(apiSummary.totalInvestment || 0)
    : holdings.reduce((total, holding) => total + holding.cost, 0);

  const currentValue = holdings.reduce(
    (total, holding) => total + holding.value,
    0,
  );
  const profitLoss = currentValue - totalInvestment;
  const totalShares = holdings.reduce(
    (total, holding) => total + holding.qty,
    0,
  );

  document.getElementById("investment").textContent = money(totalInvestment);
  document.getElementById("currentValue").textContent = money(currentValue);

  const pnlElement = document.getElementById("pnl");
  pnlElement.textContent =
    (profitLoss > 0 ? "+" : "") + money(profitLoss);
  pnlElement.className = "value " + (profitLoss >= 0 ? "green" : "red");

  document.getElementById("pnlPct").textContent =
    (totalInvestment ? (profitLoss / totalInvestment) * 100 : 0).toFixed(2) +
    "% unrealized return";

  document.getElementById("stockCount").textContent = holdings.length;
  document.getElementById("shareCount").textContent =
    totalShares + " shares in total";

  document.getElementById("holdingsBody").innerHTML = holdings
    .map(
      (holding) => `
        <tr>
          <td>
            <div class="symbol">${holding.symbol}</div>
            <div class="company">${holding.symbol === "TCS" ? "Tata Consultancy Services" : holding.symbol === "INFY" ? "Infosys Ltd." : "Portfolio holding"}</div>
          </td>
          <td>${holding.qty}</td>
          <td>${money(holding.avg)}</td>
          <td>${money(holding.current)}</td>
          <td>${money(holding.value)}</td>
          <td class="${holding.pnl >= 0 ? "green" : "red"}">${holding.pnl >= 0 ? "+" : ""}${money(holding.pnl)}</td>
        </tr>
      `,
    )
    .join("");

  document.getElementById("holdingsEmpty").hidden = holdings.length > 0;

  const sortedTransactions = [...apiTransactions].sort(
    (a, b) =>
      new Date(b.transactionDate || b.createdAt || 0) -
      new Date(a.transactionDate || a.createdAt || 0),
  );

  document.getElementById("transactionsBody").innerHTML = sortedTransactions
    .map(
      (transaction) => `
        <tr>
          <td>${dateFmt(transaction.transactionDate || transaction.createdAt)}</td>
          <td>
            <span class="pill ${transaction.type === "BUY" ? "buy-pill" : "sell-pill"}">
              ${transaction.type}
            </span>
          </td>
          <td><span class="symbol">${transaction.stockName}</span></td>
          <td>${transaction.quantity}</td>
          <td>${money(transaction.price)}</td>
          <td>${money(Number(transaction.quantity) * Number(transaction.price))}</td>
        </tr>
      `,
    )
    .join("");

  document.getElementById("transactionsEmpty").hidden =
    sortedTransactions.length > 0;

  updateAvailable();
}

async function loadPortfolio() {
  try {
    // Resolve the logged-in user from the JWT. Never hardcode a user ID.
    currentUser = await apiRequest("/users/me");

    document.getElementById("userName").textContent =
      "Welcome, " + currentUser.name + "!";
    document.getElementById("userEmail").textContent = currentUser.email;
    document.getElementById("userId").textContent =
      "User ID: " + currentUser.id;

    const userId = currentUser.id;

    const [holdings, summary, transactions] = await Promise.all([
      apiRequest(`/portfolio/user/${userId}`),
      apiRequest(`/portfolio/user/${userId}/summary`),
      apiRequest(`/transactions/user/${userId}`),
    ]);

    apiHoldings = Array.isArray(holdings) ? holdings : [];
    apiSummary = summary || null;
    apiTransactions = Array.isArray(transactions) ? transactions : [];

    render();
  } catch (error) {
    console.error("Failed to load portfolio:", error);
    toast(error.message || "Could not load portfolio from the backend.");
  }
}

function showPage(id) {
  document.querySelectorAll(".section").forEach((section) => {
    section.classList.toggle("active", section.id === id);
  });

  document.querySelectorAll(".nav button").forEach((button) => {
    button.classList.toggle("active", button.dataset.page === id);
  });

  window.scrollTo({ top: 0, behavior: "smooth" });
}

function updateTotal(prefix) {
  const quantity =
    Number(document.getElementById(prefix + "Qty").value) || 0;
  const price =
    Number(document.getElementById(prefix + "Price").value) || 0;

  document.getElementById(prefix + "Total").textContent =
    money(quantity * price);
}

function updateAvailable() {
  const symbol = document
    .getElementById("sellSymbol")
    .value.trim()
    .toUpperCase();

  const holding = getHoldings().find((item) => item.symbol === symbol);

  document.getElementById("availableShares").textContent = !symbol
    ? "Enter a symbol"
    : holding
      ? holding.qty + " shares"
      : "No shares held";
}

async function submitTransaction(type, event) {
  event.preventDefault();

  const prefix = type.toLowerCase();
  const symbol = document
    .getElementById(prefix + "Symbol")
    .value.trim()
    .toUpperCase();
  const quantity = Number(document.getElementById(prefix + "Qty").value);
  const price = Number(document.getElementById(prefix + "Price").value);

  if (!/^[A-Z0-9.-]{1,12}$/.test(symbol)) {
    toast("Enter a valid stock symbol.");
    return;
  }

  if (!Number.isInteger(quantity) || quantity < 1) {
    toast("Quantity must be a positive whole number.");
    return;
  }

  if (!Number.isFinite(price) || price <= 0) {
    toast("Price must be greater than zero.");
    return;
  }

  if (type === "SELL") {
    const holding = getHoldings().find((item) => item.symbol === symbol);

    if (!holding) {
      toast("You do not currently hold " + symbol + ".");
      return;
    }

    if (quantity > holding.qty) {
      toast(
        "You only hold " + holding.qty + " share(s) of " + symbol + ".",
      );
      return;
    }
  }

  const submitButton = event.submitter;
  if (submitButton) submitButton.disabled = true;

  try {
    // The backend associates the transaction with the authenticated user.
    // Do not send a user ID from the browser.
    await apiRequest("/transactions", {
      method: "POST",
      body: JSON.stringify({
        type,
        stockName: symbol,
        quantity,
        price,
      }),
    });

    event.target.reset();
    updateTotal(prefix);
    await loadPortfolio();
    showPage("dashboard");
    toast(type + " transaction saved successfully.");
  } catch (error) {
    console.error(type + " transaction failed:", error);
    toast(error.message || "Could not save the transaction.");
  } finally {
    if (submitButton) submitButton.disabled = false;
  }
}

document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll("[data-page]").forEach((button) => {
    button.addEventListener("click", () => showPage(button.dataset.page));
  });

  document.querySelectorAll("[data-goto]").forEach((button) => {
    button.addEventListener("click", () => showPage(button.dataset.goto));
  });

  ["buy", "sell"].forEach((prefix) => {
    ["Qty", "Price"].forEach((suffix) => {
      document
        .getElementById(prefix + suffix)
        .addEventListener("input", () => updateTotal(prefix));
    });
  });

  document
    .getElementById("sellSymbol")
    .addEventListener("input", updateAvailable);

  document.getElementById("buyForm").addEventListener("submit", (event) => {
    submitTransaction("BUY", event);
  });

  document.getElementById("sellForm").addEventListener("submit", (event) => {
    submitTransaction("SELL", event);
  });

  document.getElementById("logoutBtn").addEventListener("click", () => {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
  });

  loadPortfolio();
});
