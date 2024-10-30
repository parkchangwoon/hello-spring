import axios from "axios";
import { useState } from "react";

export const useAxiosPut = (
  url = "",
  input = "",
  message = "",
  navigate = "",
  session = false,
  isFormData = false,
  setState = ""
) => {
  const [error, setError] = useState("");

  const handleSubmit = async (formData = null) => {
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

      await axios.put(url, data, options);
      if (setState) {
        setState("");
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
