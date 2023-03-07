import fetch from "unfetch";

const URL = "/auth/role";

const getHeaders = () => {
  return {
    Accept: "application/json",
    "Content-Type": "application/json"
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

export const getAllRoles = () =>
  fetch(URL, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const addNewRole = (role) =>
  fetch(URL, {
    headers: getHeaders(),
    method: "POST",
    body: JSON.stringify(role),
  }).then(checkStatus);

export const removeRole = (roleId) =>
  fetch(`${URL}/${roleId}`, {
    headers: getHeaders(),
    method: "DELETE",
  }).then(checkStatus);
