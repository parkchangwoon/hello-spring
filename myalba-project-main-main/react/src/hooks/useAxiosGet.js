import axios from "axios";
import { useState, useEffect } from "react";

export const useAxiosGet = (url = "", session = false) => {
  const [data, setData] = useState("");

  useEffect(() => {
    const getData = async () => {
      try {
        let options = session ? { withCredentials: true } : {};
        const res = await axios.get(url, options);
        setData(res.data);
      } catch (error) {
        console.log(error);
      }
    };
    getData();
  }, [url, session]);

  return { data, setData };
};
