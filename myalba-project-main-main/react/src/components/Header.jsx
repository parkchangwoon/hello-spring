import style from "../styles/Header.module.scss";
import styled from "styled-components";
import axios from "axios";
import { useContext, useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Context } from "../context/Context";
import ToggleButton from "../components/ToggleButton";
import LoginModal from "./LoginModal";
import DropDownMenu from "./DropDownMenu";

const Header = () => {
  const [show, setShow] = useState(false); //모달

  const {
    isToggled,
    setIsToggled,
    currentUser,
    setCurrentUser,
    showDropDownMenu,
    setShowDropDownMenu,
  } = useContext(Context);

  const navigate = useNavigate();

  const location = useLocation();

  const isGalleryPage = location.pathname.includes("/gallery");
  const isSinglePage = location.pathname.includes("/single");
  const isWritePage = location.pathname.includes("/write");
  const isBoardPage = location.pathname.includes("/board");
  const isUserProfilePage = location.pathname.includes("/userprofile");

  //모달
  const handleClose = () => {
    setShow(false);
  };

  //모달
  const handleShow = () => {
    setShow(true);
  };

  const logout = async () => {
    await axios.get("http://localhost:8080/logout", {
      withCredentials: true,
    });
    setCurrentUser("");
    navigate("/");
    localStorage.removeItem("currentUser");
  };

  //새로고침 로그아웃 방지
  useEffect(() => {
    const storedUser = localStorage.getItem("currentUser");
    if (storedUser) {
      setCurrentUser(JSON.parse(storedUser));
    }
  }, [setCurrentUser]);

  return (
    <div className={style.container}>
      <div className={style.left}>
        <span
          className={style.mainBanner}
          onClick={() => {
            navigate("/");
          }}
        >
          내 알바야?!
        </span>
        <span
          onClick={() => {
            navigate("/");
          }}
        >
          <Span $backgroundColor={isToggled ? "skyblue" : "#FAEB78"}>
            {isToggled ? "자영업자들" : "아르바이트생"}
          </Span>
          을 위한 익명 커뮤니티 사이트
        </span>
      </div>
      <div className={style.right}>
        {!isGalleryPage &&
          !isSinglePage &&
          !isWritePage &&
          !isBoardPage &&
          !isUserProfilePage && (
            <ToggleButton
              isToggled={isToggled}
              onToggle={() => {
                setIsToggled(!isToggled);
              }}
            />
          )}
        <Span
          $backgroundColor={isToggled ? "skyblue" : "#FAEB78"}
          onClick={() => {
            setShowDropDownMenu(!showDropDownMenu);
          }}
        >
          전체메뉴
        </Span>
        {showDropDownMenu ? <DropDownMenu /> : ""}
        {currentUser ? (
          <>
            <span
              onClick={() => {
                navigate("/message");
              }}
            >
              메세지함
            </span>
            <span
              className={style.userNickname}
              onClick={() => {
                navigate("/userprofile/내프로필");
              }}
            >
              {currentUser.nickname}
              {currentUser.employmentType === "EMPLOYEE"
                ? " 알바생님"
                : currentUser.employmentType === "BOSS"
                ? " 자영업자님"
                : currentUser.employmentType === "MASTER"
                ? " 관리자"
                : ""}
            </span>
            <span onClick={logout}>로그아웃</span>
          </>
        ) : (
          <>
            <span onClick={handleShow}>로그인</span>
            <LoginModal show={show} handleClose={handleClose} />
            <span
              onClick={() => {
                navigate("/registerintro");
              }}
            >
              회원가입
            </span>
          </>
        )}
      </div>
    </div>
  );
};

const Span = styled.span`
  background: ${(props) => props.$backgroundColor};
`;

export default Header;
