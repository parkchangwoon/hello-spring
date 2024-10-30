import style from "../styles/LoginModal.module.scss";
import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { Modal } from "react-bootstrap";
import { useInput } from "../hooks/useInput";
import { useAxiosPost } from "../hooks/useAxiosPost";
import { Context } from "../context/Context";

const LoginModal = (props) => {
  const { setCurrentUser } = useContext(Context);

  const navigate = useNavigate();

  const [email, handleEmailChange] = useInput("");
  const [password1, handlePassword1Change] = useInput("");

  const { error, handleSubmit } = useAxiosPost(
    "http://localhost:8080/login",
    {
      email: email,
      password1: password1,
    },
    "",
    "",
    true,
    setCurrentUser,
    ""
  );

  return (
    <Modal show={props.show} onHide={props.handleClose}>
      <Modal.Header closeButton />
      <Modal.Body>
        <div className={style.container}>
          <span className={style.mainBanner}>내 알바야?!</span>
          <span>아르바이트생들을 위한 익명 커뮤니티 사이트</span>
          <div className={style.loginModalContainer}>
            <input
              type="email"
              placeholder="이메일"
              value={email}
              onChange={handleEmailChange}
            />
            <input
              type="password"
              placeholder="비밀번호"
              value={password1}
              onChange={handlePassword1Change}
            />
            <button onClick={handleSubmit}>로그인</button>
            {error && <p>{error}</p>}
            <span onClick={() => navigate("/registerintro")}>회원가입</span>
          </div>
        </div>
      </Modal.Body>
    </Modal>
  );
};

export default LoginModal;
