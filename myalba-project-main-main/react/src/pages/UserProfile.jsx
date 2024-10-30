import style from "../styles/UserProfile.module.scss";
import axios from "axios";
import { useContext, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Table, Modal, Button } from "react-bootstrap";
import { Context } from "../context/Context";
import { useAxiosGet } from "../hooks/useAxiosGet";
import { useInput } from "../hooks/useInput";
import 프로필 from "../images/logos/프로필.png";

const UserProfile = () => {
  const [showNicknameChangeInput, setShowNicknameChangeInput] = useState(false);
  const [message, setMessage] = useState("");
  const [showMessageModal, setShowMessageModal] = useState(false);

  const { currentUser, setCurrentUser } = useContext(Context);

  const navigate = useNavigate();

  const { id } = useParams();

  const [nickname, handleNicknameChange, setNickname] = useInput("");

  const { data: userInfo, setData: setUserInfo } = useAxiosGet(
    id === "내프로필"
      ? "http://localhost:8080/profile"
      : `http://localhost:8080/members/${id}/profile`,
    true
  );

  //닉네임 변경 (커스텀 훅 쓰니까 자꾸 이상하게 나와서 안씀)
  const handleSubmit = async () => {
    try {
      await axios.post(
        "http://localhost:8080/profile/updateNickname",
        { newNickname: nickname },
        {
          withCredentials: true,
        }
      );
      const updatedUserInfo = {
        ...userInfo,
        member: { ...userInfo.member, nickname: nickname },
      };
      setUserInfo(updatedUserInfo);
      setCurrentUser({ ...currentUser, nickname: nickname });
      localStorage.setItem(
        "currentUser",
        JSON.stringify({ ...currentUser, nickname: nickname })
      );
      setNickname("");
      setShowNicknameChangeInput(false);
    } catch (error) {
      console.log(error);
    }
  };

  const handleMessageSend = async () => {
    try {
      await axios.post(
        `http://localhost:8080/messages/send/${userInfo.member.id}`,
        {
          content: message,
        },
        {
          withCredentials: true,
        }
      );
      alert("메세지가 전송되었습니다.");
      setMessage("");
    } catch (error) {
      alert(error.response.data);
    }
  };

  return (
    <div className={style.container}>
      <div className={style.userInfoContainer}>
        <div className={style.left}>
          <img src={프로필} alt="" />
        </div>
        <div className={style.right}>
          <span>
            <span className={style.bold}>이름:</span>{" "}
            {userInfo && userInfo.member.name}
          </span>
          <span>
            <span className={style.bold}>닉네임:</span>{" "}
            {showNicknameChangeInput ? (
              <>
                <input
                  type="text"
                  placeholder="닉네임 변경"
                  value={nickname}
                  onChange={handleNicknameChange}
                />
                <button onClick={handleSubmit}>변경</button>
                <button
                  onClick={() => {
                    setShowNicknameChangeInput(false);
                  }}
                >
                  취소
                </button>
              </>
            ) : (
              <> {userInfo && userInfo.member.nickname}</>
            )}
          </span>
          <span>
            <span className={style.bold}>이메일:</span>{" "}
            {userInfo && userInfo.member.email}
          </span>
          <span>
            <span className={style.bold}>인증된 브랜드:</span>
            {userInfo.brands?.map((item, index) => {
              return <span key={index}>{item}</span>;
            })}
          </span>

          <div className={style.buttonContainer}>
            {currentUser.employmentType === "MASTER" ? (
              <button
                onClick={() => {
                  navigate("/board/브랜드인증목록");
                }}
              >
                브랜드 인증 목록
              </button>
            ) : (
              <>
                {currentUser?.nickname === userInfo.member?.nickname ? (
                  <>
                    {" "}
                    <button
                      onClick={() => {
                        setShowNicknameChangeInput(true);
                      }}
                    >
                      닉네임 변경
                    </button>
                    <button
                      onClick={() => {
                        navigate("/brandauthwrite");
                      }}
                    >
                      브랜드 인증하기
                    </button>
                  </>
                ) : (
                  <>
                    {" "}
                    <button
                      onClick={() => {
                        setShowMessageModal(true);
                      }}
                    >
                      메세지 보내기
                    </button>
                  </>
                )}
              </>
            )}
          </div>
        </div>
      </div>
      <div className={style.boardContainer}>
        <div className={style.header}>
          <span>최근 작성한 게시물</span>
        </div>
        <div className={style.body}>
          <Table hover>
            <thead>
              <tr>
                <th>제목</th>
                <th>작성자</th>
                <th>좋아요</th>
                <th>조회수</th>
                <th>댓글</th>
              </tr>
            </thead>
            <tbody>
              {userInfo.posts &&
                userInfo.posts.map((item, index) => {
                  return (
                    <tr
                      key={index}
                      onClick={() => {
                        navigate(`/single/${item.id}`);
                      }}
                    >
                      <td>{item.title}</td>
                      <td>{item.author.nickname}</td>
                      <td>{item.likeCount}</td>
                      <td>{item.viewCount}</td>
                      <td>{item.comments.length}</td>
                    </tr>
                  );
                })}
            </tbody>
          </Table>
        </div>
      </div>
      <Modal show={showMessageModal} onHide={() => setShowMessageModal(false)}>
        <Modal.Header>
          <Modal.Title className={style.modalTitle}>메세지 보내기</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <textarea
            className={style.textarea}
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            style={{ width: "100%" }}
          />
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={() => setShowMessageModal(false)}
          >
            닫기
          </Button>
          <Button variant="primary" onClick={handleMessageSend}>
            보내기
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default UserProfile;
