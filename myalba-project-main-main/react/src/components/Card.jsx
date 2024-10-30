import style from "../styles/Card.module.scss";
import styled from "styled-components";
import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { Dropdown } from "react-bootstrap";
import { Context } from "../context/Context";

const Card = (props) => {
  const { isToggled } = useContext(Context);

  const navigate = useNavigate();

  return (
    <div className={style.container}>
      <div className={style.header}>
        <img src={props.item.categoryImage} alt="" />
        <span>{props.item.category}</span>
      </div>
      <div className={style.body}>
        {props.item.brand.map((item, index) => {
          return <span key={index}>{item} </span>;
        })}
      </div>
      <div className={style.footer}>
        <Dropdown>
          <ToggleButton $backgroundColor={isToggled ? "skyblue" : "#FAEB78"}>
            갤러리 이동하기
          </ToggleButton>
          <Dropdown.Menu>
            {props.item.brand.map((item, index) => {
              return (
                <Dropdown.Item
                  key={index}
                  onClick={() => {
                    navigate(`/gallery/${isToggled ? "사장" : "알바"}${item}`);
                  }}
                >
                  {item}
                </Dropdown.Item>
              );
            })}
          </Dropdown.Menu>
        </Dropdown>
      </div>
    </div>
  );
};

const ToggleButton = styled(Dropdown.Toggle)`
  background: ${(props) => props.$backgroundColor};
  border: 1px solid black;
  color: black;
`;

export default Card;
