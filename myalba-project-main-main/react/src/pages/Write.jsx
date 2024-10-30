import style from "../styles/Write.module.scss";
import "react-quill/dist/quill.snow.css";
import { useState, useContext } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { Context } from "../context/Context";
import ReactQuill from "react-quill";
import { useAxiosPost } from "../hooks/useAxiosPost";
import { useAxiosPut } from "../hooks/useAxiosPut";

const Write = () => {
  const location = useLocation();
  const singleUpdatePost = location.state?.single || {};

  const [title, setTitle] = useState(singleUpdatePost.title || "");
  const [content, setContent] = useState(singleUpdatePost.content || "");
  const [images, setImages] = useState(singleUpdatePost.images || []);

  const { isToggled } = useContext(Context);

  const navigate = useNavigate();

  const { id } = useParams();

  const isUpdate = singleUpdatePost.id; // 새로운 글 작성인가 글 수정인가 판단

  const { error: postError, handleSubmit: handlePostSubmit } = useAxiosPost(
    id === "통합게시판"
      ? "http://localhost:8080/posts/new"
      : `http://localhost:8080/brands/${id.slice(2)}/posts/${
          isToggled ? "boss" : "employee"
        }/new`,
    "",
    "게시글이 등록되었습니다.",
    id === "통합게시판"
      ? () => navigate("/board/통합게시판")
      : () => navigate(`/gallery/${id}`),
    true,
    "",
    true,
    ""
  );

  const { error: putError, handleSubmit: handlePutSubmit } = useAxiosPut(
    `http://localhost:8080/posts/${singleUpdatePost.id}`,
    "",
    "게시글이 수정되었습니다.",
    () => navigate(`/single/${singleUpdatePost.id}`),
    true,
    true,
    ""
  );

  const handleImageChange = (e) => {
    setImages(e.target.files);
  };

  const onSubmit = () => {
    const formData = new FormData();
    formData.append("title", title);
    formData.append("content", content);
    for (let i = 0; i < images.length; i++) {
      formData.append("images", images[i]);
    }

    if (isUpdate) {
      handlePutSubmit(formData);
    } else {
      handlePostSubmit(formData);
    }
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
        <button onClick={onSubmit}>{isUpdate ? "수정" : "등록"}</button>
        <button onClick={() => navigate(-1)}>취소</button>
        {(postError || putError) && (
          <p className={style.errorMessage}>
            {JSON.stringify(postError || putError)}
          </p>
        )}
      </div>
    </div>
  );
};

export default Write;
