import fetch from "unfetch";

const URL = "/api/equipment";

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

export const getAllEquipments = () =>
  fetch(`${URL}/view`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const addNewEquipment = (equipment) =>
  fetch(URL, {
    headers: getHeaders(),
    method: "POST",
    body: JSON.stringify(equipment),
  }).then(checkStatus);

export const updateEquipment = (equipment, id) =>
  fetch(`${URL}/${id}`, {
    headers: getHeaders(),
    method: "PUT",
    body: JSON.stringify(equipment),
  }).then(checkStatus);

export const removeEquipment = (equipmentId) =>
  fetch(`${URL}/${equipmentId}`, {
    headers: getHeaders(),
    method: "DELETE",
  }).then(checkStatus);
