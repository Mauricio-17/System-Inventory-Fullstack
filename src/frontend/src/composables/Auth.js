import fetch from "unfetch";

const URL = "/auth";

const checkStatus = (response) => {
  if (response.ok) {
    return response;
  }
  // convert non-2xx HTTP responses into errors:
  const error = new Error(response.statusText);
  error.response = response;
  return Promise.reject(error);
};

export const login = (user) =>
  fetch(`${URL}/login`, {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(user),
  }).then(checkStatus);

export const register = (user) =>
  fetch(`${URL}/register`, {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(user)
  });


export const logOut = () => {
  
}


