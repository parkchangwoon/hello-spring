import style from "../styles/HireWrite.module.scss";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useInput } from "../hooks/useInput";

const HireWrite = () => {
  const [title, handleTitleChange] = useInput("");
  const [salary, handleSalaryChange] = useInput("");
  const [content, handleContentChange] = useInput("");

  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      const res = await axios.post(
        "http://localhost:8080/hire/new",
        {
          title: title,
          salary: salary,
          content: content,
        },
        { withCredentials: true }
      );
      alert(res.data);
      navigate("/board/채용공고게시판");
    } catch (error) {
      console.log(error.response.data);
      alert(error.response.data);
    }
  };

  return (
    <div className={style.container}>
      <input
        type="text"
        placeholder="제목"
        value={title}
        onChange={handleTitleChange}
      />
      <input
        type="number"
        placeholder="급여"
        value={salary}
        onChange={handleSalaryChange}
      />
      <textarea
        placeholder="내용"
        value={content}
        onChange={handleContentChange}
      />
      <div className={style.buttonContainer}>
        <button onClick={handleSubmit}>등록</button>
        <button>취소</button>
      </div>
    </div>
  );
};

export default HireWrite;
