import style from "../styles/BrandAuthWrite.module.scss";
import "react-quill/dist/quill.snow.css";
import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import ReactQuill from "react-quill";
import axios from "axios";
import { Context } from "../context/Context";

const BrandAuthWrite = () => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [brandName, setBrandName] = useState("");
  const [images, setImages] = useState([]);
  const [error, setError] = useState(null);

  const { brandArray } = useContext(Context);
  const navigate = useNavigate();

  const handleImageChange = (e) => {
    setImages(e.target.files);
  };

  const handleSubmit = async (formData) => {
    const url = `http://localhost:8080/allow/new/${brandName}`;
    try {
      await axios.post(url, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        withCredentials: true,
      });
      alert("브랜드 인증 게시글이 등록되었습니다.");
      navigate("/"); // 성공 후 리디렉션
    } catch (error) {
      console.error("Error posting data:", error);
      setError(error.response ? error.response.data : "Unknown error");
    }
  };

  const onSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("title", title);
    formData.append("content", content);
    for (let i = 0; i < images.length; i++) {
      formData.append("images", images[i]);
    }

    // formData의 내용을 확인하는 콘솔 로그 추가
    for (let pair of formData.entries()) {
      console.log(pair[0] + ": " + pair[1]);
    }

    handleSubmit(formData); // formData 전달
  };

  return (
    <div className={style.container}>
      <input
        className={style.title}
        type="text"
        placeholder="제목"
        value={title}
        onChange={(e) => {
          setTitle(e.target.value);
        }}
      />
      <select
        value={brandName}
        onChange={(e) => {
          setBrandName(e.target.value);
        }}
      >
        <option value="">브랜드 선택</option>
        {brandArray.map((item1) =>
          item1.brand.map((item2, index) => (
            <option key={index} value={item2}>
              {item2}
            </option>
          ))
        )}
      </select>
      <div className={style.reactQuillContainer}>
        <ReactQuill
          value={content}
          className={style.reactQuill}
          onChange={setContent}
        />
      </div>
      <input
        className={style.file}
        type="file"
        multiple
        onChange={handleImageChange}
      />
      <div className={style.buttonContainer}>
        <button onClick={onSubmit}>등록</button>
        <button onClick={() => navigate("/")}>취소</button>
        {error && <p className={style.errorMessage}>{error}</p>}
      </div>
    </div>
  );
};

export default BrandAuthWrite;
