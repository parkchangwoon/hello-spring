import style from "../styles/MessageDetail.module.scss";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import { useInput } from "../hooks/useInput";
import { Context } from "../context/Context";

const Message = () => {
  const { currentUser } = useContext(Context);
  const { id } = useParams();
  const [messageDetail, setMessageDetail] = useState("");

  const navigate = useNavigate();

  const [message, handleMessageChange, setMessage] = useInput("");

  useEffect(() => {
    const getMessageDetail = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/messages/conversation/${id}`,
          {
            withCredentials: true,
          }
        );
        setMessageDetail(res.data);
      } catch (error) {
        console.log(error);
      }
    };

    getMessageDetail();
  }, [id]);

  const handleMessageSend = async () => {
    // 상대방 ID 추출
    const recipient = messageDetail.find(
      (item) => item.sender.id !== currentUser.id
    );
    const recipientId = recipient ? recipient.sender.id : null;

    if (!recipientId) {
      alert("상대방의 ID를 찾을 수 없습니다.");
      return;
    }

    try {
      await axios.post(
        `http://localhost:8080/messages/send/${recipientId}`,
        {
          content: message,
        },
        {
          withCredentials: true,
        }
      );
      alert("메세지가 전송되었습니다.");
      setMessage("");
      window.location.reload();
    } catch (error) {
      alert(error.response.data);
    }
  };

  return (
    <div className={style.container}>
      <span className={style.title}>메세지 목록</span>
      {messageDetail &&
        messageDetail.map((item, index) => {
          return (
            <div
              key={index}
              className={
                currentUser.id === item.sender.id ? style.myself : style.message
              }
              style={{ cursor: "pointer" }}
            >
              <span className={style.nickname}>{item.sender.nickname}</span>
              <span className={style.date}>
                {item.sentAt.replace("T", " ")}
              </span>
              <span className={style.content}>{item.content}</span>
            </div>
          );
        })}
      <button
        onClick={() => {
          navigate("/message");
        }}
      >
        목록으로
      </button>
      <div className={style.messageInputContainer}>
        <textarea
          placeholder="메세지 내용"
          value={message}
          onChange={handleMessageChange}
        />
        <button onClick={handleMessageSend}>보내기</button>
      </div>
    </div>
  );
};

export default Message;
