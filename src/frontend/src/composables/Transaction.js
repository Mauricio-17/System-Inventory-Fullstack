import fetch from "unfetch";

const URL = "/api/transaction";

const getHeaders = () => {
  return {
    Accept: "application/json",
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("token")}`
  };
};

const checkStatus = (response) => {
  if (response.ok) {
    return response;
  }
  // convert non-2xx HTTP responses into errors:
  const error = new Error(response.statusText);
  error.response = response;
  return Promise.reject(error);
};

export const getAllTransactions = () =>
  fetch(`${URL}`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const addNewTransaction = (transaction) =>
  fetch(URL, {
    headers: getHeaders(),
    method: "POST",
    body: JSON.stringify(transaction),
  }).then(checkStatus);

export const removeTransaction = (transactionId) =>
  fetch(`${URL}/${transactionId}`, {
    headers: getHeaders(),
    method: "DELETE",
  }).then(checkStatus);
