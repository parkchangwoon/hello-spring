import style from "../styles/Gallery.module.scss";
import styled from "styled-components";
import { useContext, useState } from "react";
import { useParams } from "react-router-dom";
import { Context } from "../context/Context";
import { useAxiosGet } from "../hooks/useAxiosGet";
import Board from "../components/Board";
import Rating from "../pages/Rating";

const Gallery = () => {
  const [whichTabClicked, setWhichTabClicked] = useState(0);

  const { isToggled, brandArray } = useContext(Context);

  const { id } = useParams();

  const { data: brandRating } = useAxiosGet("http://localhost:8080/brands/top");

  let brandImage = "";
  let url = "";
  let averageRating = "0.0";

  // brandArray에서 브랜드 이미지 찾기
  brandArray.forEach((category) => {
    const index = category.brand.indexOf(id.slice(2));
    if (index !== -1) {
      brandImage = category.brandImage[index];
      url = category.url[index];
    }
  });

  // brandRating에서 해당 브랜드의 평점 찾기
  if (brandRating) {
    const brand = brandRating.find((item) => item.name === id.slice(2));
    if (brand) {
      averageRating = brand.averageRating.toFixed(1); // 소수점 첫째 자리까지 표시
    }
  }

  console.log(brandRating);

  return (
    <div className={style.container}>
      <Div
        className={style.header}
        $backgroundColor={isToggled ? "skyblue" : "#FAEB78"}
      >
        <div className={style.left}>
          <img src={brandImage} alt="" />
        </div>
        <div className={style.right}>
          <span className={style.galleryTitle}>{id.slice(2)} 갤러리</span>
          <span>
            홈페이지: <a href={url}>{url}</a>
          </span>
          <span>⭐️ {averageRating}</span>
        </div>
      </Div>
      <div className={style.tabs}>
        <Tab
          $active={whichTabClicked === 0}
          onClick={() => setWhichTabClicked(0)}
        >
          게시글
        </Tab>
        <Tab
          $active={whichTabClicked === 1}
          onClick={() => setWhichTabClicked(1)}
        >
          평점
        </Tab>
        <div className={style.body}>
          {whichTabClicked === 0 && <Board />}
          {whichTabClicked === 1 && <Rating />}
        </div>
      </div>
    </div>
  );
};

const Div = styled.div`
  background: linear-gradient(
    to top,
    white,
    ${(props) => props.$backgroundColor}
  );
`;

const Tab = styled.span`
  border-bottom: ${(props) => (props.$active ? "2px solid black" : "none")};
  margin: 10px;
  cursor: pointer;
`;

export default Gallery;
