import style from "../styles/Message.module.scss";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Context } from "../context/Context";

const Message = () => {
  const { currentUser } = useContext(Context);
  const navigate = useNavigate();
  const [messageList, setMessageList] = useState([]);

  useEffect(() => {
    const getMessageList = async () => {
      try {
        const res = await axios.get(
          "http://localhost:8080/messages/conversations",
          {
            withCredentials: true,
          }
        );
        setMessageList(res.data);
      } catch (error) {
        console.log(error);
      }
    };

    getMessageList();
  }, []);

  console.log(messageList);

  return (
    <div className={style.container}>
      <span className={style.title}>메세지 목록</span>
      {messageList.length > 0 ? (
        messageList.map((item, index) => {
          const latestMessage = item.latestMessage;
          const isSender = currentUser.id === latestMessage.sender.id;
          const displayName = isSender
            ? latestMessage.receiver.nickname
            : latestMessage.sender.nickname;

          return (
            <div
              key={index}
              className={style.message}
              onClick={() => {
                navigate(
                  `/messagedetail/${
                    isSender
                      ? latestMessage.receiver.id
                      : latestMessage.sender.id
                  }`
                );
              }}
            >
              <span className={style.nickname}>{displayName}</span>
              <span className={style.date}>
                {latestMessage.sentAt.replace("T", " ")}
                {/* 내가 보낸 메시지가 아니고, read가 false일 경우 "읽지 않음" 표시 */}
                {!isSender && !latestMessage.read && (
                  <span style={{ color: "red" }}> 읽지 않음</span>
                )}
              </span>
              <span className={style.content}>{latestMessage.content}</span>
            </div>
          );
        })
      ) : (
        <span>주고 받은 메세지가 없습니다.</span>
      )}
    </div>
  );
};

export default Message;
