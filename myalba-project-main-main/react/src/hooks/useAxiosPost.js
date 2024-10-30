import axios from "axios";
import { useState } from "react";

export const useAxiosPost = (
  url = "",
  input = "",
  message = "",
  navigate = "",
  session = false,
  setCurrentUser = "",
  isFormData = false,
  setState = ""
) => {
  const [error, setError] = useState("");

  const handleSubmit = async (formData = null) => {
    setError("");
    try {
      let options = session ? { withCredentials: true } : {};

      if (isFormData) {
        options = {
          withCredentials: true,
          headers: {
            "Content-Type": "multipart/form-data",
          },
        };
      }

      const data = isFormData ? formData : input;
      const res = await axios.post(url, data, options);
      if (setState) {
        setState("");
      }
      if (setCurrentUser) {
        setCurrentUser(res.data);
        localStorage.setItem("currentUser", JSON.stringify(res.data));
      }
      if (message) {
        alert(message);
      }
      if (navigate) {
        navigate();
      }
    } catch (error) {
      setError(error.response ? error.response.data : "");
      console.log(error);
    }
  };

  return { error, handleSubmit };
};
