import axios from 'axios';

const apiClient = axios.create({
  baseURL: '/api/v1',
  timeout: 60000,
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      if (error.response.status === 429) {
        alert('Too Many Requests. Please slow down and try again later.');
      } else if (error.response.status === 503) {
        alert('Service Unavailable. The AI extraction service might be down.');
      }
    } else if (error.request) {
      alert('Network error. Please check if the backend server is running.');
    }
    return Promise.reject(error);
  }
);

export default apiClient;
