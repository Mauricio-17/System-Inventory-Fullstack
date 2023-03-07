import fetch from "unfetch";

const URL = "/api/location";

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

export const getAllLocations = () =>
  fetch(`${URL}`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const getLocationById = (id) =>
  fetch(`${URL}/${id}`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const getLocationsByShelfId = (shelfId) =>
  fetch(`${URL}/shelf/${shelfId}`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const addNewLocation = (location) =>
  fetch(URL, {
    headers: getHeaders(),
    method: "POST",
    body: JSON.stringify(location),
  }).then(checkStatus);

export const updateLocation = (location, id) =>
  fetch(`${URL}/${id}`, {
    headers: getHeaders(),
    method: "PUT",
    body: JSON.stringify(location),
  }).then(checkStatus);

export const removeLocation = (locationId) =>
  fetch(`${URL}/${locationId}`, {
    headers: getHeaders(),
    method: "DELETE",
  }).then(checkStatus);
