import fetch from "unfetch";

const URL = "/api/shelf";

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

export const getAllShelves = () =>
  fetch(`${URL}`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const getShelfByLocationId = (locationId) =>
  fetch(`${URL}/location/${locationId}`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const addNewShelf = (shelf) =>
  fetch(URL, {
    headers: getHeaders(),
    method: "POST",
    body: JSON.stringify(shelf),
  }).then(checkStatus);

export const updateShelf = (shelf, id) =>
  fetch(`${URL}/${id}`, {
    headers: getHeaders(),
    method: "PUT",
    body: JSON.stringify(shelf),
  }).then(checkStatus);

export const removeShelf = (shelfId) =>
  fetch(`${URL}/${shelfId}`, {
    headers: getHeaders(),
    method: "DELETE",
  }).then(checkStatus);
