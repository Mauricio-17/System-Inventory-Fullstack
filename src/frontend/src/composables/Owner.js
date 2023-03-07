import fetch from "unfetch";

const URL = "/api/admin/owner";

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

export const getAllOwners = () =>
  fetch(`/api/owner`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const addNewOwner = (owner) =>
  fetch(URL, {
    headers: getHeaders(),
    method: "POST",
    body: JSON.stringify(owner),
  }).then(checkStatus);

export const updateOwner = (owner, ownerId) =>
  fetch(`${URL}/${ownerId}`, {
    headers: getHeaders(),
    method: "PUT",
    body: JSON.stringify(owner),
  }).then(checkStatus);

export const removeOwner = (ownerId) =>
  fetch(`${URL}/${ownerId}`, {
    headers: getHeaders(),
    method: "DELETE",
  }).then(checkStatus);
